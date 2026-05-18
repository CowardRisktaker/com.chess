package model;

public class Utilisateur {

    private int     id;
    private String  login;

    public Utilisateur(){

    }

    public Utilisateur(int id, String login, int elo) {
        this.id = id;
        this.login = login;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
}
