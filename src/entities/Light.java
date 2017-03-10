package entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by abrenner on 3/6/17.
 */
public class Light {
    private Vector3f position;
    private Vector4f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    public Light(Vector3f position, Vector4f color) {
        this.position = position;
        this.color = color;
    }

    public Light(Vector3f position, Vector4f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }
}
