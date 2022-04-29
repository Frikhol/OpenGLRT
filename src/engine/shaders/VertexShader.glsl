#version 400

in vec2 position;
out vec2 fragColor;

void main(void){
    gl_Position = vec4(position, 0.0, 1.0);
    fragColor = gl_Position.xy;
}