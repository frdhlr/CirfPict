package pictureAgents;

import processing.core.PApplet;
import processing.data.JSONObject;
import pictureConstructor.Picture;

public abstract class PictureAgent {
    public abstract void update(PApplet _parentApplet, Picture _pictureToConstruct);
    public abstract void display(PApplet _parentApplet, Picture _pictureToConstruct);
    protected abstract void setAgentParameters(PApplet _parentApplet, String _parametersFile, String _parametersSetName);


    protected JSONObject readAgentsParameters(PApplet _parentApplet, String _parametersFile, String _parametersSetName) {
        JSONObject parametersSet;

        parametersSet = _parentApplet.loadJSONObject(_parametersFile);
        parametersSet = parametersSet.getJSONObject("Agents");
        parametersSet = parametersSet.getJSONObject(this.getClass().getSimpleName());
        parametersSet = parametersSet.getJSONObject(_parametersSetName);

        return(parametersSet);
    }
}
