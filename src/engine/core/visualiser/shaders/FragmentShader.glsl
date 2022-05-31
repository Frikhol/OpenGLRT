#version 430

in vec2 dir;

out vec4 outColor;

uniform sampler2D someTexture;
uniform mat4 transformationMatrix;

layout(std430, binding = 3) buffer layoutName
{
    float modelData[];
};

vec2 sphIntersect( in vec3 ro, in vec3 rd, in vec3 ce, float ra )
{
    vec3 oc = ro - ce;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - ra*ra;
    float h = b*b - c;
    if( h<0.0 ) return vec2(-1.0); // no intersection
    h = sqrt( h );
    return vec2( -b-h, -b+h );
}

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

float plaIntersect( in vec3 ro, in vec3 rd, in vec4 p )
{
    return -(dot(ro,p.xyz)+p.w)/dot(rd,p.xyz);
}

vec3 castRay(vec3 ro, vec3 rd){
    float res = 100000;
    vec3 it;
    bool finded=false;
    vec3 norm;
    vec3 aNorm;
    for(int i = 0;i<626;i++){
        vec3 v1 = (transformationMatrix* vec4(modelData[i*18],modelData[i*18+1],modelData[i*18+2],1)).xyz;
        vec3 v2 = (transformationMatrix* vec4(modelData[i*18+6],modelData[i*18+7],modelData[i*18+8],1)).xyz;
        vec3 v3 = (transformationMatrix* vec4(modelData[i*18+12],modelData[i*18+13],modelData[i*18+14],1)).xyz;
        vec3 n1 = (transformationMatrix* vec4(modelData[i*18+3],modelData[i*18+4],modelData[i*18+5],1)).xyz;
        vec3 n2 = (transformationMatrix* vec4(modelData[i*18+9],modelData[i*18+10],modelData[i*18+11],1)).xyz;
        vec3 n3 = (transformationMatrix* vec4(modelData[i*18+15],modelData[i*18+16],modelData[i*18+17],1)).xyz;
        it = triIntersect(ro,rd,v1,v2,v3);
        if(it.x>0.0 && it.x<=res){
            finded = true;
            res = it.x;
            norm = normalize(cross(v2-v1,v3-v1));
        }
    }
    vec3 light = normalize(vec3(5,-5,100));
    if(!finded){
        return vec3(-1.0);
    }
    vec3 reflection = reflect(rd, norm);
    float diff = max(dot(light,norm),0)*0.3;
    float spec = pow(max(dot(reflection,light),0),1)*0.3;
    return vec3((diff+spec+0.1));
}

void main(void){
    vec2 uv = (dir.xy-0.5);
    vec3 sun = (vec3(10,-5,10));
    vec3 rayOrigin = vec3(0.0,0.0,15.0);
    vec3 rayDirection = normalize(vec3(uv.x,uv.y,-1.0));
    vec3 col = castRay(rayOrigin,rayDirection);
    float it = col.x;
    if(col.x<=0.0){
        vec3 planeNormal = vec3(0.0, -1.0, 0.0);
        it = plaIntersect(rayOrigin, rayDirection, vec4(planeNormal, 1.0));
        if(it>0&&it<100000)
            col=vec3(0.3);
        else col=vec3(0.3,0.6,1.0);
    }
    if(castRay(rayOrigin+rayDirection*(it-0.001),sun).x != -1.0)col*=0.2;
    outColor = vec4(col,1.0);
}