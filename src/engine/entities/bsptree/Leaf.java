package entities.bsptree;

import java.util.List;

public class Leaf {
    private Leaf left;
    private Leaf right;
    private int nodeId;
    private GeneralBox genBox;
    private int triangleId;
    private List<Triangle> triangleList;

    public int getNodeId() {
        return nodeId;
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

    public GeneralBox getGenBox() {
        return genBox;
    }

    public Leaf(Leaf left, Leaf right, int nodeId, int triangleId, List<Triangle> triangleList) {
        this.left = left;
        this.right = right;
        this.triangleList = triangleList;
        this.triangleId = triangleId;
        this.genBox = new GeneralBox(triangleList);
        this.nodeId = nodeId;
    }
}
