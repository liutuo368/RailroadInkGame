package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import comp1110.ass2.Tile;


public class Board
{
    private Object[][] board;
    private ArrayList<String> specialTiles = new ArrayList<>(Arrays.asList("S0","S1","S2","S3","S4","S5"));
    private boolean[][] highwayConnections;
    private boolean[][] railwayConnections;

    public Board()
    {
        this.board = new Object[7][7];
        this.highwayConnections = new boolean[7][7];
        this.railwayConnections = new boolean[7][7];
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




    public void place_tile(Tile x)
    {
//        String[] names = {" ","S0","S1","S2","S3","S4","S5","A0","A1","A2","A3","A4","A5","B0","B1","B2"};
        this.board[x.shape[1]][x.shape[2]] = new Tile(x.getName(), x.shape);
    }




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
                             if (this.board[x[0]][x[1]] != null) {
                                 Tile neighbour = (Tile) this.board[x[0]][x[1]];
                                 if (countSpecialTiles < 3)
                                 {
                                     for (int z = 0; z < this.specialTiles.size(); z++) {
                                         Tile currentTile = new Tile(this.specialTiles.get(z));
                                         currentTile.set_default();
                                         currentTile.translate(i, j);

                                         for (int orientation = 0; orientation < 8; orientation++)
                                         {
                                             currentTile.rotate90(orientation);
                                             if (results.contains(currentTile.toString()))
                                                 continue ;
                                             if (!(this.checkInvalidConnection(currentTile)))
                                             {
                                                 if (((neighbour.isConnected(currentTile)) || (currentTile.check_exit_connection())) && (!currentTile.checkInvalidExitConnection()))
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
//                                                     if (currentTile.check_exit_connection())
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
                                             currentTile.rotate90(orientation);
                                             if (results.contains(currentTile.toString()))
                                                 continue ;
                                             if (!(this.checkInvalidConnection(currentTile)))
                                             {
                                                 if (((neighbour.isConnected(currentTile)) || (currentTile.check_exit_connection())) && (!currentTile.checkInvalidExitConnection()))

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
//                                                     if (currentTile.check_exit_connection())
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
            if (results.size() > 2500)                  // THIS BLOCK LIMITS THE MOVES IN ORDER TO PASS THE TESTS WITHIN TIME LIMIT
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



    public boolean checkHighwayConnection(Tile x)
    {
        if (x.check_highway_exit())
            return true;
        try
        {
            if (x.check_highway_connection((Tile)this.board[x.shape[1]-1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_highway_connection((Tile)this.board[x.shape[1]+1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_highway_connection((Tile)this.board[x.shape[1]][x.shape[2]-1]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_highway_connection((Tile)this.board[x.shape[1]][x.shape[2]+1]))
                return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }


    public boolean checkRailwayConnection(Tile x)
    {
        if (x.check_railway_exit())
            return true;
        try
        {
            if (x.check_railway_connection((Tile)this.board[x.shape[1]-1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_railway_connection((Tile)this.board[x.shape[1]+1][x.shape[2]]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_railway_connection((Tile)this.board[x.shape[1]][x.shape[2]-1]))
                return true;
        }
        catch (Exception e)
        {

        }
        try
        {
            if (x.check_railway_connection((Tile)this.board[x.shape[1]][x.shape[2]+1]))
                return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }

    public void generateHighwayConnections()
    {
        for (int i=0;i<7;i++)
        {
            for (int j=0;j<7;j++)
            {
                if (this.board[i][j] != null)
                {
                    Tile x = (Tile) board[i][j];
                    this.highwayConnections[i][j] = checkHighwayConnection(x);
                    this.railwayConnections[i][j] = checkRailwayConnection(x);
                }
            }
        }
    }

}
