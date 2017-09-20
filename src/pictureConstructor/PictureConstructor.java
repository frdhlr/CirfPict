package pictureConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;
import attractorCreators.AttractorCreator;
import pictureAgents.*;

public class PictureConstructor {
    private float backgroundAlpha = 0.0f;
    private int attractorCreatorIndex;
    private int nbAgents = 1;
    private String agentName;
    private String agentType;

    private PApplet parentApplet;
    private AttractorCreator[] attractorCreatorsList;
    private Picture picture;
    private ArrayList<PictureAgent> agents;

    public PictureConstructor(PApplet _parentApplet, String _pictureName, AttractorCreator[] _attractorCreatorList,
    String _parametersFile, String _constructortName) {
        setConstructorParameters(_parentApplet, _parametersFile, _constructortName);

        parentApplet = _parentApplet;
        attractorCreatorsList = _attractorCreatorList;

        picture = new Picture(parentApplet, _pictureName);
        picture.setAttractor(parentApplet, attractorCreatorsList[attractorCreatorIndex]);

        agents = new ArrayList<PictureAgent> ();
        instanciateAgents(_parametersFile);
    }


    private void setConstructorParameters(PApplet _parentApplet, String _parametersFile, String _constructortName) {
        JSONObject parametersSet;
        JSONArray attractorCreatorsSet;
        String attribute;
        int chosenAttractorCreator;

        parametersSet = readConstructorParameters(_parentApplet, _parametersFile, _constructortName);

        for (Field field : this.getClass().getDeclaredFields()) {
            attribute = field.getName();

            if(!parametersSet.isNull(attribute)) {
                switch(attribute) {
                    case "backgroundAlpha" :
                    backgroundAlpha = parametersSet.getFloat(attribute);
                    break;

                    case "attractorCreatorIndex" :
                    attractorCreatorsSet = parametersSet.getJSONArray(attribute);
                    chosenAttractorCreator = PApplet.floor(_parentApplet.random(attractorCreatorsSet.size()));
                    attractorCreatorIndex = attractorCreatorsSet.getInt(chosenAttractorCreator);
                    break;

                    case "nbAgents" :
                    nbAgents = parametersSet.getInt(attribute);
                    break;

                    case "agentName" :
                    agentName = parametersSet.getString(attribute);
                    break;

                    case "agentType" :
                    agentType = parametersSet.getString(attribute);
                    break;
                }
            }
        }
    }


    private JSONObject readConstructorParameters(PApplet _parentApplet, String _parametersFile, String _constructorName) {
        JSONObject parametersSet;

        parametersSet = _parentApplet.loadJSONObject(_parametersFile);
        parametersSet = parametersSet.getJSONObject("PictureConstructors");
        parametersSet = parametersSet.getJSONObject(_constructorName);

        return(parametersSet);
    }


    private void instanciateAgents(String _parametersFile) {
        switch(agentName) {
            case "AttractedPainter" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new AttractedPainter(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "RandomLinker" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new RandomLinker(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "RandomDroplet" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new RandomDroplet(parentApplet, _parametersFile, agentType));
            }
            break;

            case "LightSpeeder" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new LightSpeeder(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "DarknessFattener" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new DarknessFattener(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "RandomBubble" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new RandomBubble(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "HomogenousPatchworker" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new HomogenousPatchworker(parentApplet, _parametersFile, agentType));
            }
            break;

            case "Triangulator" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new Triangulator(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            case "LevelLinker" :
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new LevelLinker(parentApplet, picture, _parametersFile, agentType));
            }
            break;

            default:
            for (int i = 0; i < nbAgents; i++) {
                agents.add(new AttractedPainter(parentApplet, picture, _parametersFile, agentType));
            }
            break;
        }
    }


    public float getBackgroundAlpha() {
        return(backgroundAlpha);
    }


    public int getNbAgents() {
        return(nbAgents);
    }


    public int getPictureWidth() {
        return(picture.getOriginalPictureWidth());
    }


    public int getPictureHeight() {
        return(picture.getOriginalPictureHeight());
    }


    public void addAgent(PictureAgent _agent) {
        agents.add(_agent);
    }


    public void removeAgent(int _index) {
        agents.remove(_index);
    }


    public void removeAllAgents() {
        int maxIndex;

        maxIndex = agents.size() -1;

        for (int i = maxIndex; i >= 0; i--) {
            agents.remove(i);
        }
    }


    public void changeParameters(PApplet _parentApplet, String _parametersFile, String _constructortName) {
        setConstructorParameters(_parentApplet, _parametersFile, _constructortName);
    }


    public void changePicture(String _newPictureName) {
        picture.setOriginalPicture(parentApplet, _newPictureName);
        picture.setAttractor(parentApplet, attractorCreatorsList[attractorCreatorIndex]);
    }


    public void changeAgents(String _parametersFile) {
        removeAllAgents();
        instanciateAgents(_parametersFile);
    }


    public void displayOriginalPicture() {
        picture.displayOriginalPicture(parentApplet);
    }


    public void displayAttractor() {
        picture.displayAttractor(parentApplet);
    }


    public void constructPicture() {
        parentApplet.noStroke();
        parentApplet.fill(255, backgroundAlpha);
        parentApplet.rect(0, 0, getPictureWidth(), getPictureHeight());

        for(int i = 0; i < agents.size(); i++) {
            agents.get(i).update(parentApplet, picture);
            agents.get(i).display(parentApplet, picture);
        }
    }
}
