package tools;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static display.Display.*;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class Converter {

    public static Matrix4f createTransformationMatrix(Vector3f position,Vector3f rotation,float scale){
        Matrix4f mat = new Matrix4f();
        mat.scale(scale);
        mat.translate(position);
        mat.rotateX(rotation.x);
        mat.rotateY(rotation.y);
        mat.rotateZ(rotation.z);
        return mat;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f mat = new Matrix4f();
        mat.identity();
        mat.rotateX((float) Math.toRadians(camera.getPitch()));
        mat.rotateY((float) Math.toRadians(camera.getYaw()));
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        mat.translate(negativeCameraPos);
        return mat;
    }

    public static Matrix4f createProjectionMatrix(){
        float FOV = 70;
        float NEAR_PLANE = 0.1f;
        float FAR_PLANE = 1000;
        Matrix4f mat = new Matrix4f();
        glfwGetWindowSize(getDisplayID(),getDisplayWIDTH(),getDisplayHEIGHT());
        int width = getDisplayWIDTH()[0];
        int height = getDisplayHEIGHT()[0];
        float aspectRatio = (float)width/height;
        mat.identity();
        mat.perspective(FOV, aspectRatio, NEAR_PLANE, FAR_PLANE);
        return mat;
    }

}
