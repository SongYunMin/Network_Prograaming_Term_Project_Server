import java.io.IOException;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws SQLException, IOException {
        init_Server init = new init_Server();                           // 초기 instance 변수 생성
        init.initServer();                                              // 자판기 초기화
        mainCommunication Communication = new mainCommunication();      // 메인 통신 인스턴스 생성
        Communication.waitClientClick();                                // 클라이언트 통신
    }
}