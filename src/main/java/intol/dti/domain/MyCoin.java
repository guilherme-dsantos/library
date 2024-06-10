package intol.dti.domain;

import java.io.Serializable;

public class MyCoin implements Serializable {
    private final int id;
    private final float value;

    public MyCoin(int id, float value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MyCoin{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
