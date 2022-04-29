package visualiser;

import core.Mesh;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {

    /**
     * Map for storing VAO id's and their VBO id's
     */
    private static Map<Integer, List<Integer>> VOMap = new HashMap<Integer,List<Integer>>();

    public static Mesh load(float[] positions){
        int vaoId = createVAO();
        createAndFillVBO(vaoId,0,3,positions);
        glBindVertexArray(0);
        return new Mesh(vaoId,positions.length/3);
    }

    private static Mesh load (float[] positions, int[] indices, float[] textureCoords, float[] normals){
        int vaoId = createVAO();
        createAndFillEBO(vaoId,0,indices);
        createAndFillVBO(vaoId,1,3,positions);
        createAndFillVBO(vaoId,2,2,textureCoords);
        createAndFillVBO(vaoId,3,3,normals);
        glBindVertexArray(0);
        return new Mesh(vaoId,indices.length);
    }

    private static int createVAO(){
        int vaoId = glGenVertexArrays();
        VOMap.put(vaoId,new ArrayList<>());
        glBindVertexArray(vaoId);
        return vaoId;
    }

    private static void createAndFillVBO(int vaoId,int attributeNumber,int coordinateSize,float[] data){
        VOMap.get(vaoId).add(attributeNumber,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,VOMap.get(vaoId).get(attributeNumber));
        glBufferData(GL_ARRAY_BUFFER,toFloatBuffer(data),GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber,coordinateSize,GL_FLOAT,false,0,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    private static void createAndFillEBO(int vaoId,int attributeNumber,int[] data){
        VOMap.get(vaoId).add(attributeNumber,glGenBuffers());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,VOMap.get(vaoId).get(attributeNumber));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,toIntBuffer(data),GL_STATIC_DRAW);
    }

    private static FloatBuffer toFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer((data.length));
        buffer.put(data).flip();
        return  buffer;
    }

    private static IntBuffer toIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer((data.length));
        buffer.put(data).flip();
        return  buffer;
    }

    public static int loadTexture(String fileName){
        PNGDecoder decoder = null;
        try {
            decoder = new PNGDecoder(Loader.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        int id = GL15.glGenTextures();
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_LOD_BIAS, -0);
        GL11.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL20.GL_TEXTURE_2D);
        GL11.glEnable(GL20.GL_TEXTURE_2D);
        return id;
    }

    public static Mesh loadObjModel (String filename){
        FileReader fr = null;
        try {
            fr = new FileReader(new File("Assets/models/"+filename+".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
        ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        try{
            while (true){
                line=reader.readLine();
                String[] currentLine = line.split(" ");
                if(line.startsWith("v ")){
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if(line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if(line.startsWith("vn ")){
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if(line.startsWith("f ")){
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }
            while(line!=null){
                if(!line.startsWith("f")){
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex:vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;

        }
        for(int i = 0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load(verticesArray,indicesArray,textureArray,normalsArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,float[] textureArray,float[] normalsArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt((vertexData[1]))-1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }

    public static void cleanUp(){
        for(Map.Entry<Integer,List<Integer>> VO : VOMap.entrySet()) {
            glDeleteVertexArrays(VO.getKey());
            for(int vbo : VO.getValue())
                glDeleteBuffers(vbo);
        }
    }

}
