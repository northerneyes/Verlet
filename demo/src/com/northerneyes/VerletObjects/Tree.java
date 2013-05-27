package com.northerneyes.VerletObjects;

import org.verletandroid.VerletCore.Composite;
import org.verletandroid.VerletCore.Contraints.AngleConstraint;
import org.verletandroid.VerletCore.Contraints.DistanceConstraint;
import org.verletandroid.VerletCore.Particle;
import org.verletandroid.VerletCore.Utils;
import org.verletandroid.VerletCore.Vec2;


/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 05.05.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class Tree extends Composite {

    public class TreeDistanceConstraint extends DistanceConstraint {
        public float p;

        public TreeDistanceConstraint(Particle a, Particle b, float stiffness) {
            super(a, b, stiffness);
        }
    }

    public class TreeParticle extends Particle{

        public TreeParticle(Vec2 pos) {
            super(pos);
        }

        public boolean leaf;
    }

    private final Particle base;
    private final Vec2 origin;
    private final Particle root;
    private final float lineCoef;
    private int branchLength;
    private float theta;

    public Tree(Vec2 origin, int depth, int branchLength, float segmentCoef, float theta) {
        this.branchLength = branchLength;
        this.theta = theta;

        lineCoef = 0.7f;
        this.origin = origin;
        this.base = new Particle(origin);
        this.root = new Particle(origin.add(new Vec2(0, 10)));

        particles.add(this.base);
        particles.add(this.root);
        pin(0);
        pin(1);

        Particle firstBranch = branch(this.base, 0, depth, segmentCoef, new Vec2(0,-1));

        constraints.add(new AngleConstraint(this.root, this.base, firstBranch, 1));

        // animates the tree at the beginning
        double noise = 10.0;
        for (Particle part:particles)
            part.pos.mutableAdd(new Vec2(Math.floor(Math.random()*noise), Math.floor(Math.random()*noise)));
    }

    private Particle branch(Particle parent, int i, int nMax, float coef, Vec2 normal)
    {
        TreeParticle particle = new TreeParticle(parent.pos.add(normal.scale(branchLength*coef)));
        particles.add(particle);

        TreeDistanceConstraint dc = new TreeDistanceConstraint(parent, particle, lineCoef);
        dc.p = i/(float)nMax; // a hint for drawing
        constraints.add(dc);

        particle.leaf = !(i < nMax);

        if (i < nMax)
        {
            Particle a = branch(particle, i+1, nMax, coef*coef, normal.rotate(new Vec2(0,0), -theta));
            Particle b = branch(particle, i+1, nMax, coef*coef, normal.rotate(new Vec2(0,0), theta));


            float jointStrength = Utils.lerp(0.7f, 0f, i / (float) nMax);
            constraints.add(new AngleConstraint(parent, particle, a, jointStrength));
            constraints.add(new AngleConstraint(parent, particle, b, jointStrength));
        }

        return particle;
    }
}
