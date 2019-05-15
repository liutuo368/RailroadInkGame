package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
public class RailroadInk {
    /**
     * Created by Jingfen Qiao
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
     * Created by Tuo Liu
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
     * Author : Jihirshu Narayan
     * Created on : 29/03/2019
     * Last modified : -
     * @param tilePlacementStringA
     * @param tilePlacementStringB
     * @return boolean value telling us if the two tile placement strings are connected neighbours or not
     *
     * Description : The tile id, tile location and tile orientation information is extracted from each tile placement string
     *              and two tile objects are connected accordingly. We check if they are connected using the isConnected function from class Tile.
     *
     */

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
     * Author : Jihirshu Narayan
     * Created : 29/03/2019
     * Last modified : 09/05/2019
     * @param boardString
     * @return boolean value representing if the boardstring represents valid placement of tiles
     * Description : The boardString is a string of multiple tileplacement strings concatenated together. We extract tile placement strings one at a time
     *              by incrementing 5 steps in the boardstring. In each iteration we create a tile object which is then added to an array of tile objects.
     *
     *              Once we have the tile array ready, we iterate over each tile object making sure its either connected to an exit or another tile object in the
     *              array. We also check for invalid connections. If all tile have a valid placement then the function returns true.
     */

    public static boolean isValidPlacementSequence(String boardString)
    {
        // FIXME Task 6: determine whether the given placement sequence is valid

        int numberOfTiles = boardString.length()/5;
        Tile[] tile_array = new Tile[numberOfTiles];

        int counter = 0;
        for (int i = 0;i < boardString.length();i=i+5)
        {
            String temp_substring = boardString.substring(i,i+5);
            tile_array[counter] = new Tile(temp_substring.substring(0,2));
            tile_array[counter].set_default();
            tile_array[counter].translate(temp_substring.substring(2,4));
            tile_array[counter].rotate90(temp_substring.charAt(4));

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
                    if (tile_array[i].getLocation().equals(tile_array[j].getLocation()))
                        return false;
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
     * Author : Jihirshu Narayan
     * Created on : 11/04/2019
     * Last modified : -
     * @return a String representing dice roll for a round.
     * Decription : 4 randomly selected dice out of A0,A1,A2,A3,A4,A5,B0,B1,B2 (repitition allowed)
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
     * Author : Jihirshu Narayan
     * Created on : 11/04/2019
     * Last Modified : -
     * Given the current state of a game board, output an integer representing the sum of all the following factors
     * that contribute to the player's final score.
     * <p>
     * * Number of exits mapped
     * * Number of centre tiles used
     * * Number of dead ends in the network
     * @param boardString a board string representing a completed game
     * @return an integer value representing basic score for the given boardstring
     * Description : We create an array of tile objects from the given boardstring. Tile B2 is repeated twice for every occurrence since it represents a highway
     *              connection as well as a railway connection but not connected to each other. Once the tile array is created, we create all possible routes
     *              from the tiles and store each as a route object in a list of routes objects. Then we iterate through the routes list and check if any of
     *              these routes are  connected to each other and merge them if they are. At the end of the iteration, we have a list of disconnected routes and
     *              we count their exits.
     *
     *              We also create a board object which contains a 2D array of tile objects. Using the board object we find out how many tiles are placed in the
     *              center and how many errors are on the board. We get the basic score by summing exit score, center tile score and subtracting the errors.
     *
     */


    public static int getBasicScore(String boardString)
    {
        // FIXME Task 8: compute the basic score

        if (!isValidPlacementSequence(boardString))
            return -9999;                                           // returning absurdly large negative number which indicates that an invalid boardstring was received

        if (boardString.isEmpty())
            return 0;

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
            board.place_tile(tile_array[counter]);
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


    /**
     * Author : Jihirshu Narayan
     * Created : 16/04/2019
     * Last modified : -
     * Given a valid boardString and a dice roll for the round,
     * return a String representing an ordered sequence of valid piece placements for the round.
     * @param boardString a board string representing the current state of the game as at the start of the round
     * @param diceRoll a String representing a dice roll for the round
     * @return A string representing the best possible move given a boardString and a dice roll
     *
     * Description : An array of Tile objects is created from the boardString. A Board object is created as well. The board object has a function which returns
     *               an array of all possible moves for a given baordString and dice roll. We add all the generated move string to the original boardString one by one
     *               and evaluate the basic score for in each iteration. The generated move which returns the highest basic score is selected and returned as the best
     *               possible single move.
     *
     */

    public static String generateMove(String boardString, String diceRoll) {
        // FIXME Task 10: generate a valid move
        String[] choices = {"","","",""};
        int tile_number = (boardString.length())/5;
        Tile[] tile_array = new Tile[tile_number];      // Adjusting the length of tile_array to accomodate B2 piece copies
        Board board = new Board();
        int counter = 0;

        for (int i=0;i<boardString.length();i=i+5)
        {
            String temp = boardString.substring(i,i+5);
            tile_array[counter] = new Tile(temp.substring(0,2));
            tile_array[counter].set_default();
            tile_array[counter].translate(temp.substring(2,4));
            tile_array[counter].rotate90(temp.charAt(4));
            board.place_tile(tile_array[counter]);
        }
        int pieceCounter=0;
        for (int i=0;i<diceRoll.length();i=i+2)
        {
                choices[pieceCounter] = diceRoll.substring(i,i+2);
                pieceCounter++;

        }

//        String[] result = board.generateMoves(choices);
        Set<String> result = board.generateMove(choices);

        int initial_score = getBasicScore(boardString);

        int max = initial_score;
        String result_final = "";
//        for (int i=0;i<result.length;i++)
        for (Object x : result)
        {
            String newBoardString = boardString + x;
            int currentScore = getBasicScore(newBoardString);
            if ( currentScore > max)
            {
                result_final = x.toString();
                max = currentScore;
            }

        }

        return result_final;



    }


    /**
     * Author : Jihirshu Narayan
     * Created : 10/05/2019
     * Last Modified : -
     * Description : This method follows the same implementation as basic score. However, in addition to basic score, we calculate the longest highway and
     *                longest railway route as well and add that as bonus points to the basic score.
     *
     * @param boardString a board string representing a completed game
     * @return an integer value representing advanced score which considers bonus points for longest highway and railway length
     */

    public static int getAdvancedScore(String boardString)
    {
        // FIXME Task 12: compute the total score including bonus points
        if (!isValidPlacementSequence(boardString))
            return -9999;                                       // returning absurdly large negative number which indicates that an invalid boardstring was received

        if (boardString.isEmpty())
            return 0;

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
            board.place_tile(tile_array[counter]);
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

        board.generateConnections();
        board.getLongestHighwayCount();
        board.getLongestRailwayCount();


        return basicScore + board.longestRailway + board.longestHighway;




    }

    public static void main(String[] args)
    {
        String diceRoll = "A1A1A4B0";
        String boardString = "A4A10A1A30A4A50S1B32A1B01A1B61B2B10A1B21S5B50A1B41A4D01A4D61A3D12B0C30A3D50A4C10A4C50A1F01A1F61A4G10B1F12A4G50B1E10A1E21A3E52B1F56S4E31";

        System.out.println(generateMove(boardString, diceRoll));

    }

}
