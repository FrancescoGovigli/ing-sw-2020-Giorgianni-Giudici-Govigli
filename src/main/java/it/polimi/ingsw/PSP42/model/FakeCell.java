package it.polimi.ingsw.PSP42.model;

import java.io.*;

public class FakeCell implements Serializable {

    public String playerName;
    public int id;
    public int workerNum;
    public int level;
    public boolean[] builtLevel;

    public FakeCell(String playerName, int id, int workerNum, int level, boolean[] builtLevel) {
        this.playerName = playerName;
        this.id = id;
        this.workerNum = workerNum;
        this.level = level;
        this.builtLevel = builtLevel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    public int getWorkerNum() {
        return workerNum;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Method that creates the copy of the game board during a specific game state.
     * @return gCopy (FakeCell matrix containing only player name, worker 1 or 2 and construction level)
     */
    public static FakeCell[][] getGameBoardCopy() {
        FakeCell[][] gCopy = new FakeCell[5][5];
        GameBoard g = GameBoard.getInstance();
        String playerName = null;
        int id = 0;
        int workerNum = 0;
        int level = 0;
        boolean[] builtLevel;
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
                builtLevel = g.getCell(i, j).getBuiltLevel();
                gCopy[i][j] = new FakeCell(playerName, id, workerNum, level, builtLevel);
            }
        }
        return gCopy;
    }
}