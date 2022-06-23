#version 430
#define M_PI 3.1415926535897932384626433832795

in vec2 dir;

out vec4 outColor;

uniform sampler2D skybox;
uniform mat4 transformationMatrix;
uniform vec3 cameraPosition;
uniform vec2 cameraRotation;
uniform vec2 seed1;
uniform vec2 seed2;
uniform float curTime;
uniform sampler2D sampleTexture;
uniform float samplePart;

layout(std430, binding = 1) buffer nodeBuffer
{
    int nodeData[];
};
layout(std430, binding = 2) buffer quadBuffer
{
    float quadData[];
};
layout(std430, binding = 3) buffer polyBuffer
{
    float polyData[];
};

uvec4 R_STATE;
vec3 light = normalize(vec3(-3,-4,1));

uint TausStep(uint z, int S1, int S2, int S3, uint M)
{
    uint b = (((z << S1) ^ z) >> S2);
    return (((z & M) << S3) ^ b);
}

uint LCGStep(uint z, uint A, uint C)
{
    return (A * z + C);
}

vec2 hash22(vec2 p)
{
    p += seed1.x;
    vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}

float random()
{
    R_STATE.x = TausStep(R_STATE.x, 13, 19, 12, uint(4294967294));
    R_STATE.y = TausStep(R_STATE.y, 2, 25, 4, uint(4294967288));
    R_STATE.z = TausStep(R_STATE.z, 3, 11, 17, uint(4294967280));
    R_STATE.w = LCGStep(R_STATE.w, uint(1664525), uint(1013904223));
    return 2.3283064365387e-10 * float((R_STATE.x ^ R_STATE.y ^ R_STATE.z ^ R_STATE.w));
}

vec3 randomOnSphere() {
    vec3 rand = vec3(random(), random(), random());
    float theta = rand.x * 2.0 * 3.14159265;
    float v = rand.y;
    float phi = acos(2.0 * v - 1.0);
    float r = pow(rand.z, 1.0 / 3.0);
    float x = r * sin(phi) * cos(theta);
    float y = r * sin(phi) * sin(theta);
    float z = r * cos(phi);
    return vec3(x, y, z);
}

vec2 sphIntersect( in vec3 ro, in vec3 rd, in vec3 ce, float ra ){
    vec3 oc = ro - ce;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - ra*ra;
    float h = b*b - c;
    if( h<0.0 ) return vec2(-1.0); // no intersection
    h = sqrt( h );
    return vec2( -b-h, -b+h );
}

vec2 boxIntersection(in vec3 ro, in vec3 rd, in vec3 rad, out vec3 oN)  {
    vec3 m = 1.0 / rd;
    vec3 n = m * ro;
    vec3 k = abs(m) * rad;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);
    if(tN > tF || tF < 0.0) return vec2(-1.0);
    oN = -sign(rd) * step(t1.yzx, t1.xyz) * step(t1.zxy, t1.xyz);
    return vec2(tN, tF);
}

