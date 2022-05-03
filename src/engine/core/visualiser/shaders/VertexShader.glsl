#version 400

in vec3 position;
in vec3 normal;

out vec3 fragColor;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    fragColor = gl_Position.xyz;

    surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

}