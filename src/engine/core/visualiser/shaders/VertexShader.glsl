#version 400

layout(location=0) in vec3 position;
layout(location=1) in vec2 texture;

out vec2 dir;

void main(void){

    gl_Position = vec4(position, 1.0);
    dir = texture;
}