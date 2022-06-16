package inputs;

import core.Engine;
import display.Display;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static display.Display.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public class CursorInputHandler {

    private static final double middlePosX = getDisplayWIDTH()[0]/2;
    private static final double middlePosY = getDisplayHEIGHT()[0]/2;

    public static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        public void invoke(long window, double x, double y) {
            setCursorX(x);
            setCursorY(y);
        }
    };

    public static void cursorInputs(){
        double curCursorX = getCursorX();
        double curCursorY = getCursorY();
        if(curCursorX>middlePosX)
            Engine.getCamera().yaw((float)(0.5f*(curCursorX-middlePosX)));
        else if(curCursorX<middlePosX)
            Engine.getCamera().yaw((float)(-0.5f*(middlePosX-curCursorX)));
        if(curCursorY>middlePosY)
            Engine.getCamera().pitch((float)(0.5f*(curCursorY-middlePosY)));
        else if(curCursorY<middlePosY)
            Engine.getCamera().pitch((float)(-0.5f*(middlePosY-curCursorY)));
        glfwSetCursorPos(Display.getDisplayID(), middlePosX, middlePosY);
    }


}
