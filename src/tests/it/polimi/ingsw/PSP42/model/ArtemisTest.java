package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArtemisTest {
    private Player p1;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() throws Exception {
        p1 = new Player("BOB", 1);

    }

    @After
    public void tearDown() throws Exception {
        p1 = null;
        g.reset();
    }

    @Test
    public void setPowerOK() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(4, 4, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(4, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPowerKOPosOccupied() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(0, 0, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 0), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPowerKOSamePos() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(2, 2, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 0), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPowerOKOneStep() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(1, 1, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void powerMoveAvailable() {
    }

    @Test
    public void setPowerPosition() {
    }

    @Test
    public void adjacentCellMovePowerAvailable() {
    }
}