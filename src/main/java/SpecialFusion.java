import java.util.Arrays;

//model class
public class SpecialFusion {

    String result;
    String[] sources;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }

    public String toString(){
        return String.format("The fusion is: %s. The personas needed: %s.", result, Arrays.toString(sources));
    }
}
