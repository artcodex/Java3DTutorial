package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by abrenner on 3/2/17.
 */
public class BasicShaderHelper {
    public static void Initialize() {
        int program = GL20.glCreateProgram();

        // create a vertex shader
        int vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShaderId, toByteBuffer(
                "#version 150 core\n" +
                        "in vec3 vVertex;\n" +
                        "void main() {\n" +
                        "  gl_Position = vec4(vVertex.x, vVertex.y, vVertex.z, 1.0);\n" +
                        "}"));
        GL20.glCompileShader(vertexShaderId);
        if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.exit(-1);
        }

        // create a fragment shader
        int fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShaderId, toByteBuffer(
                "#version 150 core\n" +
                        "out vec4 color;\n" +
                        "void main() {\n" +
                        "  color = vec4(1.0);\n" +
                        "}"));
        GL20.glCompileShader(fragmentShaderId);
        if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.exit(-1);
        }

        // attach to program
        GL20.glAttachShader(program, vertexShaderId);
        GL20.glAttachShader(program, fragmentShaderId);
        GL20.glLinkProgram(program);
        GL20.glUseProgram(program);
    }

    private static ByteBuffer toByteBuffer(final String data) {
        byte[] vertexShaderData = data.getBytes(Charset.forName("ISO-8859-1"));
        ByteBuffer vertexShader = BufferUtils.createByteBuffer(vertexShaderData.length);
        vertexShader.put(vertexShaderData);
        vertexShader.flip();
        return vertexShader;
    }
}
