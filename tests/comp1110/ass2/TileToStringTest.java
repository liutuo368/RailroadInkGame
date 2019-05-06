package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.*;

public class TileToStringTest {

    @Test
    public void testToString() {
        Tile x = new Tile("S0", new int[]{1,0,3,5,11,4,1});
        assertTrue("Tile Configuration does not match", x.toString().equals("S0A35"));

    }
}