#version 400 core

in vec3 position;
in vec2 uv;
out vec2 pass_uv;

void main(void) {
	gl_Position = vec4(position, 1.0);
	pass_uv = uv;
}

