package it.polimi.ingsw.PSP42.model;

import org.junit.*;

import static org.junit.Assert.*;

public class PlayerTest<excepted> {

    private Player p1;
    private Player p2;
    private GameBoard g=GameBoard.getInstance();

    @Before
    public void setUp() throws Exception {
        p1 = new Player("DG",1);
        p2 = new Player("CIPOOO",2);
    }

    @After
    public void tearDown() throws Exception {
        p1= null;
        p2= null;
        g.reset();
    }

    @Test
    public void setPosWorker() throws InvalidMoveException,UnavailableWorkerException,NotYourWorkerException,OccupiedCellException {
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(1,1,p1.getWorker2());
        p1.setPosWorker(0,1,p1.getWorker1());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
    }

    @Test //(expected = OccupiedCellException.class)
    public void WorkerBlockedHighLeftCorner() throws OccupiedCellException, UnavailableWorkerException, NotYourWorkerException, InvalidMoveException {
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(0,1,p1.getWorker2());
        p2.setInitialPosition(1,0,p2.getWorker1());
        p2.setInitialPosition(1,1,p2.getWorker2());
        p1.setPosWorker(1,1,p1.getWorker1());
        assertEquals(0,p1.getWorker1().getCurrentX());
        assertEquals(0,p1.getWorker1().getCurrentY());

    }
    @Test //(expected = OccupiedCellException.class)
    public void build() throws InvalidBuildException,InvalidMoveException,UnavailableWorkerException,NotYourWorkerException,OccupiedCellException {
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(1,1,p1.getWorker2());
        p1.setPosWorker(0,1,p1.getWorker1());
        p1.build(1,2,p1.getWorker1());
        assertEquals(1,GameBoard.getInstance().getCell(1,2).getLevel());
    }

    @Test //(expected = OccupiedCellException.class)
    public void setInitialPosition() throws OccupiedCellException {
        assertEquals(-1,p1.getWorker1().getCurrentX());
        assertEquals(-1,p1.getWorker1().getCurrentY());
        assertEquals(-1,p1.getWorker2().getCurrentX());
        assertEquals(-1,p1.getWorker2().getCurrentY());
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(1,1,p1.getWorker2());
    }
}