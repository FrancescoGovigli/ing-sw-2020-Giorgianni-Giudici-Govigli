package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtlasTest {
    private Player p1;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("Dan",1,21);
    }

    @After
    public void tearDown() {
        p1 = null;
        g.reset();
    }

    @Test
    public void buildPower_buildDome_level4InCell22() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(4,4, p1.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.build(2,2,4, p1.getWorker1());
        assertEquals(4, GameBoard.getInstance().getCell(2,2).getLevel());
    }

    @Test
    public void buildPower_buildNextLevel_level1InCell22() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(4,4, p1.getWorker2());
        p1.move(1,1, p1.getWorker1());
        p1.build(2,2,g.getCell(2,2).getLevel()+1, p1.getWorker1());
        assertEquals(1, GameBoard.getInstance().getCell(2,2).getLevel());
    }
}