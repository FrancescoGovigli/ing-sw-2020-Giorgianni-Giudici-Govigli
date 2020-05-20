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
        p1 = new Player("BOB", 1,"ARTEMIS");
    }

    @After
    public void tearDown() throws Exception {
        p1 = null;
        g.reset();
    }

    @Test
    public void setPower_OK() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(0, 0, p1.getWorker2());
        p1.move(3, 3, p1.getWorker1());
        p1.move(4, 4, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(4, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_Pos_Occupied() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(0, 0, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.move(0, 0, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_Same_Pos() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(0, 0, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.move(2, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_OK_One_Step() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(0, 0, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_KO_2Level_Gap() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.build(1, 2,g.getCell(1,2).getLevel()+1, p1.getWorker1());
        p1.build(1, 2,g.getCell(1,2).getLevel()+1, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(2, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.move(3, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(3, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        p1.move(1, 2, p1.getWorker1());
        assertEquals(GameBoard.getInstance().getCell(3, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
    }

    @Test
    public void setPower_OK_Step_Up_And_Down() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(0, 0, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.build(1, 2,g.getCell(1,2).getLevel()+1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.move(0, 1, p1.getWorker2());
        p1.build(0, 2,g.getCell(0,2).getLevel()+1, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(0, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.move(1, 2, p1.getWorker1());
        p1.build(0, 2,g.getCell(0,2).getLevel()+1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.move(1, 1, p1.getWorker2());
        p1.build(0, 2,g.getCell(0,2).getLevel()+1, p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(1, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(3, GameBoard.getInstance().getCell(0, 2).getLevel());
        p1.move(0, 3, p1.getWorker1());
        p1.move(0, 4, p1.getWorker1());
        p1.build(1, 3,g.getCell(1,3).getLevel()+1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 3).getLevel());
        p1.move(0, 1, p1.getWorker2());
        p1.build(1, 2,g.getCell(1,2).getLevel()+1 ,p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(0, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        p1.move(1, 3, p1.getWorker1());
        p1.move(1, 2, p1.getWorker1());
        p1.build(2, 1,g.getCell(2,1).getLevel()+1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(1, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(2, 1).getLevel());
        p1.move(1, 1, p1.getWorker2());
        p1.move(2, 1, p1.getWorker2());
        p1.build(1, 1,g.getCell(1,1).getLevel()+1 ,p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(2, 1), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(1, 1).getLevel());
        p1.move(0, 2, p1.getWorker1());
        p1.build(0, 1,g.getCell(0,1).getLevel()+1, p1.getWorker1());
        assertEquals("WIN", p1.getPlayerState());
        //assertEquals(GameBoard.getInstance().getCell(0, 2), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(0, 1).getLevel());
        p1.move(3, 2, p1.getWorker2());
        p1.move(3, 3, p1.getWorker2());
        p1.build(3, 2,g.getCell(3,2).getLevel()+1,p1.getWorker2());
        //assertEquals(GameBoard.getInstance().getCell(3, 3), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        //assertEquals(1, GameBoard.getInstance().getCell(3, 2).getLevel());
        p1.move(0, 3, p1.getWorker1());
        p1.build(1, 3,g.getCell(1,3).getLevel()+1, p1.getWorker1());
        //assertEquals(GameBoard.getInstance().getCell(0, 3), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        //assertEquals(2, GameBoard.getInstance().getCell(1, 3).getLevel());
        p1.move(2, 3, p1.getWorker2());
        p1.move(1, 3, p1.getWorker2());
        p1.build(1, 3, g.getCell(1, 3).getLevel()+1, p1.getWorker2());
        p1.move(0, 4, p1.getWorker1());
        p1.build(1, 3, g.getCell(1, 3).getLevel()+1, p1.getWorker2());
        p1.move(2, 4, p1.getWorker2());
        p1.build(1, 3, g.getCell(1, 3).getLevel()+1, p1.getWorker2());
        //  Final check
        assertEquals(GameBoard.getInstance().getCell(2, 4), GameBoard.getInstance().getCell(p1.getWorker2().getCurrentX(), p1.getWorker2().getCurrentY()));
        assertEquals(GameBoard.getInstance().getCell(0, 4), GameBoard.getInstance().getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        assertEquals(1, GameBoard.getInstance().getCell(0, 1).getLevel());
        assertEquals(3, GameBoard.getInstance().getCell(0, 2).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(1, 1).getLevel());
        assertEquals(2, GameBoard.getInstance().getCell(1, 2).getLevel());
        assertEquals(4, GameBoard.getInstance().getCell(1, 3).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(2, 1).getLevel());
        assertEquals(1, GameBoard.getInstance().getCell(3, 2).getLevel());
    }

    @Test
    public void undoMove_And_undoBuild() {
        p1.initialPosition(2, 2, p1.getWorker1());
        p1.initialPosition(4, 4, p1.getWorker2());
        p1.move(1, 1, p1.getWorker1());
        p1.move(0, 0, p1.getWorker1());
        p1.doUndoMove(p1.getWorker1());
        assertNull(g.getCell(0, 0).getWorker());
        assertNull(g.getCell(2, 2).getWorker());
        assertEquals(g.getCell(1, 1), g.getCell(p1.getWorker1().getCurrentX(), p1.getWorker1().getCurrentY()));
        p1.build(0, 0, 1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        p1.build(0, 2, 1, p1.getWorker1());
        assertEquals(0, g.getCell(0, 0).getLevel());
        assertEquals(1, g.getCell(0, 2).getLevel());
    }
}