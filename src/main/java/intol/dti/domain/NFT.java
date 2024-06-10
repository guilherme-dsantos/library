package intol.dti.domain;

import java.io.Serializable;

public class NFT implements Serializable {
    private final int id;
    private int owner;
    private final String name;
    private final String uri ;
    private float price;

    public NFT(int id, int owner, String name, String uri, float price) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.uri = uri;
        this.price = price;
    }

    public MyNFT toMyNFT() {
        return new MyNFT(id, name, uri, price);
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
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

    public void setPrice(float price) { this.price = price; }

    public void setOwner(int owner) { this.owner = owner; }

    @Override
    public String toString() {
        return "NFT{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", price=" + price +
                '}';
    }
}

