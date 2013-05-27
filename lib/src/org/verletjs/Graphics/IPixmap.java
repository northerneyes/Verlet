package org.verletjs.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public interface IPixmap {
    public int getWidth();
    public int getHeight();
    public PixmapFormat getFormat();
    public void dispose();
}
