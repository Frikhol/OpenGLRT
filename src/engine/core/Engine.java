package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Entity;
import org.joml.Vector3f;

import static core.visualiser.Renderer.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {

    static Entity some;

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color
        float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        //some = new Entity(vertices,indices);
        some = new Entity("test");
        some.getTransform().setScale(0.3f);
        some.getTransform().translateY(-5f);
    }

    public static void loop(){
        some.getTransform().rotate(new Vector3f(0f,0.01f,0f));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
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
