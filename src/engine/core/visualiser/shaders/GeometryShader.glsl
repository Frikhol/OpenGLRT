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

    float maxx;
    float maxy;
    float minx;
    float miny;
    for(int i = 0;i<2;i++){
        maxx=max(gl_in[i].gl_Position.x,gl_in[i+1].gl_Position.x);
        maxy=max(gl_in[i].gl_Position.y,gl_in[i+1].gl_Position.y);
        minx=min(gl_in[i].gl_Position.x,gl_in[i+1].gl_Position.x);
        miny=min(gl_in[i].gl_Position.y,gl_in[i+1].gl_Position.y);
    }
    float nx = 50;
    float ny = 50;
    float stepx = (maxx-minx+1)/nx;
    float stepy = (maxy-miny+1)/ny;
    vec3 ro = vec3(0.0,0.0,-1000.0);
    polyColor = 0.1;
    for(int i = 0;i<nx;i++){
        for(int j = 0;j<ny;j++){
            float x = minx+stepx*i;
            float y = miny+stepy*j;
            vec3 rd = vec3(x,y,1000);
            vec3 res = triIntersect(ro,rd,gl_in[0].gl_Position.xyz,gl_in[1].gl_Position.xyz,gl_in[2].gl_Position.xyz);
            if(res.x != -1){
                vec3 unitNormal = normalize(vertices[0].normal);
                vec3 unitLightVector = normalize(lightPosition-vec3(res[1],res[2],gl_in[0].gl_Position.z));
                float nDot1 = dot(unitNormal,unitLightVector);
                float brightness = max(nDot1,0.0);
                polyColor = max(brightness,polyColor);
            }
        }
    }

    for(int i=0;i<3;i++){
        frag.normal = vertices[i].normal;
        gl_Position = gl_in[i].gl_Position;
        frag.color = max(vertices[i].color,polyColor);
        EmitVertex();
    }
    EndPrimitive();

}
