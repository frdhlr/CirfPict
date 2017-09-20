package attractorCreators;
import pictureConstructor.Picture;
import processing.core.PApplet;

public class GrayScaleCreator implements AttractorCreator{
    private float redRatio;
    private float greenRatio;
    private float blueRatio;

    public GrayScaleCreator(float _redRatio, float _greenRatio, float _blueRatio) {
        redRatio   = _redRatio;
        greenRatio = _greenRatio;
        blueRatio  = _blueRatio;
    }

    public int [] createAttractor(PApplet _parentApplet, Picture _picture) {
        int pixelColor;
        int index;
        float redRate, greenRate, blueRate;
        int [] attractor;

        _parentApplet.colorMode(PApplet.RGB, 255);

        attractor = new int [_picture.getDimension()];

        for(float x = 0.0f; x < _picture.getOriginalPictureWidth(); x++) {
            for(float y = 0.0f; y <  _picture.getOriginalPictureHeight(); y++) {
                index = _picture.getIndex(x, y);
                pixelColor = _picture.getOriginalPicturePixel(index);

                redRate   = _parentApplet.red(pixelColor) * redRatio;
                greenRate = _parentApplet.red(pixelColor) * greenRatio;
                blueRate  = _parentApplet.red(pixelColor) * blueRatio;

                attractor[index] = PApplet.round(redRate + greenRate + blueRate);
            }
        }

        return(attractor);
    }

}
