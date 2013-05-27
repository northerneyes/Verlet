package org.verletjs.Graphics;

import android.content.res.AssetManager;
import android.graphics.*;
import org.verletjs.VerletCore.Vec2;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public class Graphics implements IGraphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public Paint getPaint() {
        return paint;
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    public Graphics(AssetManager assets, Bitmap frameBuffer) {
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
    }

    @Override
    public IPixmap newPixmap(String fileName, PixmapFormat format) {
        Bitmap.Config config = null;
        if(format == PixmapFormat.RGB565)
            config = Bitmap.Config.RGB_565;
        else if(format == PixmapFormat.ARGB4444)
            config = Bitmap.Config.ARGB_4444;
        else
            config = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        InputStream in = null;
        Bitmap bitmap = null;
        try{
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch(IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally{
            if(in != null) {
                try{
                    in.close();
                } catch(IOException e) {
                }
            }
        }
        if(bitmap.getConfig() == Bitmap.Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if(bitmap.getConfig() == Bitmap.Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;
        return new Pixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void setColorPen(int color) {
        paint.setColor(color);
    }

    @Override
    public void setStrokeWidth(float width) {
        paint.setStrokeWidth(width);
    }

    @Override
    public void setPenStyle(Paint.Style style) {
        paint.setStyle(style);
    }

    @Override
    public void drawText(String text, float x, float y)
    {
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void drawPixel(int x, int y) {
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2) {
        canvas.drawLine(x, y, x2, y2, paint);
    }


    @Override
    public void drawPath(Vec2[] pathPoint) {
        Path path = new Path();

        path.moveTo(pathPoint[0].x, pathPoint[0].y);

        for (int i = 1; i < pathPoint.length; i++){

            path.lineTo(pathPoint[i].x, pathPoint[i].y);

        }
        canvas.drawPath(path, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {

        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    @Override
    public void drawArc(float x, float y, float radius, float startAngle, float endAngle) {
        final RectF oval = new RectF();
        oval.set(x - radius, y - radius, x + radius, y+ radius);
        canvas.drawArc(oval, startAngle, endAngle, true, paint);
    }

    @Override
    public void drawPixmap(IPixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;
        canvas.drawBitmap(((Pixmap) pixmap).bitmap, srcRect, dstRect,
                null);
    }

    @Override
    public void drawPixmap(IPixmap pixmap, int x, int y) {
        canvas.drawBitmap(((Pixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawCircle(float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
