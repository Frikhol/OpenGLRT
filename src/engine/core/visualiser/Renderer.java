package core.visualiser;

import core.visualiser.shaders.MainShader;
import entities.Entity;
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
        //shader.loadProjectionMatrix(Converter.createProjectionMatrix());
        //shader.loadViewMatrix(new Camera(new Vector3f(0f,0f,5f),new Vector3f(0f,0f,0f),0f,0f,0f));
        //shader.loadLight(new Light(new Vector3f(1000f,1000f,2000f),new Vector3f(1,1,1)));
        for(Entity entity : entices) {
            prepareTexturedEntity(entity);
            //shader.loadTransformationMatrix(Converter.createTransformationMatrix(
                    //entity.getTransform().getPosition(),
                    //entity.getTransform().getRotation(),
                    //entity.getTransform().getScale()));
            GL11.glDrawArrays(GL11.GL_TRIANGLES,0,6);
            unbindTexturedModel();
        }
        shader.stop();
    }

    private static void prepareTexturedEntity(Entity entity){
        GL30.glBindVertexArray(entity.getMesh().getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(4);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTextureId());
    }

    private static void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(4);
        GL30.glBindVertexArray(0);
    }

    public static void cleanUp(){
        shader.cleanUp();
    }

}

