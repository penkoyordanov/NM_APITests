package Enums;

public enum UserEmail {
    TESTUSR("test@novamanus.com"),
    PENKO("penko.dimitrow1@abv.bg"),
    LORISCOTT("lscott1k@ftc.gov"),
    JOHNDOE("john.doe@nm.com"),
    HARALAMPI("haralampi172@abv.bg"),
    KEGUGUK("keguk@zainmax.net"),
    HARIBARI("nkvyrslx@sharklasers.com"),
    ARNOLDOGOYETTE("arnoldo.goyette@mailinator.com"),
    LATESTUSER("");

    private String userEmail;

    UserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
