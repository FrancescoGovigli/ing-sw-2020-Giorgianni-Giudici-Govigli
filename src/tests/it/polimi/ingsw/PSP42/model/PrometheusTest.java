package it.polimi.ingsw.PSP42.model;

import org.junit.*;
import static org.junit.Assert.*;

public class PrometheusTest {
    private Player p1;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("BOB", 1,"PROMETHEUS");
    }

    @After
    public void tearDown() {
        p1 = null;
        g.reset();
    }

    @Test
    public void powerPos_OK() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(4, 4, p1.getWorker2());
        // 1st round
        p1.build(3, 3,g.getCell(3, 3).getLevel()+1, p1.getWorker1());
        p1.move(2, 3, p1.getWorker1());
        p1.build(3, 3,g.getCell(3, 3).getLevel()+1, p1.getWorker1());
        p1.build(4, 3,g.getCell(4, 3).getLevel()+1, p1.getWorker2());
        p1.move(3, 4, p1.getWorker2());
        p1.build(4, 4,g.getCell(4, 4).getLevel()+1, p1.getWorker2());
        assertEquals(2, g.getCell(3, 3).getLevel());
        assertEquals(1, g.getCell(4, 3).getLevel());
        assertEquals(1, g.getCell(4, 4).getLevel());
        assertEquals(g.getCell(2, 3), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(g.getCell(3, 4), g.getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
    }

    @Test
    public void powerPos_KO() {
        p1.initialPosition(0, 0, p1.getWorker1());
        p1.initialPosition(1, 0, p1.getWorker2());
        p1.build(0, 1,g.getCell(0, 1).getLevel()+1, p1.getWorker1());
        p1.move(0, 1, p1.getWorker1());
        p1.build(0, 0,g.getCell(0, 0).getLevel()+1, p1.getWorker1());
        p1.build(2, 0,g.getCell(2, 0).getLevel()+1, p1.getWorker2());
        p1.move(1, 1, p1.getWorker2());
        p1.build(2, 0,g.getCell(2, 0).getLevel()+1, p1.getWorker2());
        assertEquals(2, g.getCell(2, 0).getLevel());
        assertEquals(1, g.getCell(0, 1).getLevel());
        assertEquals(0, g.getCell(0, 0).getLevel());
        assertEquals(g.getCell(0, 0), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(g.getCell(1, 1), g.getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
    }

    @Test
    public void normal_Play() {
        p1.initialPosition(0, 0, p1.getWorker1());
        p1.initialPosition(1, 0, p1.getWorker2());
        p1.move(0, 1, p1.getWorker1());
        p1.build(0, 0,g.getCell(0, 0).getLevel()+1, p1.getWorker1());
        p1.move(1, 1, p1.getWorker2());
        p1.build(2, 0,g.getCell(2, 0).getLevel()+1, p1.getWorker2());
        assertEquals(1, g.getCell(0, 0).getLevel());
        assertEquals(1, g.getCell(2, 0).getLevel());
        assertEquals(g.getCell(0, 1), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(g.getCell(1, 1), g.getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
    }

    @Test
    public void undoMove_And_undoBuild(){
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(4, 4, p1.getWorker2());
        p1.build(2, 1, 1, p1.getWorker1());
        p1.move(1, 1, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        p1.move(1, 2, p1.getWorker1());
        p1.build(1, 1, 1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        p1.build(2, 1, 2, p1.getWorker1());
        assertNull(g.getCell(2, 2).getWorker());
        assertEquals(g.getCell(1, 2), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(0, g.getCell(1, 1).getLevel());
        assertEquals(2, g.getCell(2, 1).getLevel());
    }
}