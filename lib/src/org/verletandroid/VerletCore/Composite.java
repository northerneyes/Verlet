package org.verletandroid.VerletCore;

import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Contraints.IConstraint;
import org.verletandroid.VerletCore.Contraints.PinConstraint;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class Composite {

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public ArrayList<IConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(ArrayList<IConstraint> constraints) {
        this.constraints = constraints;
    }

    protected ArrayList<Particle> particles = new ArrayList<Particle>();
        protected ArrayList<IConstraint> constraints = new ArrayList<IConstraint>();

    public void draw(IGraphics graphics)
    {
        drawConstraints(graphics);
        drawParticles(graphics);
    }

    public void drawParticles(IGraphics graphics)
    {
        for(IEntity particle:particles) {
            particle.draw(graphics);
        }
    }

    public IEntity pin(int index, Vec2 _pos)
    {
        Vec2 pos = _pos != null ? _pos : particles.get(index).getPos();
        IConstraint pc = new PinConstraint(particles.get(index), pos);
        constraints.add(pc);
        return pc;
    }

    public IEntity pin(int index)
    {
        return pin(index, null);
    }

    public void drawConstraints(IGraphics graphics)
    {
        for(IEntity constraint:constraints) {
            constraint.draw(graphics);
        }
    }


}
