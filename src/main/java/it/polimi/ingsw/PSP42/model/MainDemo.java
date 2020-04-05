package it.polimi.ingsw.PSP42.model;

public class MainDemo {
    public static void main(String[] args) {
        GameBoard g = GameBoard.getInstance();
        Player p1 = new Player("Fra",1);
        p1.setInitialPosition(0,0, p1.getWorker1());
        System.out.println(p1.getWorker1().getCurrentX() + " " + p1.getWorker1().getCurrentY());
        System.out.println(g.getCell(0,0).getWorker());
        System.out.println(p1.getWorker1());
    }
}
