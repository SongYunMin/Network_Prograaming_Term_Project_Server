import java.sql.*;

public class dbconn {
    public static int initStatusNum = 0;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public void Connect(String SQL) throws SQLException {
        String server = "localhost";            // Server Address
        String database = "Vending_machine";    // Vending_machine
        String user_name = "root";
        String password = "";
        // 1.드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! <JDBC 오류> Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 2.연결
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/ " + database + "?serverTimezone=UTC&useSSL=false", user_name, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("conn 오류:" + e.getMessage());
            e.printStackTrace();
        }
        if(initStatusNum > 1) {
            if (mainCommunication.clientStatus.equals("init")) {
                System.out.println("자판기 초기화 Test");
                insertData(SQL);
            }else if (mainCommunication.clientStatus.equals("select")){
                System.out.println("Select 쿼리 실행");
                SelectData("test");
            }
        }else{
            insertData(SQL);
            initStatusNum++;
        }
        // 3.해제
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
        }
    }

    public void insertData(String SQL) throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.executeUpdate();

            System.out.println("Table 에 새로운 Record 를 추가했습니다.");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("레코드 추가 실패");
        } finally {
            if(pstmt != null) try {
                pstmt.close();
            } catch (SQLException sqle){

            }
            if(conn != null) try{
                conn.close();
            } catch (SQLException sqle){

            }
        }
    }

    // TODO : DB 에서 가져온 후, 클라이언트 자판기에 Set해줘야 함
    public String SelectData(String SQL) throws SQLException {
        try{
            pstmt = conn.prepareStatement(SQL);
            rs = pstmt.executeQuery();

            while(rs.next()){
                String water_num = rs.getString("water_num");
                String coffee_num = rs.getString("coffee_num");
                String sport_num = rs.getString("sport_num");
                String highcoffee_num = rs.getString("highcoffee_num");
                String soda_num = rs.getString("soda_num");
            }


        } catch (Exception e){

        }
        return "g";
    }
}
