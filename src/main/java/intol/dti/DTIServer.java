/**
 * BFT Map implementation (server side).
 *
 */
package intol.dti;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import intol.dti.domain.Coin;
import intol.dti.domain.MyCoin;
import intol.dti.domain.MyNFT;
import intol.dti.domain.NFT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DTIServer extends DefaultSingleRecoverable {
    private final Logger logger = LoggerFactory.getLogger("dtismart");
    private List<Coin> coins;
    private List<NFT> NFTs;
    int coinId = 1;
    int nftId = 1;
    //The constructor passes the id of the server to the super class
    public DTIServer(int id) {
        coins = new ArrayList<>();
        NFTs = new ArrayList<>();

        //turn-on BFT-SMaRt'replica
        new ServiceReplica(id, this, this);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java DTIServer <server id>");
            System.exit(-1);
        }
        new DTIServer(Integer.parseInt(args[0]));
    }

    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
        //all operations must be defined here to be invoked by BFT-SMaRt
        try {
            DTIMessage response = new DTIMessage();
            DTIMessage request = DTIMessage.fromBytes(command);
            DTIRequestType cmd = request.getType();

            logger.info("Ordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MY_COINS:
                    List<MyCoin> myCoins = coins.stream().filter(coin -> coin.getOwner() == msgCtx.getSender()).map(Coin::toMyCoin).collect(Collectors.toList());
                    response.setMyCoins(myCoins);
                    return DTIMessage.toBytes(response);
                case MINT:
                    if (msgCtx.getSender() == 4){
                        Coin coin1 = new Coin(coinId, msgCtx.getSender(), request.getValue());
                        coins.add(coin1);
                        response.setCoinId(coinId);
                        coinId++;
                    } else {
                        response.setCoinId(-1);
                    }
                    return DTIMessage.toBytes(response);
                case SPEND:
                    int[] rCoins = request.getCoins();
                    int receiver = request.getReceiver();
                    float value = request.getValue();
                    float totalValue = 0;
                    for (int coinId : rCoins) {
                        Coin coin = this.coins.stream().filter(c -> c.getId() == coinId).findFirst().orElse(null);
                        if (coin == null) {
                            response.setCoinId(-1);
                            return DTIMessage.toBytes(response);
                        }
                        totalValue += coin.getValue();
                    }
                    if(totalValue < value){
                        response.setCoinId(-1);
                        return DTIMessage.toBytes(response);
                    } else if(totalValue == value) {
                        response.setCoinId(0);
                    } else {
                        Coin myCoin = new Coin(coinId, msgCtx.getSender(), totalValue - value);
                        coins.add(myCoin);
                        response.setCoinId(myCoin.getId());
                        coinId++;
                    }
                    Coin receiverCoin = new Coin(coinId++, receiver, value);
                    coins.add(receiverCoin);
                    for (int coinId : rCoins) {
                        Coin coin = this.coins.stream().filter(c -> c.getId() == coinId).findFirst().orElse(null);
                        coins.remove(coin);
                    }
                    return DTIMessage.toBytes(response);
                case MY_NFTS:
                    List<MyNFT> MyNFTs = NFTs.stream().filter(nft -> nft.getOwner() == msgCtx.getSender()).map(NFT::toMyNFT).collect(Collectors.toList());
                    response.setMyNFTs(MyNFTs);
                    return DTIMessage.toBytes(response);
                case MINT_NFT:
                    String name = request.getName();
                    String uri = request.getUri();
                    float val = request.getValue();
                    boolean exists = NFTs.stream().anyMatch(nft -> nft.getName().equals(name));
                    if (exists) {
                        response.setNftId(-1);
                        return DTIMessage.toBytes(response);
                    }
                    NFT nft = new NFT(nftId, msgCtx.getSender(), name,uri, val);
                    NFTs.add(nft);
                    response.setNftId(nft.getId());
                    nftId++;
                    return DTIMessage.toBytes(response);
                case SET_NFT_PRICE:
                    float price = request.getValue();

                    Optional<NFT> nftOptional = NFTs.stream().filter(nftfilter -> nftfilter.getId() == request.getNftId()).findFirst();
                    if (nftOptional.isEmpty()) return DTIMessage.toBytes(response);
                    if(nftOptional.get().getOwner() != msgCtx.getSender()) return DTIMessage.toBytes(response);
                    nftOptional.get().setPrice(price);
                    response.setMyNFT(nftOptional.get().toMyNFT());
                    return DTIMessage.toBytes(response);
                case BUY_NFT:
                    int[] cCoins = request.getCoins();
                    int nftId = request.getNftId();
                    NFT nft1 = NFTs.stream().filter(nftfilter -> nftfilter.getId() == nftId).findFirst().orElse(null);
                    if (nft1 == null) return DTIMessage.toBytes(response);
                    float nftPrice = nft1.getPrice();
                    float totalValue1 = 0;
                    for (int coinId : cCoins) {
                        Coin coin = this.coins.stream().filter(c -> c.getId() == coinId).findFirst().orElse(null);
                        if (coin == null) {
                            response.setCoinId(-1);
                            return DTIMessage.toBytes(response);
                        }
                        totalValue1 += coin.getValue();
                    }
                    if(totalValue1 < nftPrice){
                        response.setCoinId(-1);
                        return DTIMessage.toBytes(response);
                    } else if(totalValue1 == nftPrice) {
                        response.setCoinId(0);
                    } else {
                        Coin myCoin = new Coin(coinId, msgCtx.getSender(), totalValue1 - nftPrice);
                        coins.add(myCoin);
                        response.setCoinId(myCoin.getId());
                        coinId++;
                    }
                    Coin rc = new Coin(coinId++, nft1.getOwner(), nftPrice);
                    coins.add(rc);
                    nft1.setOwner(msgCtx.getSender());
                    for (int coinId : cCoins) {
                        Coin coin = this.coins.stream().filter(c -> c.getId() == coinId).findFirst().orElse(null);
                        coins.remove(coin);
                    }
                    return DTIMessage.toBytes(response);
                case SEARCH_NFTS:
                    String text = request.getText() ;
                    List<MyNFT> res = NFTs.stream().filter(n -> n.getName().contains(text)).map(NFT::toMyNFT).collect(Collectors.toList());
                    response.setMyNFTs(res);
                    return DTIMessage.toBytes(response);

            }

            return null;
        }catch (IOException | ClassNotFoundException ex) {
            logger.error("Failed to process ordered request", ex);
            return new byte[0];
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        //read-only operations can be defined here to be invoked without running consensus
        try {
            DTIMessage response = new DTIMessage();
            DTIMessage request = DTIMessage.fromBytes(command);
            DTIRequestType cmd = request.getType();

            logger.info("Unordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MY_NFTS:
                    List<MyNFT> MyNFTs = NFTs.stream().filter(nft -> nft.getOwner() == msgCtx.getSender()).map(NFT::toMyNFT).collect(Collectors.toList());
                    response.setMyNFTs(MyNFTs);
                    return DTIMessage.toBytes(response);
                case SEARCH_NFTS:
                    String text = request.getText() ;
                    List<MyNFT> res = NFTs.stream().filter(n -> n.getName().contains(text)).map(NFT::toMyNFT).collect(Collectors.toList());
                    response.setMyNFTs(res);
                    return DTIMessage.toBytes(response);
                case MY_COINS:
                    List<MyCoin> myCoins = coins.stream().filter(coin -> coin.getOwner() == msgCtx.getSender()).map(Coin::toMyCoin).collect(Collectors.toList());
                    response.setMyCoins(myCoins);
                    return DTIMessage.toBytes(response);
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.error("Failed to process unordered request", ex);
            return new byte[0];
        }
        return null;
    }

    @Override
    public byte[] getSnapshot() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(NFTs);
            out.flush();
            out.writeObject(coins);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void installSnapshot(byte[] state) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(state);
             ObjectInput in = new ObjectInputStream(bis)) {
            coins = (List<Coin>) in.readObject();
            NFTs = (List<NFT>) in.readObject();
        } catch (IOException | ClassNotFoundException ex ) {
            ex.printStackTrace(); //debug instruction
        }
    }
}
