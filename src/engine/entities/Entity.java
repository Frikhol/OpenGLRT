package entities;

import entities.components.Mesh;
import entities.components.Transform;
import core.visualiser.Loader;
import core.visualiser.Renderer;

public class Entity {
    private String name;
    private Mesh mesh;
    private Transform transform;
    private int textureId;

    public Entity(float[] positions,int[] indices) {
        this.mesh = Loader.load(positions,indices);
        this.transform = new Transform();
        Renderer.entices.add(this);
    }

    public Entity(String name) {
        this.name = name;
        this.mesh = Loader.loadObjModel(name);
        this.transform = new Transform();
        this.textureId = Loader.loadTexture(name);
        Renderer.entices.add(this);
    }

    public Transform getTransform() {
        return transform;
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
