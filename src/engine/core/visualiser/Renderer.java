package core.visualiser;

import core.Engine;
import core.visualiser.shaders.MainShader;
import entities.Camera;
import entities.Entity;
import inputs.CursorInputHandler;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tools.Converter;
import tools.Time;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    public static List<Entity> entices = new ArrayList<>();
    private static MainShader shader = new MainShader();
    private static int framesStill;
    private static int sampleTexture1 = glGenTextures();
    private static int sampleTexture2 = glGenTextures();

    public static void setFramesStill(int framesStill) {
        Renderer.framesStill = framesStill;
    }

    public static void prepareRender(){
        shader.start();
        shader.connectTextureUnits();
        shader.stop();
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, sampleTexture1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 720, 720, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, sampleTexture2);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 720, 720, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public static void renderTex(Entity quadSpace){
        //quad v
        shader.start();
        Camera cam = Engine.getCamera();
        shader.loadCamera(cam.getPosition(),new Vector2f(cam.getPitch()*(float)Math.PI/180,cam.getYaw()*(float)Math.PI/180));
        shader.loadTimeAndSeeds(Time.getLastTime(),new Vector2f((float) Math.random(),(float) Math.random()),new Vector2f((float) Math.random(),(float) Math.random()));
        prepareTexturedEntity(quadSpace);
        if (CursorInputHandler.getdX() != 0 || CursorInputHandler.getdY() != 0){
            framesStill = 1;
        }
        shader.connectSampler(1.0f / framesStill);
        int frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        glActiveTexture(GL_TEXTURE2);
        if (framesStill % 2 == 1) {
            glBindTexture(GL_TEXTURE_2D, sampleTexture2);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, sampleTexture1, 0);

        }else {
            glBindTexture(GL_TEXTURE_2D, sampleTexture1);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, sampleTexture2, 0);
        }
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            System.out.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");
        for(Entity entity : entices) {
            shader.loadTransformationMatrix(Converter.createTransformationMatrix(
                    entity.getTransform().getPosition(),
                    entity.getTransform().getRotation(),
                    entity.getTransform().getScale()));
        }
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0,6);
        unbindTexturedModel();
        shader.stop();
        glDeleteFramebuffers(frameBuffer);
    }

    public static void render(Entity quadSpace){
        //quad v
        shader.start();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Camera cam = Engine.getCamera();
        shader.loadCamera(cam.getPosition(),new Vector2f(cam.getPitch()*(float)Math.PI/180,cam.getYaw()*(float)Math.PI/180));
        shader.loadTimeAndSeeds(Time.getLastTime(),new Vector2f((float) Math.random(),(float) Math.random()),new Vector2f((float) Math.random(),(float) Math.random()));
        prepareTexturedEntity(quadSpace);
        shader.connectSampler(1.0f / framesStill);
        glActiveTexture(GL_TEXTURE2);
        if (framesStill % 2 == 1)
            glBindTexture(GL_TEXTURE_2D, sampleTexture1);
        else
            glBindTexture(GL_TEXTURE_2D, sampleTexture2);
        glActiveTexture(GL_TEXTURE0);
        for(Entity entity : entices) {
            shader.loadTransformationMatrix(Converter.createTransformationMatrix(
                    entity.getTransform().getPosition(),
                    entity.getTransform().getRotation(),
                    entity.getTransform().getScale()));
        }
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0,6);
        unbindTexturedModel();
        shader.stop();
        framesStill++;
    }

    private static void prepareTexturedEntity(Entity entity){
        GL30.glBindVertexArray(entity.getMesh().getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(4);
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

