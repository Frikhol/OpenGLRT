package entities.components;

import entities.bsptree.BSPTree;

public class Mesh {
    private int vaoId;
    private float[] polyBuffer;
    private int vertexCount;
    private BSPTree bspTree;

    public Mesh(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public Mesh(int vaoId, int vertexCount, float[] polyBuffer) {
        this.vaoId = vaoId;
        this.polyBuffer = polyBuffer;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public float[] getPolyBuffer() {
        return polyBuffer;
    }

    public void setPolyBuffer(float[] polyBuffer) {
        this.polyBuffer = polyBuffer;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
