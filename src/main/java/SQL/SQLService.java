package SQL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SQLService extends SQLConnectionBase{

    public static void restorePassword(String email) {
        executeUpdate(JSONtoSQL.restorePassword(email));
    }

    public static void main(String[] args) {

        /*Map<String,String> map=getMessageDetails(317);
        System.out.println();*/
        System.out.println(getUserFollowers(1));
    }

    public static Map<String,String> getUserDetails(String email) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getUserDetailsSQL(email));
        Map<String,String> userDetails=new HashMap<>();
        userDetails.put("firstName",results.get(0)[5]);
        userDetails.put("lastName",results.get(0)[6]);
        userDetails.put("locationDto",results.get(0)[8]);
        userDetails.put("placeId",results.get(0)[7]);
        userDetails.put("language",results.get(0)[9]);
        userDetails.put("userName",results.get(0)[2]);
        userDetails.put("userID",results.get(0)[0]);
        return userDetails;
    }

    public static Map<String,String> getBusinessDetails(int businessID) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getBusinessDetailsSQL(businessID));
        Map<String,String> businessDetails=new HashMap<>();
        businessDetails.put("vat",results.get(0)[0]);
        businessDetails.put("name",results.get(0)[1]);
        businessDetails.put("email",results.get(0)[2]);
        businessDetails.put("address",results.get(0)[3]);
        return businessDetails;
    }

    public static Map<String,String> getAdStatistics(int adId) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getStatisticsForAdSQL(adId));
        Map<String,String> adStatistics=new HashMap<>();
        adStatistics.put("Views",results.get(0)[2]);
        adStatistics.put("Shared",results.get(0)[0]);
        adStatistics.put("Mailed",results.get(0)[1]);
        adStatistics.put("Saved",results.get(0)[4]);
        adStatistics.put("Comments",results.get(0)[3]);
        return adStatistics;

    }

    public static Map<String,String> getMessageDetails(int messageId) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getMessagesDetailsSQL(messageId));
        Map<String,String> messageMap=new HashMap<>();
        messageMap.put("Text",results.get(0)[0]);
        messageMap.put("Sender",results.get(0)[1]);
        return messageMap;

    }

 /*   public static Map<String,String> getUserFollowings(int userId) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getUserFollowingsSQL(userId));
        Map<String,String> follower=new HashMap<>();
        follower.put("username",results.get(0)[0]);
        follower.put("firstName",results.get(0)[1]);
        follower.put("lastName",results.get(0)[2]);
        System.out.println();
        return follower;

    }*/

    public static ArrayList<Map<String, String>> getUserFollowings(int userId) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getUserFollowingsSQL(userId));

        ArrayList<Map<String, String>> followings=new ArrayList<>();
        for (int i=0;i<results.size();i++){
            Map<String,String> f=new HashMap<>();
            followings.add(i,f);
            f.put("username",results.get(i)[0]);
            f.put("firstName",results.get(i)[1]);
            f.put("lastName",results.get(i)[2]);
        }
        return followings;

    }

    public static ArrayList<Map<String, String>> getUserFollowers(int userId) {
        ArrayList<String[]> results=getResultFromDB(JSONtoSQL.getUserFollowersSQL(userId));

        ArrayList<Map<String, String>> followings=new ArrayList<>();
        for (int i=0;i<results.size();i++){
            Map<String,String> f=new HashMap<>();
            followings.add(i,f);
            f.put("email",results.get(i)[0]);
            f.put("firstName",results.get(i)[1]);
            f.put("lastName",results.get(i)[2]);
            f.put("city",results.get(i)[3]);
        }
        return followings;

    }

    private static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public static String getFPUIDByEmail(String email) {
        return getResultFromDB(JSONtoSQL.getFPUIDByEmailSQL(email)).get(0)[0];

    }

    public static String getAccessCodeByEmail(String email) {
        return getResultFromDB(JSONtoSQL.getAccessCodeByEmailSQL(email)).get(0)[0];

    }

    public static String getVerificationCode(String requestGuid) {
        return getResultFromDB(JSONtoSQL.getVerificationCodebyGuidSQL(requestGuid)).get(0)[0];

    }

    public static String getCountTodaysAds(int userID) {
        return getResultFromDB(JSONtoSQL.getCountTodaysAdsSQL(userID,getCurrentDate()+"%")).get(0)[0];

    }

    public static String getLatestUser() {
        return getResultFromDB(JSONtoSQL.getLatestUserSQL()).get(0)[0];

    }

    public static void deleteConversation() {
        executeUpdate(JSONtoSQL.getDeleteConversationSQL());

    }
}
