package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DemeterTest {

    private Player p1;
    private GameBoard g=GameBoard.getInstance();

    @Before
    public void setUp() throws Exception {
        p1 = new Player("Fra",1);
    }

    @After
    public void tearDown() throws Exception {
        p1= null;
        g.reset();
    }

    @Test
    public void buildPower() throws OccupiedCellException, UnavailableWorkerException, NotYourWorkerException, InvalidMoveException, InvalidBuildException {
        p1.setInitialPosition(0,0, p1.getWorker1());
        p1.setInitialPosition(4,4, p1.getWorker2());
        p1.setPosWorker(1,1, p1.getWorker1());
        p1.build(2,2, p1.getWorker1());
        assertEquals(1, GameBoard.getInstance().getCell(2,2).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(0,0).getLevel());
    }
}