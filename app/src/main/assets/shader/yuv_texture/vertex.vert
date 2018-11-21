#version 300 es
uniform mat4 uMVPMatrix; // mvp矩阵
in vec3 aPosition; //顶点位置
in vec2 aTexCoor; //顶点纹理坐标
out vec2 vTextureCoord; //用于传递给片元着色器的out变量
void main() {
    gl_Position = uMVPMatrix * vec4(aPosition,1);
    vTextureCoord = aTexCoor;
}