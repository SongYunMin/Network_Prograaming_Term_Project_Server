import java.sql.*;

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
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/ " + database +
                    "?serverTimezone=UTC&useSSL=false", user_name, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("conn 오류:" + e.getMessage());
            e.printStackTrace();
        }
        if (initStatusNum > 1) {               // 두번의 초기화 작업을 위한 분기
            if (mainCommunication.clientStatus.equals("init")) {                // 상태 변수가 init 일때
                System.out.println("Vending Machine init");
                insertData(SQL);
            } else if (mainCommunication.clientStatus.equals("select")) {       // 상태 변수가 select 상태일때
                System.out.println("Run Select Query");
                String index = Integer.toString(init_Server.vendingNum);
                buf = SelectData(SQL + index);
            } else if (mainCommunication.clientStatus.equals("update")) {       // 상태 변수가 update 상태일때
                System.out.println("Run Update Query");
                String index = Integer.toString(init_Server.vendingNum);
                UpdateData(SQL + index);
            } else if (mainCommunication.clientStatus.equals("money")) {        // 상태 변수가 money 상태일때
                System.out.println("Add revenue to the server.");
                String index = Integer.toString(init_Server.vendingNum);
                UpdateData(SQL + index);
            }
        } else {                        // 자판기 초기화과정에 사용될 분기
            String numBuf;              // 자판기 번호를 받아올 변수
            insertData(SQL);            // 음료들을 배치하고
            // 자판기의 번호를 가져온다
            if (initStatusNum == 0) {
                // 자판기의 번호를 가져와서 추후 어떤 자판기에 금액읋 추가 시킬지 판단 함
                numBuf = SelectData("SELECT MAX(vending_index) AS max_vending_index FROM vending");
                init_Server.vendingNum = Integer.parseInt(String.valueOf(numBuf));
            }
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
            pstmt.executeUpdate();                          // SQL Insert

            System.out.println("Table 에 새로운 Record 를 추가 했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("레코드 추가 실패");
        }
    }

    // TODO : DB 에서 가져온 후, 클라이언트 자판기에 Set해줘야 함
    public String SelectData(String SQL) throws SQLException {
        //char[] clientSend = new char[0];
        String clientSend = null;
        try {
            StringBuilder buf = new StringBuilder("");
            String str;
            pstmt = conn.prepareStatement(SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 초기화 SELECT 라면?
                if (initStatusNum < 1) {
                    buf.append(rs.getString("max_vending_index"));
                    clientSend = buf.toString();
                } else {
                    buf.append(rs.getString("water_num"));
                    buf.append(rs.getString("coffee_num"));
                    buf.append(rs.getString("sport_num"));
                    buf.append(rs.getString("highcoffee_num"));
                    buf.append(rs.getString("soda_num"));
                    clientSend = buf.toString();
                }
            }
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
        }
        return clientSend;
    }

    public void UpdateData(String SQL) throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.executeUpdate();
            System.out.println("Table 을 Update 했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("레코드 추가 실패");
        }
    }
}