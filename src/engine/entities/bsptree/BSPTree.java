package entities.bsptree;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BSPTree {
    private Leaf node;
    private int[] triangleId = new int[1];
    private int[] nodeId = new int[1];
    private int[] rootId = new int[1];
    private List<Triangle> polyList;
    private List<Leaf> nodeList;
    private static final int COPLANAR = 100500;
    private static final int FRONT = 100501;
    private static final int BACK = 100502;
    private static final int STRADDLING = 100503;
    private static final int IN_FRONT = 100600;
    private static final int BEHIND = 100601;

    public BSPTree(List<Triangle> triangles,int limit) {
        this.polyList = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.triangleId[0] = 0;
        this.nodeId[0] = 0;
        this.rootId[0] = nodeId[0];
        this.node = BuildBSPTree(triangles,limit);
        this.nodeList.sort(new SortLeaves());
    }

    public List<Leaf> getNodeList() {
        return nodeList;
    }

    public int getNodeId() {
        return nodeId[0];
    }

    public List<Triangle> getPolyList() {
        return polyList;
    }

    static class SortLeaves implements Comparator<Leaf>
    {
        public int compare(Leaf a, Leaf b)
        {
            return a.getNodeId()-b.getNodeId();
        }

    }

    public Leaf BuildBSPTree(List<Triangle> triangles,int limit){
        int polyId = triangleId[0];
        int genId = nodeId[0];
        int backId = rootId[0];

        //Empty check
        if(triangles.isEmpty()) {
            return null;
        }
        int triangleCount = triangles.size();
        //Final leaf
        if(triangleCount<=limit){
            this.getPolyList().add(triangles.get(0));
            Leaf leaf = new Leaf(null,null,genId,backId,polyId,triangles);
            nodeList.add(leaf);
            return leaf;
        }
        //Split
        int splitTriangleIndex = findOptimalTriangle(triangles);
        List<Triangle> left = new ArrayList<>();
        List<Triangle> right = new ArrayList<>();
        for(int i = 0;i<triangleCount;i++){
            Triangle currentTriangle = triangles.get(i);
            switch (classifyTriangleToTriangle(triangles.get(splitTriangleIndex),currentTriangle)){
                case COPLANAR:
                case FRONT:
                    left.add(currentTriangle); //Возможно придется добавлять копию вместо оригинала
                    break;
                case BACK:
                case STRADDLING:
                    right.add(currentTriangle);
                    break;
            }
        }
        int leftSize = left.size();
        int rightSize = right.size();
        if(leftSize==0||rightSize==0){
            if(leftSize>rightSize) {
                int splitSize = Math.round((float)left.size()/2);
                right = left.subList(splitSize,left.size());
                left = left.subList(0,splitSize);
            }
            else{
                int splitSize = Math.round((float)right.size()/2);
                left = right.subList(splitSize,right.size());
                right = right.subList(0,splitSize);
            }
        }
        //Return filled leaf
        rootId[0]=genId;
        nodeId[0]++;
        Leaf leftLeaf = BuildBSPTree(left,1);
        triangleId[0]++;
        rootId[0]=genId;
        nodeId[0]++;
        Leaf rightLeaf = BuildBSPTree(right,1);
        Leaf leaf =  new Leaf(
                leftLeaf,
                rightLeaf,
                genId,
                backId,
                polyId,
                triangles);
        nodeList.add(leaf);
        return leaf;
    }

    private int findOptimalTriangle(List<Triangle> triangles){

        float K = .5f;
        int bestTriangle=0;
        float bestScore = Float.MAX_VALUE;
        for(int i = 0;i<triangles.size();i++){
            //System.out.println("Remaining: "+(triangles.size()-i));
            int frontCount=0;
            int backCount=0;
            int straddlingCount=0;
            for(int j = 0;j<triangles.size();j++){
                if(i==j)
                    continue;
                switch (classifyTriangleToTriangle(triangles.get(i),triangles.get(j))) {
                    case COPLANAR:
                    case FRONT:
                        frontCount++;
                        break;
                    case BACK:
                        backCount++;
                        break;
                    case STRADDLING:
                        straddlingCount++;
                        break;
                }
            }
            float score = K*straddlingCount+(1-K)*Math.abs(frontCount-backCount);
            if(score<bestScore){
                bestScore=score;
                bestTriangle=i;
            }
        }
        return bestTriangle;
    }

    private int classifyTriangleToTriangle(Triangle from,Triangle to){
        int numInFront = 0;
        int numBehind = 0;
        for(int i = 0;i<3;i++){
            switch (classifyVertexToTriangle(from,to.getVertex(i))){
                case IN_FRONT:
                    numInFront++;
                    break;
                case BEHIND:
                    numBehind++;
                    break;
            }
        }
        if(numInFront!=0&&numBehind!=0)
            return STRADDLING;
        if(numInFront!=0)
            return FRONT;
        if(numBehind!=0)
            return BACK;
        return COPLANAR;
    }

    private int classifyVertexToTriangle(Triangle triangle,Vertex point){
        Vector3f p1 = (triangle.getVertex(0).getPosition());
        Vector3f p2 = (triangle.getVertex(1).getPosition());
        Vector3f p3 = (triangle.getVertex(2).getPosition());
        double maxDist = Math.sqrt(Math.pow(((new Vector3f(p2)).sub(p1)).length(),2)+Math.pow(((new Vector3f(p3)).sub(p1)).length(),2));
        Vector3f normal = new Vector3f((p1.sub(p2,p2))).cross(p1.sub(p3,p3));
        Vector3f d1 = (new Vector3f(p1)).sub(point.getPosition());
        Vector3f d2 = (new Vector3f(p2)).sub(point.getPosition());
        Vector3f d3 = (new Vector3f(p3)).sub(point.getPosition());
        double dist = Math.sqrt(Math.pow(d1.length(),2)+Math.pow(d2.length(),2)+Math.pow(d3.length(),2));
        double sign = normal.dot(p1.sub(point.getPosition()));
        if (dist > maxDist) {
            if (sign > 0)
                return IN_FRONT;
            if (sign < 0)
                return BEHIND;
        }
        return -1;
    }

}
