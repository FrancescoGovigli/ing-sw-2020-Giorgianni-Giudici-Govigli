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
        p1 = new Player("Dan",1,21,"ATHENA");
        p2 = new Player("Bob",2,21,"ATHENA");
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
    public void powerEffect_BlockedOpponentStepUp_cannotMoveUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        p1.build(0,1, 0, p1.getWorker1());
        g.setGamePhase("START");
        p1.getCard().powerEffect();
        p1.move(0,1, p1.getWorker1());
        assertTrue(p1.getCard().powerEffectAvailable());
        g.setGamePhase("END");
        p1.getCard().powerEffect();
        assertTrue(p1.getCard().powerEffectAvailable());
        p2.build(4,1, 0, p2.getWorker1());
        p2.move(4,1, p2.getWorker1());
        p2.move(4,3, p2.getWorker2());
        g.setGamePhase("START");
        p1.getCard().powerEffect();
        assertFalse(p1.getCard().powerEffectAvailable());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker1().getCurrentX());
        assertEquals(0, p2.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker2().getCurrentX());
        assertEquals(3, p2.getWorker2().getCurrentY());
    }

    @Test
    public void powerEffect_BlockedOpponentStepUp_canMoveUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        g.setGamePhase("START");
        p1.build(0,1, 0, p1.getWorker1());
        p1.getCard().powerEffect();
        p1.move(0,1, p1.getWorker1());
        g.setGamePhase("END");
        p1.getCard().powerEffect();
        p2.move(4,3, p2.getWorker2());
        assertTrue(p1.getCard().powerEffectAvailable());
        assertEquals(0, p1.getWorker1().getCurrentX());
        assertEquals(1, p1.getWorker1().getCurrentY());
        assertEquals(4, p2.getWorker1().getCurrentX());
        assertEquals(3, p2.getWorker2().getCurrentY());
    }

    @Test
    public void powerEffect_notBlockedOpponentStepUp_canMoveUp() {
        p1.initialPosition(0,0, p1.getWorker1());
        p1.initialPosition(0,4, p1.getWorker2());
        p2.initialPosition(4,0, p2.getWorker1());
        p2.initialPosition(4,4, p2.getWorker2());
        g.setGamePhase("START");
        p1.getCard().powerEffect();
        p1.move(0,1, p1.getWorker1());
        g.setGamePhase("END");
        p1.getCard().powerEffect();
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
}