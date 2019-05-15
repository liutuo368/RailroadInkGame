package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.*;

public class classTileTest
{
    @Test
    public void testCheckHighwayConnections()
    {
        String[][] connectedTilePairs = {{"S2C32","A3B32"},{"A5D32", "B1D43"},{"A4E33","S5E43"}};

        for (String[] pair : connectedTilePairs)
        {
            Tile x = new Tile(pair[0].substring(0,2));
            Tile y = new Tile(pair[1].substring(0,2));
            x.set_default();
            y.set_default();
            x.translate(pair[0].substring(2,4));
            y.translate(pair[1].substring(2,4));
            x.rotate90(pair[0].charAt(4));
            y.rotate90(pair[1].charAt(4));
            assertTrue("Tiles are connected via highway but test returned false " + pair[0] + " " + pair[1], x.check_highway_connection(y));

        }

        String[][] disconnectedTilePairs = {{"S2C31","A3B33"},{"A5D32", "B1D44"},{"A4E34","S5E43"}};

        for (String[] pair : disconnectedTilePairs)
        {
            Tile x = new Tile(pair[0].substring(0,2));
            Tile y = new Tile(pair[1].substring(0,2));
            x.set_default();
            y.set_default();
            x.translate(pair[0].substring(2,4));
            y.translate(pair[1].substring(2,4));
            x.rotate90(pair[0].charAt(4));
            y.rotate90(pair[1].charAt(4));
            assertFalse("Tiles are not connected via highway but test returned true " + pair[0] + " " + pair[1], x.check_highway_connection(y));
        }


    }

    @Test
    public void testCheckRailwayConnections()
    {
        String[][] connectedTilePairs = {{"S1C31","S0B34"},{"A0D31", "S4D42"},{"B2E32","B1E44"}};

        for (String[] pair : connectedTilePairs)
        {
            Tile x = new Tile(pair[0].substring(0,2));
            Tile y = new Tile(pair[1].substring(0,2));
            x.set_default();
            y.set_default();
            x.translate(pair[0].substring(2,4));
            y.translate(pair[1].substring(2,4));
            x.rotate90(pair[0].charAt(4));
            y.rotate90(pair[1].charAt(4));
            assertTrue("Tiles are connected via railway but test returned false " + pair[0] + " " + pair[1], x.check_railway_connection(y));

        }

        String[][] disconnectedTilePairs = {{"S2C32","A3B32"},{"A5D32", "B1D43"},{"A4E33","S5E43"}};
        for (String[] pair : disconnectedTilePairs)
        {
            Tile x = new Tile(pair[0].substring(0,2));
            Tile y = new Tile(pair[1].substring(0,2));
            x.set_default();
            y.set_default();
            x.translate(pair[0].substring(2,4));
            y.translate(pair[1].substring(2,4));
            x.rotate90(pair[0].charAt(4));
            y.rotate90(pair[1].charAt(4));
            assertFalse("Tiles are not connected via railway but test returned true " + pair[0] + " " + pair[1], x.check_railway_connection(y));
        }


    }


}