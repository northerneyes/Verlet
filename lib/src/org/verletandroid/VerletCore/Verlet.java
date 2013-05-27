package org.verletandroid.VerletCore;

import android.graphics.Color;
import android.graphics.Paint;
import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Contraints.PinConstraint;
import org.verletandroid.Graphics.IGraphics;
import org.verletandroid.VerletCore.Contraints.IConstraint;
import org.verletandroid.VerletCore.Contraints.PinConstraint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class Verlet {

    private  float groundFriction;
    private  float friction;
//    private  Vec2 mouse;
    private Map<Integer, Vec2> touchEventsBuffer;
//    private  boolean mouseDown;
    private  float selectionRadius;
    private  int highlightColor;
    private  Map<Integer, IEntity> draggedEntities;
    private  Vec2 gravity;
    private float width;
    private float height;
    private IGraphics graphics;
    private ArrayList<Composite> composites;
    private float standartGravity = 0.5f;

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public ArrayList<Composite> getComposites() {
        return composites;
    }

    public Verlet(float width, float height, IGraphics graphics) {
        this.width = width;
        this.height = height;
        this.graphics = graphics;
//        this.mouse = new Vec2(-40,-40);
//        this.mouseDown = false;
        this.draggedEntities =new HashMap<Integer, IEntity>();
        this.touchEventsBuffer =  new HashMap<Integer, Vec2>();
        this.selectionRadius = 40;
        this.highlightColor = Color.parseColor("#4f545c");

        // simulation params
        this.gravity = new Vec2(0,standartGravity);
        this.friction = 0.99f;
        this.groundFriction = 0.8f;

        // holds composite entities
        this.composites = new ArrayList<Composite>();
    }

    public void  bounds(Particle particle)
    {
        if (particle.getPos().y > this.height-1)
            particle.getPos().setY((float) (this.height-1));

        if (particle.getPos().x < 0)
            particle.getPos().setX(0);

        if (particle.getPos().x > this.width-1)
            particle.getPos().setX((float) (this.width-1));

        if (particle.getPos().y < 0)
            particle.getPos().setY(0);
    }

    public void frame(int step)
    {
        for (Composite composite:composites)
        {
            for(Particle particle:composite.getParticles())
            {
                Vec2 velocity = particle.getPos().sub(particle.getLastPos()).scale(friction);

                if(particle.getPos().y >= height -1 && velocity.length2() > 0.000001)
                {
                    float m = velocity.length();
                    velocity.setX(velocity.x/m);
                    velocity.setY(velocity.y/m);
                    velocity.mutableScale(m*this.groundFriction);
                }

                // save last good state
                particle.getLastPos().mutableSet(particle.getPos());

                // gravity
                particle.getPos().mutableAdd(this.gravity);

                // inertia
                particle.getPos().mutableAdd(velocity);
            }

        }


        // handle dragging of entities
        for(int draggedEntityId:draggedEntities.keySet())
        {
            draggedEntities.get(draggedEntityId).getPos().mutableSet(touchEventsBuffer.get(draggedEntityId));
        }
//        if(this.draggedEntity != null)
//            this.draggedEntity.getPos().mutableSet(this.mouse);


        // relax
        float stepCoef = (float) (1.0/step);
        for (Composite composite:composites)
        {
            ArrayList<IConstraint> constraints = composite.getConstraints();
            for (int i = 0; i < step; ++i)
                for (IConstraint constraint : constraints)
                    constraint.relax(stepCoef);
        }

        // bounds checking
        for (Composite composite:composites)
        {
            ArrayList<Particle> particles = composite.getParticles();
            for (Particle particle:particles)
                this.bounds(particle);
        }
    }

    public void draw()
    {
        for(Composite composite:composites)
        {
            graphics.getPaint().setStyle(Paint.Style.FILL);
            composite.draw(graphics);
        }

        // highlight nearest / dragged entity
        for(int touchId:touchEventsBuffer.keySet())
        {
            IEntity nearest = this.draggedEntities.containsKey(touchId) ?
                    this.draggedEntities.get(touchId)  : this.nearestEntity(touchEventsBuffer.get(touchId));
            if (nearest != null) {
                graphics.setPenStyle(Paint.Style.STROKE);
                graphics.setColorPen(this.highlightColor);
                graphics.drawCircle(nearest.getPos().x, nearest.getPos().y, 8);
            }
        }
    }

    private IEntity nearestEntity(Vec2 point) {
        double d2Nearest = 0;
        IEntity entity = null;
        ArrayList<IConstraint> constraintsNearest = new ArrayList<IConstraint>();

        // find nearest point
        for (Composite composite:composites) {
            ArrayList<Particle> particles = composite.getParticles();
            for (Particle particle:particles){
                double d2 = particle.getPos().dist2(point);
                if (d2 <= this.selectionRadius*this.selectionRadius && (entity == null || d2 < d2Nearest)) {
                    entity = particle;
                    constraintsNearest = composite.getConstraints();
                    d2Nearest = d2;
                }
            }
        }

        // search for pinned constraints for this entity
        for (IConstraint constraint : constraintsNearest )
            if (constraint instanceof PinConstraint)
            {
               IEntity pinConstraintEntity = ((PinConstraint)constraint).getA();
                if(pinConstraintEntity == entity)
                    entity = constraint;
            }

        return entity;
    }

//    private IEntity nearestEntities() {
//        double d2Nearest = 0;
//        IEntity entity = null;
//        ArrayList<IConstraint> constraintsNearest = new ArrayList<IConstraint>();
//
//        // find nearest point
//        for (Composite composite:composites) {
//            ArrayList<Particle> particles = composite.getParticles();
//            for (Particle particle:particles){
//                double d2 = particle.getPos().dist2(this.mouse);
//                if (d2 <= this.selectionRadius*this.selectionRadius && (entity == null || d2 < d2Nearest)) {
//                    entity = particle;
//                    constraintsNearest = composite.getConstraints();
//                    d2Nearest = d2;
//                }
//            }
//        }
//
//        // search for pinned constraints for this entity
//        for (IConstraint constraint : constraintsNearest )
//            if (constraint instanceof PinConstraint)
//            {
//                IEntity pinConstraintEntity = ((PinConstraint)constraint).getA();
//                if(pinConstraintEntity == entity)
//                    entity = constraint;
//            }
//
//        return entity;
//    }

    public void onTouchDown(float x, float y, int pointerId) {

        if(!touchEventsBuffer.containsKey(pointerId))
        {
            Vec2 point = new Vec2(x,y);
            touchEventsBuffer.put(pointerId, point);
            IEntity nearest = nearestEntity(point);
        if (nearest != null) {
            draggedEntities.put(pointerId, nearest);
            }

        }
//        mouse.x = x;
//        mouse.y = y;
//        mouseDown = true;
//        IEntity nearest = nearestEntity();
//        if (nearest != null) {
//            draggedEntity = nearest;
//        }
    }

    public void onTouchMove(float x, float y, int pointerId) {
        touchEventsBuffer.get(pointerId).mutableSet(x, y);
//        mouse.x = x;
//        mouse.y = y;
    }

    public void onTouchUp(float x, float y, int pointerId) {
        touchEventsBuffer.remove(pointerId);
        draggedEntities.remove(pointerId);
//        mouse.x = -40;
//        mouse.y = -40;
//        mouseDown = false;
//        draggedEntity = null;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity.mutableSet(gravity);
    }

    public float getStandartGravity() {
        return standartGravity;
    }

    public void setHighlightColor(int color) {
        highlightColor = color;
    }

    public void setStandartGravity(float standartGravity) {
        this.standartGravity = standartGravity;
    }
}
