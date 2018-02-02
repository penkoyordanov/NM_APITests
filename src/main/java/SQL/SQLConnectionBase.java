package SQL;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by penko.yordanov on 08-Dec-16.
 */
public class SQLConnectionBase {
    private static Statement stm;
    private static Connection con;
    private static ResultSet res;

    protected static ArrayList<String[]> getResultFromDB(String query) {
        ArrayList<String[]> records = new ArrayList<String[]>();
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            con = DriverManager.getConnection(JSONtoSQL.getConncectionString());
            Class.forName(driver);
            stm = con.createStatement();


            res = stm.executeQuery(query);
            ResultSetMetaData rsMetaData = res.getMetaData();

            int cols = rsMetaData.getColumnCount();

            while (res.next()) {

                String fields[] = new String[cols];
                int col = 0;
                for (int colIdx = 1; colIdx <= cols; colIdx++) {
                    fields[col] = res.getString(colIdx);
                    col++;
                }
                records.add(fields);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (res != null) try { res.close(); } catch(Exception e) {}
            if (stm != null) try { stm.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
        }
        return records;
    }

    protected static void executeUpdate(String query){
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            con = DriverManager.getConnection(JSONtoSQL.getConncectionString());
            Class.forName(driver);
            stm = con.createStatement();
            stm.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
