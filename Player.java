import java.util.*;

public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name)
    {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: checks this player's hand to determine if this player is winning
     * the player with a complete chain of 14 consecutive numbers wins the game
     * note that the player whose turn is now draws one extra tile to have 15 tiles in hand,
     * and the extra tile does not disturb the longest chain and therefore the winning condition
     * check the assigment text for more details on winning condition
     */
    //NOTE: DONE

    public boolean checkWinning()
    {
        if(findLongestChain() == 14)
        {
            return true;
        }
        return false;
    }

    /*
     * TODO: used for finding the longest chain in this player hand
     * this method should iterate over playerTiles to find the longest chain
     * of consecutive numbers, used for checking the winning condition
     * and also for determining the winner if tile stack has no tiles
     */
    //NOTE: DONE
    public int findLongestChain()
    {
        int longestChain = 1;

        int currentChain = 1;

        for (int i = 0; i < playerTiles.length-1; i++)
        {
            if(playerTiles[i].value + 1 == playerTiles[i+1].value)
            {
                currentChain++;
            }

            else if(playerTiles[i].value + 1 != playerTiles[i+1].value)
            {
                if(currentChain > longestChain)
                {
                    longestChain = currentChain;
                }
                currentChain = 0;
            }
        }
        
        return longestChain;
    }

    /*
     * TODO: removes and returns the tile in given index position
     */
    //NOTE: DONE
    public Tile getAndRemoveTile(int index)
    {
        Tile tileOnHook = playerTiles[index];
        for (int i = index; i < playerTiles.length-1; i++)
        {
            playerTiles[i] = playerTiles[i+1];
        }
        playerTiles = Arrays.copyOf(playerTiles, playerTiles.length -1); //playerTiles length is 14 from now on, be careful at addTile() method
        numberOfTiles--;
        return tileOnHook; 
    }

    /*
     * TODO: adds the given tile to this player's hand keeping the ascending order
     * this requires you to loop over the existing tiles to find the correct position,
     * then shift the remaining tiles to the right by one
     */
    //NOTE: DONE
    public void addTile(Tile t) //be careful the length is 14
    {
        playerTiles = Arrays.copyOf(playerTiles, playerTiles.length + 1);
        playerTiles[playerTiles.length] = t;

        for (int i = 1; i < playerTiles.length; i++)
        {
            int key = playerTiles[i].value;
            int j = i - 1;

            while (j >= 0 && playerTiles[j].value > key)
            {
                playerTiles[j + 1] = playerTiles[j];
                j = j - 1;
            }

            playerTiles[j + 1].value = key;
        }
        numberOfTiles++;
    }

    /*
     * finds the index for a given tile in this player's hand
     */
    public int findPositionOfTile(Tile t)
    {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++)
        {
            if(playerTiles[i].matchingTiles(t))
            {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /*
     * displays the tiles of this player
     */
    public void displayTiles()
    {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++)
        {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles()
    {
        return playerTiles;
    }

    public void setName(String name)
    {
        playerName = name;
    }

    public String getName()
    {
        return playerName;
    }
}
