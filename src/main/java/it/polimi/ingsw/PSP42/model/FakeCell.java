package it.polimi.ingsw.PSP42.model;

import it.polimi.ingsw.PSP42.view.*;

public class FakeCell {
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
    /**
     * Method to print the current GameBoard situation on the screen
     */
    public void show(Object o){
        FakeCell[][] gCopy = (FakeCell[][]) o;
        int rowIndex = 0;
        int colIndex = 0;
        int x = 0;
        int y = 0;
        System.out.println();
        for (int i = 0; i < 16; i++) {
            boolean row1 = (i == 1 || i == 4 || i == 7 || i == 10 || i == 13);
            boolean row2 = (i == 2 || i == 5 || i == 8 || i == 11 || i == 14);
            boolean rowBoardIndex = (i == 3 || i == 6 || i == 9 || i == 12);
            for (int j = 0; j < 41; j++) {
                boolean col1 = (j == 3 || j == 11 || j == 19 || j == 27 || j == 35);
                boolean col2 = (j == 5 || j == 13 || j == 21 || j == 29 || j == 37);
                boolean colBoardIndex = (j == 9 || j == 17 || j == 25 || j == 33);
                if (i % 3 == 0)
                    if (j % 8 == 0)
                        System.out.print(Color.ANSI_REVERSE + "+" + Color.RESET);
                    else
                        System.out.print(Color.ANSI_REVERSE + "-" + Color.RESET);
                else if (j % 8 == 0)
                    System.out.print(Color.ANSI_REVERSE + "|" + Color.RESET);
                else if (col1 && row1)  // possible worker
                    if (gCopy[x][y].getPlayerName() != null)    // if a player has a worker set
                        switch (gCopy[x][y].getId()){   // color (RGB) his "W" according to his id
                            case 1:
                                System.out.print(Color.ANSI_RED + "W" + Color.RESET);
                                break;
                            case 2:
                                System.out.print(Color.ANSI_GREEN + "W" + Color.RESET);
                                break;
                            case 3:
                                System.out.print(Color.ANSI_BLUE + "W" + Color.RESET);
                                break;
                            default:
                                System.out.print("W");
                                break;
                        }
                    else
                        System.out.print(" ");
                else if (col2 && row1)  // worker's number
                    if (gCopy[x][y].getWorkerNum() == 1)
                        System.out.print("1");
                    else if (gCopy[x][y].getWorkerNum() == 2)
                        System.out.print("2");
                    else
                        System.out.print(" ");
                else if (col1 && row2)  // level
                    System.out.print("L");
                else if (col2 && row2)  // level's level
                    System.out.print(gCopy[x][y].getLevel());
                else
                    System.out.print(" ");
                if (j == 40 && row1)
                    System.out.print(" ROW");
                else if (j == 40 && row2) {     // to print the row index out of the map
                    System.out.print(" " + rowIndex);
                    rowIndex++;
                }
                if (colBoardIndex && (row1 || row2))  // increase column index for gCopy if you are in the worker or level row
                    y++;
            }
            if (row1)   y = 0;  // reset column index for gCopy if you are in the level row
            if (rowBoardIndex) {    // reset column index  and increase row index for gCopy if you are in the "―" line
                y = 0;
                x++;
            }
            System.out.println();
        }
        for (int j = 0; j < 5; j++){    // to print the column index off the map
            System.out.print("  COL " + colIndex + " ");
            colIndex++;
        }
        System.out.println();
        System.out.println("Color matching to the letter 'W':");
        System.out.println(Color.ANSI_RED + "Player 1 " + Color.ANSI_GREEN + "Player 2 " + Color.ANSI_BLUE + "Player 3 " + Color.RESET);
        System.out.println("\n");
    }
}