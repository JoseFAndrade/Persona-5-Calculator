//can use the json to object converter thing here that I used for previous projects
//have to research it again to make sure that I am doing it correctly

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Persona implements Comparable<Persona> {
    String name;
    String arcana;
    int level;
    int[] stats;
    @JsonProperty("elems")
    String[] elements;
    // a way to store skills in the Name : level required to learn it within the persona
    @JsonProperty("skills")
    Map<String, Integer> skills;
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

    public int getLevel() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setArcana(String arcana) {
        this.arcana = arcana;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStats(int[] stats) {
        this.stats = stats;
    }

    public void setElements(String[] elements) {
        this.elements = elements;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        this.skills = skills;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setAlarmItem(String alarmItem) {
        this.alarmItem = alarmItem;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public void setInherits(String inherits) {
        this.inherits = inherits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\nThe persona name is: %s\n",this.name));
        sb.append(String.format("Inherits: %s.\tLevel: %s.\tAracana: %s.\tTrait: %s.\n",this.inherits,this.level,this.arcana,this.trait));
        sb.append(String.format("Item: %s.\tAlarm Item: %s\n",this.item,this.alarmItem));
        sb.append(String.format("Elements: %s\nSkills: %s\nStats: %s\n", Arrays.toString(this.elements),this.skills,Arrays.toString(this.stats)));

        return sb.toString();
    }

    @Override
    public int compareTo(Persona o) {
        return this.level - o.level;
    }
}
