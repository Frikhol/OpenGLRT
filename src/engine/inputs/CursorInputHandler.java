package inputs;

import core.Engine;
import display.Display;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static display.Display.*;
import static org.lwjgl.glfw.GLFW.*;

public class CursorInputHandler {

    private static final double middlePosX = getDisplayWIDTH()[0]/2;
    private static final double middlePosY = getDisplayHEIGHT()[0]/2;
    private static double dX;
    private static double dY;

    public static double getdX() {
        return dX;
    }

    public static double getdY() {
        return dY;
    }

    public static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        public void invoke(long window, double x, double y) {
            setCursorX(x);
            setCursorY(y);
        }
    };

    public static void cursorInputs(){
        if(!DefaultControls.isPause()) {
            dX = getCursorX() - middlePosX;
            dY = getCursorY() - middlePosY;
            if (dX != 0)
                Engine.getCamera().yaw((float) (0.5f * (dX)));
            if (dY != 0)
                Engine.getCamera().pitch((float) (0.5f * (dY)));
            glfwSetCursorPos(Display.getDisplayID(), middlePosX, middlePosY);
        }

    }


}
