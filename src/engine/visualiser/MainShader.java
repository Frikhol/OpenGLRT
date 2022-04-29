package visualiser;

import org.joml.Vector4f;

public class MainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/engine/shaders/VertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/engine/shaders/FragmentShader.glsl";

    public MainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {}

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadColour(Vector4f color){}

}