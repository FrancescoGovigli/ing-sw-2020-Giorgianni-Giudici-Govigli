package it.polimi.ingsw.PSP42.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AthenaTest {
    private Player p1;
    private Player p2;
    private ArrayList<Player> players = new ArrayList<>();
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("Dan",1,"ATHENA");
        p2 = new Player("Bob",2,"ATLAS");
        players.add(p1);
        players.add(p2);
        g.setPlayers(players);
    }

    @After
    public void tearDown() {
        p1 = null;
        p2 = null;
        players.clear();
        g.reset();
    }

    @Test
    public void powerEffect_blockedOpponentStepUp_cannotStepUpButCanMove() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        g.getCell(1,0).setCellLevel();
        g.getCell(4,1).setCellLevel();
        p1.effect();
        p1.move(1,0, p1.getWorker1());
        assertTrue(p1.getCard().powerEffectAvailable());
        p1.build(2,0, 0, p1.getWorker1());
        assertTrue(p1.getCard().powerEffectAvailable());
        p1.effect();
        assertTrue(p1.getCard().powerEffectAvailable());
        p2.move(4,1, p2.getWorker1());
        assertEquals(4, p2.getWorker1().getCurrentX());
        assertEquals(0, p2.getWorker1().getCurrentY());
        p2.move(3,0, p2.getWorker1());
        assertEquals(3, p2.getWorker1().getCurrentX());
        assertEquals(0, p2.getWorker1().getCurrentY());
    }

    @Test
    public void powerEffect_blockedOpponentStepUp_canMove() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.effect();
        p1.move(0,1, p1.getWorker1());
        p1.effect();
        p2.move(4,3, p2.getWorker2());
        assertFalse(p1.getCard().powerEffectAvailable());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker2().getCurrentX());
        assertEquals(3, p2.getWorker2().getCurrentY());
    }

    @Test
    public void powerEffect_notBlockedOpponentStepUp_canStepUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.effect();
        p1.move(0,1, p1.getWorker1());
        p1.effect();
        p2.build(4,1, 0, p2.getWorker1());
        p2.move(4,1, p2.getWorker1());
        p2.move(4,3, p2.getWorker2());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker1().getCurrentX());
        assertEquals(1, p2.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker2().getCurrentX());
        assertEquals(3, p2.getWorker2().getCurrentY());
    }

    @Test
    public void undoMove_undoEffect_otherPlayersCanStepUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        g.getCell(1,0).setCellLevel();
        g.getCell(4,1).setCellLevel();
        p1.effect();
        p1.move(1,0, p1.getWorker1());
        assertTrue(p1.getCard().powerEffectAvailable());
        p1.doUndoMove(p1.getWorker1());
        p1.effect();
        assertFalse(p1.getCard().powerEffectAvailable());
        p1.move(0,1, p1.getWorker1());
        p1.effect();
        assertFalse(p1.getCard().powerEffectAvailable());
        p1.build(0,2,1, p1.getWorker1());
        p2.move(4,1, p2.getWorker1());
        assertNull(g.getCell(4,0).getWorker());
        assertEquals(g.getCell(4,1).getWorker(), p2.getWorker1());
    }

    @Test
    public void undoMove_notUndoEffect_otherPlayersCannotStepUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        g.getCell(1,0).setCellLevel();
        g.getCell(4,1).setCellLevel();
        p1.effect();
        p1.move(1,0, p1.getWorker1());
        assertTrue(p1.getCard().powerEffectAvailable());
        p1.doUndoMove(p1.getWorker1());
        p1.effect();
        assertFalse(p1.getCard().powerEffectAvailable());
        p1.move(1,0, p1.getWorker1());
        p1.effect();
        assertTrue(p1.getCard().powerEffectAvailable());
        p1.build(0,2,1, p1.getWorker1());
        p1.doUndoBuild(p1.getWorker1());
        assertEquals(0, g.getCell(0,2).getLevel());
        p1.build(1,1,1, p1.getWorker1());
        assertEquals(1, g.getCell(1,1).getLevel());
        p2.move(4,1, p2.getWorker1());
        assertNull(g.getCell(4,1).getWorker());
        assertEquals(g.getCell(4,0).getWorker(), p2.getWorker1());
        p2.move(3,0, p2.getWorker1());
        assertNull(g.getCell(4,0).getWorker());
        assertEquals(g.getCell(3,0).getWorker(), p2.getWorker1());
    }
}