package entities.bsptree;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Triangle {

    private Vertex[] vertices = new Vertex[3];

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.vertices[0] = v1;
        this.vertices[1] = v2;
        this.vertices[2] = v3;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Vertex getVertex(int num){
        return this.vertices[num];
    }

    public static List<Triangle> createTriangleList(float[] verticesBuffer){
        List<Triangle> triangleList = new ArrayList<>();
        for(int i = 0;i<verticesBuffer.length/18;i++){
            triangleList.add(new Triangle(
                    new Vertex(new Vector3f(verticesBuffer[i*18],verticesBuffer[1+i*18],verticesBuffer[2+i*18]),
                            new Vector3f(verticesBuffer[3+i*18],verticesBuffer[4+i*18],verticesBuffer[5+i*18])),
                    new Vertex(new Vector3f(verticesBuffer[6+i*18],verticesBuffer[7+i*18],verticesBuffer[8+i*18]),
                            new Vector3f(verticesBuffer[9+i*18],verticesBuffer[10+i*18],verticesBuffer[11+i*18])),
                    new Vertex(new Vector3f(verticesBuffer[12+i*18],verticesBuffer[13+i*18],verticesBuffer[14+i*18]),
                            new Vector3f(verticesBuffer[15+i*18],verticesBuffer[16+i*18],verticesBuffer[17+i*18]))));
        }
        return  triangleList;
    }



}
