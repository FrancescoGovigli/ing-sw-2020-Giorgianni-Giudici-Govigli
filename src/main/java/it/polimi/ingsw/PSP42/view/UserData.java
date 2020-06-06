package it.polimi.ingsw.PSP42.view;

import java.io.*;

public class UserData implements Serializable {

    private String nickname;
    private String cardName;

    /**
     * Constructor to set UserData.
     * @param nickname player nickname
     * @param cardName player card
     */
    public UserData(String nickname, String cardName) {
        this.nickname = nickname;
        this.cardName = cardName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardChosen() {
        return cardName;
    }
}
