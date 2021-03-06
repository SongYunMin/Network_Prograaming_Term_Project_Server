import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class mainCommunication {
    public static String clientStatus = null;
    ServerSocket serverSocket = null;
    Socket socket = null;

    OutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;

    InputStream inputStream = null;
    DataInputStream dataInputStream = null;

    public void waitClientClick() {
        String clientSending = null;
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Main Communication Client Connect Ready");
            while (true) {
                // 연결 대기
                socket = serverSocket.accept();
                // 연결된  소켓 출력
                System.out.println("Main Communication Client Connect OK");
                System.out.println("socket : " + socket);
                // 입력 위한 스트림 생성
                inputStream = socket.getInputStream();
                dataInputStream = new DataInputStream(inputStream);

                // 출력 위한 스트림 생성
                outputStream = socket.getOutputStream();
                dataOutputStream = new DataOutputStream(outputStream);

                String clientMessage = dataInputStream.readUTF();
                System.out.println("clientMessage : " + clientMessage);
                if (clientMessage.equals("insert")) {             // status 전송 과정에서 insert 라면
                    clientStatus = "insert";
                } else if (clientMessage.equals("select")) {      // status 전송 과정에서 select 라면
                    clientStatus = "select";
                } else if (clientMessage.equals("update")) {      // status 전송 과정에서 update 라면
                    clientStatus = "update";
                } else if (clientMessage.equals("init")) {
                    clientStatus = "init";
                } else if (clientMessage.equals("money")){
                    clientStatus = "money";
                } else {                                         // 쿼리문 전달이라면
                    dbconn db = new dbconn();
                    clientSending = db.Connect(clientMessage);
                }
                dataOutputStream.writeUTF("SQL 전송 완료");

                // 결과 값이 null 이 아니라면 클라이언트로 결과 값 전송
                if(clientSending != null) {
                    dataOutputStream.writeUTF(clientSending);
                    dataOutputStream.flush();
                }
                dataOutputStream.flush();

                if (clientMessage.equals("stop\r\n")) {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) dataOutputStream.close();
                if (outputStream != null) outputStream.close();
                if (dataInputStream != null) dataInputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
