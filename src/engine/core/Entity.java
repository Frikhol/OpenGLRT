package core;

import renderiser.Loader;
import renderiser.Renderer;

public class Entity {
    private String name;
    private Mesh mesh;

    public Entity(String name, float[] positions) {
        this.name = name;
        this.mesh = new Mesh(Loader.load(positions),positions.length/3);
        Renderer.entices.add(this);
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
