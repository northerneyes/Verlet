package org.verletjs.componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class RenderView extends SurfaceView  {

    private final Handler handler = new Handler();
    IUpdatable activity;
    Bitmap framebuffer;
    SurfaceHolder holder;
    long startTime = 0;
    float deltaTime = 0;

    private final Runnable drawView = new Runnable() {
        public void run() {
            drawFrame();
        }
    };
    public RenderView(Context context, Bitmap framebuffer) {
        super(context);
        this.activity = (IUpdatable)context;
        this.framebuffer = framebuffer;
        this.holder = getHolder();

    }

    public void resume(){
        drawFrame();
    }

    public void drawFrame() {
        Rect dstRect = new Rect();
        Canvas canvas = null;
        if(holder.getSurface().isValid())
        {
           try
           {
                canvas = holder.lockCanvas();
                if(canvas != null)
                {
                   activity.update(deltaTime);
                   canvas.getClipBounds(dstRect);
                   canvas.drawBitmap(framebuffer, null, dstRect, null);
                   deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;

                }

            }
           finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }

        handler.removeCallbacks(drawView);
        handler.post(drawView);
        startTime = System.nanoTime();

    }

    public void pause() {
        handler.removeCallbacks(drawView);
    }
}

