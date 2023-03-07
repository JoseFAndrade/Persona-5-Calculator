//can use the json to object converter thing here that I used for previous projects
//have to research it again to make sure that I am doing it correctly

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Persona {
    String name;
    String arcana;
    String level;
    int[] stats;
    String[] elements;
    // a way to store skills in the Name : level required to learn it within the persona
    String[] skills;
    String item;
    @JsonProperty("itemr")
    String alarmItem;
    String trait;
    String inherits;


    public String getName() {
        return name;
    }

    public String getArcana() {
        return arcana;
    }

    public String getLevel() {
        return level;
    }

    public int[] getStats() {
        return stats;
    }

    public String[] getElements() {
        return elements;
    }

    public String getItem() {
        return item;
    }

    public String getAlarmItem() {
        return alarmItem;
    }

    public String getTrait() {
        return trait;
    }

    public String getInherits(){return inherits; }
}
