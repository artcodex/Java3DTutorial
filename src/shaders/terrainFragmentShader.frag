#version 400 core

in vec2 pass_uv;
in vec3 surfaceNormal;
in vec3 toLightSource;
in vec3 toCameraVector;
in float visibility;
out vec4 out_color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendTexture;


uniform vec4 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;


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
    vec3 unitLightSource = normalize(toLightSource);

    float nDot1 = dot(unitNormal, unitLightSource);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness * lightColor.xyz;

    vec3 unitCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -unitLightSource;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitCameraVector);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity *lightColor.xyz;

	out_color = vec4(diffuse, lightColor.w) * totalColor + vec4(finalSpecular, lightColor.w);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
