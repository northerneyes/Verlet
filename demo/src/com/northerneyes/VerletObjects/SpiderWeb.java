package com.northerneyes.VerletObjects;

import android.graphics.Color;
import android.graphics.Paint;
import org.verletandroid.VerletCore.Composite;
import org.verletandroid.VerletCore.Contraints.DistanceConstraint;
import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Contraints.IConstraint;
import org.verletandroid.VerletCore.Particle;
import org.verletandroid.VerletCore.Vec2;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 04.05.13
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class SpiderWeb extends Composite {
    int color = Color.parseColor("#2dad8f");

    public SpiderWeb(Vec2 origin, float radius, int segments, int depth) {
        float stiffness = 0.6f;
        float tensor = 0.3f;
        float stride = (float) ((2*Math.PI)/segments);
        int n = segments*depth;
        float radiusStride = radius/n;

        // particles
        for (int i=0; i<n; ++i) {
            float theta = i*stride + (float)Math.cos(i*0.4f)*0.05f + (float)Math.cos(i*0.05)*0.2f;
            float shrinkingRadius = radius - radiusStride*i + (float)Math.cos(i*0.1)*20;

            float offy = (float) (Math.cos(theta*2.1)*(radius/depth)*0.2);
            particles.add(new Particle(new Vec2(origin.x + Math.cos(theta) * shrinkingRadius, origin.y + Math.sin(theta) * shrinkingRadius + offy)));
        }

        for (int i=0; i<segments; i+=4)
            pin(i);

        // constraints
        for (int i=0; i<n-1; ++i) {
            // neighbor
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1), stiffness));

            // span rings
            int off = i + segments;
            if (off < n-1)
                constraints.add(new DistanceConstraint(particles.get(i), particles.get(off), stiffness));
            else
                constraints.add(new DistanceConstraint(particles.get(i), particles.get(n-1), stiffness));
        }


        constraints.add(new DistanceConstraint(particles.get(0), particles.get(segments - 1), stiffness));

        for (IConstraint con: constraints)
        {
            if(con instanceof DistanceConstraint)
            {
                float dist = ((DistanceConstraint)con).getDistance();
                ((DistanceConstraint)con).setDistance(dist*tensor);
            }
        }
    }

    public void drawParticles(IGraphics graphics)
    {
        for (Particle part:particles)
        {
            graphics.setColorPen(color);
            graphics.setPenStyle(Paint.Style.FILL);
            graphics.drawCircle(part.getPos().x, part.getPos().y, 1.3f);
        }
    }

}
