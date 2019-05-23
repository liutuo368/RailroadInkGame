/**
 * Author : Jihirshu Narayan
 * Created : 11/04/2019
 * Last Modified : 10/05/2019
 * Description : Class route stores an array of tiles that are connected to each other. This facilitates establishing routes from a boardString. This
 *               class also has a function which checks if there is any interaction between two routes and merges them if there is a connection.
 */

package comp1110.ass2;

import java.util.Arrays;

public class Route
{
    private Tile[] route;
//    private int[] highwayRoute;
//    private int[] railwayRoute;
//    public int longesthighway = 0;
//    public int longestrailway = 0;

    public Route(Tile x)
    {
        Tile[] temp = new Tile[1];
        temp[0] = x;
        this.route = temp;
    }

    public Route(Tile[] x)
    {
        this.route = x;
    }

    public void addRoute(Tile x)
    {
        Tile[] temp = new Tile[this.route.length+1];
        for (int i=0;i<temp.length;i++)
        {
            if (i < temp.length-1)
                temp[i] = this.route[i];
            else
                temp[i] = x;
        }

        this.route = temp;
    }

    public boolean connectedToRoute(Tile x)
    {
        for (int i=0;i<this.route.length;i++)
        for (Tile y : this.route)
        {
            if (y.isConnected(x))
            {
                addRoute(x);
                return true;
            }
        }

        return false;
    }

    public int numberOfExitsConnected()
    {
        int count = 0;
        for (Tile x : this.route)
        {
            if (x.checkExitConnection())
                count++;
        }

        return count;
    }

    public boolean checkRoutesConnected(Route x)
    {

        for (int i = this.route.length-1;i >=0;i--)
        {
            for (int j = x.route.length-1;j>=0;j--)
            {
                if (this.route[i].isConnected(x.route[j]))
                {
                    return true;
                }
            }
        }


        return false;
    }

    public void mergeRoutes(Route x)
    {
        Tile[] temp = new Tile[this.route.length + x.route.length];
        int temp_counter = 0;
        for (int i = 0;i<this.route.length;i++)
        {
            temp[temp_counter] = this.route[i];
            temp_counter++;
        }

        for (int i=0;i<x.route.length;i++)
        {
            temp[temp_counter] = x.route[i];
            temp_counter++;
        }


        this.route = temp;

    }

    @Override
    public String toString()
    {
        String output = "";
        for (int i=0;i<this.route.length;i++)
        {
            output = output + this.route[i].toString();
        }

        return output;
    }



}
