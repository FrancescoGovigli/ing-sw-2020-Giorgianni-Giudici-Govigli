package it.polimi.ingsw.PSP42.model;

import org.junit.*;

import static org.junit.Assert.*;

public class WorkerTest {

    private GameBoard board = GameBoard.getInstance();
    private Player p = new Player("Cipo", 1);
    private Worker w = new Worker(-1,-1, p);

    @Test
    public void getCurrentX() {
        assertEquals(-1, w.getCurrentX());
    }

    @Test
    public void getCurrentY() {
        assertEquals(-1, w.getCurrentY());
    }

    @Test
    public void setPosition() {
        w.setPosition(0,0);
        assertEquals(0, w.getCurrentX());
        assertEquals(0, w.getCurrentY());
    }

    @Test
    public void getPlayer() {
        assertEquals(p, w.getPlayer());
    }

    @Test
    public void getAvailable() {
        assertTrue(w.getAvailable());
    }

    @Test
    public void setAvailable() {
        w.setAvailable(false);
        assertFalse(w.getAvailable());
    }

    @Test
    public void buildBlock() {
        w.buildBlock(1,1);
        assertEquals(1, board.getCell(1,1).getLevel());
    }
}