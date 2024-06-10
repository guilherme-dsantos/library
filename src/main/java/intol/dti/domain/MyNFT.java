package intol.dti.domain;


import java.io.Serializable;

public class MyNFT implements Serializable {
    private final int id;
    private final String name;
    private final String uri;
    private final float price;

    public MyNFT(int id, String name, String uri, float price) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "MyNFT{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", price=" + price +
                '}';
    }
}
