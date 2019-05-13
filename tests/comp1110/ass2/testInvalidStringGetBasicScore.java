package comp1110.ass2;

import javafx.animation.Timeline;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class testInvalidStringGetBasicScore {

    private String[] inValidBoardStrings = {
            "A4B10A4C10B2D10",
            "A3B50B0C50A2D50",

            "A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35A4A41S5B34A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27S3B20A4C10A1D50A0F23B2G25A3E30A4E41",
            "A2A30A0A43A3A50B2B50A4C50A3B20A2B43B0G12B0A14A2B33A0B11A4E50A3D61A2B21A3G52B1G45A3F52B2F41A3F33A1E40A1D40A3E32A3E27B0F10S0E12B1D17A4D01A1B61A0C43",

            "A3D61A3D53B0C52A0B52A2B63A4D41B0E60A0F61A3D31A3D23A2G30B0F34A3E32A1B01B2B10A1B21A0A63A4D01A1G41B0G12S2D10A4C10B2A10A2B33B0A30S4E11A4E21A3C21A3C31S5F11",
            "A4A50A1F61A0B61S5F50B1F46A1F01S1F12A2F23A1E20B2D21S0D01A1C20A0B22B1A61A4D11A4G10B1G44A2G30A3C01A3C12B0B31A1B01A4B50B0C50A2F32A0E32A0E40A4D31B1D47A1B11",

    };

    @Rule
    public Timeout globalTimeOut = Timeout.millis(2000);


   // test the invalid strings
    @Test
    public void testGetBasicScore() {
        for(int i = 0; i < inValidBoardStrings.length; i++)
        {
            int score = RailroadInk.getBasicScore(inValidBoardStrings[i]);
            assertTrue("Score for invalid string should be -9999", score == -9999);
        }
    }

    //test the empty strings
    @Test
    public void testEmptyStringGetBasicScore() {

            int score = RailroadInk.getBasicScore("");
            assertTrue("Score for empty string should be 0", score == 0);

    }

}