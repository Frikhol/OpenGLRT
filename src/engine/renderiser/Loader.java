package renderiser;

import org.lwjgl.BufferUtils;

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

    public static int load(float[] positions){
        int vaoId = createVAO();
        createAndFillVBO(vaoId,0,3,positions);
        glBindVertexArray(0);
        return vaoId;
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

    public static void cleanUp(){
        for(Map.Entry<Integer,List<Integer>> VO : VOMap.entrySet()) {
            glDeleteVertexArrays(VO.getKey());
            for(int vbo : VO.getValue())
                glDeleteBuffers(vbo);
        }
    }

}
