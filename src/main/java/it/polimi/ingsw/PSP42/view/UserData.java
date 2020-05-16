package it.polimi.ingsw.PSP42.view;

import java.io.*;

public class UserData implements Serializable {
    private String nickname;
    private String cardName;

    public UserData(String nickname, String cardName) {
        this.nickname = nickname;
        this.cardName = cardName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardChoosed() {
        return cardName;
    }
}
