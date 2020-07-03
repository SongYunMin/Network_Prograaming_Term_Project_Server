import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;


public class init_Server {
    // 자판기 초기화 코드 받을 준비
    String SQL;
    ServerSocket serverSocket = null;
    Socket socket = null;

    OutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;

    InputStream inputStream = null;
    DataInputStream  dataInputStream = null;

    public void initServer() throws IOException {
        try{
            serverSocket = new ServerSocket(9000);
            System.out.println("Initialization Client Connect Ready");

            socket = serverSocket.accept();
            System.out.println("Initialization Client Connect OK");
            System.out.println("socket : " + socket);

            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            String clientMessage = dataInputStream.readUTF();
            SQL = clientMessage;
            System.out.println("clientMessage : " + clientMessage);

            dbconn db = new dbconn();
            String managementSQL = "INSERT INTO `management` (`day_Sales`, `month_Sales`, `All_Sales`) VALUES ('0', '0', '0')";
            db.Connect(SQL);
            System.out.println("Vending Machine");
            db.Connect(managementSQL);

            dataOutputStream.writeUTF("자판기 초기화 완료");
            System.out.println("자판기 초기화 완료");
            dataOutputStream.flush();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (dataOutputStream != null) dataOutputStream.close();
            if (outputStream != null) outputStream.close();
            if (dataInputStream != null) dataInputStream.close();
            if (inputStream != null) inputStream.close();
//            mainCommunication conn = new mainCommunication();
//            conn.waitClientClick();
        }
    }
}
