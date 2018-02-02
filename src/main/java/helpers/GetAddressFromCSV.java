package helpers;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GetAddressFromCSV {
    private static String csvFile = "addresses.csv";
    private static String line;
    private static Map<String,String> randomAddress=new HashMap<String, String>();

    public static Map<String, String> getRandomAddress() {
        setRandomAddress();
        return randomAddress;
    }

    private static void setRandomAddress() {
        BufferedReader br = null;
        Random r = new Random();
        int random = r.nextInt(28-1) + 1;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            int lineNumber=0;
            while ((line = br.readLine()) != null) {
                if(random==lineNumber){
                    // use comma as separator
                    String[] adTD =line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    randomAddress.put("streetAddress",adTD[0]);
                    randomAddress.put("placeId",adTD[1]);
                    randomAddress.put("countryCode",adTD[2]);
//                    line=line;
                    break;
                }
                lineNumber++;

            }


        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
