package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Entity;
import inputs.*;

import static core.visualiser.Renderer.render;
import static display.Display.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {

    private static Entity some;
    private static InputList inputList;

    public static InputList getInputList() {
        return inputList;
    }

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color
        inputList = new DefaultControls();
        glfwSetKeyCallback(getDisplayID(), KeyInputHandler.keyCallback);
        glfwSetMouseButtonCallback(getDisplayID(), MouseInputHandler.mouseButtonCallback);
        glfwSetCursorPosCallback(getDisplayID(), CursorInputHandler.cursorPosCallback);
        //some = new Entity("test2");
        //BSPTree someTree = new BSPTree(Triangle.createTriangleList(some.getMesh().getPolyBuffer()),1);
        //System.out.println("Tree built");
        //some.getTransform().translateY(1f);
        //some.getTransform().rotateX((float) Math.toRadians(-180));

        /*int nodeSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,nodeSSBO);
        int[] nodeBuffer = Bufferizer.createNodeBuffer(someTree);
        System.out.println(nodeBuffer.length);
        glBufferData(GL_SHADER_STORAGE_BUFFER, nodeBuffer,GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,1,nodeSSBO);
        int boxSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,boxSSBO);
        float[] boxBuffer = Bufferizer.createBoxBuffer(someTree);
        System.out.println(boxBuffer.length);
        glBufferData(GL_SHADER_STORAGE_BUFFER,boxBuffer,GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,2,boxSSBO);
        int polySSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,polySSBO);
        float[] polyBuffer = Bufferizer.createPolyBuffer(someTree);
        System.out.println(polyBuffer.length);
        glBufferData(GL_SHADER_STORAGE_BUFFER,polyBuffer,GL_DYNAMIC_READ);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,3,polySSBO);
        */
        //single loop

    }

    public static void loop(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //some.getTransform().rotateY((float) Math.toRadians(-0.2));
        KeyInputHandler.getInputs();
        MouseInputHandler.getInputs();
        CursorInputHandler.cursorInputs();
        System.out.println("x:"+getCursorX()+" y:"+getCursorY());
        render();
        glfwSwapBuffers(getDisplayID()); // Don't delete
        debugError();
        glfwPollEvents();

    }

    private static void debugError(){
        int errorCode = glGetError();
        if (errorCode != GL_NO_ERROR) {
            throw new RuntimeException("GL error " + errorCode);
        }
    }

    public static void close(){
        Loader.cleanUp();
        Renderer.cleanUp();
        Display.closeDisplay();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
