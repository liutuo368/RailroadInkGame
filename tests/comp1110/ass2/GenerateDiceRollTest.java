package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GenerateDiceRollTest {

    private static int BASE_ITERATIONS = 100;

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testFourDice() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            int numRolls = roll.length() / 2;
            assertFalse("Expected a string of four die rolls, but you rolled: " + numRolls, numRolls != 4);
        }
    }

    @Test
    public void testCorrectDice() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            int a = 0;
            int b = 0;
            int x = 0;
            for (int j = 0; j < roll.length(); j += 2) {
                if (roll.charAt(j) == 'A') {
                    a++;
                } else if (roll.charAt(j) == 'B') {
                    b++;
                } else {
                    x++;
                }
            }
            assertFalse("Expected dice A to be rolled 3 times, but you rolled it " + a + " time/s " + roll, a != 3);
            assertFalse("Expected dice B to be rolled once, but you rolled it " + b + " time/s: " + roll, b != 1);
            assertFalse("Expected dice A and B but you rolled a different die" + x + " time/s " + roll, x != 0);
        }

    }

    private char[][] validFace = {{'0', '1', '2', '3', '4', '5'}, {'0', '1', '2'}};

    @Test
    public void testCorrectFace() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            for (int j = 0; j < roll.length(); j += 2) {
                char a = roll.charAt(j);
                char b = roll.charAt(j + 1);
                assertTrue("Invalid die: " + a, a - 'A' >= 0 && a - 'A' <= 1);
                assertTrue("Invalid face for die " + roll.charAt(j) + ": " + b, Arrays.binarySearch(validFace[a - 'A'], b) >= 0);
            }
        }
    }

    @Test
    public void testDieFaces() {
        int zero = 0;
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;

        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            for (int j = 1; j < roll.length(); j += 2) {
                int face = Character.getNumericValue(roll.charAt(j));
                switch (face) {
                    case 0:
                        zero++;
                    case 1:
                        one++;
                    case 2:
                        two++;
                    case 3:
                        three++;
                    case 4:
                        four++;
                    case 5:
                        five++;
                }
                assertFalse("Expected a number between 0 and 5, but you rolled: " + face, face < 0 || face > 5);
            }
        }
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '0'", zero > 0);
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '1'", one > 0);
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '2'", two > 0);
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '3'", three > 0);
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '4'", four > 0);
        assertTrue("Expected numbers 0 - 5 in 100 rolls, but you didn't roll '5'", five > 0);
    }

}
