package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.*;

public class TileToStringTest {

    @Test
    public void testToString() {
        Tile x = new Tile("S0", new int[]{1,0,3,5,11,4,1});
        assertTrue("Tile Configuration does not match", x.toString().equals("S0A35"));
        Tile y = new Tile("S1", new int[]{2,0,4,5,2,13,1});
        assertTrue("Tile Configuration does not match", y.toString().equals("S1A45"));
        Tile z = new Tile("A2", new int[]{9,3,3,0,0,11,1});
        assertTrue("Tile Configuration does not match", z.toString().equals("A2D30"));

    }
}