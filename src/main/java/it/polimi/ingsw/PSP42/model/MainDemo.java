package it.polimi.ingsw.PSP42.model;

public class MainDemo {
    public static void main(String[] args) throws OccupiedCellException, UnavailableWorkerException, NotYourWorkerException, InvalidMoveException, InvalidBuildException {
        GameBoard g = GameBoard.getInstance();
        Player p1 = new Player("cii",1);
        p1.setInitialPosition(0,0,p1.getWorker1());
        p1.setInitialPosition(1,1,p1.getWorker2());
        p1.setPosWorker(0,1,p1.getWorker1());
        p1.build(1,2,p1.getWorker1());
        System.out.println("COstruscie"+ g.getCell(1,2).getLevel());
    }
}
