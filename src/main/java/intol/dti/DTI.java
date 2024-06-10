/**
 * BFT Map implementation (client side).
 *
 */
package intol.dti;

import bftsmart.tom.ServiceProxy;
import intol.dti.domain.MyCoin;
import intol.dti.domain.MyNFT;
import intol.dti.operations.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class DTI implements Operations {

    private final Logger logger = LoggerFactory.getLogger("dtismart");
    private final ServiceProxy serviceProxy;

    public DTI(int id) {
        serviceProxy = new ServiceProxy(id);
    }

    @Override
    public List<MyCoin> myCoins() {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.MY_COINS);
            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_COINS request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }

        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getMyCoins();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of GET request");
            return null;
        }
    }

    @Override
    public Integer mint(float value) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.MINT);
            request.setValue(value);
            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MINT(Value) request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }

        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of GET request");
            return null;
        }
    }

    @Override
    public int spend(int[] coins, int receiver, float value) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.SPEND);
            request.setValue(value);
            request.setReceiver(receiver);
            request.setCoins(coins);
            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MINT(Value) request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }

        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of GET request");
            return -1;
        }
    }

    @Override
    public List<MyNFT> myNFTs() {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.MY_NFTS);
            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_NFTS request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }

        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getMyNFTs();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_NFTS request");
            return null;
        }
    }

    @Override
    public int mintNFT(String name, String uri, float value) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.MINT_NFT);
            request.setName(name);
            request.setUri(uri);
            request.setValue(value);

            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MINT_NFT request");
            return -1;
        }
        if (rep.length == 0) {
            return -1;
        }
        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getNftId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MINT_NFT request");
            return -1;
        }
    }

    @Override
    public MyNFT setNFTPrice(int nftId, float value) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.SET_NFT_PRICE);
            request.setNftId(nftId);
            request.setValue(value);

            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send SET_NFT_PRICE request");
            return null;
        }
        if(rep.length == 0) {
            return null;
        }
        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getMyNFT();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SET_NFT_PRICE request");
            return null;
        }
    }

    @Override
    public List<MyNFT> searchNFTs(String text) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.SEARCH_NFTS);
            request.setText(text);

            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send SEARCH_NFTS request");
            return null;
        }
        if (rep.length == 0) {
            return null;
        }
        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getMyNFTs();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SEARCH_NFTS request");
            return null;
        }
    }

    @Override
    public int buyNFT(int nftId, int[] coins) {
        byte[] rep;
        try {
            DTIMessage request = new DTIMessage();
            request.setType(DTIRequestType.BUY_NFT);
            request.setNftId(nftId);
            request.setCoins(coins);

            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send BUY_NFT request");
            return -1;
        }
        if (rep.length == 0) {
            return -1;
        }
        try {
            DTIMessage response = DTIMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of BUY_NFT request");
            return -1;
        }
    }
}
