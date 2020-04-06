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
    public void setPower_OK() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(4, 4, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(4, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_Pos_Occupied() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(0, 0, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(2, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_Same_Pos() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(2, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(2, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_OK_One_Step() {
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(1, 1, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_2Level_Gap(){
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.build(1, 2, p1.getWorker1());
        p1.build(1, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(2, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.setPosWorker(3, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(3, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        p1.setPosWorker(1, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(3, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_OK_Step_Up_And_Down(){
        p1.setInitialPosition(2, 2, p1.getWorker1());
        p1.setInitialPosition(0, 0, p1.getWorker2());
        p1.setPosWorker(1, 1, p1.getWorker1());
        p1.build(1, 2, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.setPosWorker(0, 1, p1.getWorker2());
        p1.build(0, 2, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(0, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.setPosWorker(1, 2, p1.getWorker1());
        p1.build(0, 2, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.setPosWorker(1, 1, p1.getWorker2());
        p1.build(0, 2, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(3, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.setPosWorker(0, 4, p1.getWorker1());
        p1.build(1, 3, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 3).getLevel());
        p1.setPosWorker(0, 1, p1.getWorker2());
        p1.build(1, 2, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(0, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.setPosWorker(1, 2, p1.getWorker1());
        p1.build(2, 1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(2, 1).getLevel());
        p1.setPosWorker(2, 1, p1.getWorker2());
        p1.build(1, 1, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(2, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 1).getLevel());
        p1.setPosWorker(0, 2, p1.getWorker1());
        p1.build(0, 1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(0, 1).getLevel());
        p1.setPosWorker(3, 3, p1.getWorker2());
        p1.build(3, 2, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(3, 3), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(3, 2).getLevel());
        p1.setPosWorker(0, 3, p1.getWorker1());
        p1.build(1, 3, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 3), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(1, 3).getLevel());
        p1.setPosWorker(1, 3, p1.getWorker2());
        //  Final check
        assertEquals(GameBoard.getInstance().getCell(3, 3), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        assertEquals(GameBoard.getInstance().getCell(0, 3), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(1, GameBoard.getInstance().getCell(0, 1).getLevel());
        assertEquals(3, GameBoard.getInstance().getCell(0, 2).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(1, 1).getLevel());
        assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        assertEquals(2, GameBoard.getInstance().getCell(1, 3).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(2, 1).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(3, 2).getLevel());
    }

    /*
    @Test
    public void powerMoveAvailable() {
    }

    @Test
    public void setPowerPosition() {
    }

    @Test
    public void adjacentCellMovePowerAvailable() {
    }
    */
}