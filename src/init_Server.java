import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class init_Server {
    // 자판기 초기화 코드 받을 준비
    ServerSocket serverSocket = null;
    Socket socket = null;

    OutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;

    InputStream inputStream = null;
    DataInputStream  dataInputStream = null;

    public void initServer(){
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
            System.out.println("clientMessage : " + clientMessage);

            dataOutputStream.writeUTF("자판기 초기화 완료");
            dataOutputStream.flush();

            clientMessage.equals("stop");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
