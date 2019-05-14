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
    private int[] highwayRoute;
    private int[] railwayRoute;
    public int longesthighway = 0;
    public int longestrailway = 0;

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

    public boolean connected_to_route(Tile x)
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
            if (x.check_exit_connection())
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

//        Route mergedRoute = new Route(temp);
        this.route = temp;

//        return mergedRoute;
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

    public void highWayRoutes()
    {
        this.highwayRoute = new int[this.route.length];

        for (int i = 0;i < this.route.length;i++)
        {
            for (int j = 0; j<this.route.length;j++)
            {
                if (this.highwayRoute[i] == 1)
                    continue;
                if (i!=j)
                {
                    if (this.route[i].check_highway_connection(this.route[j]))
                        this.highwayRoute[i] = 1;
                }
            }
        }

    }

    public void railwayRoutes()
    {
        this.railwayRoute = new int[this.route.length];

        for (int i = 0;i < this.route.length;i++)
        {
            for (int j = 0; j<this.route.length;j++)
            {
                if (this.railwayRoute[i] == 1)
                    continue;
                if (i!=j)
                {
                    if (this.route[i].check_railway_connection(this.route[j]))
                        this.railwayRoute[i] = 1;
                }
            }
        }

    }

    public void countLongestHighway()
    {

        int[] clone = new int[this.highwayRoute.length];

        while (!(Arrays.equals(clone, this.highwayRoute)))
        {
            for (int k = 0; k<this.highwayRoute.length;k++)
                clone[k] = this.highwayRoute[k];

            int counter = 1;
            int currentTileCounter = 0;
            for (int i = 1; i < this.route.length; i++) {
                if (this.highwayRoute[i] == 1)
                {
                    if ((this.route[currentTileCounter].check_highway_connection(this.route[i])))
                    {
                        counter++;
                    }
                    else
                    {
                        this.highwayRoute[currentTileCounter] = 0;
                        counter = 1;
                    }
                    currentTileCounter = i;
                }
            }

            if (counter > longesthighway)
                longesthighway = counter;
        }
    }

    public void countLongestRailway()
    {
        int[] clone = new int[this.railwayRoute.length];

        while (!(Arrays.equals(clone, this.railwayRoute)))
        {
            for (int k = 0;k<this.railwayRoute.length;k++)
                clone[k] = this.railwayRoute[k];
            int counter = 1;
            int currentTileCounter = 0;
            for (int i = 1; i < this.route.length; i++) {
                if (this.railwayRoute[i] == 1) {
                    if ((this.route[currentTileCounter].check_railway_connection(this.route[i])))
                    {
                        counter++;
                    }
                    else
                    {
                        this.railwayRoute[currentTileCounter] = 0;
                        counter = 1;
                    }
                    currentTileCounter = i;
                }
            }

            if (counter > longestrailway)
                longestrailway = counter;
        }
    }



}
