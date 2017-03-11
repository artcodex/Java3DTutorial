#version 400 core

in vec2 pass_uv;
in vec3 surfaceNormal;
in vec3 toLightSource[4];
uniform vec3 lightAttenuation[4];
in vec3 toCameraVector;
in float visibility;
out vec4 out_color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendTexture;


uniform vec4 lightColor[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform float useCellShading;

const float levels = 3.0;

void main(void) {
    vec4 blendMapColour = texture(blendTexture, pass_uv);
    float backgroundTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
    vec2 tiledCoords = pass_uv * 40.0;
    vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backgroundTextureAmount;
    vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColour.r;
    vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColour.g;
    vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColour.b;

    vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitCameraVector = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0);
    vec3 totalSpecular = vec3(0);

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

	out_color = vec4(totalDiffuse, 1) * totalColor + vec4(totalSpecular, 1);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
