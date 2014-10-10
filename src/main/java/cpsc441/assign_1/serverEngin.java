package cpsc441.assign_1;


/**
 * Created by Yx on 10/10/2014.
 */
public class serverEngin {
    public static void main(String[] args){
        WebServer server = new WebServer(3000);
        new Thread(server).start();

        try {
            Thread.sleep(120000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}
