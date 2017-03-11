package engineTester;

import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import primitives.Cube;
import renderEngine.*;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import toolbox.SettingsHelper;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

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
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        //TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");

        MasterRenderer renderer = new MasterRenderer(loader);

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

        TexturedModel pine = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
                new ModelTexture(loader.loadTexture("pine")));

        grass.getModelTexture().setHasTransparency(true);
        grass.getModelTexture().setUseFakeLighting(true);

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern2")));
        fern.getModelTexture().setNumberOfRows(2);

        TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("person", loader),
                new ModelTexture(loader.loadTexture("playerTexture")));

        bunnyModel.getModelTexture().setShineDamper(10);
        bunnyModel.getModelTexture().setReflectivity(1.0f);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));

        TexturedModel bobbleTree = new TexturedModel(OBJLoader.loadObjModel("bobbleTree", loader),
                new ModelTexture(loader.loadTexture("bobbleTree")));

        TexturedModel toonRock = new TexturedModel(OBJLoader.loadObjModel("toonRocks", loader),
                new ModelTexture(loader.loadTexture("toonRocks")));

        lamp.getModelTexture().setUseFakeLighting(true);

        fern.getModelTexture().setUseFakeLighting(true);
        fern.getModelTexture().setHasTransparency(true);

        Player player = new Player(bunnyModel, new Vector3f(100,0,-150), 0,180,0,0.5f);
        Camera camera = new Camera(player);

        //Light light = new Light(new Vector3f(0,1000,-7000), new Vector4f(1f, 1f, 1f, 1f));
        Light light = new Light(new Vector3f(0,1000,-7000), new Vector4f(0.2f, 0.2f, 0.2f, 1f));
        List<Light> lights = new ArrayList<>();
        lights.add(light);
        lights.add(new Light(new Vector3f(185,10,-293), new Vector4f(2,0,0,1), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300), new Vector4f(0,2,2,1), new Vector3f(1, 0.01f, 0.002f)));
        Light movableLight = new Light(new Vector3f(293,7,-305), new Vector4f(2,2,0,1), new Vector3f(1, 0.01f, 0.002f));
        lights.add(movableLight);

        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(lamp, new Vector3f(185,-4.7f,-293), 0, 0, 0, 1));
        entities.add(new Entity(lamp, new Vector3f(370,4.2f,-300), 0, 0, 0, 1));
        Entity movableLamp = new Entity(lamp, new Vector3f(293,-6.8f,-305), 0, 0, 0, 1);
        entities.add(movableLamp);
        entities.add(player);

        Random random = new Random();
        /*for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800,0,random.nextFloat() * 600),0,0,0,3));
        }*/

        for(int i=0;i<200;i++){
            entities.add(createEntity(random, terrain2, staticModel, 3, 0));
            entities.add(createEntity(random, terrain2, lowPolyTree, 0.5f, 0));
            //entities.add(createEntity(random, terrain2, pine, 1.0f, 0));
            entities.add(createEntity(random, terrain2, bobbleTree, 0.5f, 0));
            entities.add(createEntity(random, terrain2, toonRock, 1.0f, 0));
            //entities.add(createEntity(random, terrain2, grass, 1));
            //entities.add(createEntity(random, terrain2, flower, 1));
            //entities.add(createEntity(random, terrain2, fern, 0.9f, random.nextInt(4)));
        }

        //entities.add(new Entity(box, new Vector3f(100, 0,-200),0,0,0,5));
        //entities.add(new Entity(box, new Vector3f(0, 0,-250),0,0,0,5));

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.5f, 0.7f), new Vector2f(0.25f, 0.25f));
        GuiTexture gui3 = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.7f, -0.7f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        guis.add(gui2);
        guis.add(gui3);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain2);
        SettingsHelper settings = new SettingsHelper();

        //Setup whether to use cell Shading or not
        MasterRenderer.setUseCellShading(false);
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain2);

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<>();
        waters.add(new WaterTile(140,-175, 0));

        WaterFrameBuffers fbos = new WaterFrameBuffers();
        GuiTexture gui4 = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui4);

        while(!Display.isCloseRequested()) {
            player.move(terrain2);
            camera.move();
            picker.update();
            settings.update();

            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null && Mouse.isButtonDown(0)) {
                movableLamp.setPosition(terrainPoint);
                movableLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y+15, terrainPoint.z));
            }

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            fbos.bindReflectionFrameBuffer();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, 15));
            fbos.unbindCurrentFrameBuffer();

            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -5));
            guiRenderer.render(guis);
            waterRenderer.render(waters, camera);

            DisplayManager.updateDisplay();
        }

        fbos.cleanUp();
        waterShader.cleanUp();
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
