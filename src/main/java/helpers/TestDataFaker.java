package helpers;

import com.github.javafaker.Faker;

import java.util.Locale;

public class TestDataFaker {
    private static Faker faker = new Faker(new Locale("en"));

    public static String getTitle() {
        return faker.lorem().sentence();
    }
    public static String getDescription() {
        return faker.lorem().sentence(10);
    }

    public static int getPriceInt() {
        return faker.number().numberBetween(1,2000000);
    }

    public static String getKeyword(){
        return faker.lorem().word();
    }

    /*public static void main(String[] args) {
        for (int i=0;i<20;i++){
            System.out.println(faker.name().firstName());
        }
    }*/

}