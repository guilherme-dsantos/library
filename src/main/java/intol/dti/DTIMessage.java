package intol.dti;

import intol.dti.domain.MyCoin;
import intol.dti.domain.MyNFT;

import java.io.*;
import java.util.List;

public class DTIMessage implements Serializable {

    private DTIRequestType type;
    private float value;
    private int[] coins;
    private int coinId;
    private List<MyCoin> myCoins;
    private List<MyNFT> myNFTs;
    private MyNFT myNFT;
    private int receiver;
    private String name;
    private String uri;
    private int nftId;
    private String text;

    public DTIMessage() {

    }

    public static byte[] toBytes(DTIMessage message) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);
        objOut.flush();
        byteOut.flush();
        return byteOut.toByteArray();
    }


    public static DTIMessage fromBytes(byte[] rep) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (DTIMessage) objIn.readObject();
    }

    public DTIRequestType getType() {
        return type;
    }

    public void setType(DTIRequestType type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public void setMyCoins(List<MyCoin> mycoin) {
        this.myCoins = mycoin;
    }

    public void setMyNFTs(List<MyNFT> myNFTs) {
        this.myNFTs = myNFTs;
    }

    public int[] getCoins() {
        return coins;
    }

    public List<MyCoin> getMyCoins() {
        return myCoins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }

    public int getCoinId() {
        return coinId;
    }

    public MyNFT getMyNFT() {
        return myNFT;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getNftId() {
        return nftId;
    }

    public void setMyNFT(MyNFT myNFT) {
        this.myNFT = myNFT;
    }

    public List<MyNFT> getMyNFTs() {
        return myNFTs;
    }

    public void setNftId(int nftId) {
        this.nftId = nftId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
