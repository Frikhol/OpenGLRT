#version 400

in vec2 fragColor;
out vec4 outColor;

void main(void){

    outColor = vec4((fragColor.x+0.5)/2,(fragColor.y+0.5)/2,0.1,1.0);

}