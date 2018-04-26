package cl.motoratrib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlowException {
    private int result;
    private String ref;
    private String alerta;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    @JsonCreator
    public FlowException(@JsonProperty("result") int result,
                         @JsonProperty("ref") String ref,
                         @JsonProperty("alerta") String alerta) {
        this.result = result;
        this.ref = ref;
        this.alerta = alerta;
    }
}
