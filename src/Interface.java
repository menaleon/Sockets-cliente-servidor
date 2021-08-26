import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.net.Socket;

/**
 * Execute the client GUI by instantiating a window. Zero parameters and returns
 */
public class Interface { // creates a window
    private static Window win;

    /**
     * Initialize the GUI. Zero returns, one parameter
     * @param args An array of string arguments
     */
    public static void main(String[] args){
        win = new Window();
        win.setVisible(true);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * Defines characteristics of the GUI´s window and instantiates a TextField class
 */
class Window extends JFrame { // defines characteristics of the window
    private TextField textField;

    /**
     * Constructor of the window class. Set the boundaries of the window and adds a TextField instance
     */
    public Window(){
        setBounds(400, 200, 500, 300);
        textField = new TextField();
        add(textField);
    }
}

/**
 * Create text fields to type on, and a text area to read messages. Implements Runnable from the Thread class to receive msgs
 */
class TextField extends JPanel implements Runnable{ // text fields to type on and read. THREAD used to receive msgs in parallel
    private JTextField valueTxt, weightTxt, percTxt, ipTxt;
    private JTextArea areaTxt;
    private JButton sendBtn;
    private SendText eventBtn;
    private Thread receive;

    /**
     * Constructor of TextField class. Creates fields and area to write/read, the send button, the mouse-type event and starts a thread
     */
    public TextField(){

        valueTxt = new JTextField("Value",5);
        add(valueTxt);

        weightTxt = new JTextField("Weight",5);
        add(weightTxt);

        percTxt = new JTextField("Percentage",10);
        add(percTxt);

        ipTxt = new JTextField("Client 2 IP",13);
        add(ipTxt);

        eventBtn = new SendText();

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(eventBtn);
        add(sendBtn);

        areaTxt = new JTextArea();
        add(areaTxt);

        receive = new Thread(this); // THREAD used to receive msgs in parallel, while typing values on the text fields
        receive.start();
    }

    @Override
    public void run() { // TO RECEIVE MSGS IN PARALLEL
        ServerSocket client2Server;
        Socket client2Socket;
        DataPack receivedPack;
        ObjectInputStream inputFlux;
        double monto;
        int valor, peso;
        float porc;

        try {
            client2Server = new ServerSocket(8080);
            receivedPack = new DataPack();

            while (true){
                client2Socket = client2Server.accept();
                inputFlux = new ObjectInputStream(client2Socket.getInputStream());
                receivedPack = (DataPack) inputFlux.readObject(); // reads data received

                valor = Integer.parseInt(receivedPack.getValue());
                peso = Integer.parseInt(receivedPack.getWeight());
                porc = Float.parseFloat(receivedPack.getPerc());

                monto = (valor * porc / 100) + (peso * 0.15);
                areaTxt.append(String.format("\n%s", monto));
            }
        }catch (Exception e){
            System.out.println("Exception in client. Make sure you type the numbers correctly :)");
            e.printStackTrace();
        }
    }

    /**
     * Sends messages to server by clicking the send button. This class is inside the TextField class
     */
    class SendText implements ActionListener{ // send button: SENDS TO SERVER
        Socket clientSocket;
        DataPack data;
        ObjectOutputStream dataPack;

        @Override
        public void actionPerformed(ActionEvent e) {
            try{    // sends to a computer located on IP: 192.168.0.6 (that is my IP)
                clientSocket = new Socket("192.168.0.6", 9090); // my IP and a random port available
                data = new DataPack();

                // pack the numbers into an object
                data.setValue(valueTxt.getText());
                data.setWeight(weightTxt.getText());
                data.setPerc(percTxt.getText());
                data.setIp(ipTxt.getText());

                // place the object that contains the data in the flux of data
                dataPack = new ObjectOutputStream(clientSocket.getOutputStream());
                dataPack.writeObject(data);
                clientSocket.close();

            }catch(UnknownHostException e1){
                System.out.println("Unkwown Host EXCEPTION");
                e1.printStackTrace();

            }catch(IOException e1){
                System.out.println("IOEXCEPTION");
                e1.printStackTrace();
            }
        }
    }

    /**
     * Packs data written in the text fields to send them to server. It serializes data pack
     */
    static class DataPack implements Serializable { // object containing data we type. "Serialize" = convert data into binary
        private String value, weight, perc, ip;      // so it can be sent and re-convert into readable data when it gets to server

        /**
         * Sets the value of the attribute "value"
         * @param value int value
         */
        public void setValue(String value) {this.value = value;}

        /**
         * Sets the value of the attribute "weight"
         * @param weight int weight of the product
         */
        public void setWeight(String weight) {this.weight = weight;}

        /**
         * Sets the value of the attribute "perc" (percentage)
         * @param perc float Percentage
         */
        public void setPerc(String perc) {this.perc = perc;}

        /**
         * Sets the value of the attribute "ip"
         * @param ip this ip is from the person who will receive the message
         */
        public void setIp(String ip) {this.ip = ip;}

        /**
         * Gets the value of the attribute "value"
         * @return value
         */
        public String getValue() {return value;}

        /**
         * Gets the value of the attribute "weight"
         * @return weight
         */
        public String getWeight() {return weight;}

        /**
         * Gets the value of the attribute "perc" (percentage)
         * @return perc
         */
        public String getPerc() {return perc;}

        /**
         * Gets the value of the attribute "ip" (this ip is from the person who will receive the message)
         * @return ip
         */
        public String getIp() {return ip;}
    }
}