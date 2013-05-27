package org.verletandroid.VerletCore.Objects;

import org.verletandroid.VerletCore.Composite;
import org.verletandroid.VerletCore.Contraints.DistanceConstraint;
import org.verletandroid.VerletCore.Particle;
import org.verletandroid.VerletCore.Vec2;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class LineSegments extends Composite {

    public LineSegments(ArrayList<Vec2> vertices, float stiffness) {

        int i = 0;
        for(Vec2 v:vertices)
        {
            particles.add(new Particle(v));
            if(i > 0)
                constraints.add(new DistanceConstraint(particles.get(i), particles.get(i-1), stiffness));
            i++;
        }
    }
}
