package pictureConstructor;
import processing.core.PApplet;
import processing.core.PImage;
import attractorCreators.AttractorCreator;

public class Picture {
    private int originalPictureWidth;
    private int originalPictureHeight;
    private int [] originalPicture;
    private int [] attractor;

    public Picture(PApplet _parentApplet, String _imageName) {
        setOriginalPicture(_parentApplet, _imageName);
    }


    public void setOriginalPicture(PApplet _parentApplet, String _imageName) {
        PImage originalImage;

        originalImage = _parentApplet.loadImage(_imageName);

        originalPictureWidth  = originalImage.width;
        originalPictureHeight = originalImage.height;
        originalPicture       = originalImage.pixels;
    }


    public void setAttractor(PApplet _parentApplet, AttractorCreator _attractorCreator) {
        attractor = _attractorCreator.createAttractor(_parentApplet, this);
    }


    public int getOriginalPictureWidth() {
        return(originalPictureWidth);
    }


    public int getOriginalPictureHeight() {
        return(originalPictureHeight);
    }


    public int getOriginalPicturePixel(float _x, float _y) {
        return(originalPicture[getIndex(_x, _y)]);
    }


    public int getOriginalPicturePixel(int _index) {
        return(originalPicture[_index]);
    }


    public int getAttractorPixel(float _x, float _y) {
        return(attractor[getIndex(_x, _y)]);
    }


    public int getAttractorPixel(int _index) {
        return(attractor[_index]);
    }


    public int getDimension() {
        return(originalPictureWidth * originalPictureHeight);
    }


    public int getIndex(float _x, float _y) {
        return(PApplet.floor(_x) + originalPictureWidth * PApplet.floor(_y));
    }


    public void updateAttractorPixel(float _x, float _y, float _incrementValue) {
        attractor[getIndex(_x, _y)] += _incrementValue;
    }


    public void displayOriginalPicture(PApplet _parentApplet) {
        for(float x = 0.0f; x < originalPictureWidth; x++) {
            for(float y = 0.0f; y < originalPictureHeight; y++) {
                _parentApplet.stroke(getOriginalPicturePixel(x, y));
                _parentApplet.point(x, y);
            }
        }
    }


    public void displayAttractor(PApplet _parentApplet) {
        for(float x = 0.0f; x < originalPictureWidth; x++) {
            for(float y = 0.0f; y < originalPictureHeight; y++) {
                _parentApplet.stroke(getAttractorPixel(x, y));
                _parentApplet.point(x, y);
            }
        }
    }
}
