package renderEngine;

import Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abrenner on 3/2/17.
 */
public class Loader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] uv, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, uv);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;

        try {
            //TODO: Write code with actual texture functions this is a helper
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (texture != null) {
            int textureID = texture.getTextureID();
            textures.add(textureID);

            return textureID;
        }

        return -1;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaos.add(vaoID);

        return vaoID;
    }

    public void cleanUp() {
        for (int vao:vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo:vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for (int texture:textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);

        //Make buffer ready to be read from (writing done)
        buffer.flip();

        return buffer;
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private void unbindVAO() {
        //Unbind the VAO
        GL30.glBindVertexArray(0);
    }
}
