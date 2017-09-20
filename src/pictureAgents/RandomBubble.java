package pictureAgents;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

import java.lang.reflect.Field;

import pictureConstructor.Picture;

public class RandomBubble extends PictureAgent {
    private float  maxSpeed             = 3.0f;
    private float  minSize              = 1.0f;
    private float  maxSize              = 50.0f;
    private float  greyValue            = 0.0f;
    private String alphaMode            = "relative";
    private float  alpha                = 150.0f;
    private float  attractorUpdateValue = 100.0f;

    private PVector position;
    private PVector velocity;

    public RandomBubble(PApplet _parentApplet, PVector _initialPosition, PVector _initialVelocity, String _parametersSetsFile, String _parametersSetName) {
        setAgentParameters(_parentApplet, _parametersSetsFile, _parametersSetName);

        position = _initialPosition.copy();
        velocity = _initialVelocity.copy();
    }


    public RandomBubble(PApplet _parentApplet, Picture _pictureToConstruct, String _parametersFile, String _parametersSetName) {
        float initialX, initialY;

        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        initialX = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
        initialY = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());

        position = new PVector(initialX, initialY);
        velocity = new PVector(_parentApplet.random(-maxSpeed, maxSpeed), _parentApplet.random(-maxSpeed, maxSpeed));
    }


    protected void setAgentParameters(PApplet _parentApplet, String _parametersSetsFile, String _parametersSetName) {
        JSONObject parametersSet;
        String attribute;

        parametersSet = readAgentsParameters(_parentApplet, _parametersSetsFile, _parametersSetName);

        for(Field field : this.getClass().getDeclaredFields()) {
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

                    case "maxSpeed" :
                    maxSpeed = parametersSet.getFloat(attribute);
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
        velocity.limit(maxSpeed);
        position.add(velocity);

        velocity.x += _parentApplet.random(-maxSpeed, maxSpeed);
        velocity.y += _parentApplet.random(-maxSpeed, maxSpeed);

        manageBounce(_pictureToConstruct);
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        int pixelColor;
        float radius;
        float alphaValue;

        pixelColor = _pictureToConstruct.getAttractorPixel(position.x, position.y);
        radius = _parentApplet.random(0, PApplet.map(pixelColor, 0.0f, 255.0f, maxSize, minSize));
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


    private void manageBounce(Picture _pictureToConstruct) {
        int originalPictureWidth, originalPictureHeight;
        int xConstraint, yConstraint;

        originalPictureWidth = _pictureToConstruct.getOriginalPictureWidth();
        originalPictureHeight = _pictureToConstruct.getOriginalPictureHeight();

        xConstraint = originalPictureWidth - 1;
        yConstraint = originalPictureHeight - 1;

        if(position.x < 0.0f) {
            position.x = 0.0f;
            velocity.x *= -1.0f;
        }

        if(position.x >= originalPictureWidth) {
            position.x = xConstraint;
            velocity.x *= -1.0f;
        }

        if(position.y < 0.0f) {
            position.y = 0.0f;
            velocity.y *= -1.0f;
        }

        if(position.y >= originalPictureHeight) {
            position.y = yConstraint;
            velocity.y *= -1.0f;
        }
    }
}
