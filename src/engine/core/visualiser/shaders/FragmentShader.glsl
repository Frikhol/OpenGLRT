#version 400

in vec2 dir;

out vec4 outColor;

uniform sampler2D someTexture;

void main(void){



    outColor = vec4(dir,0.0,1.0);

}