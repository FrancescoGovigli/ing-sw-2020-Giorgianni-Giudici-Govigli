package it.polimi.ingsw.PSP42.view;

public class UserData {
    private int age;
    private String nickname;
    private String cardName;

    public UserData(String nickname, int age, String cardName){
        this.nickname = nickname;
        this.age = age;
        this.cardName = cardName;
    }

    public int getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardChoosed() {
        return cardName;
    }
}
