package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import java.util.List;

/**
 * Created by avibrenner on 3/2/17.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_SHADER="src/shaders/vertexShader.vert";
    private static final String FRAGMENT_SHADER="src/shaders/fragmentShader.frag";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_lightAttenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_xyOffset;
    private int location_numberOfRows;
    private int location_useCellShading;
    private int location_plane;


    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_xyOffset = super.getUniformLocation("xyOffset");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_useCellShading = super.getUniformLocation("useCellShading");
        location_plane = super.getUniformLocation("plane");

        //Fix this code to directly work with uniform arrays in one shot.
        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        location_lightAttenuation = new int[MAX_LIGHTS];

        for (int i=0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor["+ i + "]");
            location_lightAttenuation[i] = super.getUniformLocation("lightAttenuation["+ i + "]");
        }
    }

    @Override
    protected void bindAttributes() {
        //TODO: We can also layout locations in shader code itself for this and don't need the bind
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uv");
        super.bindAttribute(2, "normal");

    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadCullingPlane(Vector4f plane) {
        super.loadVector(location_plane, plane);
    }

    public void loadLights(List<Light> lights) {
        //TODO: Improve performance by only handling as many lights as exist in the shader code
        //and not go over blank lights
        for (int i=0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColor[i], lights.get(i).getColor());
                super.loadVector(location_lightAttenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
                super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
                super.loadVector(location_lightAttenuation[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadUseCellShading(boolean useCellShading) {
        super.loadBoolean(location_useCellShading, useCellShading);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadUseFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadOffset(float offsetX, float offsetY) {
        super.loadVector(location_xyOffset, new Vector2f(offsetX, offsetY));
    }

    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(location_skyColor, skyColor);
    }
}
