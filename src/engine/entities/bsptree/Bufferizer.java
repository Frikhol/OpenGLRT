package entities.bsptree;

public class Bufferizer {

    public static int[] createNodeBuffer(BSPTree bspTree){
        int nodeCount = bspTree.getNodeId();
        System.out.println(nodeCount);
        int[] nodeBuffer = new int[(nodeCount+1)*4];
        for(int i = 0;i<nodeCount+1;i++){
            Leaf leaf = bspTree.getNodeList().get(i);
            nodeBuffer[i*4] = getNext(bspTree,i);
            nodeBuffer[1+i*4] = leaf.getTriangleId();
            nodeBuffer[2+i*4] = leaf.getTriangleList().size();
            nodeBuffer[3+i*4] = leaf.getLeft()==null?1:0;
        }
        return nodeBuffer;
    }

    private static int getNext(BSPTree bspTree,int id){
        if(id == 0)
            return -1;
        Leaf leaf = bspTree.getNodeList().get(id);
        if(leaf.getLeft()==null)
            if(id!=bspTree.getNodeId())
                return id+1;
        int root = leaf.getRootId();
        int right = bspTree.getNodeList().get(root).getRight().getNodeId();
        while(id == right) {
            id = root;
            if(id == 0)
                return -1;
            root = bspTree.getNodeList().get(id).getRootId();
            right = bspTree.getNodeList().get(root).getRight().getNodeId();
        }
        return right;
    }

    public static float[] createBoxBuffer(BSPTree bspTree){
        int nodeCount = bspTree.getNodeId();
        float[] quadBuffer = new float[(nodeCount+1)*24];
        for(int i = 0;i<nodeCount+1;i++) {
            BoundingBox box = bspTree.getNodeList().get(i).getBoundingBox();
            quadBuffer[i * 24] = box.getBoxPoint(0).x;
            quadBuffer[1 + i * 24] = box.getBoxPoint(0).y;
            quadBuffer[2 + i * 24] = box.getBoxPoint(0).z;
            quadBuffer[3 + i * 24] = box.getBoxPoint(1).x;
            quadBuffer[4 + i * 24] = box.getBoxPoint(1).y;
            quadBuffer[5 + i * 24] = box.getBoxPoint(1).z;
            quadBuffer[6 + i * 24] = box.getBoxPoint(2).x;
            quadBuffer[7 + i * 24] = box.getBoxPoint(2).y;
            quadBuffer[8 + i * 24] = box.getBoxPoint(2).z;
            quadBuffer[9 + i * 24] = box.getBoxPoint(3).x;
            quadBuffer[10 + i * 24] = box.getBoxPoint(3).y;
            quadBuffer[11 + i * 24] = box.getBoxPoint(3).z;
            quadBuffer[12 + i * 24] = box.getBoxPoint(4).x;
            quadBuffer[13 + i * 24] = box.getBoxPoint(4).y;
            quadBuffer[14 + i * 24] = box.getBoxPoint(4).z;
            quadBuffer[15 + i * 24] = box.getBoxPoint(5).x;
            quadBuffer[16 + i * 24] = box.getBoxPoint(5).y;
            quadBuffer[17 + i * 24] = box.getBoxPoint(5).z;
            quadBuffer[18 + i * 24] = box.getBoxPoint(6).x;
            quadBuffer[19 + i * 24] = box.getBoxPoint(6).y;
            quadBuffer[20 + i * 24] = box.getBoxPoint(6).z;
            quadBuffer[21 + i * 24] = box.getBoxPoint(7).x;
            quadBuffer[22 + i * 24] = box.getBoxPoint(7).y;
            quadBuffer[23 + i * 24] = box.getBoxPoint(7).z;
        }
        return quadBuffer;
    }

    public static float[] createPolyBuffer(BSPTree bspTree){
        int triangleCount = bspTree.getPolyList().size();
        float[] polyBuffer = new float[triangleCount*18];
        for(int i = 0;i<triangleCount;i++){
            Triangle poly = bspTree.getPolyList().get(i);
            polyBuffer[i*18] = poly.getVertex(0).getPosition().x;
            polyBuffer[1+i*18] = poly.getVertex(0).getPosition().y;
            polyBuffer[2+i*18] = poly.getVertex(0).getPosition().z;
            polyBuffer[3+i*18] = poly.getVertex(0).getNormal().x;
            polyBuffer[4+i*18] = poly.getVertex(0).getNormal().y;
            polyBuffer[5+i*18] = poly.getVertex(0).getNormal().z;
            polyBuffer[6+i*18] = poly.getVertex(1).getPosition().x;
            polyBuffer[7+i*18] = poly.getVertex(1).getPosition().y;
            polyBuffer[8+i*18] = poly.getVertex(1).getPosition().z;
            polyBuffer[9+i*18] = poly.getVertex(1).getNormal().x;
            polyBuffer[10+i*18] = poly.getVertex(1).getNormal().y;
            polyBuffer[11+i*18] = poly.getVertex(1).getNormal().z;
            polyBuffer[12+i*18] = poly.getVertex(2).getPosition().x;
            polyBuffer[13+i*18] = poly.getVertex(2).getPosition().y;
            polyBuffer[14+i*18] = poly.getVertex(2).getPosition().z;
            polyBuffer[15+i*18] = poly.getVertex(2).getNormal().x;
            polyBuffer[16+i*18] = poly.getVertex(2).getNormal().y;
            polyBuffer[17+i*18] = poly.getVertex(2).getNormal().z;
        }
        return polyBuffer;
    }

}
