package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Entity;

import static core.visualiser.Renderer.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {

    private static Entity some;

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color

        float[] vertices = {
                -1.0f,1.0f,0.0f, //2
                -1.0f,-1.0f,0.0f, //3
                1.0f,-1.0f,0.0f, //1

                1.0f,-1.0f,0.0f, //1
                1.0f,1.0f,0.0f, //4
                -1.0f,1.0f,0.0f, //2
        };

        float[] texCoords = {
                0.0f,0.0f,
                0.0f,1.0f,
                1.0f,1.0f,

                1.0f,1.0f,
                1.0f,0.0f,
                0.0f,0.0f,
        };
        Entity some2 = new Entity("test");
        some = new Entity(vertices,texCoords);
        some.getMesh().setPolyBuffer(some2.getMesh().getPolyBuffer());
    }

    public static void loop(){
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
