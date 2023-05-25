import com.fasterxml.jackson.annotation.JsonProperty;

public class TreasureDemonFusionRule {

    @JsonProperty("")
    String arcana;

    int[] changeBy;

    public String getArcana() {
        return arcana;
    }

    public void setArcana(String arcana) {
        this.arcana = arcana;
    }

    public int[] getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(int[] changeBy) {
        this.changeBy = changeBy;
    }
}
