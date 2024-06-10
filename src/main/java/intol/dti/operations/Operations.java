package intol.dti.operations;

import intol.dti.domain.MyCoin;
import intol.dti.domain.MyNFT;
import java.util.List;

public interface Operations {
    List<MyCoin> myCoins();
    Integer mint(float value);
    int spend(int[] coins, int receiver, float value);
    List<MyNFT> myNFTs();
    int mintNFT(String name, String uri, float value);
    MyNFT setNFTPrice(int nftId, float value);
    List<MyNFT> searchNFTs(String text);
    int buyNFT(int nftId, int[] coins);
}
