package core;

import visualiser.Loader;
import visualiser.Renderer;

public class Entity {
    private String name;
    private Mesh mesh;
    private int textureId;

    public Entity(String name, float[] positions) {
        this.name = name;
        this.mesh = Loader.load(positions);
        Renderer.entices.add(this);
    }

    public Entity(String name) {
        this.name = name;
        this.mesh = Loader.loadObjModel(name);
        this.textureId = Loader.loadTexture("models/test.png");
        Renderer.entices.add(this);
    }

    public int getTextureId() {
        return textureId;
    }

    public String getName() {
        return name;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public static void add(Entity entity){
        if(!Renderer.entices.contains(entity))
            Renderer.entices.add(entity);
    }

    public static void remove(Entity entity){
        Renderer.entices.remove(entity);
    }

    public static void clear(){
        Renderer.entices.clear();
    }
}
