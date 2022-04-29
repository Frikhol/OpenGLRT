package visualiser;

import core.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    public static List<Entity> entices = new ArrayList<>();
    private static MainShader shader = new MainShader();

    public static void render(){
        shader.start();
        for(Entity entity : entices) {
            prepareTexturedEntity(entity);
            GL11.glDrawElements(GL11.GL_TRIANGLES,entity.getMesh().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        }
        shader.stop();
    }

    private static void prepareTexturedEntity(Entity entity){
        GL30.glBindVertexArray(entity.getMesh().getVaoId());
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTextureId());
    }

    private static void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    public static void cleanUp(){
        shader.cleanUp();
    }

}

