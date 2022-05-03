package display;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Display {
    private static long displayID;
    private static int[] WIDTH = new int[1]; //make changeable
    private static int[] HEIGHT = new int[1]; //make changeable
    //private static int SAMPLES = 16;

    public static long getDisplayID(){
        return displayID;
    }

    public static int[] getDisplayWIDTH() {
        return WIDTH;
    }

    public static int[] getDisplayHEIGHT() {
        return HEIGHT;
    }

    public static void createDisplay() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_SAMPLES, 16);
        WIDTH[0] = 1280;
        HEIGHT[0] = 720;
        displayID = glfwCreateWindow(WIDTH[0], HEIGHT[0], "OpenGLRT", NULL, NULL);
        if (displayID == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(displayID,(vidMode.width() - WIDTH[0]) / 2,(vidMode.height() - HEIGHT[0]) / 2);
        glfwMakeContextCurrent(displayID);
        glfwSwapInterval(1);
        glfwShowWindow(displayID);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    public static void closeDisplay() {
        glfwFreeCallbacks(displayID);
        glfwDestroyWindow(displayID);
    }

}
