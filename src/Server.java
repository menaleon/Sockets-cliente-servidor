import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        Connection input = new Connection();
    }
}

class Connection {
    public Connection() {
        ServerSocket serverSocket;
        Socket entrySocket, sendDestinatary;
        String value, weight, perc, ip;
        ObjectInputStream dataReceived;
        ObjectOutputStream dataReSent;
        TextField.DataPack received;
        Thread listen;

        received = new TextField.DataPack();

        try {
            serverSocket = new ServerSocket(9090);
            while(true){ // constantly receives packed data

                // From client 1 to server
                entrySocket = serverSocket.accept();
                System.out.println("You are now connected");

                dataReceived = new ObjectInputStream(entrySocket.getInputStream());
                received = (TextField.DataPack) dataReceived.readObject(); // Converting types of objects

                value = received.getValue();
                weight = received.getWeight();
                perc = received.getPerc();
                ip = received.getIp(); // ip of the person we want to send the msg to

                System.out.println(value + " " + weight + " " + perc + " " + ip);

                // From server to client 2
                sendDestinatary = new Socket(ip, 8080); // sends to a third computer (client 2)
                dataReSent = new ObjectOutputStream(sendDestinatary.getOutputStream());
                dataReSent.writeObject(received);
                dataReSent.close();

                sendDestinatary.close();
                entrySocket.close();
                System.out.println("You are now disconnected");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception while sending to Client 2 from Server");
            e.printStackTrace();
        }
    }
}
