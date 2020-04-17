package it.polimi.ingsw.PSP42.view;

public class UserData {
    private int age;
    private String nickname;
    private String cardname;
    public UserData(String nickname,int age,String cardname){
        this.nickname=nickname;
        this.age=age;
        this.cardname=cardname;
    }

    public int getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCardChoosed() {
        return cardname;
    }
}
