package it.polimi.ingsw.PSP42.model;

import org.junit.*;
import static org.junit.Assert.*;

public class WorkerTest {

    private GameBoard board = GameBoard.getInstance();
    private Player p = new Player("CIPO", 1,"APOLLO");
    private Worker w = new Worker(-1,-1, p);

    @Test
    public void getCurrentX_afterConstructor_currentXMinus1() {
        assertEquals(-1, w.getCurrentX());
    }

    @Test
    public void getCurrentY_afterConstructor_currentYMinus1() {
        assertEquals(-1, w.getCurrentY());
    }

    @Test
    public void setPosition_move_00() {
        w.setPosition(0,0);
        assertEquals(0, w.getCurrentX());
        assertEquals(0, w.getCurrentY());
    }

    @Test
    public void getPlayer() {
        assertEquals(p, w.getPlayer());
    }

    @Test
    public void getAvailable_noAction_true() {
        assertTrue(w.getAvailable());
    }

    @Test
    public void setAvailable_setFalse_returnFalse() {
        w.setAvailable(false);
        assertFalse(w.getAvailable());
    }

    @Test
    public void buildBlock_in11_levelIn11Is1() {
        w.buildBlock(1,1);
        assertEquals(1, board.getCell(1,1).getLevel());
    }
}