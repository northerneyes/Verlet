package org.verletjs.VerletCore.Objects;

import org.verletjs.VerletCore.Composite;
import org.verletjs.VerletCore.Contraints.DistanceConstraint;
import org.verletjs.VerletCore.Particle;
import org.verletjs.VerletCore.Vec2;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 29.04.13
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public class Tire extends Composite {

    public Tire(Vec2 origin, float radius, int segments, float spokeStiffness, float treadStiffness) {
        double stride = (2 * Math.PI) / segments;
        int i;

        for (i=0;i<segments;++i) {
            double theta = i * stride;
            particles.add(new Particle(new Vec2(origin.x + Math.cos(theta) * radius, origin.y + Math.sin(theta) * radius)));
        }

        Particle center = new Particle(origin);
        particles.add(center);

        // constraints
        for (i=0;i<segments;++i) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get((i + 1) % segments), treadStiffness));
            constraints.add(new DistanceConstraint(particles.get(i), center, spokeStiffness));
            constraints.add(new DistanceConstraint(particles.get(i), particles.get((i + 5) % segments), treadStiffness));
        }
    }
}
