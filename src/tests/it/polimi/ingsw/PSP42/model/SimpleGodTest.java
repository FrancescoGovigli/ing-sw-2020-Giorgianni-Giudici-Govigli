package it.polimi.ingsw.PSP42.model;

import org.junit.*;

import static org.junit.Assert.*;
// TEST CON CARTA FISSA
public class SimpleGodTest {
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
    public void highLeftCorner_4WorkerSet_ApolloPower() {
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(0,1,p1.getWorker2());
        p2.setInitialPosition(1,0,p2.getWorker1());
        p2.setInitialPosition(1,1,p2.getWorker2());
        p1.setPosWorker(1,1,p1.getWorker1());
        assertEquals(1,p1.getWorker1().getCurrentX());
        assertEquals(1,p1.getWorker1().getCurrentY());
    }
}