package pictureAgents;

import java.lang.reflect.Field;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;
import pictureConstructor.Picture;

public class AttractedPainter extends PictureAgent {
    private float  maxForce 		= 0.01f;
    private float  maxSpeed 		= 3.0f;
    private float  radar		= 15.0f;
    private float  greyValue		= 0.0f;
    private String alphaMode		= "absolute";
    private float  alpha		= 20.0f;
    private float  attractorUpdateValue	= 50.0f;

    private PVector position;
    private PVector previousPosition;
    private PVector velocity;

    public AttractedPainter(PApplet _parentApplet, PVector _initialPosition, PVector _initialVelocity,
    String _parametersSetsFile, String _parametersSetName) {
        setAgentParameters(_parentApplet, _parametersSetsFile, _parametersSetName);

        position 	 = _initialPosition.copy();
        previousPosition = position.copy();
        velocity 	 = _initialVelocity.copy();
    }


    public AttractedPainter(PApplet _parentApplet, Picture _pictureToConstruct, String _parametersFile,
    String _parametersSetName) {
        float initialX, initialY;

        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        initialX = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
        initialY = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());

        position = new PVector(initialX, initialY);
        velocity = new PVector(0.0f, 0.0f);
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

                    case "maxForce" :
                    maxForce = parametersSet.getFloat(attribute);
                    break;

                    case "maxSpeed" :
                    maxSpeed = parametersSet.getFloat(attribute);
                    break;

                    case "radar" :
                    radar = parametersSet.getFloat(attribute);
                    break;

                    case "attractorUpdateValue" :
                    attractorUpdateValue = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        int originalPictureWidth, originalPictureHeight;
        int xConstraint, yConstraint;
        float xMin, xMax, yMin, yMax;
        PVector accelaration;
        PVector force;

        originalPictureWidth = _pictureToConstruct.getOriginalPictureWidth();
        originalPictureHeight = _pictureToConstruct.getOriginalPictureHeight();

        xConstraint = originalPictureWidth - 1;
        yConstraint = originalPictureHeight - 1;

        accelaration = new PVector(0.0f, 0.0f);

        xMin = PApplet.max(position.x - radar, 0.0f);
        yMin = PApplet.max(position.y - radar, 0.0f);
        xMax = PApplet.min(position.x + radar, xConstraint);
        yMax = PApplet.min(position.y + radar, yConstraint);

        for(float x = xMin; x <= xMax; x++) {
            for(float y = yMin; y <= yMax; y++) {
                force = PVector.sub(new PVector(x, y), position);
                force.normalize();
                force.div(_pictureToConstruct.getAttractorPixel(x, y));

                accelaration.add(force);
            }
        }

        accelaration.limit(maxForce);
        velocity.add(accelaration);
        velocity.limit(maxSpeed);
        previousPosition = position.copy();
        position.add(velocity);

        if(position.x < 0.0f) position.x = 0.0f;
        if(position.x >= originalPictureWidth) position.x = xConstraint;
        if(position.y < 0.0f) position.y = 0.0f;
        if(position.y >= originalPictureHeight) position.y = yConstraint;
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        int   pixelColor;
        float alphaValue;

        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - _pictureToConstruct.getAttractorPixel(position.x, position.y);
        }

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.stroke(pixelColor, alphaValue);
        }
        else {
            _parentApplet.stroke(greyValue, alphaValue);
        }

        _parentApplet.strokeWeight(1);
        _parentApplet.line(previousPosition.x, previousPosition.y, position.x, position.y);
        _pictureToConstruct.updateAttractorPixel(position.x, position.y, attractorUpdateValue);
    }
}
