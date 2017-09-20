package pictureAgents;

import java.lang.reflect.Field;

import pictureConstructor.Picture;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class DarknessFattener extends PictureAgent {
    private String direction            = "vertical";
    private float stepX                 = 5.0f;
    private float stepY                 = 6.0f;
    private float speedMultiplicator	= 2.0f;
    private float maxFatValue           = 7.0f;
    private float  greyValue            = 0.0f;
    private String alphaMode            = "absolute";
    private float  alpha                = 20.0f;

    private PVector position;
    private float velocity;
    private float fattenerStep;

    public DarknessFattener(PApplet _parentApplet, Picture _pictureToConstruct, String _parametersFile, String _parametersSetName) {
        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        if(direction.equals("vertical")) {
            fattenerStep = _pictureToConstruct.getOriginalPictureWidth() / stepX;
            position = new PVector(_parentApplet.random(fattenerStep) * stepX, 0.0f);
        }
        else {
            fattenerStep = _pictureToConstruct.getOriginalPictureHeight() / stepY;
            position = new PVector(0.0f, _parentApplet.random(fattenerStep) * stepY);
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

                    case "stepX" :
                    stepX = parametersSet.getFloat(attribute);
                    break;

                    case "stepY" :
                    stepY = parametersSet.getFloat(attribute);
                    break;

                    case "speedMultiplicator" :
                    speedMultiplicator = parametersSet.getFloat(attribute);
                    break;

                    case "maxFatValue" :
                    maxFatValue = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        if(direction.equals("vertical")) {
            velocity = stepY * _parentApplet.random(speedMultiplicator);
            position.y += velocity;

            if(position.y >= _pictureToConstruct.getOriginalPictureHeight()) {
                position.y = 0.0f;
                position.x = _parentApplet.random(fattenerStep) * stepX;
            }
        }
        else {
            velocity = stepX * _parentApplet.random(0.9f, 1.1f);
            position.x += velocity;

            if(position.x >= _pictureToConstruct.getOriginalPictureWidth()) {
                position.x = 0.0f;
                position.y = _parentApplet.random(fattenerStep) * stepY;
            }
        }
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        float pixelWeight;
        int pixelColor;
        float alphaValue;

        pixelWeight = _pictureToConstruct.getAttractorPixel(position.x, position.y);
        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - pixelWeight;
        }

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.stroke(pixelColor, alphaValue);
        }
        else {
            _parentApplet.stroke(greyValue, alphaValue);
        }

        pixelWeight = PApplet.map(pixelWeight, 0.0f, 255.0f, maxFatValue, 0.0f);
        _parentApplet.strokeWeight(pixelWeight);

        if(direction.equals("vertical")) {
            _parentApplet.line(position.x , position.y- velocity, position.x, position.y + velocity);
        }
        else {
            _parentApplet.line(position.x - velocity, position.y, position.x + velocity, position.y);
        }
    }

}
