#version 400

in vec3 position;
in vec3 normal;

out vData
{
    vec3 normal;
    vec3 color;
}vertex;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;


void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    vertex.normal = (transformationMatrix * vec4(normal,0.0)).xyz;
    vertex.color = vec3(0.0);

}