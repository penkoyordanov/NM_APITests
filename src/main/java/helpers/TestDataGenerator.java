package helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestDataGenerator {

    public static String getTimeOneHourInAdvance(){
        DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        Date date=new Date();
        date.setTime(date.getTime()+1000*60*60*1);
        return dateFormat.format(date);
    }

    public static String getCurrentDate(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        return dateFormat.format(date);
    }

    public static int getRandomPrice(){
        return randomInteger(200,200000);
    }

    private static int randomInteger(int aStart, int aEnd){
        Random random=new Random();
        if (aStart>aEnd){
            throw  new IllegalArgumentException("Start cannot exceed end");
        }
        long range=(long) aEnd-(long)aStart+1;
        long fraction = (long)(range * random.nextDouble());
        return (int)(fraction + aStart);
    }
}
