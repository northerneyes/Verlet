package org.verletandroid.VerletCore;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class Vec2 {
    public float x;
    public float y;

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }

    public Vec2(float x, float y, float scaleX, float scaleY) {
        this.x = x*scaleX;
        this.y = y*scaleY;
    }

    public Vec2 add(Vec2 v)
    {
        return new Vec2(this.x + v.x, this.y + v.y);
    }

    public Vec2 sub(Vec2 v)
    {
        return new Vec2(this.x - v.x, this.y - v.y);
    }

    public Vec2 mul(Vec2 v)
    {
        return new Vec2(this.x * v.x, this.y * v.y);
    }

    public Vec2 div(Vec2 v)
    {
        return new Vec2(this.x / v.x, this.y / v.y);
    }

    public Vec2 scale(float coef)
    {
        return new Vec2(this.x * coef, this.y * coef);
    }

    public Vec2 mutableSet(Vec2 v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vec2 mutableSet(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2 mutableAdd(Vec2 v)
    {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        return this;
    }

    public Vec2 mutableSub(Vec2 v)
    {
        this.x = this.x - v.x;
        this.y = this.y -  v.y;
        return this;
    }

    public Vec2 mutableMul(Vec2 v)
    {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vec2 mutableDiv(Vec2 v)
    {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public Vec2 mutableScale(float coef)
    {
        this.x *= coef;
        this.y *= coef;
        return this;
    }

    public boolean equals(Vec2 v)
    {
        return this.x == v.x && this.y == v.y;
    }

    public boolean epsilonEquals(Vec2 v, float epsilon)
    {
        return Math.abs(this.x - v.x) <= epsilon && Math.abs(this.y - v.y) <= epsilon;
    }

    public float length()
    {
        return (float)Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public float length2()
    {
        return this.x*this.x + this.y*this.y;
    }

    public float dist(Vec2 v)
    {
        return  (float)Math.sqrt(this.dist2(v));
    }

    public float dist2(Vec2 v)
    {
        float x = v.x - this.x;
        float y = v.y - this.y;
        return x*x + y*y;
    }

    public Vec2 normal()
    {
        float m = (float)Math.sqrt(this.x*this.x + this.y*this.y);
        return new Vec2(this.x/m, this.y/m);
    }

    public float dot(Vec2 v)
    {
        return this.x*v.x + this.y*v.y;
    }

    public float angle(Vec2 v)
    {
        return (float)Math.atan2(this.x*v.y-this.y*v.x, this.x*v.x+this.y*v.y);
    }

    public float angle2(Vec2 vLeft, Vec2 vRight)
    {
        return vLeft.sub(this).angle(vRight.sub(this));
    }

    public Vec2 rotate(Vec2 origin, float theta)
    {
        float x = this.x - origin.x;
        float y = this.y - origin.y;
        return new Vec2((x*Math.cos(theta) - y*Math.sin(theta) + origin.x), x*Math.sin(theta) + y*Math.cos(theta) + origin.y);
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }


    public void setY(float v) {
        y = v;
    }

    public void setX(float x) {
        this.x = x;
    }
}
