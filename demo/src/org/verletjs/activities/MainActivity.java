package org.verletjs.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.*;
import org.verletjs.Graphics.Graphics;
import org.verletjs.Graphics.IGraphics;
import org.verletjs.Handlers.AccelerometerVerletHandler;
import org.verletjs.Handlers.MultyTouchVerletHandler;
import org.verletjs.VerletCore.Objects.Cloth;
import org.verletjs.VerletCore.Objects.LineSegments;
import org.verletjs.VerletCore.Objects.Tire;
import org.verletjs.VerletCore.Particle;
import org.verletjs.VerletCore.Vec2;
import org.verletjs.VerletCore.Verlet;
import org.verletjs.VerletObjects.Spider;
import org.verletjs.VerletObjects.SpiderWeb;
import org.verletjs.VerletObjects.Tree;
import org.verletjs.componets.IUpdatable;
import org.verletjs.componets.RenderView;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends Activity implements IUpdatable {

    private int BACKGRAOUND_COLOR;
    private int VERLET_STEP = 16;
    private SampleType sample;
    private Spider spider;

    public enum SampleType
    {
        SHAPES(0),
        CLOTH(1),
        SPIDERWEB(2),
        TREE(3);

        private final int _id;

        SampleType(int id) {
            _id = id;
        }

        public int getValue()
        {
            return _id;
        }

        private static final HashMap<Integer, SampleType> intToTypeMap = new HashMap<Integer, SampleType>();
        static {
            for (SampleType type : SampleType.values()) {
                intToTypeMap.put(type.getValue(), type);
            }
        }

        public static SampleType fromInt(int i) {
            SampleType type = intToTypeMap.get(Integer.valueOf(i));
            return type;
        }
    }

    RenderView renderView;
    private IGraphics graphics;
    private Particle particle;
    private int frameBufferWidth;
    private int frameBufferHeight;
    private Verlet sim;
    private float scaleX;
    private float scaleY
            ;
    private AccelerometerVerletHandler accHandler;
    private PowerManager.WakeLock wakeLock;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        frameBufferWidth = isLandscape ? 800 :  480;
        frameBufferHeight = isLandscape ? 480 : 800;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Bitmap.Config.RGB_565);

        scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        scaleY = (float) frameBufferHeight
                /  getWindowManager().getDefaultDisplay().getHeight();

        renderView = new RenderView(this, frameBuffer);
        graphics = new Graphics(getAssets(), frameBuffer);


        setContentView(renderView);
        sim = new Verlet(frameBufferWidth, frameBufferHeight, graphics);
        MultyTouchVerletHandler handler = new MultyTouchVerletHandler(sim, scaleX, scaleY);
         accHandler = new AccelerometerVerletHandler(this, sim);
        sim.setFriction(1);
        renderView.setOnTouchListener(handler);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

         sample  = SampleType.fromInt(getIntent().getIntExtra("Sample", 0));
        switch (sample)
        {
            case SHAPES:
                loadShapes();
                break;
            case CLOTH:
                loadCloth();
                break;
            case SPIDERWEB:
                loadSpider();
                break;
            case TREE:
                
                loadTree();
                break;
        }

        PowerManager powerManager = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Verlet");
    }

    private void loadTree() {
        VERLET_STEP = 16;
        BACKGRAOUND_COLOR = Color.WHITE;
        sim.setStandartGravity(-0.1f);
        sim.setFriction(0.98f);

        Tree tree = new Tree(new Vec2(frameBufferWidth/4, frameBufferHeight-120), 5, 70, 0.95f, (float) ((Math.PI/2)/3));
        sim.getComposites().add(tree);
    }

    private void loadSpider() {
        VERLET_STEP = 16;
        BACKGRAOUND_COLOR = Color.WHITE;
        SpiderWeb web = new SpiderWeb(new Vec2(frameBufferWidth/2, frameBufferHeight/2), Math.min(frameBufferWidth, frameBufferHeight)/2, 20, 7);
        spider = new Spider(new Vec2(frameBufferWidth/2, 100));
        spider.setSpiderWeb(web);
        sim.getComposites().add(web);
        sim.getComposites().add(spider);

    }

    private void loadCloth() {
        BACKGRAOUND_COLOR = Color.WHITE;
        VERLET_STEP = 8;
        sim.setFriction(1);
        sim.setHighlightColor(Color.BLUE);

        float min = Math.min(frameBufferWidth, frameBufferHeight)*0.8f;
        int segments = 20;
        Cloth cloth = new Cloth(new Vec2(frameBufferWidth/2, frameBufferHeight/3), min, min, segments, 6, 0.9f);
        sim.getComposites().add(cloth);
    }

    private void loadShapes() {
        BACKGRAOUND_COLOR = Color.WHITE;
        VERLET_STEP = 16;
        ArrayList<Vec2> vecs = new ArrayList<Vec2>();
        vecs.add(new Vec2(20,10));
        vecs.add(new Vec2(40,10));
        vecs.add(new Vec2(60,10));
        vecs.add(new Vec2(80,10));
        vecs.add(new Vec2(100,10));
        LineSegments segment = new LineSegments(vecs, 0.02f);

        Tire tire1 = new Tire(new Vec2(100, 150), 50*2, 20, 0.3f, 0.9f);
        Tire tire2 = new Tire(new Vec2(200, 150), 70*2, 7, 0.1f, 0.2f);
        Tire tire3 = new Tire(new Vec2(300, 150), 70*2, 3, 1, 1);

        segment.pin(0);
        segment.pin(4);

        sim.getComposites().add(segment);
        sim.getComposites().add(tire1);
        sim.getComposites().add(tire2);
        sim.getComposites().add(tire3);

    }

    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        renderView.resume();
    }

    public void onPause()
    {
        super.onPause();
        wakeLock.release();
        renderView.pause();
    }

    int i = 0;
    float fps = 0;
    float fps2 = 0;
    float result = 0;
    float result2 = 0;
    long startTime;
    float _deltaTime = 0;
    int legIndex = 0;
    @Override
    public void update(float deltaTime) {

        if(sample == SampleType.SPIDERWEB)
        {
            if (Math.floor(Math.random()*4) == 0) {
                spider.crawl(((legIndex++)*3)%8);
            }
        }

        graphics.clear(BACKGRAOUND_COLOR);
        fps += 1.0/deltaTime;
        fps2 += 1.0/_deltaTime;
        i++;

        if(i >= 25)
        {
            result = fps/i;
            result2 = fps2/i;
            fps = 0;
            fps2=0;
            i = 0;
        }
        graphics.setColorPen(Color.BLACK);
        graphics.setPenStyle(Paint.Style.FILL);
        graphics.getPaint().setTextSize(20);
        graphics.drawText(String.format("FPS: %.2f", result), frameBufferWidth - 95, 30);
//        graphics.drawText(String.format("x: %.2f", accHandler.getAccelX()), frameBufferWidth - 80, 80);
//        graphics.drawText(String.format("y: %.2f", accHandler.getAccelY()), frameBufferWidth - 80, 110);
//        graphics.drawText(String.format("z: %.2f", accHandler.getAccelZ()), frameBufferWidth - 80, 130);
//        graphics.drawText(String.format("math: %.2f", result2), frameBufferWidth - 100, 160);
//        graphics.drawText(String.format("draw: %.2f", 1/(1/result - 1/result2)), frameBufferWidth - 100, 180);

        startTime = System.nanoTime();
        sim.frame(VERLET_STEP);
        _deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
        sim.draw();

        //graphics.drawArc(frameBufferWidth/2.0, frameBufferHeight/2.0, 75.0, 90, 270, Color.GREEN);
    }
}