import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FusionCalculator {

    //hash table of Arcana:  to a list of personas sorted by the level of the personas
    Hashtable<String, List<Persona>> personaTable = new Hashtable<String, List<Persona>>();

    Map<String, Persona> stringPersonaMap = new Hashtable<>();

    public FusionCalculator() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("./Data/Personas.json"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }


        //TODO: try to use JSONParser here

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        jsonObject = new JSONObject(jsonObject.get("personas").toString());
        System.out.println(jsonObject);

        Map<String, Object> test = jsonObject.toMap();
        for( String key: test.keySet())
        {
            System.out.println(key);
            //String isJson = test.get(key).toString().replaceAll(  "(?<== ?)(?![ \\{\\[])(.+?)(?=,|})", "\"$1\"");
            System.out.println(test.get(key).toString());
            //System.out.println(isJson);
        }



        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        try {

            for(String key: test.keySet()){

                System.out.println(test.get(key));
                Persona persona = objectMapper.readValue(test.get(key).toString(),Persona.class);
            }
            //Persona persona = objectMapper.readValue(stringBuilder.toString(), Persona[].class);
            //System.out.println(personas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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

        List<Persona>  personaList = personaTable.get(arcana);

        int tier = 0;
        for( Persona each: personaList){
            if( p.getName().equals(each.getName()))
                return tier;
            tier++;
        }
        return -1;
    }

}
