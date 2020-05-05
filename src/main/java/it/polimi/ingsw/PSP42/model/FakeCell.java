package it.polimi.ingsw.PSP42.model;

import it.polimi.ingsw.PSP42.view.*;

import java.io.*;

public class FakeCell implements Serializable {
    public String playerName;
    public int workerNum;
    public int level;
    public int id;

    public FakeCell(String playerName, int workerNum, int level, int id){
        this.playerName = playerName;
        this.workerNum = workerNum;
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public int getWorkerNum() {
        return workerNum;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    /**
     * Method that creates the copy of the game board at the call state
     * @return gCopy (FakeCell matrix containing only player name, worker 1 or 2 and construction level)
     */
    public static FakeCell[][] getGameBoardCopy(){
        FakeCell[][] gCopy = new FakeCell[5][5];
        GameBoard g = GameBoard.getInstance();
        String playerName = null;
        int workerNum = 0;
        int level = 0;
        int id = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                playerName = null;
                workerNum = 0;
                id = 0;
                if (g.getCell(i, j).getWorker() != null){
                    playerName = g.getCell(i, j).getWorker().getPlayer().getNickname();
                    id = g.getCell(i, j).getWorker().getPlayer().getId();
                    if (g.getCell(i, j).getWorker().getPlayer().getWorker1() == g.getCell(i, j).getWorker())
                        workerNum = 1;
                    else
                        workerNum = 2;
                }
                level = g.getCell(i, j).getLevel();
                gCopy[i][j] = new FakeCell(playerName, workerNum, level, id);
            }
        }
        return gCopy;
    }

}