package attractorCreators;
import pictureConstructor.Picture;
import processing.core.PApplet;

public interface AttractorCreator {
    public int [] createAttractor(PApplet _parentApplet, Picture _picture);
}
