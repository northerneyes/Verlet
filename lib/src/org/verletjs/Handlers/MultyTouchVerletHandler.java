package org.verletjs.Handlers;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import org.verletjs.VerletCore.Verlet;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 02.05.13
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public class MultyTouchVerletHandler implements View.OnTouchListener {

    private Verlet verlet;
    private final float scaleX;
    private final float scaleY;

    public MultyTouchVerletHandler(Verlet sim, float scaleX, float scaleY) {

        this.verlet = sim;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        int pointerId = event.getPointerId(pointerIndex);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                verlet.onTouchDown(event.getX(pointerIndex)*scaleX, event.getY(pointerIndex)*scaleY, pointerId);
                Log.v("Touch", String.format("ACTION_DOWN x:%.2f y:%.2f index:%d", event.getX(pointerIndex), event.getY(pointerIndex), pointerId));
            break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    verlet.onTouchMove(event.getX(pointerIndex)*scaleX, event.getY(pointerIndex)*scaleY, pointerId);
                    Log.v("Touch", String.format("ACTION_MOVE %d", pointerId));
                }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                verlet.onTouchUp(event.getX(pointerIndex)*scaleX, event.getY(pointerIndex)*scaleY, pointerId);
                Log.v("Touch", String.format("ACTION_UP %d", pointerId));
                break;
        }

        return true;
    }
}
