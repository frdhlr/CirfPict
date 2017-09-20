package pictureAgents;

import java.lang.reflect.Field;

import pictureConstructor.Picture;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class LightSpeeder extends PictureAgent {
    private String direction    = "vertical";
    private float minSpeed      = 0.1f;
    private float maxSpeed      = 5.0f;
    private float radius        = 3.0f;
    private float  greyValue    = 0.0f;
    private String alphaMode    = "absolute";
    private float  alpha        = 20.0f;

    private PVector position;

    public LightSpeeder(PApplet _parentApplet, Picture _pictureToConstruct, String _parametersFile, String _parametersSetName) {
        float originalPosition;

        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        if(direction.equals("vertical")) {
            originalPosition = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
            position = new PVector(originalPosition, 0.0f);
        }
        else {
            originalPosition = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());
            position = new PVector(0.0f, originalPosition);
        }
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

                    case "direction" :
                    direction = parametersSet.getString(attribute);
                    break;

                    case "minSpeed" :
                    minSpeed = parametersSet.getFloat(attribute);
                    break;

                    case "maxSpeed" :
                    maxSpeed = parametersSet.getFloat(attribute);
                    break;

                    case "radius" :
                    radius = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        int pixelColor;
        float velocity;

        pixelColor = _pictureToConstruct.getAttractorPixel(position.x, position.y);
        velocity = PApplet.map(pixelColor, 0.0f, 255.0f, minSpeed, maxSpeed);
        velocity *= _parentApplet.random(0.9f, 1.1f);

        if(direction.equals("vertical")) {
            position.y += velocity;

            if(position.y >= _pictureToConstruct.getOriginalPictureHeight()) {
                position.x = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
                position.y = 0.0f;
            }
        }
        else {
            position.x += velocity;

            if(position.x >= _pictureToConstruct.getOriginalPictureWidth()) {
                position.y = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());
                position.x = 0.0f;
            }
        }
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        int pixelColor;
        float alphaValue;

        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - _pictureToConstruct.getAttractorPixel(position.x, position.y);
        }

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.fill(pixelColor, alphaValue);
        }
        else {
            _parentApplet.fill(greyValue, alphaValue);
        }

        _parentApplet.noStroke();
        _parentApplet.ellipse(position.x, position.y, radius, radius);
    }
}
