package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Entity;
import entities.bsptree.BSPTree;
import entities.bsptree.Bufferizer;
import entities.bsptree.Triangle;

import static core.visualiser.Renderer.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

public class Engine {

    private static Entity some;

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color
        some = new Entity("test2");
        BSPTree someTree = new BSPTree(Triangle.createTriangleList(some.getMesh().getPolyBuffer()),1);
        some.getTransform().translateY(1f);
        some.getTransform().rotateX((float) Math.toRadians(-180));

        int nodeSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,nodeSSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER, Bufferizer.createNodeBuffer(someTree),GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,1,nodeSSBO);
        int boxSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,boxSSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER,Bufferizer.createBoxBuffer(someTree),GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,2,boxSSBO);
        int polySSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,polySSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER,Bufferizer.createPolyBuffer(someTree),GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,3,polySSBO);

        //single loop

    }

    public static void loop(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        some.getTransform().rotateY((float) Math.toRadians(-0.2));
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
