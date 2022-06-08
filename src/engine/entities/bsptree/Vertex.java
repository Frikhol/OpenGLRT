package entities.bsptree;

import org.joml.Vector3f;

public class Vertex {

    private Vector3f position;
    private Vector3f normal;

    public Vertex(Vector3f position, Vector3f normal) {
        this.position = position;
        this.normal = normal;
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public Vector3f getNormal() {
        return new Vector3f(normal);
    }

}
