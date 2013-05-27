package org.verletandroid.VerletCore;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 05.05.13
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static float lerp(float a, float b, float u) {
        return (1 - u)*a + u*b;
    }
}
