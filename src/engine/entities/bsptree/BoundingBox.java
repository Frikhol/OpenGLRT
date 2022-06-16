package entities.bsptree;

import org.joml.Vector3f;

import java.util.List;

public class BoundingBox {
    private Vector3f[] boxPoints;

    public BoundingBox(List<Triangle> triangles) {
        float[] limits = getLimits(triangles);
        this.boxPoints = composePoints(limits);
    }

    public Vector3f getBoxPoint(int num) {
        return boxPoints[num];
    }

    private Vector3f[] composePoints(float[] limits){
        Vector3f[] points = new Vector3f[8];
        points[0] = new Vector3f(limits[1],limits[2],limits[4]);
        points[1] = new Vector3f(limits[0],limits[2],limits[4]);
        points[2] = new Vector3f(limits[0],limits[3],limits[4]);
        points[3] = new Vector3f(limits[1],limits[3],limits[4]);
        points[4] = new Vector3f(limits[1],limits[2],limits[5]);
        points[5] = new Vector3f(limits[0],limits[2],limits[5]);
        points[6] = new Vector3f(limits[0],limits[3],limits[5]);
        points[7] = new Vector3f(limits[1],limits[3],limits[5]);
        return points;
    }

    private float[] getLimits(List<Triangle> triangles){
        int polyCount = triangles.size();
        float[] limits = new float[6];
        limits[0] = -Float.MAX_VALUE; // Max X
        limits[1] = Float.MAX_VALUE; // Min X
        limits[2] = -Float.MAX_VALUE; // Max Y
        limits[3] = Float.MAX_VALUE; // Min Y
        limits[4] = -Float.MAX_VALUE; // Max Z
        limits[5] = Float.MAX_VALUE; // Min Z
        for (Triangle poly : triangles) {
            for (int j = 0; j < 3; j++) {
                limits[0] = Math.max(limits[0], poly.getVertex(j).getPosition().x);
                limits[1] = Math.min(limits[1], poly.getVertex(j).getPosition().x);
                limits[2] = Math.max(limits[2], poly.getVertex(j).getPosition().y);
                limits[3] = Math.min(limits[3], poly.getVertex(j).getPosition().y);
                limits[4] = Math.max(limits[4], poly.getVertex(j).getPosition().z);
                limits[5] = Math.min(limits[5], poly.getVertex(j).getPosition().z);
            }
        }
        return limits;
    }

}
