package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.frag";

	private static final float ROTATE_SPEED = 1.0f;

	private float rotation;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
	private int location_useCellShading;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;

		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_useCellShading = super.getUniformLocation("useCellShading");
	}

	public void loadUseCellShading(boolean useCellShading) {
		super.loadBoolean(location_useCellShading, useCellShading);
	}

	public void loadBlendFactor(float blendFactor) {
		super.loadFloat(location_blendFactor, blendFactor);
	}

	public void connectCubeMaps() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r, g, b));
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
