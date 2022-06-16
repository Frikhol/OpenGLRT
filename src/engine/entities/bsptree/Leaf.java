package entities.bsptree;

import java.util.List;

public class Leaf {
    private Leaf left;
    private Leaf right;
    private int nodeId;
    private int rootId;
    private BoundingBox BBox;
    private int triangleId;
    private List<Triangle> triangleList;

    public int getNodeId() {
        return nodeId;
    }

    public int getRootId() {
        return rootId;
    }

    public Leaf getLeft() {
        return left;
    }

    public Leaf getRight() {
        return right;
    }

    public int getTriangleId() {
        return triangleId;
    }

    public List<Triangle> getTriangleList() {
        return triangleList;
    }

    public BoundingBox getBoundingBox() {
        return BBox;
    }

    public Leaf(Leaf left, Leaf right, int nodeId, int rootId, int triangleId, List<Triangle> triangleList) {
        this.left = left;
        this.right = right;
        this.triangleList = triangleList;
        this.triangleId = triangleId;
        this.BBox = new BoundingBox(triangleList);
        this.nodeId = nodeId;
        this.rootId = rootId;
    }
}
