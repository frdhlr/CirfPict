package pictureAgents;

import java.lang.reflect.Field;

import pictureConstructor.Picture;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class HomogenousPatchworker extends PictureAgent {
    private PVector position;

    private float  minSize              = 7.0f;
    private float  maxSize              = 50.0f;
    private float  radar                = 10.0f;
    private float  dispersionLimit      = 400.0f;
    private float  greyValue            = 0.0f;
    private String alphaMode            = "relative";
    private float  alpha                = 150.0f;
    private float  attractorUpdateValue = 50.0f;

    public HomogenousPatchworker(PApplet _parentApplet, String _parametersFile, String _parametersSetName) {
        setAgentParameters(_parentApplet, _parametersFile, _parametersSetName);

        position = new PVector(0.0f, 0.0f);
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

                    case "minSize" :
                    minSize = parametersSet.getFloat(attribute);
                    break;

                    case "maxSize" :
                    maxSize = parametersSet.getFloat(attribute);
                    break;

                    case "radar" :
                    radar = parametersSet.getFloat(attribute);
                    break;

                    case "dispersionLimit" :
                    dispersionLimit = parametersSet.getFloat(attribute);
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
        float xMin, yMin, xMax, yMax;
        int   pixelColor;
        int   diffColors;
        int   sumColor;
        float radarDimension;
        float colorDispersion;
        float size, halfSize;
        float alphaValue;

        pixelColor = _pictureToConstruct.getAttractorPixel(position.x, position.y);
        sumColor = 0;
        radarDimension = 4 * radar * radar;

        xMin = PApplet.max(position.x - radar, 0.0f);
        yMin = PApplet.max(position.y - radar, 0.0f);
        xMax = PApplet.min(position.x + radar, _pictureToConstruct.getOriginalPictureWidth() - 1);
        yMax = PApplet.min(position.y + radar, _pictureToConstruct.getOriginalPictureHeight() - 1);

        for(float x = xMin; x < xMax; x++) {
            for(float y = yMin; y < yMax; y++) {
                diffColors =  _pictureToConstruct.getAttractorPixel(x, y) - pixelColor;
                sumColor += PApplet.abs(diffColors);
                _pictureToConstruct.updateAttractorPixel(x, y, attractorUpdateValue);
            }
        }

        colorDispersion = PApplet.min(sumColor / radarDimension, dispersionLimit);
        size = PApplet.map(colorDispersion, 0.0f, dispersionLimit, maxSize, minSize);
        halfSize = size / 2.0f;

        alphaValue = alpha;

        if(alphaMode.equals("relative")) {
            alphaValue = alpha - _pictureToConstruct.getAttractorPixel(position.x, position.y);
        }

        _parentApplet.stroke(255, 10);

        if(greyValue == 0.0f) {
            pixelColor = _pictureToConstruct.getOriginalPicturePixel(position.x, position.y);
            _parentApplet.fill(pixelColor, alphaValue);
        }
        else {
            _parentApplet.fill(greyValue, alphaValue);
        }

        _parentApplet.rect(position.x - halfSize, position.y - halfSize, size, size);
    }

}
