package org.verletjs.VerletCore;

import org.verletjs.Graphics.IGraphics;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 22:31
 * To change this template use File | Settings | File Templates.
 */
public interface IEntity {
    void draw(IGraphics graphics);
    Vec2 getPos();
}
