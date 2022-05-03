#version 400

in vec3 fragColor;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 outColor;

uniform vec3 lightColor;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1,0.0);
    vec3 diffuse = brightness * (fragColor+0.5)/2;
    outColor = vec4(diffuse,1.0);

}