package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by abrenner on 3/2/17.
 */
public class DisplayManager {
    private static final int WIDTH=1280;
    private static final int HEIGHT=720;
    private static final int FPS_CAP=60;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3,2)
        .withForwardCompatible(true)
        .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Test Playground");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        final String version = GL11.glGetString(GL11.GL_VERSION);
        GL11.glViewport(0,0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }
    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();

        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000.0f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    private static long getCurrentTime() {
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
