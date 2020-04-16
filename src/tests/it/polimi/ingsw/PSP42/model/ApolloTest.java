package it.polimi.ingsw.PSP42.model;

import org.junit.*;
import static org.junit.Assert.*;

public class ApolloTest {
    private Player p1;
    private Player p2;
    private GameBoard g=GameBoard.getInstance();
    @Before
    public void setUp() throws Exception {
        p1 = new Player("DG",1,21/*, "APOLLO"*/);
        p2 = new Player("CIPO",2,21/*, "APOLLO"*/);
    }

    @After
    public void tearDown() throws Exception {
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
}