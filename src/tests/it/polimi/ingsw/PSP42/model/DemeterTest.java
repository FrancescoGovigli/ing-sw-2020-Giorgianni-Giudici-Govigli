package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DemeterTest {

    private Player p1;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("Fra",1,21,"DEMETER");
    }

    @After
    public void tearDown() {
        p1 = null;
        g.reset();
    }

    @Test
    public void buildPower_buildIn2Cell_level1In22And00() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(4,4, p1.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.build(2,2,g.getCell(2,2).getLevel()+1, p1.getWorker1());
        p1.build(0,0,g.getCell(0,0).getLevel()+1, p1.getWorker1());
        assertEquals(1, GameBoard.getInstance().getCell(2,2).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(0,0).getLevel());
    }

    @Test
    public void buildPower_buildTwiceInSameCell_level1In22() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(4,4, p1.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.build(2,2,g.getCell(2,2).getLevel()+1, p1.getWorker1());
        p1.build(0,0,g.getCell(2,2).getLevel()+1, p1.getWorker1());
        assertEquals(1, GameBoard.getInstance().getCell(2,2).getLevel());
    }

    @Test
    public void buildPower_ManyInteractionsToVerifyCorrectUseOfPower_AllOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(4,4, p1.getWorker2());
        p1.move(1,0, p1.getWorker1());
        p1.build(2,0,g.getCell(2,0).getLevel()+1, p1.getWorker1());
        p1.build(2,0,g.getCell(2,0).getLevel()+1, p1.getWorker1());
        assertEquals(1, g.getCell(2,0).getLevel());
        p1.move(1,1, p1.getWorker1());
        p1.build(2,0,g.getCell(2,0).getLevel()+1, p1.getWorker1());
        p1.build(2,0,g.getCell(2,0).getLevel()+1, p1.getWorker1());
        assertEquals(2, g.getCell(2,0).getLevel());
        p1.build(2,1,g.getCell(2,2).getLevel()+1, p1.getWorker1());
        assertEquals(1, g.getCell(2,1).getLevel());
    }

    @Test
    public void undoMoveAndUndoBuild_MoveAnd2Build_AllOK(){
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(4, 4, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertNull(g.getCell(1, 1).getWorker());
        assertEquals(g.getCell(2, 2), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        p1.build(1, 1, 1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        p1.build(3, 3, 1, p1.getWorker1());
        p1.build(3,2,1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        p1.build(2,3,1, p1.getWorker1());
        assertEquals(0, g.getCell(1, 1).getLevel());
        assertEquals(1, g.getCell(3, 3).getLevel());
        assertEquals(0, g.getCell(3, 2).getLevel());
        assertEquals(1, g.getCell(2, 3).getLevel());
    }
}