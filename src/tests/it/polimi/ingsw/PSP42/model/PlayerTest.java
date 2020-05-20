package it.polimi.ingsw.PSP42.model;

import org.junit.*;

import static org.junit.Assert.*;

//STATE ATTENTI NON FUNZIONA SE METTETE UNA CARTA != NOGOD

public class PlayerTest<excepted> {

    private Player p1;
    private Player p2;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() throws Exception {
        p1 = new Player("DG",1,"NOGOD");
        p2 = new Player("CIPO",2,"NOGOD");
    }

    @After
    public void tearDown() throws Exception {
        p1= null;
        p2= null;
        g.reset();
    }

    @Test
    public void checkNickname(){
        assertEquals("DG", p1.getNickname());
    }

    @Test
    public void setPosWorker_positionAvailable_expectedNewPos() {
        p1.initialPosition(0,0,p1.getWorker1());
        p1.initialPosition(1,1,p1.getWorker2());
        p1.move(0,1,p1.getWorker1());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
    }

    @Test
    public void highLeftCorner_4Worker_1Blocked() {
        p1.initialPosition(0,0,p1.getWorker1());
        p1.initialPosition(0,1,p1.getWorker2());
        p2.initialPosition(1,0,p2.getWorker1());
        p2.initialPosition(1,1,p2.getWorker2());
        p1.move(1,1,p1.getWorker1());
        assertEquals(0,p1.getWorker1().getCurrentX());
        assertEquals(0,p1.getWorker1().getCurrentY());
    }

    @Test
    public void build_positionAvailable_expectedLevel1() {
        p1.initialPosition(0,0,p1.getWorker1());
        p1.initialPosition(1,1,p1.getWorker2());
        p1.move(0,1,p1.getWorker1());
        p1.build(1,2,g.getCell(1,2).getLevel()+1,p1.getWorker1());
        assertEquals(1, GameBoard.getInstance().getCell(1,2).getLevel());
    }

    @Test //(expected = OccupiedCellException.class)
    public void setInitialPosition_2Worker_expectedOK() {
        assertEquals(-1, p1.getWorker1().getCurrentX());
        assertEquals(-1, p1.getWorker1().getCurrentY());
        assertEquals(-1, p1.getWorker2().getCurrentX());
        assertEquals(-1, p1.getWorker2().getCurrentY());
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(1,1, p1.getWorker2());
    }
}