import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts firs
     * other players get 14 tiles, this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() 
    {
        
        int tilePerPlayer = 14;

        for(int i = 0 ; i < tilePerPlayer ; i++)
        {
            for( int j = 0 ; j < players.length ; j++)
            {            
                players[j].addTile(tiles[0]);   
                // tiles.removeTheTile(tiles[tilesToGive]); gibi bişi eklenebilir.
                removeTile();
            }

            if(i == tilePerPlayer -1 )
            {
                players[0].addTile(tiles[0]);
                removeTile();
            }
    
        }

    } 
    // oyuncuya dağıtılan kartı tiles arrayından çıkarmak için method
    public void removeTile ()
    {

        Tile shortenedTiles[] = new Tile[this.tiles.length -1];
        for( int m = 0 ; m < shortenedTiles.length ; m++)
        {
            shortenedTiles[m] = this.tiles[m + 1];

        }
        this.tiles = shortenedTiles ;  
    }  

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() 
    {
        players[this.getCurrentPlayerIndex()].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        if (tiles.length > 0) {
            players[this.getCurrentPlayerIndex()].addTile(tiles[0]);
            return tiles[0].toString();
        } else {
            System.out.println("No more tiles in the stack.");
            return null; // or you can return a specific string indicating no tile available
        }
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Random rand = new Random();

        for (int i = tiles.length - 1; i > 0; i--) 
        {
            int j = rand.nextInt(i + 1);
            Tile temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */
    public boolean didGameFinish() {
        if (!hasMoreTileInStack()) {
            // No more tiles in the stack
            return true;
        }
        for (Player player : players) {
            if (player.checkWinning()) {
                // Player has won
                return true;
            }
        }
        return false;
    }
    

    /* TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    public Player[] getPlayerWithHighestLongestChain() {
        
        ArrayList<Player> winners = new ArrayList<>();
        int longestChain = 0;

        for (int i = 0; i < players.length;  i++) 
        {
            int playerChainLength = players[i].findLongestChain();
            if (playerChainLength > longestChain) 
            {
                longestChain = playerChainLength;
            }
        }

        for (int i = 0; i < players.length;  i++) 
        {
            if (players[i].findLongestChain() == longestChain) 
            {
                winners.add(players[i]);
            }
        }

        return winners.toArray(new Player[0]);
    }
    
    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    public void pickTileForComputer() {
        Player currentPlayer = players[this.getCurrentPlayerIndex()];
    
        if (checkIfUseful(currentPlayer)) {
            currentPlayer.addTile(lastDiscardedTile);
        } else {
            if (tiles.length > 0) {
                currentPlayer.addTile(tiles[0]);
                tiles = Arrays.copyOfRange(tiles, 1, tiles.length);
                tileCount--;
            } else {
                handleEndOfGame(); // Handle end of game
                return; // Exit the method to prevent further processing
            }
        }
        passTurnToNextPlayer(); // Move to the next player
    }
    
    private void handleEndOfGame() {
        System.out.println("No more tiles in the stack. Game over.");
    
        // Display longest chains of all players
        for (Player player : players) {
            System.out.println(player.getName() + "'s longest chain: " + player.findLongestChain());
        }
    
        // Find the player with the highest longest chain
        Player[] winners = getPlayerWithHighestLongestChain();
        if (winners.length == 1) {
            System.out.println("Winner: " + winners[0].getName());
        } else {
            System.out.println("It's a tie between the following players:");
            for (Player winner : winners) {
                System.out.println(winner.getName());
            }
        }
    }
    

    public boolean checkIfUseful(Player currentPlayer) {
        int preLongestChain = currentPlayer.findLongestChain();
        currentPlayer.addTile(lastDiscardedTile);
        int currentLongestChain = currentPlayer.findLongestChain();
        currentPlayer.getAndRemoveTile(currentPlayer.getTiles().length - 1);

        return currentLongestChain > preLongestChain;
    }


    /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() {
        Tile[] currentTile = players[getCurrentPlayerIndex()].getTiles();

        boolean isSameTile = false;
        int indexOfDiscardedTile = 0;

        for (int i = 0; i < currentTile.length - 1; i++) 
        {
            for (int j = i + 1; j < currentTile.length; j++) 
            {
                if (currentTile[i] == currentTile[j]) 
                {
                    isSameTile =  true;
                    indexOfDiscardedTile = i;
                }
            }
        }

        //If there is any duplicated tile
        if(isSameTile)
        {
            this.discardTile(indexOfDiscardedTile);
        }
        else //find shortest chain contribution
        {
            checkIsUsefulForDiscard(players[getCurrentPlayerIndex()]);
        }
    }


    public void checkIsUsefulForDiscard(Player currentPlayer)
    {
        int preLongestChain = currentPlayer.findLongestChain();
        
        for (int i = 0; i < currentPlayer.getTiles().length; i++) 
        {
            Tile tile = new Tile(currentPlayer.getTiles()[i].getValue());
            this.discardTile(i);
            if(currentPlayer.findLongestChain() < preLongestChain)
            {
                currentPlayer.addTile(tile);
            }
            else
            {
                break;
            }
        }
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        this.lastDiscardedTile = players[getCurrentPlayerIndex()].getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }
}

