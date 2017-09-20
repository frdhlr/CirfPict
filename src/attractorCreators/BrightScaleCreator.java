package attractorCreators;

import pictureConstructor.Picture;
import processing.core.PApplet;

public class BrightScaleCreator implements AttractorCreator {

    public BrightScaleCreator() {

    }

    public int [] createAttractor(PApplet _parentApplet, Picture _picture) {
        int pixelColor;
        int index;
        float brightRate;
        int [] attractor;

        _parentApplet.colorMode(PApplet.HSB, 255);

        attractor = new int [_picture.getDimension()];

        for(float x = 0.0f; x < _picture.getOriginalPictureWidth(); x++) {
            for(float y = 0.0f; y <  _picture.getOriginalPictureHeight(); y++) {
                index = _picture.getIndex(x, y);
                pixelColor = _picture.getOriginalPicturePixel(index);

                brightRate = _parentApplet.brightness(pixelColor);

                attractor[index] = PApplet.round(brightRate);
            }
        }

        return(attractor);
    }
}
