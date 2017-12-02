#version 120

uniform sampler2D tex0;
uniform vec3 rColor; // upperLeft
uniform vec3 gColor; // upperRight
uniform vec3 bColor; // lowerLeft
uniform vec3 aColor; // lowerRight

void main() {
	vec3 combined = vec3(0.0);
	
	vec2 texCoord = gl_TexCoord[0].st;
	
	vec4 weights = texture2D(tex0, texCoord); // confirmed: Konstante weights kommen genau so an, wie sie sollen
	
	vec3 rColorr = vec3(1, 0, 0);
	vec3 gColorr = vec3(0, 1, 0);
	vec3 bColorr = vec3(0, 0, 1);
	vec3 aColorr = vec3(1, 1, 0);
	
	combined += weights.rrr * rColor;
	combined += weights.ggg * gColor;
	combined += weights.bbb * bColor;
	combined += weights.aaa * aColor;

	gl_FragColor = vec4(combined, 1); // note that color values > 1 will be clipped to 1 (same goes - analogously - for < 0)
}