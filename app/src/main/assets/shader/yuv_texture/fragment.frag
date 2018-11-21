#version 300 es

in vec2 vTextureCoord; // 接收从顶点着色器过来的参数
out vec4 fragColor;

uniform sampler2D tex_y;
uniform sampler2D tex_u;
uniform sampler2D tex_v;
const mat3 bt601_coeff = mat3(1.164, 1.164, 1.164,
                              0.0, -0.392, 2.017,
                              1.596, -0.813, 0.0);

const vec3 offsets = vec3(-0.0625,-0.5,-0.5);


vec3 sampleRgb(vec2 loc) {
    float y = texture(tex_y,loc).r;
    float u = texture(tex_u,loc).r;
    float v = texture(tex_v,loc).r;
    return bt601_coeff * (vec3(y,u,v) + offsets);
}

void main() {
	fragColor = vec4(sampleRgb(vTextureCoord),1.);
}