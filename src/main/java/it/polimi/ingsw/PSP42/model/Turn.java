package it.polimi.ingsw.PSP42.model;

public enum Turn {
    START("START"), PREMOVE("PREMOVE"), MOVE("MOVE"), PREBUILD("PREBUILD"), BUILD("BUILD"), END("END");

    private String state;

    private Turn(String state){
        this.state = state;
    }
}
