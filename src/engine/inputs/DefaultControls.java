package inputs;

import core.Engine;
import display.Display;
import entities.Camera;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultControls implements InputList {

    private static boolean pause = false;

    public static boolean isPause() {
        return pause;
    }

    @Override
    public void keyDown(int key, int mods) {
        if(!pause) {
            Camera camera = Engine.getCamera();
            if (key == KeyCode.GLFW_KEY_W)
                camera.move(new Vector3f((float) (.3f * Math.sin(camera.getYaw() * Math.PI / 180)), 0f, (float) (-.3f * Math.cos(camera.getYaw() * Math.PI / 180))));
            if (key == KeyCode.GLFW_KEY_S)
                camera.move(new Vector3f((float) (-.3f * Math.sin(camera.getYaw() * Math.PI / 180)), 0f, (float) (.3f * Math.cos(camera.getYaw() * Math.PI / 180))));
            if (key == KeyCode.GLFW_KEY_A)
                camera.move(new Vector3f((float) (-.3f * Math.cos(camera.getYaw() * Math.PI / 180)), 0f, (float) (-.3f * Math.sin(camera.getYaw() * Math.PI / 180))));
            if (key == KeyCode.GLFW_KEY_D)
                camera.move(new Vector3f((float) (.3f * Math.cos(camera.getYaw() * Math.PI / 180)), 0f, (float) (.3f * Math.sin(camera.getYaw() * Math.PI / 180))));
            if (key == KeyCode.GLFW_KEY_SPACE)
                camera.move(new Vector3f(0f, -0.3f, 0f));
            if (key == KeyCode.GLFW_KEY_LEFT_CONTROL)
                camera.move(new Vector3f(0f, 0.3f, 0f));
        }
    }

    @Override
    public void keyPressed(int key, int mods) {
        if(key == KeyCode.GLFW_KEY_ESCAPE) {
            pause = !pause;
            if(!pause) {
                glfwSetCursorPos(Display.getDisplayID(), Display.getDisplayWIDTH()[0]/2, Display.getDisplayHEIGHT()[0]/2);
                glfwSetInputMode(Display.getDisplayID(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            }
            else
                glfwSetInputMode(Display.getDisplayID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    @Override
    public void keyReleased(int key, int mods) {
    }

    @Override
    public void mouseDown(int button, int mods) {
    }

    @Override
    public void mousePressed(int button, int mods) {
    }

    @Override
    public void mouseReleased(int button, int mods) {
    }
}