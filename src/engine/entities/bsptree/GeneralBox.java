package entities.bsptree;

import org.joml.Vector3f;

import java.util.List;

public class GeneralBox {
    Vector3f position;
    Vector3f size;

    public GeneralBox(List<Triangle> triangleList) {
        float maxX = Float.MIN_VALUE;
        float minX = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxZ = Float.MIN_VALUE;
        float minZ = Float.MAX_VALUE;
        for(int i = 0;i<triangleList.size();i++){
            maxX = Math.max(Math.max(Math.max(triangleList.get(i).getVertex(0).getPosition().x,triangleList.get(i).getVertex(1).getPosition().x),triangleList.get(i).getVertex(2).getPosition().x),maxX);
            minX = Math.min(Math.min(Math.min(triangleList.get(i).getVertex(0).getPosition().x,triangleList.get(i).getVertex(1).getPosition().x),triangleList.get(i).getVertex(2).getPosition().x),minX);
            maxY = Math.max(Math.max(Math.max(triangleList.get(i).getVertex(0).getPosition().y,triangleList.get(i).getVertex(1).getPosition().y),triangleList.get(i).getVertex(2).getPosition().y),maxY);
            minY = Math.min(Math.min(Math.min(triangleList.get(i).getVertex(0).getPosition().y,triangleList.get(i).getVertex(1).getPosition().y),triangleList.get(i).getVertex(2).getPosition().y),minY);
            maxZ = Math.max(Math.max(Math.max(triangleList.get(i).getVertex(0).getPosition().z,triangleList.get(i).getVertex(1).getPosition().z),triangleList.get(i).getVertex(2).getPosition().z),maxZ);
            minZ = Math.min(Math.min(Math.min(triangleList.get(i).getVertex(0).getPosition().z,triangleList.get(i).getVertex(1).getPosition().z),triangleList.get(i).getVertex(2).getPosition().z),minZ);
        }
        this.position = new Vector3f();
        this.size = new Vector3f();
        this.position.x = (maxX+minX)/2;
        this.position.y = (maxY+minY)/2;
        this.position.z = (maxZ+minZ)/2;
        this.size.x = maxX-minX;
        this.size.y = maxY-minY;
        this.size.z = maxZ-minZ;
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public Vector3f getSize() {
        return new Vector3f(size);
    }
}
