import java.io.IOException;

public class Main {

    public static void main(String[] args){


        try {
            FusionCalculator fusionCalculator = new FusionCalculator();
            fusionCalculator.testerFunction();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