vec3 triIntersect( in vec3 ro, in vec3 rd, in vec3 v0, in vec3 v1, in vec3 v2 ){
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

float plaIntersect( in vec3 ro, in vec3 rd, in vec4 p ){
    return -(dot(ro,p.xyz)+p.w)/dot(rd,p.xyz);
}

mat2 rot(float a) {
    float s = sin(a);
    float c = cos(a);
    return mat2(c, -s, s, c);
}

vec3 getSky(vec3 rd){
    vec2 uv = vec2(atan(rd.z,rd.x),asin(rd.y)*2.0);
    uv/=M_PI;
    uv = uv*0.5+0.5;
    vec3 col = texture(skybox,uv).rgb;
    float specular = pow(max(dot(light,rd),0),256)*256;
    return col+vec3(specular);
}

vec3 checkPoly(vec3 ro,vec3 rd,int id,out vec3 norm,inout vec3 minIt){
    vec3 it;
    vec3 v1 = (transformationMatrix*vec4(polyData[id*18],polyData[id*18+1],polyData[id*18+2],1)).xyz;
    vec3 v2 = (transformationMatrix*vec4(polyData[id*18+6],polyData[id*18+7],polyData[id*18+8],1)).xyz;
    vec3 v3 = (transformationMatrix*vec4(polyData[id*18+12],polyData[id*18+13],polyData[id*18+14],1)).xyz;
    vec3 n1 = (transformationMatrix*vec4(polyData[id*18+3],polyData[id*18+4],polyData[id*18+5],0)).xyz;
    vec3 n2 = (transformationMatrix*vec4(polyData[id*18+9],polyData[id*18+10],polyData[id*18+11],0)).xyz;
    vec3 n3 = (transformationMatrix*vec4(polyData[id*18+15],polyData[id*18+16],polyData[id*18+17],0)).xyz;
    it = triIntersect(ro,rd,v1,v2,v3);
    if(it.x>0&&it.x<=minIt.x){
        minIt.x = it.x;
        norm = normalize(vec3(((1-it.y-it.z)*n1.x+(it.y)*n2.x+(it.z)*n3.x),((1-it.y-it.z)*n1.y+(it.y)*n2.y+(it.z)*n3.y),((1-it.y-it.z)*n1.z+(it.y)*n2.z+(it.z)*n3.z)));
    }
    return it;
}

float checkNode(vec3 ro,vec3 rd,int id){
    vec3 it;
    vec3 norm;
    vec3 v0 = (transformationMatrix*vec4(quadData[id*24],quadData[1+id*24],quadData[2+id*24],1)).xyz;
    vec3 v1 = (transformationMatrix*vec4(quadData[3+id*24],quadData[4+id*24],quadData[5+id*24],1)).xyz;
    vec3 v2 = (transformationMatrix*vec4(quadData[6+id*24],quadData[7+id*24],quadData[8+id*24],1)).xyz;
    vec3 v3 = (transformationMatrix*vec4(quadData[9+id*24],quadData[10+id*24],quadData[11+id*24],1)).xyz;
    vec3 v4 = (transformationMatrix*vec4(quadData[12+id*24],quadData[13+id*24],quadData[14+id*24],1)).xyz;
    vec3 v5 = (transformationMatrix*vec4(quadData[15+id*24],quadData[16+id*24],quadData[17+id*24],1)).xyz;
    vec3 v6 = (transformationMatrix*vec4(quadData[18+id*24],quadData[19+id*24],quadData[20+id*24],1)).xyz;
    vec3 v7 = (transformationMatrix*vec4(quadData[21+id*24],quadData[22+id*24],quadData[23+id*24],1)).xyz;
    norm = cross((v0-v1),(v0-v3));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v0,v1,v3);
    if(it.x>0)
        return 1;
    norm = cross((v1-v2),(v1-v3));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v1,v2,v3);
    if(it.x>0)
        return 1;
    norm = cross((v1-v5),(v1-v2));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v1,v5,v2);
    if(it.x>0)
        return 1;
    norm = cross((v5-v6),(v5-v2));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v5,v6,v2);
    if(it.x>0)
        return 1;
    norm = cross((v5-v4),(v5-v6));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v5,v4,v6);
    if(it.x>0)
        return 1;
    norm = cross((v4-v7),(v4-v6));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v4,v7,v6);
    if(it.x>0)
        return 1;
    norm = cross((v4-v0),(v4-v7));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v4,v0,v7);
    if(it.x>0)
        return 1;
    norm = cross((v0-v3),(v0-v7));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v0,v3,v7);
    if(it.x>0)
        return 1;
    norm = cross((v3-v2),(v3-v6));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v3,v2,v6);
    if(it.x>0)
        return 1;
    norm = cross((v2-v6),(v2-v7));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v2,v6,v7);
    if(it.x>0)
        return 1;
    norm = cross((v4-v5),(v4-v0));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v4,v5,v0);
    if(it.x>0)
        return 1;
    norm = cross((v5-v0),(v5-v1));
    //if(dot(norm,rd)>0)
        it = triIntersect(ro,rd,v5,v0,v1);
    if(it.x>0)
        return 1;
    return -1;
}

