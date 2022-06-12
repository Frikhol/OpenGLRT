package entities.bsptree;

public class Bufferizer {

    public static int[] createNodeBuffer(BSPTree bspTree){
        int nodeCount = bspTree.getNodeId();
        int[] nodeBuffer = new int[(nodeCount+1)*5];
        for(int i = 0;i<nodeCount;i++){
            Leaf leaf = bspTree.getNodeList().get(i);
            nodeBuffer[i*5] = (leaf.getLeft() == null)?-1:leaf.getLeft().getNodeId();
            nodeBuffer[1+i*5] = (leaf.getRight() == null)?-1:leaf.getRight().getNodeId();
            nodeBuffer[2+i*5] = leaf.getRootId();
            nodeBuffer[3+i*5] = leaf.getTriangleId();
            nodeBuffer[4+i*5] = leaf.getTriangleList().size();
        }
        return nodeBuffer;
    }

    public static float[] createBoxBuffer(BSPTree bspTree){
        int nodeCount = bspTree.getNodeId();
        float[] boxBuffer = new float[(nodeCount+1)*6];
        for(int i = 0;i<nodeCount;i++){
            Leaf leaf = bspTree.getNodeList().get(i);
            boxBuffer[i*6] = leaf.getGenBox().getPosition().x;
            boxBuffer[1+i*6] = leaf.getGenBox().getPosition().y;
            boxBuffer[2+i*6] = leaf.getGenBox().getPosition().z;
            boxBuffer[3+i*6] = leaf.getGenBox().getSize().x;
            boxBuffer[4+i*6] = leaf.getGenBox().getSize().y;
            boxBuffer[5+i*6] = leaf.getGenBox().getSize().z;
        }
        return boxBuffer;
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
