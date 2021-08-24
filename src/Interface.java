import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.net.Socket;

public class Interface {
    private static Window win;
    public static void main(String[] args){
        win = new Window();
        win.setVisible(true);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class Window extends JFrame {
    private TextField textField;
    public Window(){
        setBounds(300, 200, 500, 300);
        textField = new TextField();
        add(textField);
    }
}

class TextField extends JPanel{
    private JTextField nickTxt, valueTxt, weightTxt, percTxt, ipTxt;
    private JButton sendBtn;
    private SendText eventBtn;

    public TextField(){
        nickTxt = new JTextField("Nickname", 10);
        add(nickTxt);

        valueTxt = new JTextField("Value",5);
        add(valueTxt);

        weightTxt = new JTextField("Weight",5);
        add(weightTxt);

        percTxt = new JTextField("%",5);
        add(percTxt);

        ipTxt = new JTextField("Destinatary IP",15);
        add(ipTxt);

        eventBtn = new SendText();

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(eventBtn);
        add(sendBtn);
    }

    class SendText implements ActionListener{
        Socket clientSocket;
        DataPack data;
        ObjectOutputStream dataPack;
        @Override
        public void actionPerformed(ActionEvent e) {
            try{ // 192.168.0.6
                clientSocket = new Socket("192.168.0.6", 8080);
                data = new DataPack();

                data.setNick(nickTxt.getText());
                data.setValue(valueTxt.getText());
                data.setWeight(weightTxt.getText());
                data.setPerc(percTxt.getText());
                data.setIp(ipTxt.getText());

                dataPack = new ObjectOutputStream(clientSocket.getOutputStream());
                dataPack.writeObject(data);
                clientSocket.close();

            }catch(UnknownHostException e1){
                e1.printStackTrace();
            }catch(IOException e1){
                System.out.println(e1.getMessage());
            }
        }
    }
    static class DataPack implements Serializable {
        private String nick, value, weight, perc, ip;

        public void setNick(String nick) {this.nick = nick;}
        public void setValue(String value) {this.value = value;}
        public void setWeight(String weight) {this.weight = weight;}
        public void setPerc(String perc) {this.perc = perc;}
        public void setIp(String ip) {this.ip = ip;}

        public String getNick() {return nick;}
        public String getValue() {return value;}
        public String getWeight() {return weight;}
        public String getPerc() {return perc;}
        public String getIp() {return ip;}
    }
}