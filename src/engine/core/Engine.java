package core;

import core.visualiser.Loader;
import core.visualiser.Renderer;
import display.Display;
import entities.Camera;
import entities.Entity;
import entities.bsptree.BSPTree;
import entities.bsptree.Bufferizer;
import entities.bsptree.Triangle;
import inputs.*;
import org.joml.Vector3f;
import tools.Time;

import static core.visualiser.Renderer.render;
import static core.visualiser.Renderer.renderTex;
import static display.Display.countTime;
import static display.Display.getDisplayID;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

public class Engine {

    private static Entity some;
    private static InputList inputList;
    private static Camera camera;
    private static Entity quadSpace;

    public static InputList getInputList() {
        return inputList;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static void start(){
        Display.createDisplay();
        glClearColor(0f,  0f, 0f, 0.0f); //background's color
        inputList = new DefaultControls();
        camera = new Camera(new Vector3f(-2.0f,-12.0f,50.0f),new Vector3f(),13,0,0);
        glfwSetKeyCallback(getDisplayID(), KeyInputHandler.keyCallback);
        glfwSetMouseButtonCallback(getDisplayID(), MouseInputHandler.mouseButtonCallback);
        glfwSetCursorPosCallback(getDisplayID(), CursorInputHandler.cursorPosCallback);
        some = new Entity("test");
        BSPTree someTree = new BSPTree(Triangle.createTriangleList(some.getMesh().getPolyBuffer()),1);
        System.out.println("Tree built successfully");
        some.getTransform().translateY(1f);
        some.getTransform().rotateX((float) Math.toRadians(-180));
        some.getTransform().rotateY((float) Math.toRadians(-30));


        int[] nodeBuffer = Bufferizer.createNodeBuffer(someTree);
        float[] boxBuffer = Bufferizer.createBoxBuffer(someTree);
        float[] polyBuffer = Bufferizer.createPolyBuffer(someTree);

        int nodeSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,nodeSSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER, nodeBuffer,GL_STATIC_DRAW);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,1,nodeSSBO);
        int quadSSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,quadSSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER, boxBuffer,GL_STATIC_DRAW);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,2,quadSSBO);
        int polySSBO = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER,polySSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER, polyBuffer,GL_STATIC_DRAW);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER,3,polySSBO);


        float[] vertices = new float[]{
                -1.0f, 1.0f, 0.0f, //2
                -1.0f, -1.0f, 0.0f, //3
                1.0f, -1.0f, 0.0f, //1

                1.0f, -1.0f, 0.0f, //1
                1.0f, 1.0f, 0.0f, //4
                -1.0f, 1.0f, 0.0f, //2
        };
        //quad vt
        float[] texCords = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,

                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
        };
        quadSpace = new Entity(vertices, texCords);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D,Loader.loadTexture("skybox"));
        Renderer.prepareRender();
        CursorInputHandler.cursorInputs();
    }



    public static void loop(){
        countTime();
        KeyInputHandler.getInputs();
        MouseInputHandler.getInputs();
        CursorInputHandler.cursorInputs();
        renderTex(quadSpace);
        render(quadSpace);
        System.out.println(Time.getDeltaTime());
        glfwSwapBuffers(getDisplayID()); // Don't delete
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
