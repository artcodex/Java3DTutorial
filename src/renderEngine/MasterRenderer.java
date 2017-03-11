package renderEngine;

import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static entities.Camera.FAR_PLANE;
import static entities.Camera.FOV;
import static entities.Camera.NEAR_PLANE;

/**
 * Created by abrenner on 3/7/17.
 */
public class MasterRenderer {
    private static boolean useCellShading = false;
    private StaticShader entityShader = new StaticShader();
    private TerrainShader terrainShader = new TerrainShader();
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private SkyboxRenderer skyboxRenderer;
    private Matrix4f projectionMatrix;

    private static final Vector3f skyColor = new Vector3f(0.61f, 0.66f, 0.71f);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);;
    }

    public static boolean isUseCellShading() {
        return useCellShading;
    }

    public static void setUseCellShading(boolean useCellShading) {
        MasterRenderer.useCellShading = useCellShading;
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }

        for (Entity entity : entities) {
            processEntity(entity);
        }

        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        prepare();
        entityShader.start();
        entityShader.loadCullingPlane(clipPlane);
        entityShader.loadUseCellShading(MasterRenderer.isUseCellShading());
        entityShader.loadSkyColor(skyColor);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.stop();
        terrainShader.start();
        terrainShader.loadUseCellShading(MasterRenderer.isUseCellShading());
        terrainShader.loadSkyColor(skyColor);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera, skyColor.getX(), skyColor.getY(), skyColor.getZ());
        terrains.clear();
        entities.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        if (batch == null) {
            batch = new ArrayList<>();
            entities.put(entityModel, batch);
        }

        batch.add(entity);
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z ,0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void cleanUp() {
        entityShader.cleanUp();
        terrainShader.cleanUp();
    }
}
