#version 400 core

in vec3 position;
in vec2 uv;
in vec3 normal;
out vec2 pass_uv;
out vec3 surfaceNormal;
out vec3 toLightSource[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];

uniform float useFakeLighting;

uniform float numberOfRows;
uniform vec2 xyOffset;

const float density = 0.003;
const float gradient = 1.5;

uniform vec4 plane;

void main(void) {
	vec3 actualNormal = normal;

	if (useFakeLighting > 0.5) {
	    actualNormal = vec3(0.0,1.0,0.0);
	}

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_ClipDistance[0] = dot(worldPosition, plane);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;

	for (int i=0; i<4; i++) {
	    toLightSource[i] = lightPosition[i] - worldPosition.xyz;
	}

	//inverse of view matrix is camera position since view matrix calculates opposites to create illusion, then multiply by 4D vector
	//to get vector of position
	toCameraVector = (inverse(viewMatrix) * vec4(0,0,0,1.0)).xyz - worldPosition.xyz;
	pass_uv = (uv/numberOfRows) + xyOffset;

	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}

