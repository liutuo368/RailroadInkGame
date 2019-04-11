package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;


public class Board
{
    private Object[][] board;

    public Board()
    {
        this.board = new Object[7][7];

    }

    public void place_tile(Tile x) {
        Integer[] left = {4, 6, 10, 7, 12, 13, 14, 15};
        Integer[] right = {2, 6, 8, 9, 11, 12, 14, 15};
        Integer[] down = {3, 5, 9, 10, 11, 12, 13, 15};
        Integer[] up = {1, 5, 7, 8, 11, 13, 14, 15};
        int[] shape = x.getShape();
        this.board[shape[1]][shape[2]] = x;
    }

    public void place_tile(int[] x)
    {
        String[] names = {" ","S0","S1","S2","S3","S4","S5","A0","A1","A2","A3","A4","A5","B0","B1","B2"};
        this.board[x[1]][x[2]] = new Tile(names[x[0]], x);
    }

//        if (shape[4]!=0)
//        {
//            if (Arrays.asList(left).contains(shape[4]))
//                this.roadDirection[shape[1]][shape[2]].left = "H";
//            if (Arrays.asList(right).contains(shape[4]))
//                this.roadDirection[shape[1]][shape[2]].right = "H";
//            if (Arrays.asList(up).contains(shape[4]))
//                this.roadDirection[shape[1]][shape[2]].up = "H";
//            if (Arrays.asList(down).contains(shape[4]))
//                this.roadDirection[shape[1]][shape[2]].down = "H";
//        }
//        if (shape[5]!=0)
//        {
//            if (Arrays.asList(left).contains(shape[5]))
//                this.roadDirection[shape[1]][shape[2]].left = "R";
//            if (Arrays.asList(right).contains(shape[5]))
//                this.roadDirection[shape[1]][shape[2]].right = "R";
//            if (Arrays.asList(up).contains(shape[5]))
//                this.roadDirection[shape[1]][shape[2]].up = "R";
//            if (Arrays.asList(down).contains(shape[5]))
//                this.roadDirection[shape[1]][shape[2]].down = "R";
//        }
//
//    }

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



}
