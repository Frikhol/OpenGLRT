package inputs;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import static display.Display.setCursorX;
import static display.Display.setCursorY;

public class CursorInputHandler {

    public static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        public void invoke(long window, double x, double y) {
            setCursorX(x);
            setCursorY(y);
        }
    };

    public static void cursorInputs(){

    }


}
