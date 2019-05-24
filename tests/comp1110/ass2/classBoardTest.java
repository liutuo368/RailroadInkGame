package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Written by Tuo Liu
 */
public class classBoardTest {

    @Test
    public void testPossibleMove() {
        String placement = "B0A10A0A30A1A21S3B10A1B01A4A50A1B21A5B50B0B41S5B30A1B61A1C10A3C30S0D00B1D13A4C00A2F02B2G01A1E00A5C40A5G13B1D34A1D21";
        String diceRolls = "A1A2B0B1";
        Board board = new Board(placement);
        assertTrue("There exists valid next move for placement " + placement + " and diceRolls " + diceRolls + " but got false", board.checkIfMovePossible(diceRolls));
    }

    @Test
    public void testImpossibleMove() {
        String placement = "A4A10A0A34B0A41A5A50A1B01B1B14A0B62B0C62A5D03A5D61B0E00A0F00B1F56A1F61A5G12B0G23A0G33A4G50";
        String diceRolls = "A1A2B0B1";
        Board board = new Board(placement);
        assertFalse("There is no valid next move for placement " + placement + " and diceRolls " + diceRolls + " but got true", board.checkIfMovePossible(diceRolls));
    }

    @Test
    public void testEmptyDiceRolls() {
        String placement = "B0A10A0A30A1A21S3B10A1B01A4A50A1B21A5B50B0B41S5B30A1B61A1C10A3C30S0D00B1D13A4C00A2F02B2G01A1E00A5C40A5G13B1D34A1D21";
        String diceRolls = "";
        Board board = new Board(placement);
        assertFalse("There is no valid next move for empty diceRolls but got true", board.checkIfMovePossible(diceRolls));
    }

    @Test
    public void testEmptyBoardString() {
        String placement = "";
        String diceRolls = "A2B0B0A1";
        Board board = new Board(placement);
        assertTrue("There exists valid next move for empty placement but got false", board.checkIfMovePossible(diceRolls));
    }

}
