package core.visualiser.shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
    private int location_cameraPosition;
    private int location_cameraRotation;
    private int location_skybox;
    private int location_curTime;
    private int location_seed1;
    private int location_seed2;
    private int location_sampleTexture;
    private int location_samplePart;

    public MainShader() {
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_cameraPosition = super.getUniformLocation("cameraPosition");
        location_cameraRotation = super.getUniformLocation("cameraRotation");
        location_skybox = super.getUniformLocation("skybox");
        location_curTime = super.getUniformLocation("curTime");
        location_seed1 = super.getUniformLocation("seed1");
        location_seed2 = super.getUniformLocation("seed2");
        location_sampleTexture = super.getUniformLocation("sampleTexture");
        location_samplePart = super.getUniformLocation("samplePart");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(1, "position");
        super.bindAttribute(2,"textureCoords");
        super.bindAttribute(3,"normal");
    }

    public void connectTextureUnits(){
        super.loadInt(location_skybox,1);
        super.loadInt(location_sampleTexture,2);
    }

    public void connectSampler(float samplePart) {
        super.loadFloat(location_samplePart,samplePart);
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

    public void loadCamera(Vector3f position, Vector2f rotation) {
        super.loadVector(location_cameraPosition,position);
        super.load2DVector(location_cameraRotation,rotation);
    }

    public void loadTimeAndSeeds(float time,Vector2f seed1,Vector2f seed2){
        super.loadFloat(location_curTime,time);
        super.load2DVector(location_seed1,seed1);
        super.load2DVector(location_seed2,seed2);
    }

}