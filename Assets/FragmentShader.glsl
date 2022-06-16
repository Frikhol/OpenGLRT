#version 430

in vec2 dir;

out vec4 outColor;

uniform sampler2D someTexture;

vec3 sphIntersect( in vec3 ro, in vec3 rd, in vec3 ce, float ra )
{
    vec3 oc = ro - ce;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - ra*ra;
    float h = b*b - c;
    if( h<0.0 ) return vec3(-1.0); // no intersection
    h = sqrt( h );
    return vec3( -b-h, -b+h, 1 );
}

vec3 boxIntersection(in vec3 ro, in vec3 rd, in vec3 rad, out vec3 oN)  {
    vec3 m = 1.0 / rd;
    vec3 n = m * ro;
    vec3 k = abs(m) * rad;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);
    if(tN > tF || tF < 0.0) return vec3(-1.0);
    oN = -sign(rd) * step(t1.yzx, t1.xyz) * step(t1.zxy, t1.xyz);
    return vec3(tN, tF,1);
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
    vec3 light = normalize(vec3(10,20,10));
    float minDist = 100000;
    vec3 n;
    vec3 result = vec3(0);
    vec3 sphPos = vec3(-1,0,0);
    result = sphIntersect(ro, rd,sphPos,1);
    if(result.x>0 && result.x<minDist){
        minDist = result.x;
        n = ro-sphPos+rd*result.x;
    }
    vec3 boxN;
    vec3 boxPos = vec3(2,0,0);
    result = boxIntersection(ro-boxPos,rd,vec3(1),boxN);
    if(result.x>0 && result.x<minDist){
        minDist = result.x;
        n = boxN;
    }
    vec3 planeN = vec3(0,1,0);
    result.x = plaIntersect(ro,rd,vec4(planeN,1));
    if(result.x>0 && result.x<minDist){
        minDist = result.x;
        n = planeN;
    }
    if(minDist == 100000) return vec3(0);
    float diffuse = max(dot(light,n),0.0)*0.7+0.1;
    float specular = pow(max(dot(reflect(rd,n),light),0.0),32)*0.6;
    return vec3(diffuse+specular);
}

vec3 traceRay(vec3 ro,vec3 rd){
    vec3 result;
    vec3 col;
    result = castRay(ro,rd);
    return result;
}

void main(void){
    vec2 uv = (dir.xy-0.5);
    vec3 rayOrigin = vec3(0.0,0.0,10.0);
    vec3 rayDirection = normalize(vec3(uv.x,-uv.y,-1.0));
    vec3 col = traceRay(rayOrigin,rayDirection);
    outColor = vec4(col,1.0);
}