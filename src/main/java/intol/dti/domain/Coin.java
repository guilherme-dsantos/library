package intol.dti.domain;

import java.io.Serializable;

public class Coin implements Serializable {
    private final int id;
    private final int owner;
    private final float value;

    public Coin(int id, int owner, float value) {
        this.id = id;
        this.owner = owner;
        this.value = value;
    }

    public MyCoin toMyCoin() {
        return new MyCoin(id, value);
    }

    public int getId() {
        return id;
    }
    public int getOwner() {
        return owner;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "id=" + id +
                ", owner=" + owner +
                ", value=" + value +
                '}';
    }
}

