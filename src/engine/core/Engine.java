package core;

import display.Display;
import renderiser.Loader;
import renderiser.Renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static renderiser.Renderer.render;

public class Engine {

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color
        float pos[] = {
                0.5f,  0.5f, 0.0f,  // top right
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f,  0.5f, 0.0f,  // top left
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f, -0.5f, 0.0f,  // bottom left
                -0.5f,  0.5f, 0.0f   // top left
        };
        new Entity("test",pos);
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
