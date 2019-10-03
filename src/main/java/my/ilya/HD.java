package my.ilya;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;

import java.sql.*;


public class HD
{
    public static final String USER_NAME = "DEV_SQL_HD151_WIP";
    public static final String USER_NAME_YP1 = "YP1_ME_WIP";
    public static final String PASSWORD = "visiprise";

    public static void main( String[] args ){

        String url = "jdbc:sqlserver://192.168.55.123:1433";
        Connection con15 = null;
        Connection conYP = null;

        try {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            System.out.println("Failed to load mSQL driver.");
            return;
        }
        try {
            con15 = DriverManager.getConnection(url, USER_NAME, PASSWORD);
            conYP = DriverManager.getConnection(url, USER_NAME_YP1, PASSWORD);
            conYP.setAutoCommit(false);
            con15.setAutoCommit(false);
            PreparedStatement select = conYP.prepareStatement("SELECT HANDLE,RECIPE_STEP_BO, SFC_STEP_BO,SFC_BO,OPERATION,ROUTING_SEQUENCE,DONE,BYPASSED,CURRENT_STEP,FAILED,ERROR_CODE,ERROR_DETAILS FROM RECIPE_ROUTING_STEP order by RECIPE_STEP_BO asc ",
                    SQLServerResultSet.TYPE_SCROLL_SENSITIVE, SQLServerResultSet.CONCUR_READ_ONLY);
            select.setFetchSize(100);
            int counter = 0;
            int mod = -1;
            ResultSet resultSet = select.executeQuery();
            //resultSet.absolute(1351955);
            resultSet.last();

            while (resultSet.previous()) {
                //System.out.println("HANDLE: "+resultSet.getString(1));
                counter++;
                if(counter / 1000 != mod){
                    mod = counter/1000;
                    System.out.println("Counter: " + counter);
                }

                Statement select15 = con15.createStatement();
                select15.setFetchSize(500);
                ResultSet rs15 = select15.executeQuery("SELECT * FROM RECIPE_ROUTING_STEP WHERE HANDLE='" + resultSet.getString(1)+"'");
                if(!rs15.next()){
                    PreparedStatement insert = con15.prepareStatement("INSERT INTO RECIPE_ROUTING_STEP (HANDLE,RECIPE_STEP_BO, SFC_STEP_BO,SFC_BO,OPERATION,ROUTING_SEQUENCE,DONE,BYPASSED,CURRENT_STEP,FAILED,ERROR_CODE,ERROR_DETAILS)" +
                            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

                    insert.setString(1, resultSet.getString(1));
                    insert.setString(2, resultSet.getString(2));
                    insert.setString(3, resultSet.getString(3));
                    insert.setString(4, resultSet.getString(4));
                    insert.setString(5, resultSet.getString(5));
                    insert.setInt(6, resultSet.getInt(6));
                    insert.setString(7, resultSet.getString(7));
                    insert.setString(8, resultSet.getString(8));
                    insert.setString(9, resultSet.getString(9));
                    insert.setString(10, resultSet.getString(10));
                    insert.setString(11, resultSet.getString(11));
                    insert.setString(12, resultSet.getString(12));
                    insert.execute();
                    con15.commit();
               }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con15 != null) {
                try {
                    con15.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conYP != null) {
                try {
                    conYP.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
