package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author : Jihirshu Narayan
 */

public class RouteTest
{
    @Test
    public void checkiftwoRoutesConnected()
    {
        String routeOneString = "A4A10A3B10A4C10";
        String routeTwoString = "B1A33A5A22A5B20";
        Tile[] tileArray1 = new Tile[routeOneString.length()/5];
        Tile[] tileArray2 = new Tile[routeTwoString.length()/5];
        int j = 0;
        for (int i = 0 ; i<routeOneString.length();i=i+5,j++)
        {
            String temp = routeOneString.substring(i,i+5);
            Tile x = new Tile(temp.substring(0,2));
            x.set_default();
            x.translate(temp.substring(2,4));
            x.rotate90(temp.charAt(4));
            tileArray1[j] = x;
        }
        j=0;
        for (int i = 0 ; i<routeTwoString.length();i=i+5,j++)
        {
            String temp = routeTwoString.substring(i,i+5);
            Tile x = new Tile(temp.substring(0,2));
            x.set_default();
            x.translate(temp.substring(2,4));
            x.rotate90(temp.charAt(4));
            tileArray2[j] = x;
        }

        Route firstRoute = new Route(tileArray1);
        Route secondRoute = new Route(tileArray2);

        assertTrue("Routes are connected but returned false", firstRoute.checkRoutesConnected(secondRoute));

    }

}