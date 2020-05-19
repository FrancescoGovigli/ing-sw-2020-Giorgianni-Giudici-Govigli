package it.polimi.ingsw.PSP42.model;

import org.junit.*;
import static org.junit.Assert.*;

public class PanTest {
    private Player p1;
    private Player p2;
    private GameBoard g = GameBoard.getInstance();

    @Before
    public void setUp() {
        p1 = new Player("BOB", 1, "PAN");
        p2 = new Player("FRA", 1, "PAN");
    }

    @After
    public void tearDown() {
        p1 = null;
        p2 = null;
        g.reset();
    }

    @Test
    public void win_OK() {
        p1.initialPosition(0, 0, p1.getWorker1());
        p1.initialPosition(0, 4, p1.getWorker2());
        p2.initialPosition(4, 0, p2.getWorker1());
        p2.initialPosition(4, 4, p2.getWorker2());
        // 1st round
        p1.move(1, 1, p1.getWorker1());
        p1.build(0, 0, g.getCell(0, 0).getLevel()+1, p1.getWorker1());
        p1.move(1, 3, p1.getWorker2());
        p1.build(0, 4, g.getCell(0, 4).getLevel()+1, p1.getWorker2());
        p2.move(3, 1, p2.getWorker1());
        p2.build(4, 0, g.getCell(4, 0).getLevel()+1, p2.getWorker1());
        p2.move(3, 3, p2.getWorker2());
        p2.build(4, 4, g.getCell(4, 4).getLevel()+1, p2.getWorker2());
        // 2nd round
        p1.move(1, 0, p1.getWorker1());
        p1.build(1, 1, g.getCell(1, 1).getLevel()+1, p1.getWorker1());
        p1.move(1, 4, p1.getWorker2());
        p1.build(1, 3, g.getCell(1, 3).getLevel()+1, p1.getWorker2());
        p2.move(3, 0, p2.getWorker1());
        p2.build(3, 1, g.getCell(3, 1).getLevel()+1, p2.getWorker1());
        p2.move(3, 4, p2.getWorker2());
        p2.build(3, 3, g.getCell(3, 3).getLevel()+1, p2.getWorker2());
        // 3rd round
        p1.move(1, 1, p1.getWorker1());
        p1.build(0, 0, g.getCell(0, 0).getLevel()+1, p1.getWorker1());
        p1.move(1, 3, p1.getWorker2());
        p1.build(0, 4, g.getCell(0, 4).getLevel()+1, p1.getWorker2());
        p2.move(3, 1, p2.getWorker1());
        p2.build(4, 0, g.getCell(4, 0).getLevel()+1, p2.getWorker1());
        p2.move(3, 3, p2.getWorker2());
        p2.build(4, 4, g.getCell(4, 4).getLevel()+1, p2.getWorker2());
        // 4th round
        p1.move(0, 0, p1.getWorker1());
        p1.build(1, 1, g.getCell(1, 1).getLevel()+1, p1.getWorker1());
        p1.move(0, 4, p1.getWorker2());
        p1.build(1, 3, g.getCell(1, 3).getLevel()+1, p1.getWorker2());
        p2.move(4, 0, p2.getWorker1());
        p2.build(3, 1, g.getCell(3, 1).getLevel()+1, p2.getWorker1());
        p2.move(4, 4, p2.getWorker2());
        p2.build(3, 3, g.getCell(3, 3).getLevel()+1, p2.getWorker2());
        // 4 winners
        p1.move(0, 1, p1.getWorker1());
        p1.move(0, 3, p1.getWorker2());
        p2.move(4, 1, p2.getWorker1());
        p2.move(4, 3, p2.getWorker2());
        assertEquals("WIN", p1.getPlayerState());
        assertEquals("WIN", p2.getPlayerState());
        assertEquals(2, g.getCell(0, 0).getLevel());
        assertEquals(2, g.getCell(1, 1).getLevel());
        assertEquals(2, g.getCell(0, 4).getLevel());
        assertEquals(2, g.getCell(1, 3).getLevel());
        assertEquals(2, g.getCell(4, 0).getLevel());
        assertEquals(2, g.getCell(3, 1).getLevel());
        assertEquals(2, g.getCell(4, 4).getLevel());
        assertEquals(2, g.getCell(3, 3).getLevel());
    }
}
