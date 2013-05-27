package org.verletjs.VerletCore.Objects;

import android.graphics.Color;
import android.graphics.Paint;
import org.verletjs.Graphics.IGraphics;
import org.verletjs.VerletCore.Composite;
import org.verletjs.VerletCore.Contraints.DistanceConstraint;
import org.verletjs.VerletCore.Contraints.IConstraint;
import org.verletjs.VerletCore.Contraints.PinConstraint;
import org.verletjs.VerletCore.Particle;
import org.verletjs.VerletCore.Utils;
import org.verletjs.VerletCore.Vec2;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 01.05.13
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class Cloth extends Composite {

    private final float min;
    private float width;
    private float height;
    private int segments;
   private int pinConstrainColor = Color.BLACK;
    Vec2[] myPath = {  new Vec2(),
    new Vec2(),
    new Vec2(),
    new Vec2(),
    new Vec2()};

//    private Vec2 path5 = new Vec2();

    public Cloth(Vec2 origin, float width, float height, int segments, float pinMod, float stiffness) {
        this.width = width;
        this.height = height;
        this.segments = segments;
        this.min = Math.min(height, width);
        float xStride  = width/segments;
        float yStride = height/segments;

        int  x, y;
        for (y = 0; y < segments; ++y) {
            for (x = 0; x < segments; ++x) {
                float px = origin.x + x*xStride - width/2 + xStride/2;
                float py = origin.y + y*yStride - height/2 + yStride/2;
                particles.add(new Particle(new Vec2(px, py)));

                if (x > 0)
                    constraints.add(new DistanceConstraint(particles.get(y * segments + x), particles.get(y * segments + x - 1), stiffness));

                if (y > 0)
                    constraints.add(new DistanceConstraint(particles.get(y * segments + x), particles.get((y - 1) * segments + x), stiffness));
            }
        }

        for (x=0; x<segments; ++x) {
            if (x%pinMod == 0)
                pin(x);
        }
    }

    @Override
    public void  drawConstraints(IGraphics graphics)
    {
        float stride = min/segments;
        int x,y;
        for (y=1; y<segments; ++y) {
            for (x=1; x<segments; ++x) {
                int i1 = (y-1)*segments+x-1;
                int i2 = (y)*segments+x;
                float off = particles.get(i2).pos.x - particles.get(i1).pos.x;
                off +=  particles.get(i2).pos.y - particles.get(i1).pos.y;
                off *= 0.25;

                float coef = Math.round((Math.abs(off)/stride)*255);
                if (coef > 255)
                    coef = 255;

                graphics.setColorPen(Color.argb((int)(Utils.lerp(0.25f, 1, coef / 255)*255), (int)coef, 0 , (int)(255 - coef)));
                graphics.setPenStyle(Paint.Style.FILL);
                myPath[0].mutableSet(particles.get(i1).pos.x, particles.get(i1).pos.y);
                myPath[1].mutableSet( particles.get(i1+1).pos.x, particles.get(i1+1).pos.y);
                myPath[2].mutableSet(particles.get(i1+1).pos.x, particles.get(i1+1).pos.y);
                myPath[3].mutableSet(particles.get(i2).pos.x, particles.get(i2).pos.y);
                myPath[4].mutableSet(particles.get(i2 - 1).pos.x, particles.get(i2-1).pos.y);

                graphics.drawPath(myPath);
            }
        }


        for (IConstraint constraint: constraints) {
            if (constraint instanceof PinConstraint) {
                graphics.setColorPen(pinConstrainColor);

                graphics.drawCircle(constraint.getPos().x, constraint.getPos().y, 4f);
            }
        }
    }
    @Override
    public void drawParticles(IGraphics graphics)
    {

    }


}
