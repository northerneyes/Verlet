package org.verletjs.Graphics;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class Pixmap implements IPixmap {
    Bitmap bitmap;
    PixmapFormat format;

    public Pixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
       bitmap.recycle();
    }
}
