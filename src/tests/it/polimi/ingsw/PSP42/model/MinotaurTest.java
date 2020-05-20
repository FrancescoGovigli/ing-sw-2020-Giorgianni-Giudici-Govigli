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
        p1 = new Player("Fra",1,"MINOTAUR");
        p2 = new Player("Luca", 2,"ATLAS");
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
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(2, p2.getWorker1().getCurrentX());
        assertEquals(2, p2.getWorker1().getCurrentY());
        assertEquals(g.getCell(2,2).getWorker(), p2.getWorker1());
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
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(0, p2.getWorker1().getCurrentX());
        assertEquals(0, p2.getWorker1().getCurrentY());
        assertEquals(g.getCell(0,0).getWorker(), p2.getWorker1());
    }

    @Test
    public void multipleUndoMove_undoPushOpponent_AllOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,1, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 0, p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p2.getWorker1());
        g.getCell(2,1).setCellLevel();
        p1.move(1,1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        p1.move(2, 0, p1.getWorker1());
        assertEquals(g.getCell(2,0).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p2.getWorker1());
    }

    @Test
    public void multipleUndoMove_notUndoPushOpponent_AllOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,1, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(2,2).getWorker(), p2.getWorker1());
    }

    @Test
    public void undoBuild_notUndoPushOpponent_allOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,1, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(2,2).getWorker(), p2.getWorker1());
        p1.build(2,1,1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        assertEquals(0, g.getCell(2,2).getLevel());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(2,2).getWorker(), p2.getWorker1());
    }
}