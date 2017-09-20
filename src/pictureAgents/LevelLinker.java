package pictureAgents;

import java.lang.reflect.Field;

import pictureConstructor.Picture;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class LevelLinker extends PictureAgent {
    private float  radar     = 20.0f;
    private float  greyValue = 0.0f;
    private String alphaMode = "absolute";
    private float  alpha     = 20.0f;

    private int level = 0;

    private PVector position;
    private PVector previousPosition;

    public LevelLinker(PApplet _parentApplet,  Picture _pictureToConstruct, String _parametersSetsFile,
    String _parametersSetName) {
        float initialX, initialY;

        setAgentParameters(_parentApplet, _parametersSetsFile, _parametersSetName);

        initialX = _parentApplet.random(_pictureToConstruct.getOriginalPictureWidth());
        initialY = _parentApplet.random(_pictureToConstruct.getOriginalPictureHeight());

        position = new PVector(initialX, initialY);
        previousPosition = new PVector(0.0f, 0.0f);
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

                    case "radar" :
                    radar = parametersSet.getFloat(attribute);
                    break;
                }
            }
        }
    }


    public void update(PApplet _parentApplet, Picture _pictureToConstruct) {
        boolean nearestFound;

        nearestFound = exploreSurroundings(_parentApplet, _pictureToConstruct);

        if(!nearestFound) {
            skipToAnotherArea(_parentApplet, _pictureToConstruct);
        }
    }


    public void display(PApplet _parentApplet, Picture _pictureToConstruct) {
        int   pixelColor;
        float alphaValue;
        float attractorValue;

        alphaValue = alpha;
        attractorValue = _pictureToConstruct.getAttractorPixel(position.x, position.y);

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - attractorValue;
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
        _pictureToConstruct.updateAttractorPixel(position.x, position.y, 255.0f);
    }


    private boolean exploreSurroundings(PApplet _parentApplet, Picture _pictureToConstruct) {
        float   xMin, yMin, xMax, yMax;
        float   x, y;
        float   radius;
        boolean nearestFound;

        radius = 1.0f;
        nearestFound = false;

        while (!nearestFound && radius <= radar) {
            xMin = PApplet.max(position.x - radius, 0.0f);
            yMin = PApplet.max(position.y - radius, 0.0f);
            xMax = PApplet.min(position.x + radius, _pictureToConstruct.getOriginalPictureWidth() - 1);
            yMax = PApplet.min(position.y + radius, _pictureToConstruct.getOriginalPictureHeight() - 1);

            x = xMin;
            y = yMin;

            while(!nearestFound && x <= xMax) {
                while(!nearestFound && y <= yMax) {
                    if(x != y && _pictureToConstruct.getAttractorPixel(x, y) == level) {
                        nearestFound = true;
                        previousPosition = position.copy();
                        position.x = x;
                        position.y = y;
                    }

                    y += radius;
                }

                x += radius;
            }

            radius += 1.0f;
        }

        return(nearestFound);
    }


    private void skipToAnotherArea(PApplet _parentApplet, Picture _pictureToConstruct) {
        int	index;
        PVector originalPosition;

        originalPosition = position.copy();

        index = _pictureToConstruct.getIndex(originalPosition.x, originalPosition.y) + 1;

        while(index < _pictureToConstruct.getDimension() && _pictureToConstruct.getAttractorPixel(index) != level) {
            index++;
        }

        if(index == _pictureToConstruct.getDimension()) {
            level += 10;

            if(level > 250) {
                level = 0;
            }

            position.x = 0.0f;
            position.y = 0.0f;
        }
        else {
            position.x = index % _pictureToConstruct.getOriginalPictureWidth();
            position.y = PApplet.floor(index / _pictureToConstruct.getOriginalPictureWidth());
        }

        previousPosition = position.copy();
    }
}
