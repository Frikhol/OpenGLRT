package renderiser;

import core.Entity;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    public static List<Entity> entices = new ArrayList<>();
    private static MainShader shader = new MainShader();

    public static void render(){
        shader.start();
        for(Entity entity : entices) {
            glBindVertexArray(entity.getMesh().getVaoId());
            glEnableVertexAttribArray(0);
            glDrawArrays(GL_TRIANGLES, 0, entity.getMesh().getVertexCount());
        }
        shader.stop();
    }

    public static void cleanUp(){
        shader.cleanUp();
    }

}

