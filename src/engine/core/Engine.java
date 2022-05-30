package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Entity;

import static core.visualiser.Renderer.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

public class Engine {

    private static Entity some;
    private static Entity some2;

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color

        //quad v
        float[] vertices = {
                -1.0f,1.0f,0.0f, //2
                -1.0f,-1.0f,0.0f, //3
                1.0f,-1.0f,0.0f, //1

                1.0f,-1.0f,0.0f, //1
                1.0f,1.0f,0.0f, //4
                -1.0f,1.0f,0.0f, //2
        };
        //quad vt
        float[] texCoords = {
                0.0f,0.0f,
                0.0f,1.0f,
                1.0f,1.0f,

                1.0f,1.0f,
                1.0f,0.0f,
                0.0f,0.0f,
        };
        some2 = new Entity("test");
        some2.getTransform().translateZ(2.5f);
        some2.getTransform().rotateX((float) Math.toRadians(-90));
        Renderer.setModel(some2);
        System.out.println(some2.getMesh().getPolyBuffer().length/18);
        int SSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,SSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER,some2.getMesh().getPolyBuffer(),GL_STATIC_DRAW);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,3,SSBO);
        some = new Entity(vertices,texCoords);
    }

    public static void loop(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        some2.getTransform().rotateY((float) Math.toRadians(0.3));
        render();
        glfwSwapBuffers(Display.getDisplayID()); // Don't delete
        glfwPollEvents();
    }

    public static void close(){
        Loader.cleanUp();
        Renderer.cleanUp();
        Display.closeDisplay();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
