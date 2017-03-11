#version 400 core

in vec2 pass_uv;
in vec3 surfaceNormal;
in vec3 toLightSource[4];
in vec3 toCameraVector;
out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec4 lightColor[4];
uniform vec3 lightAttenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
in float visibility;
uniform float useCellShading;

const float levels = 3.0;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitCameraVector = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i=0; i < 4; i++) {
        float distance  = length(toLightSource[i]);
        float attFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
        vec3 unitLightSource = normalize(toLightSource[i]);
        float nDot1 = dot(unitNormal, unitLightSource);
        float brightness = max(nDot1, 0.0);

        float level = 0.0;
        if (useCellShading > 0.5) {
            level = floor(brightness * levels);
            brightness = level / levels;
        }

        vec3 lightDirection = -unitLightSource;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
        float specularFactor = dot(reflectedLightDirection, unitCameraVector);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);

        if (useCellShading > 0.5) {
            level = floor(dampedFactor * levels);
            dampedFactor = level / levels;
        }

        totalDiffuse = totalDiffuse + ((brightness * lightColor[i].xyz) / attFactor);
        totalSpecular = totalSpecular + ((dampedFactor * reflectivity * lightColor[i].xyz) / attFactor);
    }

    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(textureSampler, pass_uv);

    if (textureColor.a < 0.5) {
        discard;
    }

	out_color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
