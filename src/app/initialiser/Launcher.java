package initialiser;

import core.Engine;
import display.Display;

import static org.lwjgl.glfw.GLFW.*;

public class Launcher implements Runnable {
    public static void main(String[] args){
        new Thread(new Launcher()).start();
    }

    @Override
    public void run() {
        Engine.start();
        while(!glfwWindowShouldClose(Display.getDisplayID())) {
            update();
            Engine.loop();
        }
        close();
    }

    private  void update(){

    }

    private  void close(){
        Engine.close();
    }


}
