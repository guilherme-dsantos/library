package intol.dti;

import intol.dti.domain.MyCoin;
import intol.dti.domain.MyNFT;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DTInteractiveClient {

    public static void main(String[] args) throws IOException {
        int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
        DTI dti = new DTI(clientId);

        Console console = System.console();

        System.out.println("\nCommands:\n");
        System.out.println("\tMY_COINS: List your coins");
        System.out.println("\tMINT: Create a coin (exclusive to client 4)");
        System.out.println("\tSPEND: Transfer currency to another client");
        System.out.println("\tMY_NFTS: List your NFTs");
        System.out.println("\tMINT_NFT: Create a NFT");
        System.out.println("\tSET_NFT_PRICE: Set a NFT's price");
        System.out.println("\tSEARCH_NFT: Search NFT's");
        System.out.println("\tBUY_NFT: Buy a NFT");

        while (true) {
            String cmd = console.readLine("\n  > ");
            if (cmd.equalsIgnoreCase("MY_COINS")) {
                List<MyCoin> coins = dti.myCoins();
                System.out.println("Your Coins: ");
                for (MyCoin coin : coins) {
                    System.out.println(coin);
                }
            } else if (cmd.equalsIgnoreCase("MINT")) {
                Float value = parseFloat("Enter a numeric value for the new coin: ", console);
                if (value == null) continue;
                Integer coinId = dti.mint(value);
                System.out.println("New created coin's ID is " + coinId);
            } else if (cmd.equalsIgnoreCase("SPEND")) {
                int[] coins = parseIntArray(console);
                if (coins == null) continue;
                Integer receiver = parseInteger("Enter the receiver's ID: ", console);
                if (receiver == null) continue;
                Float value =  parseFloat("Enter a numeric value to be transferred: ", console);
                if (value == null) continue;
                int result = dti.spend(coins, receiver, value);
                System.out.println("Operation returned with coinID/result " + result);
            } else if (cmd.equalsIgnoreCase("MY_NFTS")) {
                List<MyNFT> nfts = dti.myNFTs();
                System.out.println("Your NFTs: ");
                for (MyNFT nft : nfts) {
                    System.out.println(nft);
                }
            } else if (cmd.equalsIgnoreCase("MINT_NFT")) {
                String name = console.readLine("Enter the name of the NFT to be created: ");
                String uri = console.readLine("Enter the URI of the NFT to be created: ");
                Float value = parseFloat("Enter a numeric value of the NFT to be created: ", console);
                if (value == null) continue;
                int nftId = dti.mintNFT(name, uri, value);
                System.out.println("New created NFT's ID is " + nftId);
            } else if (cmd.equalsIgnoreCase("SET_NFT_PRICE")) {
                Integer nftId = parseInteger("Enter the NFT's ID: ", console);
                if (nftId == null) continue;
                Float value =  parseFloat("Enter the numeric value to be updated: ", console);
                if (value == null) continue;
                MyNFT nft = dti.setNFTPrice(nftId, value);
                System.out.println("The NFT's price was updated successfully: ");
                System.out.println(nft);
            }  else if (cmd.equalsIgnoreCase("SEARCH_NFT")) {
                String text = console.readLine("Enter the name of the NFTs to be searched: ");
                List<MyNFT> nfts = dti.searchNFTs(text);
                System.out.println("NFTs found: ");
                for (MyNFT nft : nfts) {
                    System.out.println(nft);
                }
            } else if (cmd.equalsIgnoreCase("BUY_NFT")) {
                Integer nftId = parseInteger("Enter the NFT's ID to be bought: ", console);
                if (nftId == null) continue;
                int[] coins = parseIntArray(console);
                if (coins == null) continue;
                int result = dti.buyNFT(nftId, coins);
                System.out.println("Operation returned with coinID/result " + result);
            } else {
                System.out.println("\tInvalid command :P\n");
            }
        }
    }

    private static Float parseFloat(String message, Console console) {
        float value;
        try {
            value = Float.parseFloat(console.readLine(message));
        } catch (NumberFormatException e) {
            System.out.println("The value is supposed to be a float!");
            return null;
        }
        return value;
    }

    private static Integer parseInteger(String message, Console console) {
        int value;
        try {
            value = Integer.parseInt(console.readLine(message));
        } catch (NumberFormatException e) {
            System.out.println("The value is supposed to be an integer!");
            return null;
        }
        return value;
    }

    private static int[] parseIntArray(Console console) {
        List<Integer> coinsList = new ArrayList<>();
        String input = console.readLine("Enter the IDs of the coins to be spent (eg: 1,2,3): ");
        try {
            String[] parts = input.split(",");
            for (String part : parts) {
                int intValue = Integer.parseInt(part.trim());
                coinsList.add(intValue);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integer from specified ID's!");
            return null;
        }
        return coinsList.stream().mapToInt(i -> i).toArray();
    }
}