vec4 castRay(inout vec3 ro,inout vec3 rd){
    vec3 minIt = vec3(100000);
    vec4 color = vec4(1.0);
    vec3 norm;
    vec3 it;
    //Пересечение с моделью
    for(int i = 0;i<199999;i++){
        if(nodeData[3+i*4]==1){
            it = checkPoly(ro,rd,nodeData[i*4+1],norm,minIt);
        }else{
            it.x = checkNode(ro,rd,i);
            if(it.x<0){
                int id = nodeData[i*4]-1;
                if(id<0)
                break;
                i = id;
            }
        }
    }
    if(minIt.x != 100000){
        color = vec4(0.0,0.4,0.0,1.0);
    }
    //Пересечение с прозрачной сферой
    vec3 sph0 = vec3(5.0,-1.0,15.0);
    it.xy = sphIntersect(ro,rd,sph0, 2);
    if(it.x > 0.0 && it.x < minIt.x){
        minIt = it;
        vec3 itPos = ro + rd * it.x;
        norm = normalize(itPos - sph0);
        color = vec4(1.0,1.0,1.0,-0.5);
    }
    //Пересечение с глянцевой сферой
    vec3 sph1 = vec3(-11.0,-5.0,-13.0);
    it.xy = sphIntersect(ro,rd,sph1, 4);
    if(it.x > 0.0 && it.x < minIt.x){
        minIt = it;
        vec3 itPos = ro + rd * it.x;
        norm = normalize(itPos - sph1);
        color = vec4(0.8,0.0,0.0,1.0);
    }
    //Пересечение с полуматовой сферой
    vec3 sph2 = vec3(-13.0,-2.0,4.0);
    it.xy = sphIntersect(ro,rd,sph2, 4);
    if(it.x > 0.0 && it.x < minIt.x){
        minIt = it;
        vec3 itPos = ro + rd * it.x;
        norm = normalize(itPos - sph2);
        color = vec4(0.3,0.3,0.3,0.7);
    }
    //Пересечение с плоскостью
    vec3 planeNormal = vec3(0.0, -1.0, 0.0);
    it.x = plaIntersect(ro, rd, vec4(planeNormal, 1.0));
    if (it.x>0&&it.x<minIt.x){
        norm = planeNormal;
        minIt = vec3(it.x,-1.0,0.0);
        color = vec4(1.0,0.7,0.5,0.01);
    }
    if(minIt.x == 100000){

        return vec4(getSky(rd),-1.0);

    }//Если ни с чем не пересеклись рисуем небо
    vec3 reflected = reflect(rd, norm);
    if(color.w < 0){
        float fresnel = 1.0 - abs(dot(-rd, norm));
        if(random() - 0.1 < fresnel * fresnel) {
            rd = reflected;
            return vec4(color.xyz,1.0);
        }
        ro += rd * (minIt.y + 0.001);
        rd = refract(rd, norm, 1.0 / (1.0 - color.w));
        return vec4(color.xyz,1.0);
    }
    vec3 rand = randomOnSphere();
    vec3 spec = reflect(rd,norm);
    vec3 diff = normalize(rand*dot(rand,norm));
    ro += rd*(minIt.x-0.001);
    rd = mix(diff, spec, color.w);
    return vec4(color.xyz,1.0);
}

vec3 traceRay(vec3 ro,vec3 rd){
    vec3 color = vec3(1.0);
    for(int i = 0;i<4;i++){
        vec4 result = castRay(ro,rd);
        if(result.w == -1.0){
            return color * result.xyz;
        }
        vec3 ro0 = ro;
        vec3 lightDir = light;
        color*=result.xyz;
    }
    return color;
}

void main(void){
    vec2 uv = (dir.xy-0.5);
    vec2 uvRes = hash22(uv + 1.0) * 720 + 720;
    R_STATE.x = uint(seed1.x + uvRes.x);
    R_STATE.y = uint(seed1.y + uvRes.x);
    R_STATE.z = uint(seed2.x + uvRes.y);
    R_STATE.w = uint(seed2.y + uvRes.y);
    vec3 rayOrigin = cameraPosition;
    vec3 rayDirection = normalize(vec3(uv.x,uv.y,-1.0));
    rayDirection.zy *= rot(-cameraRotation.x);
    rayDirection.zx *= rot(-cameraRotation.y);
    vec3 col = vec3(0.0);
    int samples = 5;
    for(int i = 0; i < samples; i++) {
        col += traceRay(rayOrigin, rayDirection);
    }
    col /= samples;
    float white = 20.0;
    col *= white * 0.2;
    col = (col * (1.0 + col / white / white)) / (1.0 + col);
    vec3 sampleCol = texture(sampleTexture, vec2(dir.x,-dir.y)).rgb;
    col = mix(sampleCol, col, samplePart);
    outColor = vec4(col,1.0);
}