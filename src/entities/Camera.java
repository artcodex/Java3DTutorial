package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by abrenner on 3/3/17.
 */
public class Camera {
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000.0f;

    private Vector3f position = new Vector3f(20,1,30);
    private float pitch = 20.0f;
    private float yaw;
    private float roll;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        /*if (Mouse.isButtonDown(0)) {
            position.z -= 0.2f;
        }

        if (Mouse.isButtonDown(1)) {
            position.z += 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            position.x += 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            position.x -= 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            position.y -= 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            position.y += 0.2f;
        }*/
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        position.y = player.getPosition().y + verticalDistance;
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;

        this.yaw = 180 - (theta);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        float testDistance = distanceFromPlayer - zoomLevel;

        if (testDistance > 20 && testDistance < 100) {
            distanceFromPlayer = testDistance;
        }
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            float testPitch = pitch - pitchChange;

            if (testPitch > 0.5 && testPitch < 40) {
                pitch = testPitch;
            }
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
