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
            System.out.println("Client Connect Ready");

            socket = serverSocket.accept();
            System.out.println("Client Connect OK");
            System.out.println("socket : " + socket);

            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            String clientMessage = dataInputStream.readUTF();
            SQL = clientMessage;
            System.out.println("clientMessage : " + clientMessage);

            dataOutputStream.writeUTF("자판기 초기화 완료");
            System.out.println("자판기 초기화 완료");
            dataOutputStream.flush();

            dbconn db = new dbconn();
            db.Connect(SQL);
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
