/**
 * Author : Jihirshu Narayan
 * Created : 29/03/2019
 * Last Modifed - 10/05/2019
 * Descritption : Class board stores a collection of tiles as a two dimensional array. This representation of board makes it easier
 *                to track tile placements and explore their interaction with one another.
 */

package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;



public class Board
{
    private Object[][] board;
    private ArrayList<String> specialTiles = new ArrayList<>(Arrays.asList("S0","S1","S2","S3","S4","S5"));
    private int[][] highwayConnections;
    private int[][] railwayConnections;
    public int longestHighway = 0;
    public int longestRailway = 0;


    public Board()
    {
        this.board = new Object[7][7];
        this.highwayConnections = new int[7][7];
        this.railwayConnections = new int[7][7];
    }

    public Board(Board board)
    {
        this.board = new Object[7][7];
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if (board.board[i][j] != null)
                {
                    Tile temp = (Tile) board.board[i][j];
                    this.board[i][j] = new Tile(temp.getName(), temp.shape);
                }
            }
        }
    }


    public Board(String boardString)
    {
        int tile_number = (boardString.length())/5;
        this.board = new Object[7][7];

        for (int i=0;i<boardString.length();i=i+5)
        {
            String temp = boardString.substring(i,i+5);
            Tile x = new Tile(temp.substring(0,2));
            x.set_default();
            x.translate(temp.substring(2,4));
            x.rotate90(temp.charAt(4));
            place_tile(x);
        }
    }


    public void place_tile(Tile x)
    {
//        String[] names = {" ","S0","S1","S2","S3","S4","S5","A0","A1","A2","A3","A4","A5","B0","B1","B2"};
        this.board[x.shape[1]][x.shape[2]] = new Tile(x.getName(), x.shape);
    }


    /**
     * Author : Jihirshu Narayan
     * @param : x, which is the tile for which we check if there is any invalid connection
     * @return : returns true if there is an invalid connection of tile x with any of its neighbouring tile
     */

    public boolean checkInvalidConnection(Tile x)
    {
        try
        {
            if (x.isInvalidConenction((Tile)this.board[x.shape[1]-1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.isInvalidConenction((Tile)this.board[x.shape[1]+1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.isInvalidConenction((Tile)this.board[x.shape[1]][x.shape[2]-1]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.isInvalidConenction((Tile)this.board[x.shape[1]][x.shape[2]+1]))
                return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }

    /**
     * Author : Jihirshu Narayan
     * @return : returns the count of number of tiles placed in the centre region of board. Used for calculating basic score.
     */

    public int centreTileScore()
    {
        int centerTilesCount = 0;
        for (int i=2;i<=4;i++)
        {
            for (int j=2;j<=4;j++)
            {
                if (!(this.board[i][j] == null))
                    centerTilesCount++;
            }
        }

        return centerTilesCount;

    }

    /**
     * Author : Jihirshu Narayan
     * @return : counts number of erros on a board
     * Description : Iterates over every board location and if a tile is placed on that location,
     * makes sure that all of its highways and railways are connected. If not, then count of errors is incremented.
     */

    public int countErrors()
    {
        int errors = 0;
        Integer[] left = {4,6,10,7,12,13,14,15};
        Integer[] right = {2,6,8,9,11,12,14,15};
        Integer[] down = {3,5,9,10,11,12,13,15};
        Integer[] up = {1,5,7,8,11,13,14,15};
        for (int i = 0 ; i < 7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if (!(this.board[i][j] == null))
                {
                    Tile temp = (Tile) this.board[i][j];

                    if (((Arrays.asList(up).contains(temp.getShape()[4])) || (Arrays.asList(up).contains(temp.getShape()[5]))) && (i != 0))
                    {
                        if (!temp.isConnected((Tile)this.board[i-1][j]))
                            errors++;
                    }
                    if (((Arrays.asList(down).contains(temp.getShape()[4])) || (Arrays.asList(down).contains(temp.getShape()[5]))) && (i != 6))
                    {
                        if (!temp.isConnected((Tile)this.board[i+1][j]))
                            errors++;
                    }
                    if (((Arrays.asList(left).contains(temp.getShape()[4])) || (Arrays.asList(left).contains(temp.getShape()[5]))) && (j != 0))
                    {
                        if (!temp.isConnected((Tile)this.board[i][j-1]))
                            errors++;
                    }
                    if (((Arrays.asList(right).contains(temp.getShape()[4])) || (Arrays.asList(right).contains(temp.getShape()[5]))) && (j != 6))
                    {
                        if (!temp.isConnected((Tile)this.board[i][j+1]))
                            errors++;
                    }
                }
            }
        }

        return errors;
    }

    /**
     * Author : Jihirshu Narayan
     * @return : count of special tiles placed on the board
     */

    public int countSpecialTiles()
    {
        int countSpecialTiles = 0;
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if (this.board[i][j] != null) {
                    Tile temp = (Tile) this.board[i][j];
                    if (temp.getName().charAt(0) == 'S')
                    {
                        this.specialTiles.remove(temp.getName());
                        countSpecialTiles++;
                    }

                }
            }
        }

        return countSpecialTiles;

    }

    /**
     * Author : Jihirshu Narayan
     * @param choices
     * @return array of moves
     * This is a backup move generator. Not used in the game currently, but is capable of solving task 10.
     */

    public String[] generateMoves(String[] choices)
    {
        if (choices.length == 0)
            return null;
        ArrayList<String> results = new ArrayList<>();
        int countSpecialTiles = this.countSpecialTiles();

        String result_string = "";
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                int[][] x_y_array = {{i, j+1},{i,j-1},{i+1,j}, {i-1,j}};
//                int[] y_array = {j-1, j+1};
                if (this.board[i][j] == null)
                {
                    inner:
                    for (int[] x : x_y_array)
                    {
                         try
                         {
                             if (this.board[x[0]][x[1]] != null)
                             {
                                 Tile neighbour = (Tile) this.board[x[0]][x[1]];
                                 if (countSpecialTiles < 3)
                                 {
                                     for (int z = 0; z < this.specialTiles.size(); z++) {
                                         String special = this.specialTiles.get(z);
                                         Tile currentTile = new Tile(special);
                                         currentTile.set_default();
                                         currentTile.translate(i, j);

                                         for (int orientation = 0; orientation < 8; orientation++)
                                         {
                                             if (((special.equals("S2")) || (special.equals("S3"))) && (j > 1))
                                                 continue;
                                             if ((special.equals("S5")) && (j>1))
                                                 continue;

                                             currentTile.rotate90(orientation);
                                             if (results.contains(currentTile.toString()))
                                                 continue ;
                                             if (!(this.checkInvalidConnection(currentTile)))
                                             {
                                                 if (((neighbour.isConnected(currentTile)) || (currentTile.checkExitConnection())) && (!currentTile.checkInvalidExitConnection()))
                                                 {
                                                     ArrayList<String> temp_list = new ArrayList<>(Arrays.asList(choices));
                                                     String[] new_choice = temp_list.toArray(new String[temp_list.size()]);
                                                     Board board = new Board(this);
                                                     board.place_tile(currentTile);
                                                     result_string = result_string + currentTile.toString();
                                                     String[] temp_results = board.generateMoves(new_choice);
                                                     for (int k=0;k<temp_results.length;k++)
                                                     {
                                                         results.add(result_string+temp_results[k]);
                                                     }

                                                     if (temp_results==null)
                                                         results.add(result_string);
                                                     else
                                                     {
                                                         for (int k=0;k<temp_results.length;k++)
                                                         {
                                                             results.add(result_string+temp_results[k]);
                                                         }
                                                     }

                                                     result_string = "";
                                                 }
//                                                 if ((i == 0) || (i == 6) || (j == 0) || (j == 6))
//                                                     if (currentTile.checkExitConnection())
//                                                         results.add(currentTile.toString());
                                             }

                                         }
                                     }
                                 }
                                 for (String z : choices)
                                 {
                                     if (!z.equals(""))
                                     {
                                         Tile currentTile = new Tile(z);
                                         currentTile.set_default();
                                         currentTile.translate(i, j);
                                         for (int orientation = 0; orientation < 8; orientation++) {
//                                             if (((z.equals("A1")) || (z.equals("A4"))) && (j>1))
//                                                 continue;
//
//                                             if ((!(z.equals("B1"))) && (j > 3))
//                                                 continue;

                                             currentTile.rotate90(orientation);
                                             if (results.contains(currentTile.toString()))
                                                 continue ;
                                             if (!(this.checkInvalidConnection(currentTile)))
                                             {
                                                 if (((neighbour.isConnected(currentTile)) || (currentTile.checkExitConnection())) && (!currentTile.checkInvalidExitConnection()))

                                                 {
                                                     ArrayList<String> temp_list = new ArrayList<>(Arrays.asList(choices));
                                                     temp_list.remove(currentTile.getName());
                                                     String[] new_choice = temp_list.toArray(new String[temp_list.size()]);
                                                     Board board = new Board(this);
                                                     board.place_tile(currentTile);
                                                     result_string = result_string + currentTile.toString();
                                                     String[] temp_results = board.generateMoves(new_choice);

                                                     if (temp_results==null)
                                                         results.add(result_string);
                                                     else
                                                     {
                                                         for (int k=0;k<temp_results.length;k++)
                                                         {
                                                             results.add(result_string + temp_results[k]);
                                                         }
                                                     }

                                                     result_string = "";

                                                 }
//                                                 if ((i == 0) || (i == 6) || (j == 0) || (j == 6))
//                                                     if (currentTile.checkExitConnection())
//                                                         results.add(currentTile.toString());
                                             }


                                         }
                                     }
                                 }
                             }
                         }
                         catch (Exception e)
                         {
                            continue inner;
                         }
                    }
                }
            }
            if (results.size() > 250)                  // THIS BLOCK LIMITS THE MOVES IN ORDER TO PASS THE TESTS WITHIN TIME LIMIT
            {
                String[] result = results.toArray(new String[results.size()]);
                return result;
            }
        }


        if (results.size() == 0)
            results.add("");

        String[] result = results.toArray(new String[results.size()]);
        return result;
    }

    /**
     * Author : Jihirshu Narayan
     * @param x, an instance of Tile
     * @return true if there is a highway connection, otherwise false
     * Description : For the parameter Tile x, we check if it has a highway connection with any of its neighbours
     */

    public boolean checkHighwayConnection(Tile x)
    {
        if (x.checkHighwayExit())
            return true;
        try
        {
            if (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1]))
                return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }

    /**
     * Author : Jihirshu Narayan
     * @param x, an instance of Tile
     * @return true if there is a railway connection, otherwise false
     * Description : For the parameter Tile x, we check if it has a railway connection with any of its neighbours
     */
    public boolean checkRailwayConnection(Tile x)
    {
        if (x.checkRailwayExit())
            return true;
        try
        {
            if (x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1]))
                return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }


    /**
     * Author : Jihirshu Narayan
     * Description : Populates highway and railway connection maps. Setting highwayConnections and railwayConnections 1 in appropriate places.
     */

    public void generateConnections()
    {
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if (this.board[i][j] != null)
                {
                    Tile x = (Tile) board[i][j];
                    if (checkHighwayConnection(x))
                        this.highwayConnections[i][j] = 1;
                    if (checkRailwayConnection(x))
                        this.railwayConnections[i][j] = 1;
                }
            }
        }
    }

    /**
     * Author : Jihirshu Narayan
     * Gets Longest Railway conenction count
     */

    public void getLongestRailwayCount()
    {
        for (int i = 0;i<7;i++)
        {
            for (int j = 0;j<7;j++)
            {
                if (this.board[i][j] != null)
                {
                    int[][] clone = new int[7][7];

                    for (int x = 0; x < 7; x++)
                    {
                        for (int y = 0; y < 7; y++)
                        {
                            clone[x][y] = this.railwayConnections[x][y];
                        }
                    }

                    if (this.railwayConnections[i][j] == 1)
                    {
                        findRailwayNeighboursRecursively((Tile) this.board[i][j], 1, clone);
                    }
                }
            }
        }
    }

    /**
     * Author : Jihirshu Narayan
     * @param x, current instance of Tile,
     * @param count, integer which keeps count of railway connections so far
     * @param map, 2d integer array carrying map of railways
     * Description : The function checks for railway connection of Tile x with its neighbours and recursively branches off
     *             in each direction where it finds a connection, with its count incremented and its railway map updated
     */

    public void findRailwayNeighboursRecursively(Tile x, int count, int[][] map)
    {
        if (count > longestRailway)
            longestRailway = count;


        int[][] clone = new int[7][7];

        for (int i = 0;i<7;i++)
        {
            for (int j = 0;j<7;j++)
            {
                clone[i][j] = map[i][j];
            }
        }
        clone[x.shape[1]][x.shape[2]] = 0;

        try
        {
            if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) && (map[x.shape[1]-1][x.shape[2]] == 1))
            {

                findRailwayNeighboursRecursively((Tile) this.board[x.shape[1]-1][x.shape[2]], count+1,clone);
            }
        }

        catch (Exception e)
        {

        }
        try
        {
            if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) && (map[x.shape[1]+1][x.shape[2]] == 1))
            {

                findRailwayNeighboursRecursively((Tile) this.board[x.shape[1]+1][x.shape[2]], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }
        try
        {
            if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) && (map[x.shape[1]][x.shape[2]-1] == 1))
            {

                findRailwayNeighboursRecursively((Tile)this.board[x.shape[1]][x.shape[2]-1], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }
        try
        {
            if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) && (map[x.shape[1]][x.shape[2]+1] == 1))
            {

                findRailwayNeighboursRecursively((Tile)this.board[x.shape[1]][x.shape[2]+1], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }


    }

    /**
     * Author : Jihirshu Narayan
     * Gets Longest Highway conenction count
     */

    public void getLongestHighwayCount()
    {
        for (int i = 0;i<7;i++)
        {
            for (int j = 0;j<7;j++)
            {
                if (this.board[i][j] != null)
                {
                    int[][] clone = new int[7][7];

                    for (int x = 0; x < 7; x++)
                    {
                        for (int y = 0; y < 7; y++)
                        {
                            clone[x][y] = this.highwayConnections[x][y];
                        }
                    }

                    if (this.highwayConnections[i][j] == 1)
                    {
                        findHighwayNeighboursRecursively((Tile) this.board[i][j], 1, clone);
                    }
                }
            }
        }
    }

    /**
     * Author : Jihirshu Narayan
     * @param x, current instance of Tile,
     * @param count, integer which keeps count of highway connections so far
     * @param map, 2d integer array carrying map of railways
     * Description : The function checks for highway connection of Tile x with its neighbours and recursively branches off
     *             in each direction where it finds a connection, with its count incremented and its highway map updated
     */


    public void findHighwayNeighboursRecursively(Tile x, int count, int[][] map)
    {
        if (count > longestHighway)
            longestHighway = count;


        int[][] clone = new int[7][7];
        for (int i = 0;i<7;i++)
        {
            for (int j = 0;j<7;j++)
            {
                clone[i][j] = map[i][j];
            }
        }

        clone[x.shape[1]][x.shape[2]] = 0;

        try
        {
            if ((x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) && (map[x.shape[1]-1][x.shape[2]] == 1))
            {
                findHighwayNeighboursRecursively((Tile) this.board[x.shape[1]-1][x.shape[2]], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }
        try
        {
            if ((x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) && (map[x.shape[1]+1][x.shape[2]] == 1))
            {
                findHighwayNeighboursRecursively((Tile) this.board[x.shape[1]+1][x.shape[2]], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }

        try
        {
            if ((x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) && (map[x.shape[1]][x.shape[2]-1] == 1))
            {
                findHighwayNeighboursRecursively((Tile)this.board[x.shape[1]][x.shape[2]-1], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }
        try
        {
            if ((x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) && (map[x.shape[1]][x.shape[2]+1] == 1))
            {
                findHighwayNeighboursRecursively((Tile)this.board[x.shape[1]][x.shape[2]+1], count+1, clone);
            }
        }
        catch (Exception e)
        {

        }

    }

    /**
     * Author : Jihirshu Narayan
     * @param completeMoves Set which will contain all the complete moves (All tiles were used)
     * @param possibleMoves Set which keeps list of all possible moves (Not mandatory that all tiles are used)
     * @param choices, array of tiles available for play
     * @param specialTiles, count of specialtiles on board
     * @param moveSoFar, the move constructed so far
     * @param row, current row number
     * @param col, current col number
     * @param specialFlag, specialFlag indicates if specialTile can be used or not
     * @param numberOfMoves, limit factor to save time
     *
     * Description : The function goes to every empty board location and checks if any of the available tiles or special tiles can be placed.
     *      All orientations are tried, and if any tile fits, that move is added to result array and the function calls itself with the updated choices.
     *       Number of moves is limited to make the function time efficient. This function captures moves only if all tiles are used. Meanwhile, it keeps adding,
     *       incomplete moves to possible moves. In the parent function it is checked if completeMoves is empty then possibleMoves is returned.
     */


    public void generateMovesRecursively(Set completeMoves, Set possibleMoves, String[] choices, int specialTiles, String moveSoFar, int row, int col, boolean specialFlag, int numberOfMoves)
    {

        if (choices.length == 0)
        {
            completeMoves.add(moveSoFar);
        }

        if ((completeMoves.size() > (numberOfMoves/2)) || (possibleMoves.size() > numberOfMoves))
            return;


        for (int i = 0;i<choices.length;i++)
        {
            Tile x = new Tile(choices[i]);
            x.set_default();
            x.translate(row,col);

            for (int j = 0;j<8;j++)
            {
                if (((choices[i].equals("A1")) || (choices[i].equals("A4"))) && (j>1))
                    continue;

                if ((!(choices[i].equals("B1"))) && (j > 3))
                    continue;

                x.rotate90(j);
                if (this.checkInvalidConnection(x))
                    continue;
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }

//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))

                    if (flag || x.checkExitConnection())

                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();

                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursively(completeMoves, possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursively(completeMoves, possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursively(completeMoves, possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursively(completeMoves, possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
            }

        }


        if ((specialTiles < 3) && (specialFlag))
        {

            for (String special : this.specialTiles)
            {
                Tile x = new Tile(special);
                x.set_default();
                x.translate(row,col);

                for (int j = 0;j<4;j++)
                {
                    if (((special.equals("S2")) || (special.equals("S3"))) && (j > 1))
                        continue;
                    if ((special.equals("S5")) && (j>1))
                        continue;

                    x.rotate90(j);
                    if (this.checkInvalidConnection(x))
                        continue;
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
    //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursively(completeMoves, possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
    //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))

                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursively(completeMoves, possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
    //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursively(completeMoves, possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
    //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursively(completeMoves, possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                }

            }
        }

    }

    /**
     * Author : Jihishu Narayan
     * @param choices
     * @param round
     * @return
     * Description : Parent function for GenerateMovesRecursively
     */

    public Set generateMove(String[] choices, int round)
    {
        Set possibleMoves = new mySet();
        Set completeMoves = new mySet();
        int specialtiles = this.countSpecialTiles();
        boolean specialFlag = true;
        if (specialtiles>=3)
            specialFlag=false;

        int numberOfMoves = 0;

        if (round == 1)
            numberOfMoves=1000;
        else if (round<4)
            numberOfMoves = 6000;
        else
            numberOfMoves = 10000;

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (board[i][j] == null)
                {
                    this.generateMovesRecursively(completeMoves, possibleMoves, choices, specialtiles, "", i, j, specialFlag, numberOfMoves);
                }
            }
        }

        if (completeMoves.isEmpty())
            return possibleMoves;
        else
            return completeMoves;
    }

    /**
     * Author : Jihishu Narayan
     * @param choices
     * @param round
     * @return
     * Description : Parent function for GenerateMovesRecursivelyNoRestriction
     */

    public Set generateMoveNoRestriction(String[] choices, int round)
    {
        Set possibleMoves = new mySet();
        int specialtiles = this.countSpecialTiles();

        int numberOfMoves;
        if (round == 1)
            numberOfMoves = 2000;
        else if (round < 4)
            numberOfMoves = 6000;
        else
            numberOfMoves = 10000;

        boolean specialFlag = true;
        if (specialtiles>=3)
            specialFlag=false;
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (board[i][j] == null)
                {
                    this.generateMovesRecursivelyNoRestriction(possibleMoves, choices, specialtiles, "", i, j, specialFlag, numberOfMoves);
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Author : Jihirshu Narayan
     * @param possibleMoves Set which keeps list of all possible moves (Not mandatory that all tiles are used)
     * @param choices, array of tiles available for play
     * @param specialTiles, count of specialtiles on board
     * @param moveSoFar, the move constructed so far
     * @param row, current row number
     * @param col, current col number
     * @param specialFlag, specialFlag indicates if specialTile can be used or not
     * @param numberOfMoves, limit factor to save time
     *
     * Description : The function goes to every empty board location and checks if any of the available tiles or special tiles can be placed.
     *      All orientations are tried, and if any tile fits, that move is added to result array and the function calls itself with the updated choices.
     *       Number of moves is limited to make the function time efficient. This function captures all moves irrespective of the number of choices left.
     */

    public void generateMovesRecursivelyNoRestriction(Set possibleMoves, String[] choices, int specialTiles, String moveSoFar, int row, int col, boolean specialFlag, int numberOfMoves)
    {

        // FIXME : Check why invalid moves are being generated

        if (possibleMoves.size() > numberOfMoves)
            return;

        if ((choices.length == 0) && (!specialFlag))
        {
            possibleMoves.add(moveSoFar);
            return;
        }

        for (int i = 0;i<choices.length;i++)
        {
            Tile x = new Tile(choices[i]);
            x.set_default();
            x.translate(row,col);

            for (int j = 0;j<8;j++)
            {
                if (((choices[i].equals("A1")) || (choices[i].equals("A4"))) && (j>1))
                    continue;

                if ((!(choices[i].equals("B1"))) && (j > 3))
                    continue;

                x.rotate90(j);
                if (this.checkInvalidConnection(x))
                    continue;
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }

//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))

                    if (flag || x.checkExitConnection())

                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();

                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursivelyNoRestriction(possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursivelyNoRestriction(possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursivelyNoRestriction(possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }
                        String newMove = moveSoFar + x.toString();
                        possibleMoves.add(newMove);

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    board.generateMovesRecursivelyNoRestriction(possibleMoves, new_choices, specialTiles, newMove, m,n, specialFlag, numberOfMoves);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
            }

        }


        if ((specialTiles < 3) && (specialFlag))
        {

            for (String special : this.specialTiles)
            {
                Tile x = new Tile(special);
                x.set_default();
                x.translate(row,col);

                for (int j = 0;j<4;j++)
                {
                    if (((special.equals("S2")) || (special.equals("S3"))) && (j > 1))
                        continue;
                    if ((special.equals("S5")) && (j>1))
                        continue;

                    x.rotate90(j);
                    if (this.checkInvalidConnection(x))
                        continue;
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursivelyNoRestriction(possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))

                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursivelyNoRestriction(possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursivelyNoRestriction(possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();
                            String newMove = moveSoFar + x.toString();
                            if (newMove.equals("A4A20B0A62S5D20S0C63"))
                                System.out.println("good");
                            possibleMoves.add(newMove);

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        board.generateMovesRecursivelyNoRestriction(possibleMoves, choices, newSpecialTiles, newMove, m,n, false, numberOfMoves);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                }

            }
        }

    }

    /**
     * Author : Jihirshu Narayan
     * @param diceRoll
     * @return
     * Description : Parent function for checkAllPiecesUsed
     */

    public boolean checkIfMovePossible(String diceRoll)
    {
        boolean returnFlag = false;
        String[] choices = {"","","",""};
        int pieceCounter=0;
        for (int i=0;i<diceRoll.length();i=i+2)
        {
            choices[pieceCounter] = diceRoll.substring(i,i+2);
            pieceCounter++;

        }
        boolean specialFlag=true;
        int specialtiles = this.countSpecialTiles();
        outer:
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (board[i][j] == null)
                {
                    if (this.checkAllPiecesUsed(choices, specialtiles, i, j, specialFlag))
                    {
                        returnFlag = true;
                        break outer;
                    }
                }
            }
        }

        return returnFlag;

    }

    /**
     * Author : Jihirshu Narayan
     * @param choices
     * @param specialTiles
     * @param row
     * @param col
     * @param specialFlag
     * @return
     * Description : Very similar to GenerateMovesRecursively but this one does not capture moves. It just checks if there is a valid move possible
     * on the current board with the available choices.
     */

    public boolean checkAllPiecesUsed( String[] choices, int specialTiles, int row, int col, boolean specialFlag)
    {
        boolean returnFlag = false;

        if (choices.length == 0)
            return true;

        for (int i = 0;i<choices.length;i++)
        {
            Tile x = new Tile(choices[i]);
            x.set_default();
            x.translate(row,col);

            for (int j = 0;j<8;j++)
            {
                if (((choices[i].equals("A1")) || (choices[i].equals("A4"))) && (j>1))
                    continue;

                if ((!(choices[i].equals("B1"))) && (j > 3))
                    continue;

                x.rotate90(j);
                if (this.checkInvalidConnection(x))
                    continue;
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }

//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))

                    if (flag || x.checkExitConnection())

                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }


                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    returnFlag = returnFlag || board.checkAllPiecesUsed(new_choices, specialTiles, m,n, specialFlag);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                   returnFlag = returnFlag || board.checkAllPiecesUsed(new_choices, specialTiles, m,n, specialFlag);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }


                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    returnFlag = returnFlag || board.checkAllPiecesUsed(new_choices, specialTiles, m,n, specialFlag);
                                }
                            }
                        }

                    }
                }
                catch (Exception E)
                {

                }
                try
                {
                    boolean flag;
                    try
                    {
                        flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                    }
                    catch (Exception E)
                    {
                        flag=false;
                    }
//                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                    if (flag || x.checkExitConnection())
                    {
                        Board board =  new Board(this);
                        board.place_tile(x);
                        String[] new_choices = new String[choices.length-1];
                        int counter=0;
                        for (int k = 0;k<choices.length;k++)
                        {
                            if (i == k)
                                continue;
                            new_choices[counter] = choices[k];
                            counter++;
                        }

                        for (int m = 0;m<7;m++)
                        {
                            for (int n=0;n<7;n++)
                            {
                                if ((m==row) && (n==col))
                                    continue;
                                if (board.board[m][n] == null)
                                {
                                    returnFlag = returnFlag || board.checkAllPiecesUsed(new_choices, specialTiles, m,n, specialFlag);
                                }
                            }
                        }

                    }

                }
                catch (Exception E)
                {

                }
            }

        }


        if ((specialTiles < 3) && (specialFlag))
        {

            for (String special : this.specialTiles)
            {
                Tile x = new Tile(special);
                x.set_default();
                x.translate(row,col);

                for (int j = 0;j<4;j++)
                {
                    if (((special.equals("S2")) || (special.equals("S3"))) && (j > 1))
                        continue;
                    if ((special.equals("S5")) && (j>1))
                        continue;

                    x.rotate90(j);
                    if (this.checkInvalidConnection(x))
                        continue;
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]-1])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        returnFlag = returnFlag || board.checkAllPiecesUsed(choices, newSpecialTiles, m,n, false);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]][x.shape[2]+1])) || (x.checkExitConnection()))

                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        returnFlag = returnFlag || board.checkAllPiecesUsed( choices, newSpecialTiles, m,n, false);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]-1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        returnFlag = returnFlag || board.checkAllPiecesUsed( choices, newSpecialTiles, m,n, false);
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception E)
                    {

                    }
                    try
                    {
                        boolean flag;
                        try
                        {
                            flag = ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])));
                        }
                        catch (Exception E)
                        {
                            flag=false;
                        }
                        //                    if ((x.checkRailwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkHighwayConnection((Tile)this.board[x.shape[1]+1][x.shape[2]])) || (x.checkExitConnection()))
                        if (flag || x.checkExitConnection())
                        {
                            Board board =  new Board(this);
                            board.place_tile(x);
                            int newSpecialTiles = board.countSpecialTiles();

                            for (int m = 0;m<7;m++)
                            {
                                for (int n=0;n<7;n++)
                                {
                                    if ((m==row) && (n==col))
                                        continue;
                                    if (board.board[m][n] == null)
                                    {
                                        returnFlag = returnFlag || board.checkAllPiecesUsed( choices, newSpecialTiles, m,n, false);
                                    }
                                }
                            }

                        }

                    }
                    catch (Exception E)
                    {

                    }
                }

            }
        }

        return returnFlag;

    }


}
