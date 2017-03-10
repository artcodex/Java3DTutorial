package engineTester;

import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import primitives.Cube;
import renderEngine.*;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abrenner on 3/2/17.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();


        //Rectangle rectangle = new Rectangle();
        Cube cube = new Cube();

        //RawModel model = loader.loadToVAO(cube.vertices, cube.uv, cube., cube.indices);
        /*RawModel model = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("cream"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1.0f);*/

        //Entity entity = new Entity(texturedModel, new Vector3f(0,0,-30.0f), 0.0f,0.0f,0.0f, 1);
        Light light = new Light(new Vector3f(20000,20000,2000), new Vector4f(1.0f, 1.0f, 1.0f, 1f));

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(-1,-1, loader, texturePack, blendMap, "heightMap");
        Terrain terrain2 = new Terrain(0,-1, loader, texturePack, blendMap, "heightMap");

        MasterRenderer renderer = new MasterRenderer();

        /*List<Entity> entities = new ArrayList<>();
        Random random = new Random();

        for (int i=0; i < 10; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            entities.add(new Entity(texturedModel, new Vector3f(x, y, z),
                    random.nextFloat() * 180.0f, random.nextFloat() * 180.0f, 0.0f, 0.5f));
        }*/

        RawModel model = OBJLoader.loadObjModel("tree", loader);
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));

        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture")));

        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("flower")));

        TexturedModel box = new TexturedModel(OBJLoader.loadObjModel("box", loader),
                new ModelTexture(loader.loadTexture("box")));
        box.getModelTexture().setUseFakeLighting(true);

        flower.getModelTexture().setHasTransparency(true);
        flower.getModelTexture().setUseFakeLighting(true);

        TexturedModel lowPolyTree = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
            new ModelTexture(loader.loadTexture("lowPolyTree")));

        grass.getModelTexture().setHasTransparency(true);
        grass.getModelTexture().setUseFakeLighting(true);

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern2")));
        fern.getModelTexture().setNumberOfRows(2);

        TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("person", loader),
                new ModelTexture(loader.loadTexture("playerTexture")));

        fern.getModelTexture().setUseFakeLighting(true);
        fern.getModelTexture().setHasTransparency(true);

        Player player = new Player(bunnyModel, new Vector3f(100,0,-150), 0,180,0,0.5f);
        Camera camera = new Camera(player);

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        /*for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800,0,random.nextFloat() * 600),0,0,0,3));
        }*/

        for(int i=0;i<200;i++){
            entities.add(createEntity(random, terrain2, staticModel, 3, 0));
            entities.add(createEntity(random, terrain2, lowPolyTree, 0.5f, 0));
            //entities.add(createEntity(random, terrain2, grass, 1));
            //entities.add(createEntity(random, terrain2, flower, 1));
            entities.add(createEntity(random, terrain2, fern, 0.9f, random.nextInt(4)));
        }

        //entities.add(new Entity(box, new Vector3f(100, 0,-200),0,0,0,5));
        //entities.add(new Entity(box, new Vector3f(0, 0,-250),0,0,0,5));

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        int frames =0;
        long elapsed =0;
        long seconds =0;
        long last = System.currentTimeMillis();
        while(!Display.isCloseRequested()) {
            //entity.increaseRotation(0.0f, 1f, 0.0f);
            /*if (Mouse.isButtonDown(0) && Mouse.isInsideWindow()) {
                int mouseX = Mouse.getX();
                int mouseY = Mouse.getY();
                int halfWidth = Display.getWidth() / 2;
                int halfHeight = Display.getHeight() / 2;
                float rotationY = ((mouseX - halfWidth) / (float) Display.getWidth()) * 100.0f;
                float rotationX = ((mouseY - halfHeight) / (float) Display.getHeight()) * 100.0f;
                entity.setRotY(rotationY * 8);
                entity.setRotX(rotationX * 8);
            }*/

            elapsed += System.currentTimeMillis()-last;
            last = System.currentTimeMillis();
            frames++;

            if (elapsed > 1000) {
                elapsed = 0;
                seconds++;
            }

            player.move(terrain2);
            camera.move();

            renderer.processEntity(player);
            //renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for (Entity entity: entities) {
                //if (entity.getPosition().z >= camera.getPosition().z - camera.FAR_PLANE && entity.getPosition().z <= camera.getPosition().z) {
                    renderer.processEntity(entity);
                    //entity.increaseRotation(0.0f, 1f, 0.0f);
                //}
            }

            if (seconds > 0) {
                System.out.printf("\r");
                System.out.printf("Framerate = %d", (long) (frames / seconds));
            }

            renderer.render(light, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static Entity createEntity(Random random, Terrain terrain, TexturedModel model, float scale, int textureIndex) {
        float x = random.nextFloat()*800 - 400;
        float z = random.nextFloat() * -600;
        float y = terrain.getHeightOfTerrain(x, z);

        return new Entity(model, new Vector3f(x, y, z),0,random.nextFloat() * 360,0, scale, textureIndex);
    }
}
