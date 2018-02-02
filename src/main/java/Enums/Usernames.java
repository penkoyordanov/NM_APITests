package Enums;

public enum Usernames {
    USERNAME_LORI("LoriScott861"),
    USERNAME_IVAN("ivanivanov84");

    private String username;

    Usernames(String username) {
        this.username=username;
    }

    public String getUsername() {
        return username;
    }
}
