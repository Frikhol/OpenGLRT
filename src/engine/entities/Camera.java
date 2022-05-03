package entities;

import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f direction;
    private float pitch;
    private float roll;
    private float yaw;

    public Camera(Vector3f position, Vector3f direction, float pitch, float roll, float yaw) {
        this.position = position;
        this.direction = direction;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }
}
