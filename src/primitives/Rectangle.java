package primitives;

/**
 * Created by abrenner on 3/3/17.
 */
public class Rectangle {
    public float[] vertices = {
            -0.5f, 0.5f, 0f, // V0
            -0.5f, -0.5f, 0f, // V1
            0.5f, -0.5f, 0f, // V2
            0.5f, 0.5f, 0f // V3
    };

    public float[] uv = {
            0.0f,0.0f, // V0
            0.0f,1.0f, // V1
            1.0f,1.0f, // V2
            1.0f,0.0f // V3
    };

    public int[] indices = {
            0,1,3,
            3,1,2
    };
}
