//TODO DEBUG#version 430

in vec2 dir;

out vec4 outColor;

uniform sampler2D someTexture;
uniform mat4 transformationMatrix;

layout(std430, binding = 1) buffer nodeBuffer
{
    int nodeData[];
};
layout(std430, binding = 2) buffer boxBuffer
{
    float boxData[];
};
layout(std430, binding = 3) buffer polyBuffer
{
    float polyData[];
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

vec3 checkPoly(vec3 ro,vec3 rd,inout vec3 norm,inout float minIt,inout int id,inout vec3 res){
    vec3 it;
    vec3 v1 = (vec4(polyData[id*18],polyData[id*18+1],polyData[id*18+2],1)).xyz;
    vec3 v2 = (vec4(polyData[id*18+6],polyData[id*18+7],polyData[id*18+8],1)).xyz;
    vec3 v3 = (vec4(polyData[id*18+12],polyData[id*18+13],polyData[id*18+14],1)).xyz;
    vec3 n1 = (vec4(polyData[id*18+3],polyData[id*18+4],polyData[id*18+5],1)).xyz;
    vec3 n2 = (vec4(polyData[id*18+9],polyData[id*18+10],polyData[id*18+11],1)).xyz;
    vec3 n3 = (vec4(polyData[id*18+15],polyData[id*18+16],polyData[id*18+17],1)).xyz;
    it = triIntersect(ro,rd,v1,v2,v3);
    if(it.x>0.0 && it.x<=minIt){
        minIt = it.x;
        res = it;
        norm = normalize(vec3(((1-it.y-it.z)*n1.x+(it.y)*n2.x+(it.z)*n3.x),((1-it.y-it.z)*n1.y+(it.y)*n2.y+(it.z)*n3.y),((1-it.y-it.z)*n1.z+(it.y)*n2.z+(it.z)*n3.z)));
    }
    return it;
}

vec3 checkNode(vec3 ro,vec3 rd,inout int id){
    vec3 minIt = vec3(100000);
    vec3 boxN;
    vec3 it;
    vec3 boxPos = vec3(boxData[id*6],boxData[1+id*6],boxData[2+id*6]);
    vec3 boxSize = vec3(boxData[3+id*6],boxData[4+id*6],boxData[5+id*6]);
    it = vec3(boxIntersection(ro - boxPos, rd, boxSize, boxN),0);
    //если пересесекает, переходим в левый узел
    if(it.x > 0.0 && it.x < minIt.x) {
        id = nodeData[id*5];
    }
    else
        it.x = -1;
    return it;
}

int nextStep(inout int i){
    if(i==0) //Если мы в начальном корне, черный пиксель
        return -1;
    else { //Если не в корне проверяем правый узел от корня
        while (i==nodeData[nodeData[i*5+2]*5+1]){ //Пока не перейдем в новый правый
            if(i==0) //Если в начальном корне, черный пиксель
                return -1;
            i = nodeData[i*5+2]; //идем в корень
        }
        //Переходим в правый
        i = nodeData[nodeData[i*5+2]*5+1];
    }
    return i;
}

vec3 castRay(vec3 ro, vec3 rd){
    float minIt = 100000;
    vec3 norm;
    vec3 it;
    vec3 res = vec3(0);
    vec3 col;
    int id;
    int i = 0;
    while(i<1251&&i>=0){
        if(nodeData[i*5]==-1){
            id = nodeData[4+i*5];
            //Если финальный узел, проверяем пересечение с треугольником
            it = checkPoly(ro,rd,norm,minIt,id,res);
        } else {
            //Если не финальный, проверяем пересечение с коробкой, если есть, переходим в левый
            it = checkNode(ro, rd, i);
        }
        if (it.x < 0 && nextStep(i)==-1){ //Переходим в правый, если крайний или корень, черный пиксель
            return vec3(0);
        }
    }
    col = vec3(res);
    //Проверки на цвет
    return col;
}

/*vec3 castRay(vec3 ro, vec3 rd){
    float res = 100000;
    vec3 it;
    bool finded=false;
    vec3 norm;
    vec3 col = vec3(1.0);
    for(int i = 0;i<626;i++){
        vec3 v1 = (transformationMatrix* vec4(polyData[i*18],polyData[i*18+1],polyData[i*18+2],1)).xyz;
        vec3 v2 = (transformationMatrix* vec4(polyData[i*18+6],polyData[i*18+7],polyData[i*18+8],1)).xyz;
        vec3 v3 = (transformationMatrix* vec4(polyData[i*18+12],polyData[i*18+13],polyData[i*18+14],1)).xyz;
        vec3 n1 = (transformationMatrix* vec4(polyData[i*18+3],polyData[i*18+4],polyData[i*18+5],1)).xyz;
        vec3 n2 = (transformationMatrix* vec4(polyData[i*18+9],polyData[i*18+10],polyData[i*18+11],1)).xyz;
        vec3 n3 = (transformationMatrix* vec4(polyData[i*18+15],polyData[i*18+16],polyData[i*18+17],1)).xyz;
        it = triIntersect(ro,rd,v1,v2,v3);
        if(it.x>0.0 && it.x<=res){
            finded = true;
            res = it.x;
            norm = normalize(vec3(((1-it.y-it.z)*n1.x+(it.y)*n2.x+(it.z)*n3.x),((1-it.y-it.z)*n1.y+(it.y)*n2.y+(it.z)*n3.y),((1-it.y-it.z)*n1.z+(it.y)*n2.z+(it.z)*n3.z)));
            //norm = normalize(cross(v2-v1,v3-v1));
        }
    }
    vec3 light = normalize(vec3(5,-5,10));
    if(!finded){
        vec3 planeNormal = vec3(0.0, -1.0, 0.0);
        it.x = plaIntersect(ro, rd, vec4(planeNormal, 1.0));
        if(it.x>0&&it.x<res){
            norm = planeNormal;
            if(dot(norm,light)>0)
                return vec3(it.x,0.5,0.0);
            else
                return vec3(it.x,0.5,-1.0);
        }
        else return vec3(-1.0);
    }
    vec3 reflection = reflect(rd, norm);
    float diff = max(dot(light,norm),0)*0.5;
    float spec = pow(max(dot(reflection,light),0),4)*10;
    it.y = (diff+spec+0.1);
    it.x = res;
    it.z = dot(norm,light);
    return it;
}*/

vec3 traceRay(vec3 ro,vec3 rd){
    vec3 res = castRay(ro,rd);
    if(res.x == -1.0) return vec3(0.3,0.6,1.0);
    vec3 col = vec3(1.0)*res.y;
    if(res.y != 0.5)
        col*=vec3(0.0,1.0,0.0);
    vec3 sun = normalize(vec3(5,-5,10));
    //if(res.z >=0)
        if(castRay(ro+rd*(res.x-0.001),sun).x != -1.0)col*=0.5;
    return col;
}

void main(void){
    vec2 uv = (dir.xy-0.5);
    vec3 rayOrigin = vec3(0.0,-5.0,20.0);
    vec3 rayDirection = normalize(vec3(uv.x,uv.y,-1.0));
    vec3 col = castRay(rayOrigin,rayDirection);
    outColor = vec4(col,1.0);
}