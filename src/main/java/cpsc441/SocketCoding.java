package cpsc441;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by keynan on 18-Sep-14.
 */
public class SocketCoding {
    public static ExecutorService serverThread = Executors.newSingleThreadExecutor();

    public static void main(String [] args) throws Exception {

        ExecutorService clientHandler = Executors.newFixedThreadPool(4);
        // bind to the default address
        ServerSocket server = new ServerSocket(8080);

        Socket client = server.accept();
        clientHandler.submit(new Request(client));
    }


}

// http://blog.netherlabs.nl/articles/2009/01/18/the-ultimate-so_linger-page-or-why-is-my-tcp-not-reliable
class Request implements Runnable {

    final Socket client;

    public Request(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // by default Java blocks forever. Let's fix that.
            client.setSoTimeout((int)1e4);

            //send a heart-beat
            client.setKeepAlive(true);

            client.shutdownOutput();
            //hang around for a while
            //  until we're sure the otherside is done talking
            //client.setSoLinger(true, 10000);

            client.shutdownInput();

            client.close();
        } catch(IOException e) {
            //
        }
    }
}