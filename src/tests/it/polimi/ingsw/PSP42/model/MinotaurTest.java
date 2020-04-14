package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinotaurTest {
    private Player p1;
    private Player p2;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("Fra",1,21);
        p2 = new Player("Luca", 2,21);
    }

    @After
    public void tearDown() {
        p1 = null;
        p2 = null;
        g.reset();
    }

    @Test
    public void powerMove_pushOpponent_opponentWorkerIn22() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,1, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        assertEquals(1, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(2, p2.getWorker1().getCurrentX());
        assertEquals(2, p2.getWorker1().getCurrentY());
    }

    @Test
    public void powerMove_cannotPushOpponent_opponentWorkerInSamePosition() {
        p1.initialPosition(1,1, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(0,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        assertEquals(1, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(0, p2.getWorker1().getCurrentX());
        assertEquals(0, p2.getWorker1().getCurrentY());
    }
}