package it.polimi.ingsw.PSP42.model;

import org.junit.*;
import static org.junit.Assert.*;

public class ApolloTest {
    private Player p1;
    private Player p2;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("DG",1,"APOLLO");
        p2 = new Player("CIPO",2,"APOLLO");
    }

    @After
    public void tearDown() {
        p1= null;
        p2= null;
        g.reset();
    }

    @Test
    public void highLeftCorner_4WorkerSwitch_ApolloPower() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,0, p2.getWorker1());
        p2.initialPosition(1,1, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p2.move(0,1, p2.getWorker1());
        p1.move(0,0, p1.getWorker2());
        p2.move(1,1, p2.getWorker2());
        assertEquals(1, p1.getWorker1().getCurrentX());
        assertEquals(0, p1.getWorker1().getCurrentY());
        assertEquals(0, p2.getWorker1().getCurrentX());
        assertEquals(1, p2.getWorker1().getCurrentY());
        assertEquals(0, p1.getWorker2().getCurrentX());
        assertEquals(0, p1.getWorker2().getCurrentY());
        assertEquals(1, p2.getWorker2().getCurrentX());
        assertEquals(1, p2.getWorker2().getCurrentY());
    }

    @Test
    public void undoMove_And_undoSwap(){
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,0, p2.getWorker1());
        p2.initialPosition(1,1, p2.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker2());
        assertEquals(g.getCell(0, 0).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(1, 1).getWorker(), p2.getWorker2());
        p1.move(1, 2, p1.getWorker2());
        p2.move(1, 2, p2.getWorker2());
        p2.move(1, 1, p2.getWorker1());
        p2.doUndoMove(p2.getWorker1());
        assertEquals(g.getCell(1, 0).getWorker(), p2.getWorker1());
        assertEquals(g.getCell(1, 1).getWorker(), p1.getWorker2());
        assertEquals(g.getCell(1, 2).getWorker(), p2.getWorker2());
        p1.move(1, 1,p1.getWorker1());
        assertEquals(g.getCell(0, 0).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(1, 1).getWorker(), p1.getWorker2());
    }


    @Test
    public void undoMove_undoSwitch_allOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,0, p2.getWorker1());
        p2.initialPosition(1,1, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p2.getWorker2());
        p1.doUndoMove(p1.getWorker1());
        p1.move(1,0, p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p2.getWorker2());
    }

    @Test
    public void undoBuild_notUndoMove_allOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p1.move(1,0, p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p1.getWorker1());
        p1.move(1,0, p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        p1.build(2,0,1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        assertEquals(g.getCell(1,0).getWorker(), p1.getWorker1());
        assertEquals(0, g.getCell(2,0).getLevel());
        p1.build(1,1,1, p1.getWorker1());
        assertEquals(1, g.getCell(1,1).getLevel());
    }

    @Test
    public void undoBuild_notUndoSwap_allOK() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,1, p1.getWorker2());
        p2.initialPosition(1,0, p2.getWorker1());
        p2.initialPosition(1,1, p2.getWorker2());
        p1.move(1,1, p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p2.getWorker2());
        p1.build(2,2,1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        assertEquals(g.getCell(1,1).getWorker(), p1.getWorker1());
        assertEquals(g.getCell(0,0).getWorker(), p2.getWorker2());
        assertEquals(0, g.getCell(2,2).getLevel());
    }
}