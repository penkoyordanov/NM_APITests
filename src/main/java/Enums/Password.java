package Enums;

public enum Password {

    TESTUSR_PASSWORD("123456"),
    DEFAULT_PASSWORD("123456");

    private String password;

    Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
