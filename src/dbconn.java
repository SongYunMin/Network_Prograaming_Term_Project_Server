import java.sql.*;

public class dbconn {
    Connection conn = null;
    PreparedStatement pstmt = null;

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

        insertData(SQL);

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
}
