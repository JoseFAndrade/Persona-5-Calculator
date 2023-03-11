import java.util.Arrays;

public class ArcanaFusionCombination {
    String result;
    String source[];

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public String toString(){
        return String.format("Source: %s. Result: %s. ", Arrays.toString(source),result);
    }
}
