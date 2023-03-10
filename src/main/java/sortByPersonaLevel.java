import java.util.Comparator;

public class sortByPersonaLevel implements Comparator<Persona> {

    //sort in increasing order
    public int compare(Persona a, Persona b){
        return Integer.parseInt(a.level) - Integer.parseInt(b.level);
    }
}
