package AYGO.demo;

import org.jgroups.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleClass implements Receiver {
    JChannel channel; //Aqui se creo default pero se puede pasar un archivo de configuracion XML new JChannel("/home/bela/udp.xml")
    String user_name=System.getProperty("user.name", "n/a");

    public static void main(String[] args) throws Exception {
        new SimpleClass().start();
    }
    public SimpleClass(){

    }

    private void start() throws Exception {
        channel=new JChannel().setReceiver(this).connect("ChatCluster");
        eventLoop();
        channel.close();
    }
    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit"))
                    break;
                line="[" + user_name + "] " + line;
                Message msg=new ObjectMessage(null, line);
                channel.send(msg);
            }
            catch(Exception e) {
            }
        }
    }
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }
    public void receive(Message msg) {
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }

}
