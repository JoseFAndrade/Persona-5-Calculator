import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FusionCalculator {

    //hash table of Arcana:  to a list of personas sorted by the level of the personas
    Map<String, List<Persona>> arcanaToPersona = new Hashtable<>();
    Map<String, Persona> stringPersonaMap = new Hashtable<>();

    //a table that contains the combination rules between arcana types for fast lookup times
    Map<String, Map<String, String>> arcanaToArcanaFusion = new Hashtable<>();

    //this map will contain a Key: Persona to Value: materials required to create it
    Map<String, List<String>> specialFusions = new Hashtable<>();


    //list of treasure demons -> we can use .indexOf to find out what the index is in order to go through the chart
    List<String> treasureDemonArray;

    //keep track of the chart
    Map<String, List<Integer>> treasureDemonFusionChart  = new Hashtable<>();


    public FusionCalculator() throws IOException {

        readPersonaInformation("./Data/Personas.json");
        readPersonaRules("./Data/Rules.json");
        readSpecialFusions("./Data/Rules.json");
        readTreasureDemonRules("./Data/Rules.json");
    }

    private void readTreasureDemonRules(String file) throws IOException{

        // obtain the names and set them in the list

        StringBuilder stringBuilder = readFromFile(file);
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        String treasureDemonJSON = jsonObject.get("rarePersona").toString();
        String[] treasureDemonList = objectMapper.readValue(treasureDemonJSON, String[].class);
        treasureDemonArray = new ArrayList<>(Arrays.asList(treasureDemonList));

        // time to obtain the chart and insert it into a map
        //this will be in a JSON object instead because it's not easily indexed by using an object mapper
        JSONObject treasureDemonChartJSON = new JSONObject(jsonObject.get("rareCombos").toString());

        for(String key: treasureDemonChartJSON.keySet()){
            ArrayList<Integer> value = new ArrayList<>();
            //get a temp JSON array of Object and turn them into Integers because I know that they will always be convertable

            JSONArray temp = (JSONArray) treasureDemonChartJSON.get(key);
            for(Object each: temp){
                value.add((Integer) each);
            }

            this.treasureDemonFusionChart.put(key, value );
        }
    }

    private void readSpecialFusions(String file) throws IOException{
        StringBuilder stringBuilder = readFromFile(file);
        ObjectMapper objectMapper = new ObjectMapper();

        //this contains both advancedFusion json key values in it
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        String advancedFusionJSON = jsonObject.get("advancedFusion").toString();

        SpecialFusion[] advancedFusionList = objectMapper.readValue(advancedFusionJSON, SpecialFusion[].class);

        for(SpecialFusion each: advancedFusionList){
            specialFusions.put( each.getResult(), new ArrayList<>(Arrays.asList(each.getSources())) );
        }
    }

    private void readPersonaRules(String file) throws IOException{
        StringBuilder stringBuilder = readFromFile(file);
        ObjectMapper objectMapper = new ObjectMapper();

        //this contains both advancedFusion & ArcanafusionArcana json key values in it
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        //System.out.println(jsonObject);

        String arcanaFusionRules = jsonObject.get("ArcanafusionArcana").toString();

        //maps all the fusion combinations in one
        ArcanaFusionCombination[] arcanaFusionCombinations = objectMapper.readValue(arcanaFusionRules,ArcanaFusionCombination[].class);

        //this is adding it into a map for fast look up
        for(ArcanaFusionCombination each: arcanaFusionCombinations) {
            //System.out.println(each);
            String[] source = each.getSource();
            String result = each.getResult();
            //will only do it one way when retrieving it we can just swap the two arcanas if not found
            // i.e will only keep one version of a + b = c instead of also adding in b + a = c
            Map<String,String> map = arcanaToArcanaFusion.get(source[0]);

            if( arcanaToArcanaFusion.get(source[0]) == null )
                map = new Hashtable<>();
            map.put(source[1], result);
            arcanaToArcanaFusion.put(source[0],map);
        }


    }

    private void readPersonaInformation(String file) throws IOException {
        StringBuilder stringBuilder = readFromFile("./Data/Personas.json");
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        //we need to index the outer layer of the json
        jsonObject = new JSONObject(jsonObject.get("personas").toString());

        //created a map because the name of personas is the outer layer of the json, and it has no "type: personaName" format
        Map<String, Object> personaToValue = jsonObject.toMap();

        for( String key: personaToValue.keySet())
        {
            Persona persona = objectMapper.readValue(jsonObject.get(key).toString(),Persona.class);
            persona.setName(key); //setting the name outside because it is not within the value (json)
            //System.out.println(key);
            //System.out.println(persona);

            //add the persona into the map data -- this is temporary and will change in the future to make it better
            updatePersonaTables(persona);
        }

    }

    /**
     * This function will update the tables related to the Persona Information
     * @param persona A persona object whose info will be added onto the maps
     */
    private void updatePersonaTables(Persona persona){
        String name = persona.getName();
        String arcana = persona.getArcana();
        stringPersonaMap.put(name,persona);

        if(arcanaToPersona.containsKey(arcana)){
            ArrayList<Persona> personaList = new ArrayList<>(arcanaToPersona.get(arcana));
            personaList.add(persona);
            Collections.sort(personaList,new sortByPersonaLevel());
            arcanaToPersona.put(arcana, personaList);
        }
        else{
            arcanaToPersona.put(arcana, new ArrayList<>(Arrays.asList(persona)));
        }
    }

    /**
     * This function will read a json file and turn it into a StringBuilder
     * @param fileName Location path for the file
     * @return  Returns a StringBuilder with the data
     * @throws IOException
     */
    private StringBuilder readFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder;
    }

    //---------------------------------------------------------------------------------------------------------

    public void testerFunction(){
        Persona arsene = new Persona();
        arsene.setLevel(9);
        arsene.setArcana("Fool");
        arsene.setName("Arsène");

        TreasureDemon treasureDemon = new TreasureDemon();
        treasureDemon.setName("Regent");

        combineTreasureToPersona(arsene, treasureDemon);

        Persona pixie = new Persona();
        pixie.setLevel(16);
        pixie.setArcana("Fool");

        Persona obariyon = new Persona();
        obariyon.setLevel(8);
        obariyon.setArcana("Fool");

        combinePersonaToPersona(pixie,obariyon);
    }


    /**
     * Fusion between a treasure demon and regular persona. The level of personas matter here and I need to make changes
     * to the default arcana list for that tier.
     */

    //todo Need to test out some theories about the way that tiers work | need to launch the game and test this out
    //TODO if the level of the persona ends up the same level as the higher persona -> means it reaches the same tier
    //todo but does that mean that the old tier now doesn't have any personas? or does it get broken down and the tiers shift?
        //todo it will not get broken down the tier will remain tierless
    //todo for now the code will assume that the tier list does not shift down if one is empty
    private Persona combineTreasureToPersona(Persona persona, TreasureDemon treasureDemon ){

        //will always make a persona of the same tier
        Integer treasureDemonIndex = this.treasureDemonArray.indexOf(treasureDemon.getName());
        Integer changeBy = this.treasureDemonFusionChart.get("Fool").get(treasureDemonIndex);
        System.out.println("change by: " + changeBy);
        Integer personaTier = calculateTier(persona,true);
        System.out.println("persona tier: " + personaTier);
        Integer finalTier = personaTier + changeBy;

        //make a deep copy so we don't modify the default
        List<Persona> personaList = new ArrayList<>(arcanaToPersona.get(persona.arcana));

        //will now need to go through the list and make some sort of tier changes within it
        //todo Idea numero uno: Make it so that you replace the persona within the list to a null if the tiers are not the same
        int index = 0;

        //todo change this to a while statement
        for(Persona eachPersona: personaList){
            if( eachPersona.name.equals(persona.name) && index != personaTier){
                break;
            }
            index++;
        }

        //need to replace the same persona object with null -> this will remove that specific tier
        personaList.set(index, null);

        Persona target = personaList.get(finalTier);
        System.out.println(target);

        return target;

    }

    private Persona combinePersonaToPersona(Persona one, Persona two){

        Persona result = null;

        double formulaResult = fusionFormula(one.level, two.level);
        System.out.println("Resulting value from putting into the formula is: " + formulaResult);

        String arcana = obtainArcanaFromFusion(one.arcana,two.arcana);
        System.out.println("Resulting Arcana is: " + arcana);


        //need to find the arcana that either matches that level or is the closest to it rounded up

        for( Persona p : this.arcanaToPersona.get(arcana)){
            if(p.level == formulaResult){
                result = p;
                break;
            }
            else if ( p.level < formulaResult) {
                result = p;
                break;
            }
        }

        System.out.println("The persona is:\n" + result);
        return result;
    }


    public Persona calculateFusion(Persona one, Persona two){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Persona[] personas = objectMapper.readValue("./Data/Personas.json", Persona[].class);
            System.out.println(personas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double fusionFormula(int level1, int level2){
        double answer = ((level1 + level2))/2;

        if (answer % 1 == 0)
            answer += 1;
        else
            answer = Math.ceil(answer + .5);

        return answer;
    }

    public String obtainArcanaFromFusion(String arcana1, String arcana2){
        String value = this.arcanaToArcanaFusion.get(arcana1).get(arcana2);

        if(value == null)
            value = this.arcanaToArcanaFusion.get(arcana2).get(arcana1);

        return value;
    }


    //todo This function might only be used for treasure demon fusions I believe
    /**
     * This function will return the tier of the persona within their arcana type. It will be used for when combining
     * a treasure demon and a regular persona.
     * @param p A persona
     * @param special A boolean that will determine if the fusion involves a treasure demon
     * @return An integer in the form of 0 indexing of the tier of the persona given
     */
    public Integer calculateTier(Persona p, Boolean special){
        if(!special){
            return calculateTierRegular(p);
        }
        return calculateTierSpecial(p);
    }

    private Integer calculateTierRegular( Persona p){
        String arcana = p.getArcana();
        List<Persona> personaList = arcanaToPersona.get(arcana);
        int tier = 0;

        for( Persona each: personaList){
            if( p.getName().equals(each.getName()))
                return tier;
            tier++;
        }

        return tier;
    }

    private Integer calculateTierSpecial(Persona p ){
        String arcana = p.getArcana();
        List<Persona> personaList = arcanaToPersona.get(arcana);
        int tier = 0;

        for( Persona each: personaList){
            if( p.level == each.level || p.level < each.level)
                return tier;
            tier++;
        }

        return tier;
    }




}
