package pictureAgents;

import java.lang.reflect.Field;

import pictureConstructor.Picture;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class RandomDroplet extends PictureAgent {
    private PVector position;

    private float  minSize              = 7.0f;
    private float  maxSize              = 50.0f;
    private float  greyValue            = 0.0f;
    private String alphaMode            = "relative";
    private float  alpha                = 150.0f;
    private float  attractorUpdateValue = 50.0f;

    public RandomDroplet(PApplet _parentApplet, String _parametersFile, String _parametersSetName) {
        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        position = new PVector(0.0f, 0.0f);
    }


    protected void setAgentParameters(PApplet _parentApplet, String _parametersFile, String _parametersSetName) {
        JSONObject parametersSet;
        String 	   attribute;

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

                    case "minSize" :
                    minSize = parametersSet.getFloat(attribute);
                    break;

                    case "maxSize" :
                    maxSize = parametersSet.getFloat(attribute);
                    break;

                    case "attractorUpdateValue" :
                    attractorUpdateValue = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        position.x = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
        position.y = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        int   pixelColor;
        float radius;
        float alphaValue;

        pixelColor = _pictureToConstruct.getAttractorPixel(position.x, position.y);
        radius = PApplet.map(pixelColor, 0.0f, 255.0f, minSize, maxSize);
        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - _pictureToConstruct.getAttractorPixel(position.x, position.y);
        }

        _parentApplet.noStroke();

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.fill(pixelColor, alphaValue);
        }
        else {
            _parentApplet.fill(greyValue, alphaValue);
        }

        _parentApplet.ellipse(position.x, position.y, radius, radius);
        _pictureToConstruct.updateAttractorPixel(position.x, position.y, attractorUpdateValue);
    }
}
