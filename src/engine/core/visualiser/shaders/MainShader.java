package core.visualiser.shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import tools.Converter;

public class MainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/engine/core/visualiser/shaders/VertexShader.glsl";
    private static final String GEOMETRY_FILE = "src/engine/core/visualiser/shaders/GeometryShader.glsl";
    private static final String FRAGMENT_FILE = "src/engine/core/visualiser/shaders/FragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;

    public MainShader() {
        super(VERTEX_FILE,GEOMETRY_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(1, "position");
        super.bindAttribute(2,"textureCoords");
        super.bindAttribute(3,"normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Converter.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition,light.getPosition());
        super.loadVector(location_lightColor,light.getColor());
    }

    public void loadColor(Vector4f color){}

}