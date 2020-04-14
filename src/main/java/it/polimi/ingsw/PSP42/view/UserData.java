package it.polimi.ingsw.PSP42.view;

public class UserData {
    private int age;
    private String nickname;
    public UserData(String nickname,int age){
        this.nickname=nickname;
        this.age=age;
    }

    public int getAge() {
        return age;
    }

    public String getNickname() {
        return nickname;
    }
}
