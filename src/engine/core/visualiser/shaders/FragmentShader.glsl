#version 400

in fData
{
    vec3 normal;
    vec3 color;
}frag;

out vec4 outColor;

uniform vec3 lightColor;

void main(void){

    outColor = vec4(frag.color,1.0);

}