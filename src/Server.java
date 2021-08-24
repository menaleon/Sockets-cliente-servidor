import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        Connection input = new Connection();
    }
}

class Connection{ //implements Runnable{
    public Connection() {
        ServerSocket serverSocket;
        Socket entrySocket;
        String nick, value, weight, perc, ip;
        TextField.DataPack received;

        received = new TextField.DataPack();
        try {
            serverSocket = new ServerSocket(8080);
            entrySocket = serverSocket.accept();
            System.out.println("You are now connected");

            ObjectInputStream dataReceived = new ObjectInputStream(entrySocket.getInputStream());
            received = (TextField.DataPack) dataReceived.readObject();

            nick = received.getNick();
            value = received.getValue();
            weight = received.getWeight();
            perc = received.getPerc();
            ip = received.getIp();

            System.out.println(nick + " " + value + " " + weight + " " + perc + " " + ip);

            entrySocket.close();
            System.out.println("You are now disconnected");


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
