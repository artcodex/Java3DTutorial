package shaders;

/**
 * Created by avibrenner on 3/2/17.
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_SHADER="src/shaders/vertexShader.vert";
    private static final String FRAGMENT_SHADER="src/shaders/fragmentShader.frag";

    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }
    @Override
    protected void bindAttributes() {
        //TODO: We can also layout locations in shader code itself for this and don't need the bind
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uv");
    }
}
