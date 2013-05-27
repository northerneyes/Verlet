package com.northerneyes.VerletObjects;

import android.graphics.Color;
import android.graphics.Paint;
import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Composite;
import org.verletandroid.VerletCore.Contraints.AngleConstraint;
import org.verletandroid.VerletCore.Contraints.DistanceConstraint;
import org.verletandroid.VerletCore.Contraints.IConstraint;
import org.verletandroid.VerletCore.Particle;
import org.verletandroid.VerletCore.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 03.05.13
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class Spider extends Composite {

    private final Particle head;
    private final Particle abdomen;
    private final Particle thorax;
    private final ArrayList<Particle> legs;

    private SpiderWeb web;

    public Spider(Vec2 origin) {
        float legSeg1Stiffness = 0.99f;
        float legSeg2Stiffness = 0.99f;
        float legSeg3Stiffness = 0.99f;
        float legSeg4Stiffness = 0.99f;

        float joint1Stiffness = 1;
        float joint2Stiffness = 0.4f;
        float joint3Stiffness = 0.9f;

        float bodyStiffness = 1;
        float bodyJointStiffness = 1;

        legs = new ArrayList<Particle>();


        thorax = new Particle(origin);
        head = new Particle(origin.add(new Vec2(0,-5)));
        abdomen = new Particle(origin.add(new Vec2(0,10)));

        particles.add(thorax);
        particles.add(head);
        particles.add(abdomen);

        constraints.add(new DistanceConstraint(head, thorax, bodyStiffness));


        constraints.add(new DistanceConstraint(abdomen, thorax, bodyStiffness));
        constraints.add(new AngleConstraint(abdomen, thorax, head, 0.4f));

        // legs
        for (int i=0; i< 4; ++i) {
            particles.add(new Particle(particles.get(0).pos.add(new Vec2(3, (i - 1.5) * 3))));
            particles.add(new Particle(particles.get(0).pos.add(new Vec2(-3, (i - 1.5) * 3))));

            int len = particles.size();

            constraints.add(new DistanceConstraint(particles.get(len - 2), thorax, legSeg1Stiffness));
            constraints.add(new DistanceConstraint(particles.get(len - 1), thorax, legSeg1Stiffness));


            float lenCoef = 1;
            if (i == 1 || i == 2)
                lenCoef = 0.7f;
            else if (i == 3)
                lenCoef = 0.9f;

            particles.add(new Particle(particles.get(len - 2).pos.add((new Vec2(20, (i - 1.5) * 30)).normal().mutableScale(20 * lenCoef))));
            particles.add(new Particle(particles.get(len - 1).pos.add((new Vec2(-20, (i - 1.5) * 30)).normal().mutableScale(20 * lenCoef))));

            len = particles.size();
            constraints.add(new DistanceConstraint(particles.get(len - 4), particles.get(len - 2), legSeg2Stiffness));
            constraints.add(new DistanceConstraint(particles.get(len - 3), particles.get(len - 1), legSeg2Stiffness));

            particles.add(new Particle(particles.get(len-2).pos.add((new Vec2(20,(i-1.5)*50)).normal().mutableScale(20*lenCoef))));
            particles.add(new Particle(particles.get(len-1).pos.add((new Vec2(-20,(i-1.5)*50)).normal().mutableScale(20*lenCoef))));

            len = particles.size();
            constraints.add(new DistanceConstraint(particles.get(len-4), particles.get(len-2), legSeg3Stiffness));
            constraints.add(new DistanceConstraint(particles.get(len-3), particles.get(len-1), legSeg3Stiffness));


            Particle rightFoot = new Particle(particles.get(len-2).pos.add((new Vec2(20,(i-1.5)*100)).normal().mutableScale(12*lenCoef)));
            Particle leftFoot = new Particle(particles.get(len-1).pos.add((new Vec2(-20,(i-1.5)*100)).normal().mutableScale(12*lenCoef)));
            particles.add(rightFoot);
            particles.add(leftFoot);

            legs.add(rightFoot);
            legs.add(leftFoot);

            len = particles.size();
            constraints.add(new DistanceConstraint(particles.get(len-4), particles.get(len-2), legSeg4Stiffness));
            constraints.add(new DistanceConstraint(particles.get(len-3), particles.get(len-1), legSeg4Stiffness));


            constraints.add(new AngleConstraint(particles.get(len-6), particles.get(len-4), particles.get(len-2), joint3Stiffness));
            constraints.add(new AngleConstraint(particles.get(len-6+1), particles.get(len-4+1), particles.get(len-2+1), joint3Stiffness));

            constraints.add(new AngleConstraint(particles.get(len-8), particles.get(len-6), particles.get(len-4), joint2Stiffness));
            constraints.add(new AngleConstraint(particles.get(len-8+1), particles.get(len-6+1), particles.get(len-4+1), joint2Stiffness));

            constraints.add(new AngleConstraint(particles.get(0), particles.get(len-8), particles.get(len-6), joint1Stiffness));
            constraints.add(new AngleConstraint(particles.get(0), particles.get(len-8+1), particles.get(len-6+1), joint1Stiffness));

            constraints.add(new AngleConstraint(particles.get(1), particles.get(0), particles.get(len-8), bodyJointStiffness));
            constraints.add(new AngleConstraint(particles.get(1), particles.get(0), particles.get(len-8+1), bodyJointStiffness));
        }
    }

    public void setSpiderWeb(SpiderWeb web)
    {
        this.web = web;
    }

    public void crawl(int leg)
    {
        if(web == null)
            return;

        int stepRadius = 100;
        int minStepRadius = 35;

        float theta = particles.get(0).pos.angle2(particles.get(0).pos.add(new Vec2(1,0)), particles.get(1).pos);

        Vec2 boundry1 = (new Vec2(Math.cos(theta), Math.sin(theta)));
        Vec2 boundry2 = (new Vec2(Math.cos(theta+Math.PI/2), Math.sin(theta+Math.PI/2)));


        int flag1 = leg < 4 ? 1 : -1;
        int flag2 = leg%2 == 0 ? 1 : 0;

        ArrayList<Particle> paths = new ArrayList<Particle>();

        for (Particle webPoint:web.getParticles())
        {
            if (webPoint.pos.sub(particles.get(0).pos).dot(boundry1)*flag1 >= 0
                    && webPoint.pos.sub(particles.get(0).pos).dot(boundry2)*flag2 >= 0 )
            {
                float d2 = webPoint.pos.dist2(particles.get(0).pos);

                if (!(d2 >= minStepRadius*minStepRadius && d2 <= stepRadius*stepRadius))
                    continue;

                boolean leftFoot = false;
                for (IConstraint cont:constraints) {
                    for (int k=0; k<8; ++k)
                    {

                        if (cont instanceof DistanceConstraint
                                        && ((DistanceConstraint)cont).getA() == legs.get(k)
                                        && ((DistanceConstraint)cont).getB() == webPoint)
                        {
                            leftFoot = true;
                        }
                    }
                }

                if (!leftFoot)
                    paths.add(webPoint);
            }
        }

        int i = 0;
        for (IConstraint cont: constraints) {

            if (cont instanceof DistanceConstraint && ((DistanceConstraint)cont).getA() == legs.get(leg)) {
                constraints.remove(i);
                break;
            }
            i++;
        }

        if (paths.size() > 0) {
            ShuffleList.shuffleList(paths);
            constraints.add(new DistanceConstraint(legs.get(leg), paths.get(0), 1, 0));
        }
    }

    public static class ShuffleList {
        public static <T> void shuffleList(List<T> a) {
            int n = a.size();
            Random random = new Random();
            random.nextInt();
            for (int i = 0; i < n; i++) {
                int change = i + random.nextInt(n - i);
                swap(a, i, change);
            }
        }

        private static <T> void swap(List<T> a, int i, int change) {
            T helper = a.get(i);
            a.set(i, a.get(change));
            a.set(change, helper);
        }
    }

    @Override
    public void drawConstraints(IGraphics graphics)
    {
        graphics.setColorPen(Color.BLACK);
        graphics.setPenStyle(Paint.Style.FILL);

        graphics.drawCircle(head.pos.x, head.pos.y, 4*1.2f);
        graphics.drawCircle(thorax.pos.x, thorax.pos.y, 4*1.2f);
        graphics.drawCircle(abdomen.pos.x, abdomen.pos.y, 8*1.2f);

        for (int i=3; i<constraints.size(); ++i) {
            IConstraint constraint = constraints.get(i);
            if (constraint instanceof DistanceConstraint) {
                DistanceConstraint distanceConstraint = (DistanceConstraint)constraint;
                graphics.setColorPen(Color.BLACK);
                graphics.setPenStyle(Paint.Style.STROKE);
//                graphics.drawLine(distanceConstraint.getA().pos.x, distanceConstraint.getA().pos.y,
//                          distanceConstraint.getB().pos.x, distanceConstraint.getB().pos.y);
                // draw legs
                if (
                        (i >= 2 && i <= 4)
                                || (i >= (2*9)+1 && i <= (2*9)+2)
                                || (i >= (2*17)+1 && i <= (2*17)+2)
                                || (i >= (2*25)+1 && i <= (2*25)+2)
                        ) {
                  //  graphics.setColorPen(Color.BLACK);
                    graphics.setStrokeWidth(4);

                   // constraint.draw(graphics);
                } else if (
                        (i >= 4 && i <= 6)
                                || (i >= (2*9)+3 && i <= (2*9)+4)
                                || (i >= (2*17)+3 && i <= (2*17)+4)
                                || (i >= (2*25)+3 && i <= (2*25)+4)
                        ) {

                  //  graphics.setColorPen(Color.BLACK);
                    graphics.setStrokeWidth(3);
                   // constraint.draw(graphics);

                } else if (
                        (i >= 6 && i <= 8)
                                || (i >= (2*9)+5 && i <= (2*9)+6)
                                || (i >= (2*17)+5 && i <= (2*17)+6)
                                || (i >= (2*25)+5 && i <= (2*25)+6)
                        ) {
                //    graphics.setColorPen(Color.BLACK);
                    graphics.setStrokeWidth(2.5f);
                  //  constraint.draw(graphics);
                }
                graphics.drawLine(distanceConstraint.getA().pos.x, distanceConstraint.getA().pos.y,
                        distanceConstraint.getB().pos.x, distanceConstraint.getB().pos.y);


            }
        }
    }

    @Override
    public void drawParticles(IGraphics graphics)
    {

    }


}
