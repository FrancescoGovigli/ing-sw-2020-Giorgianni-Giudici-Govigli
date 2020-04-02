package it.polimi.ingsw2020.PSP042.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    GameBoard board = GameBoard.getInstance();
    Player p = new Player("Cipo", 1);
    Worker w = new Worker(-1,-1, p);

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
        assertEquals(true, w.getAvailable());
    }

    @Test
    public void setAvailable() {
        w.setAvailable(false);
        assertEquals(false, w.getAvailable());
    }

    @Test
    public void buildBlock() {
        w.buildBlock(1,1);
        assertEquals(1, board.getCell(1,1).getLevel());
    }
}