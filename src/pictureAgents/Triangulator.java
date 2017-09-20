package pictureAgents;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

import java.lang.reflect.Field;

import pictureConstructor.Picture;

public class Triangulator extends PictureAgent {
    private float  minRadar  = 10.0f;
    private float  maxRadar  = 30.0f;
    private float  greyValue = 0.0f;
    private String alphaMode = "absolute";
    private float  alpha     = 20.0f;

    private PVector position;

    public Triangulator(PApplet _parentApplet, Picture _pictureToConstruct, String _parametersFile, String _parametersSetName) {
        float initialX, initialY;

        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        initialX = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
        initialY = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());

        position = new PVector(initialX, initialY);
    }


    protected void setAgentParameters(PApplet _parentApplet, String _parametersFile, String _parametersSetName) {
        JSONObject parametersSet;
        String attribute;

        parametersSet = readAgentsParameters(_parentApplet, _parametersFile, _parametersSetName);

        for (Field field : this.getClass().getDeclaredFields()) {
            attribute = field.getName();

            if(!parametersSet.isNull(attribute)) {
                switch(attribute) {
                    case "greyValue" :
                    greyValue = parametersSet.getFloat(attribute);
                    break;

                    case "alphaMode" :
                    alphaMode = parametersSet.getString(attribute);
                    break;

                    case "alpha" :
                    alpha = parametersSet.getFloat(attribute);
                    break;

                    case "minRadar" :
                    minRadar = parametersSet.getFloat(attribute);
                    break;

                    case "maxRadar" :
                    maxRadar = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        float positionOffset;

        positionOffset = maxRadar + minRadar;

        position.x = _parentApplet.random(positionOffset, _pictureToConstruct.getOriginalPictureWidth()  - positionOffset);
        position.y = _parentApplet.random(positionOffset, _pictureToConstruct.getOriginalPictureHeight() - positionOffset);
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        float xMin, xMax, yMin, yMax;
        float alphaValue;
        int attractorPixel;
        int vertexBrightness;
        int pixelColor;
        PVector[] triangleVertices = new PVector[3];

        for(int i = 0; i < 3; i++) {
            triangleVertices[i] = new PVector(0.0f, 0.0f);
        }

        for(int i = 0; i < 3; i++) {
            switch(i) {
                case 0 :
                xMin = position.x - maxRadar - minRadar;
                yMin = position.y - maxRadar - minRadar;
                xMax = position.x - maxRadar + minRadar;
                yMax = position.y - maxRadar + minRadar;
                break;

                case 1 :
                xMin = position.x + maxRadar - minRadar;
                yMin = position.y - maxRadar - minRadar;
                xMax = position.x + maxRadar + minRadar;
                yMax = position.y - maxRadar + minRadar;
                break;

                default :
                xMin = position.x - minRadar;
                yMin = position.y + maxRadar - minRadar;
                xMax = position.x + minRadar;
                yMax = position.y + maxRadar + minRadar;
            }

            vertexBrightness = 255;

            for(float x = xMin; x <= xMax; x++) {
                for (float y = yMin; y <= yMax; y++) {
                    attractorPixel = _pictureToConstruct.getAttractorPixel(x, y);
                    if(attractorPixel <= vertexBrightness) {
                        vertexBrightness = attractorPixel;
                        triangleVertices[i].x = x;
                        triangleVertices[i].y = y;
                    }
                }
            }
        }

        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - _pictureToConstruct.getAttractorPixel(position.x, position.y);
        }

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.fill(pixelColor, alphaValue);
            _parentApplet.stroke(200, 20);
        }
        else {
            _parentApplet.fill(greyValue, alphaValue);
            _parentApplet.stroke(200, 10);
        }

        _parentApplet.triangle(triangleVertices[0].x, triangleVertices[0].y,
        triangleVertices[1].x, triangleVertices[1].y,
        triangleVertices[2].x, triangleVertices[2].y);
    }
}
