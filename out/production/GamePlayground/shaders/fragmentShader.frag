#version 400 core

in vec2 pass_uv;
in vec3 surfaceNormal;
in vec3 toLightSource;
in vec3 toCameraVector;
out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec4 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
in float visibility;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightSource = normalize(toLightSource);

    float nDot1 = dot(unitNormal, unitLightSource);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness * lightColor.xyz;

    vec3 unitCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -unitLightSource;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitCameraVector);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor.xyz;

    vec4 textureColor = texture(textureSampler, pass_uv);

    if (textureColor.a < 0.5) {
        discard;
    }

	out_color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
