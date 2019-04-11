package comp1110.ass2;

import java.util.ArrayList;
import java.util.Random;

public class RailroadInk {
    /**
     * Determine whether a tile placement string is well-formed:
     * - it consists of exactly 5 characters;
     * - the first character represents a die A or B, or a special tile S
     * - the second character indicates which tile or face of the die (0-5 for die A and special tiles, or 0-2 for die B)
     * - the third character represents the placement row A-G
     * - the fourth character represents the placement column 0-6
     * - the fifth character represents the orientation 0-7
     *
     * @param tilePlacementString a candidate tile placement string
     * @return true if the tile placement is well formed
     */
    public static boolean isTilePlacementWellFormed(String tilePlacementString) {
        // FIXME Task 2: determine whether a tile placement is well-formed
        if(tilePlacementString.length() == 5) {
            if(tilePlacementString.charAt(0) == 'A' || tilePlacementString.charAt(0) == 'B' || tilePlacementString.charAt(0) == 'S') {
                int tileLength;
                if(tilePlacementString.charAt(0) == 'A' || tilePlacementString.charAt(0) == 'S') {
                    tileLength = 6;

                } else {
                    tileLength = 3;
                }
                if(Character.isDigit(tilePlacementString.charAt(1)) && Character.isDigit(tilePlacementString.charAt(3)) && Character.isDigit(tilePlacementString.charAt(4))) {

                    if (Integer.parseInt(String.valueOf(tilePlacementString.charAt(1))) >= 0 && Integer.parseInt(String.valueOf(tilePlacementString.charAt(1))) < tileLength) {
                        if (tilePlacementString.charAt(2) >= 'A' && tilePlacementString.charAt(2) <= 'G') {
                            if (Integer.parseInt(String.valueOf(tilePlacementString.charAt(3))) >= 0 && Integer.parseInt(String.valueOf(tilePlacementString.charAt(3))) <= 6) {
                                if (Integer.parseInt(String.valueOf(tilePlacementString.charAt(4))) >= 0 && Integer.parseInt(String.valueOf(tilePlacementString.charAt(4))) <= 7) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determine whether a board string is well-formed:
     * - it consists of exactly N five-character tile placements (where N = 1 .. 31);
     * - each piece placement is well-formed
     * - no more than three special tiles are included
     *
     * @param boardString a board string describing the placement of one or more pieces
     * @return true if the board string is well-formed
     */
    public static boolean isBoardStringWellFormed(String boardString) {
        // FIXME Task 3: determine whether a board string is well-formed
        boolean isWellFormed = true;
        if(boardString!=null){
            if(boardString.length()%5 == 0 && (boardString.length()/5 >= 1 && boardString.length()/5 <= 31 )) {
                int cntS = 0;
                for(int i=0; i<boardString.length(); i+=5) {
                    if(isTilePlacementWellFormed(boardString.substring(i, i+5))) {
                        if(boardString.charAt(i) == 'S') {
                            if(++cntS > 3){
                                isWellFormed = false;
                                break;
                            }
                        }
                    } else {
                        isWellFormed = false;
                        break;
                    }
                }
            } else {
                isWellFormed = false;
            }
        } else {
            isWellFormed = false;
        }

        return isWellFormed;
    }


    /**
     * Determine whether the provided placements are neighbours connected by at least one validly connecting edge.
     * For example,
     * - areConnectedNeighbours("A3C10", "A3C23") would return true as these tiles are connected by a highway edge;
     * - areConnectedNeighbours("A3C23", "B1B20") would return false as these neighbouring tiles are disconnected;
     * - areConnectedNeighbours("A0B30", "A3B23") would return false as these neighbouring tiles have an
     * invalid connection between highway and railway; and
     * areConnectedNeighbours("A0B30", "A3C23") would return false as these tiles are not neighbours.
     *
     * @return true if the placements are connected neighbours
     */
//    public static boolean areConnectedNeighbours(String tilePlacementStringA, String tilePlacementStringB) {
//        Tiles a = Tiles.valueOf(tilePlacementStringA.substring(0,2));
//        Tiles b = Tiles.valueOf(tilePlacementStringB.substring(0,2));
//        a.set_default();
//        b.set_default();
//        a.translate(tilePlacementStringA.substring(2,4));
//        b.translate(tilePlacementStringB.substring(2,4));
//
//        a.rotate90(tilePlacementStringA.charAt(4));
//        b.rotate90(tilePlacementStringB.charAt(4));
//
//
//
//        return (a.isConnected(b));
//
//    }
    public static boolean areConnectedNeighbours(String tilePlacementStringA, String tilePlacementStringB)
    {
        Tile a = new Tile(tilePlacementStringA.substring(0,2));
        Tile b = new Tile(tilePlacementStringB.substring(0,2));
        a.set_default();
        b.set_default();
        a.translate(tilePlacementStringA.substring(2,4));
        b.translate(tilePlacementStringB.substring(2,4));
        a.rotate90(tilePlacementStringA.charAt(4));
        b.rotate90(tilePlacementStringB.charAt(4));

        return (a.isConnected(b));
    }

    /**
     * Given a well-formed board string representing an ordered list of placements,
     * determine whether the board string is valid.
     * A board string is valid if each tile placement is legal with respect to all previous tile
     * placements in the string, according to the rules for legal placements:
     * - A tile must be placed such that at least one edge connects to either an exit or a pre-existing route.
     *   Such a connection is called a valid connection.
     * - Tiles may not be placed such that a highway edge connects to a railway edge;
     *   this is referred to as an invalid connection.
     *   Highways and railways may only join at station tiles.
     * - A tile may have one or more edges touching a blank edge of another tile;
     *   this is referred to as disconnected, but the placement is still legal.
     *
     * @param boardString a board string representing some placement sequence
     * @return true if placement sequence is valid
     */
    public static boolean isValidPlacementSequence(String boardString)
    {
        // FIXME Task 6: determine whether the given placement sequence is valid

        int numberOfTiles = boardString.length()/5;
        Tile[] tile_array = new Tile[numberOfTiles];
//        boolean[][] board =  new boolean[7][7];
        int counter = 0;
        for (int i = 0;i < boardString.length();i=i+5)
        {
            String temp_substring = boardString.substring(i,i+5);
            tile_array[counter] = new Tile(temp_substring.substring(0,2));
            tile_array[counter].set_default();
            tile_array[counter].translate(temp_substring.substring(2,4));
            tile_array[counter].rotate90(temp_substring.charAt(4));
//            if (board[tile_array[counter].getShape()[1]][tile_array[counter].getShape()[2]])
//                return false;
//            else
//                board[tile_array[counter].getShape()[1]][tile_array[counter].getShape()[2]] = true;

            counter++;
        }


        boolean[] tile_result = new boolean[numberOfTiles];

        for (int i = 0;i<numberOfTiles;i++)
        {
            if (tile_array[i].checkInvalidExitConnection())
                return false;
            if ((i==0) && (!tile_array[i].check_exit_connection()))
                return false;
            if (tile_array[i].check_exit_connection())
                tile_result[i] = true;

            for (int j = 0;j<numberOfTiles;j++)
            {
                if (i!=j)
                {
                    if (tile_array[i].isInvalidConenction(tile_array[j]))
                        return false;
                    if (tile_array[i].isConnected(tile_array[j]))
                        tile_result[i] = true;
                }
            }
        }
        boolean result = true;
        for (int i = 0;i<numberOfTiles;i++)
        {
            result = result && tile_result[i];
        }

        return result;

    }

    /**
     * Generate a random dice roll as a string of eight characters.
     * Dice A should be rolled three times, dice B should be rolled once.
     * Die A has faces numbered 0-5.
     * Die B has faces numbered 0-2.
     * Each die roll is composed of a character 'A' or 'B' representing the dice,
     * followed by a digit character representing the face.
     *
     * @return a String representing the die roll e.g. A0A4A3B2
     */

    public static String generateDiceRoll() {
        // FIXME Task 7: generate a dice roll
        String dice_roll = "";
        int temp;
        Random random = new Random();
        for (int i=0;i<4;i++)
        {
            if (i == 3)
            {
                temp = random.nextInt(3);
                dice_roll = dice_roll + "B" + Integer.toString(temp);

            }
            else
            {
                temp = random.nextInt(6);
                dice_roll = dice_roll + "A" + Integer.toString(temp);
            }
        }

        return dice_roll;
    }

    /**
     * Given the current state of a game board, output an integer representing the sum of all the following factors
     * that contribute to the player's final score.
     * <p>
     * * Number of exits mapped
     * * Number of centre tiles used
     * * Number of dead ends in the network
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for score *not* considering longest rail/highway
     */
    public static int getBasicScore(String boardString)
    {
        // FIXME Task 8: compute the basic score
        int basicScore = 0;
        int b2Counter = 0;
        for (int i = 0;i < boardString.length();i=i+5)
        {
            if (boardString.substring(i,i+2).equals("B2"))          //Piece B2 will be treated as two individual piece on the same location. Counting B2 pieces in boardstring here
                b2Counter++;

        }
        ArrayList<Route> routes = new ArrayList<>();
        
        int tile_number = (boardString.length())/5;
        Tile[] tile_array = new Tile[tile_number + b2Counter];      // Adjusting the length of tile_array to accomodate B2 piece copies
        Board board = new Board();
        int counter = 0;
        for (int i = 0;i < boardString.length();i=i+5)
        {
            String temp_substring = boardString.substring(i,i+5);
            tile_array[counter] = new Tile(temp_substring.substring(0,2));
            tile_array[counter].set_default();
            tile_array[counter].translate(temp_substring.substring(2,4));
            tile_array[counter].rotate90(temp_substring.charAt(4));
            board.place_tile(tile_array[counter].shape);
            if (tile_array[counter].getName().equals("B2"))
            {
                tile_array[counter+1] = new Tile(temp_substring.substring(0,2));
                tile_array[counter+1].set_default();
                tile_array[counter+1].translate(temp_substring.substring(2,4));
                tile_array[counter+1].rotate90(temp_substring.charAt(4));
                tile_array[counter].shape[5] = 0;
                tile_array[counter+1].shape[4] = 0;
                counter++;
            }


            counter++;
        }
        Route route = new Route(tile_array[0]);
        outer:
        for (int i=1;i<tile_array.length;i++)
        {
            if (routes.isEmpty())
                routes.add(route);
            inner:
            for (Route x : routes)
            {
                if (x.connected_to_route(tile_array[i]))
                    continue outer;
            }
            routes.add(new Route(tile_array[i]));
        }

        for (int i = 0;i<routes.size();i++)
        {
            for (int j = 0;j<routes.size();j++)
            {
                if (i!=j)
                {
                    if (routes.get(i).checkRoutesConnected(routes.get(j)))
                    {
                        routes.get(i).mergeRoutes(routes.get(j));
                        routes.remove(j);
                        i=0;
                        j=0;
                    }
                }
            }
        }

        for (Route x : routes)
        {
            int exits = x.numberOfExitsConnected();
            if (exits == 12)
                basicScore = basicScore + 45;
            else if (exits > 1)
                basicScore = basicScore + (4*(exits-1));
        }

        basicScore = basicScore + board.centreTileScore();

        basicScore = basicScore - board.countErrors();

        return basicScore;
    }

    /**
     * Given a valid boardString and a dice roll for the round,
     * return a String representing an ordered sequence of valid piece placements for the round.
     * @param boardString a board string representing the current state of the game as at the start of the round
     * @param diceRoll a String representing a dice roll for the round
     * @return a String representing an ordered sequence of valid piece placements for the current round
     * @see RailroadInk#generateDiceRoll()
     */
    public static String generateMove(String boardString, String diceRoll) {
        // FIXME Task 10: generate a valid move
        return null;
    }

    /**
     * Given the current state of a game board, output an integer representing the sum of all the factors contributing
     * to `getBasicScore`, as well as those attributed to:
     * <p>
     * * Longest railroad
     * * Longest highway
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for final score (not counting expansion packs)
     */
    public static int getAdvancedScore(String boardString) {
        // FIXME Task 12: compute the total score including bonus points
        return -1;
    }

    //public int getSpecialTileCount(){
        // return 0;
    //}

    public static void main(String[] args) {
        int x = getBasicScore("A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35A4A41A2B31A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27S3B20A4C10A1D50A0F23B2G25A3E30A4E41");
        System.out.println(x);
    }

}


