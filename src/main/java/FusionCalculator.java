import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public FusionCalculator() throws IOException {

        readPersonaInformation("./Data/Personas.json");
        readPersonaRules("./Data/Rules.json");
        readSpecialFusions("./Data/Rules.json");
    }

    private void readSpecialFusions(String file) throws IOException{
        StringBuilder stringBuilder = readFromFile(file);
        ObjectMapper objectMapper = new ObjectMapper();

        //this contains both advancedFusion json key values in it
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        String advancedFusionJSON = jsonObject.get("advancedFusion").toString();

        SpecialFusion[] advancedFusionList = objectMapper.readValue(advancedFusionJSON, SpecialFusion[].class);

        for(SpecialFusion each: advancedFusionList){
            System.out.println(each);
        }


        //do I leave it in list form or do I put it into a Map?


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

    public String figureArcana(String arcana1, String arcana2){

        return "";
    }

    /**
     * This function will return the tier of the persona within their arcana type. It will be used for when combining
     * a treasure demon and a regular persona.
     * @param p A persona
     * @return An integer in the form of 0 indexing of the tier of the persona given
     */
    private int calculatetier(Persona p){
        String arcana = p.getArcana();

        List<Persona>  personaList = arcanaToPersona.get(arcana);

        int tier = 0;
        for( Persona each: personaList){
            if( p.getName().equals(each.getName()))
                return tier;
            tier++;
        }
        return -1;
    }



}
