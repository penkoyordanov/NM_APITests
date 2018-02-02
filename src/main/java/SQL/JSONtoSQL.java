package SQL;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

public class JSONtoSQL {
    private static JSONParser parser = new JSONParser();

    private static JSONObject parseJsonFromFile(){
        Object obj = null;
        try {

            obj = parser.parse(new FileReader(sqlFolderName()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (JSONObject) obj;
    }

    static String sqlFolderName(){
        Path parentFolder= Paths.get(System.getProperty("user.dir")).getParent();
        File root = new File(parentFolder.toString());
        String fileName = "SQL.json";
        String fileAbsPath="";
        try {
            boolean recursive = true;


            Collection files = FileUtils.listFiles(root, null, recursive);

            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                if (file.getName().equals(fileName))
                    fileAbsPath=file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileAbsPath;
    }

    static String getConncectionString(){
        return (String) parseJsonFromFile().get("connectionString");
    }


    static String restorePassword(String email){
        return String.format((String) parseJsonFromFile().get("restorePassword"),email);
    }

    static String getUserDetailsSQL(String email){
        return String.format((String) parseJsonFromFile().get("getUserDetails"),email);
    }

    static String getBusinessDetailsSQL(int businessID){
        return String.format((String) parseJsonFromFile().get("getBusinessProfile"),businessID);
    }

    static String getFPUIDByEmailSQL(String email){
        return String.format((String) parseJsonFromFile().get("getForgottenPasswordUniqueIDByEmail"),email);
    }

    static String getAccessCodeByEmailSQL(String email){
        return String.format((String) parseJsonFromFile().get("getAccessCodeByEmail"),email);
    }

    static String getVerificationCodebyGuidSQL(String requestGuid){
        return String.format((String) parseJsonFromFile().get("getVerificationCodeByGuid"),requestGuid);
    }

    static String getCountTodaysAdsSQL(int userID, String dateToday){
        return String.format((String) parseJsonFromFile().get("getCountTodaysAds"),userID,dateToday);
    }

    static String getStatisticsForAdSQL(int adId){
        return String.format((String)parseJsonFromFile().get("getAdStatistics"),adId);
    }

    static String getMessagesByConversationAdSQL(int conversationId){
        return String.format((String)parseJsonFromFile().get("getMessagesByConversation"),conversationId);
    }

    static String getMessagesDetailsSQL(int messageId){
        return String.format((String)parseJsonFromFile().get("getMessageDetails"),messageId);
    }

    static String getDeleteConversationSQL(){
        return String.format((String)parseJsonFromFile().get("deleteConversation"));
    }

    static String getLatestUserSQL(){
        return String.format((String)parseJsonFromFile().get("getLatestUser"));
    }

    static String getUserFollowingsSQL(int userId){
        return String.format((String)parseJsonFromFile().get("getUserFollowings"),userId);
    }

    static String getUserFollowersSQL(int userId){
        return String.format((String)parseJsonFromFile().get("getUserFollowers"),userId);
    }


    public static void main(String[] args) {
        System.out.println(getUserFollowersSQL(1));
    }
}
