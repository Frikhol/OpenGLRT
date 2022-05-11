#version 400

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vData
{
    vec3 normal;
    vec3 color;
}vertices[];

out fData
{
    vec3 normal;
    vec3 color;
}frag;

out float polyColor;

uniform vec3 lightPosition;


vec3 triIntersect( in vec3 ro, in vec3 rd, in vec3 v0, in vec3 v1, in vec3 v2 )
{
    vec3 v1v0 = v1 - v0;
    vec3 v2v0 = v2 - v0;
    vec3 rov0 = ro - v0;
    vec3  n = cross( v1v0, v2v0 );
    vec3  q = cross( rov0, rd );
    float d = 1.0/dot( rd, n );
    float u = d*dot( -q, v2v0 );
    float v = d*dot(  q, v1v0 );
    float t = d*dot( -n, rov0 );
    if( u<0.0 || v<0.0 || (u+v)>1.0 ) t = -1.0;
    return vec3( t, u, v );
}

void main() {



    for(int i=0;i<3;i++){
        frag.normal = vertices[i].normal;
        gl_Position = gl_in[i].gl_Position;
        vec3 unitLightVector = normalize(lightPosition-gl_in[i].gl_Position.xyz);
        float nDot1 = dot(vertices[i].normal,unitLightVector);
        float brightness = max(nDot1,0.0);
        frag.color = vertices[i].color*brightness*3;
        EmitVertex();
    }
    EndPrimitive();

}
