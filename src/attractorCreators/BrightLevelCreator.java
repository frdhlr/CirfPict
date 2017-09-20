package attractorCreators;

import pictureConstructor.Picture;
import processing.core.PApplet;

public class BrightLevelCreator implements AttractorCreator {
    private float pixelStep;
    private float brightnessLevel;

    public BrightLevelCreator(float _pixelStep, float _brightnessLevel) {
        pixelStep       = _pixelStep;
        brightnessLevel = _brightnessLevel;
    }


    public int[] createAttractor(PApplet _parentApplet, Picture _picture) {
        int pixelColor;
        int index;
        float brightRate;
        int [] attractor;

        _parentApplet.colorMode(PApplet.HSB, 255);

        attractor = new int [_picture.getDimension()];

        for(float x = 0.0f; x < _picture.getOriginalPictureWidth(); x++) {
            for(float y = 0.0f; y <  _picture.getOriginalPictureHeight(); y++) {
                index = _picture.getIndex(x, y);

                if(x % pixelStep == 0.0f && y % pixelStep == 0.0f) {
                    pixelColor = _picture.getOriginalPicturePixel(index);
                    brightRate = _parentApplet.brightness(pixelColor);
                    attractor[index] = PApplet.floor(brightRate / brightnessLevel) * PApplet.floor(brightnessLevel);
                }
                else {
                    attractor[index] = 250;
                }
            }
        }

        return(attractor);
    }

}
