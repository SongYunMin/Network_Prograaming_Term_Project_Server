import java.sql.*;
import java.util.Arrays;

public class dbconn {
    public static int initStatusNum = 0;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public String Connect(String SQL) throws SQLException {
        String buf = null;
        String server = "localhost";            // Server Address
        String database = "Vending_machine";    // Vending_machine
        String user_name = "root";
        String password = "";
        char[] selectReturn;
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
        if (initStatusNum > 1) {
            if (mainCommunication.clientStatus.equals("init")) {
                System.out.println("자판기 초기화 Test");
                insertData(SQL);
            } else if (mainCommunication.clientStatus.equals("select")) {
                System.out.println("Select 쿼리 실행");
                selectReturn = SelectData("SELECT * FROM vending");
                buf = Arrays.toString(selectReturn);
            } else if (mainCommunication.clientStatus.equals("update")) {

            }
        } else {                        // 자판기 초기화 DB Insert
            char[] numBuf;              // 자판기 번호를 받아올 변수
            insertData(SQL);            // 음료들을 배치하고
            // 자판기의 번호를 가져온다
            numBuf = SelectData("SELECT MAX(vending_index) AS max_vending_index FROM vending");
            init_Server.vendingNum = Integer.parseInt(String.valueOf(numBuf));
            initStatusNum++;
        }
        // 3.해제
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
        }
        return buf;
    }

    public void insertData(String SQL) throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.executeUpdate();

            System.out.println("Table 에 새로운 Record 를 추가했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("레코드 추가 실패");
        }
//        finally {
//            if (pstmt != null) try {
//                pstmt.close();
//            } catch (SQLException sqle) {
//                sqle.printStackTrace();
//            }
//            if (conn != null) try {
//                conn.close();
//            } catch (SQLException sqle) {
//                sqle.printStackTrace();
//            }
//        }
    }

    // TODO : DB 에서 가져온 후, 클라이언트 자판기에 Set해줘야 함
    public char[] SelectData(String SQL) throws SQLException {
        char[] clientSend = new char[0];
        try {
            StringBuilder buf = new StringBuilder("");
            String str;
            pstmt = conn.prepareStatement(SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 초기화 SELECT 라면?
                if (initStatusNum < 1) {
                    buf.append(rs.getString("max_vending_index"));
                    str = buf.toString();
                    clientSend = str.toCharArray();
                } else {
                    buf.append(rs.getString("water_num"));
                    buf.append(rs.getString("coffee_num"));
                    buf.append(rs.getString("sport_num"));
                    buf.append(rs.getString("highcoffee_num"));
                    buf.append(rs.getString("soda_num"));
                }
            }
            str = buf.toString();
            clientSend = str.toCharArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("레코드 추가 실패");
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            return clientSend;
        }
    }
}
