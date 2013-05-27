package org.verletandroid.VerletCore.Contraints;

import android.graphics.Color;
import android.graphics.Paint;
import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Particle;
import org.verletandroid.VerletCore.Vec2;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 03.05.13
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class AngleConstraint implements IConstraint {

    private final Particle a;
    private final Particle b;
    private final Particle c;
    private final float angle;
    private final float stiffness;
    private int color = Color.argb((int) (0.2f*255), 255, 255, 0);

    Vec2[] drawPath = {  new Vec2(),
            new Vec2(),
            new Vec2()};

    public AngleConstraint(Particle a, Particle b, Particle c, float stiffness) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.angle = this.b.pos.angle2(this.a.pos, this.c.pos);
        this.stiffness = stiffness;
    }

    @Override
    public void relax(float stepCoef) {
        float angle = this.b.pos.angle2(this.a.pos, this.c.pos);
        float diff = angle - this.angle;

        if (diff <= -Math.PI)
            diff += 2*Math.PI;
        else if (diff >= Math.PI)
            diff -= 2*Math.PI;

        diff *= stepCoef*this.stiffness;

        this.a.pos = this.a.pos.rotate(this.b.pos, diff);
        this.c.pos = this.c.pos.rotate(this.b.pos, -diff);
        this.b.pos = this.b.pos.rotate(this.a.pos, diff);
        this.b.pos = this.b.pos.rotate(this.c.pos, -diff);
    }

    @Override
    public void draw(IGraphics graphics) {
        graphics.setColorPen(color);
        graphics.setStrokeWidth(5);
        graphics.setPenStyle(Paint.Style.STROKE);

        drawPath[0].mutableSet(this.a.pos.x, this.a.pos.y);
        drawPath[1].mutableSet(this.b.pos.x, this.b.pos.y);
        drawPath[2].mutableSet(this.b.pos.x, this.b.pos.y);

        graphics.drawPath(drawPath);
    }

    @Override
    public Vec2 getPos() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
