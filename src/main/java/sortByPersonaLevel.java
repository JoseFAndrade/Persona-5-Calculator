import java.util.Comparator;

public class sortByPersonaLevel implements Comparator<Persona> {

    //sort in increasing order
    public int compare(Persona a, Persona b){
        return a.level - b.level;
    }
}
