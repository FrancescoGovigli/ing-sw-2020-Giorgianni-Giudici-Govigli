package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest {

    private GameBoard board = GameBoard.getInstance();
    private Player p = new Player("BOB", 1,"APOLLO");
    private Worker w = new Worker(-1,-1, p);

    @Before
    public void setUp() throws Exception {
        p = new Player("BOB", 1,"APOLLO");

    }

    @After
    public void tearDown() throws Exception {
        p = null;
        board.reset();
    }

    @Test
    public void getInstance() {
        assertEquals(GameBoard.getInstance(), board);
    }

    @Test
    public void getCell() {
        assertEquals(board.getCell(0, 0), GameBoard.getInstance().getCell(0, 0) );
    }

    @Test
    public void moveAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.moveAvailable(0, 1, w));
    }

    @Test
    public void buildAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.buildAvailable(0, 1, w));
    }

    @Test
    public void workerAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.workerAvailable(w));
    }

    @Test
    public void demo_Step_Up_And_Down(){
        p.initialPosition(2, 2, p.getWorker1());
        p.build(2, 1,board.getCell(2,1).getLevel()+1, p.getWorker1());
        p.build(1, 1,board.getCell(1,1).getLevel()+1, p.getWorker1());
        p.build(1, 1,board.getCell(1,1).getLevel()+1, p.getWorker1());
        p.build(1, 2,board.getCell(1,2).getLevel()+1, p.getWorker1());
        p.build(1, 2,board.getCell(1,2).getLevel()+1, p.getWorker1());
        p.build(1, 2,board.getCell(1,2).getLevel()+1,p.getWorker1());
        p.move(2, 1, p.getWorker1());
        p.move(1, 1, p.getWorker1());
        p.move(1, 2, p.getWorker1());
        p.move(0, 2, p.getWorker1());
        assertEquals(null, board.getCell(2, 2).getWorker());
        assertEquals(1, board.getCell(2, 1).getLevel());
        assertEquals(2, board.getCell(1, 1).getLevel());
        assertEquals(3, board.getCell(1, 2).getLevel());
        assertEquals(board.getCell(0, 2), board.getCell(p.getWorker1().getCurrentX(), p.getWorker1().getCurrentY()));
    }

    @Test
    public void demo_Worker_unAvailable(){
        p.initialPosition(0, 0, p.getWorker1());
        p.build(0, 1,board.getCell(0,1).getLevel()+1, p.getWorker1());
        p.build(0, 1,board.getCell(0,1).getLevel()+1, p.getWorker1());
        p.build(1, 1,board.getCell(1,1).getLevel()+1, p.getWorker1());
        p.build(1, 1,board.getCell(1,1).getLevel()+1 ,p.getWorker1());
        p.build(1, 0,board.getCell(1,0).getLevel()+1, p.getWorker1());
        p.build(1, 0,board.getCell(1,0).getLevel()+1, p.getWorker1());
        assertEquals(2, board.getCell(0, 1).getLevel());
        assertEquals(2, board.getCell(1, 1).getLevel());
        assertEquals(2, board.getCell(1, 0).getLevel());
        assertEquals(false, board.workerAvailable(p.getWorker1()));
    }

    @Test
    public void loseConditionSTART_unSetWorker_outOfMap() {
        p.initialPosition(0,0, p.getWorker1());
        p.initialPosition(1,1, p.getWorker2());
        p.getWorker1().setAvailable(false);
        p.getWorker2().setAvailable(false);
        board.loseCondition(p,"START");
        assertEquals(-1, p.getWorker1().getCurrentX());
        assertEquals(-1, p.getWorker1().getCurrentY());
    }
}