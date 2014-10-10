package cpsc441.testing;

/**
 * Created by YX on 10/10/2014.
 */
public class engin {
    public static void main(String[] args){
        ThreadPooledServer server = new ThreadPooledServer(9000);
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
