package engineTester;

import Models.RawModel;
import Models.TexturedModel;
import org.lwjgl.opengl.Display;
import renderEngine.*;
import shaders.StaticShader;

/**
 * Created by abrenner on 3/2/17.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        //BasicShaderHelper.Initialize();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        float[] vertices = {
                -0.5f, 0.5f, 0f, // V0
                -0.5f, -0.5f, 0f, // V1
                0.5f, -0.5f, 0f, // V2
                0.5f, 0.5f, 0f // V3
        };

        float[] uv = {
                0.0f,0.0f, // V0
                0.0f,1.0f, // V1
                1.0f,1.0f, // V2
                1.0f,0.0f // V3
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };

        RawModel model = loader.loadToVAO(vertices, uv, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("texture"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while(!Display.isCloseRequested()) {
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        shader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
