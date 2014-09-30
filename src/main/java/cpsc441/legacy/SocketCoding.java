package cpsc441.legacy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.*;

/**
 * Created by keynan on 18-Sep-14.
 */
public class SocketCoding {
    public static ExecutorService serverThread = Executors.newSingleThreadExecutor();

    public static void main(String [] args) throws Exception {
        final CyclicBarrier cb = new CyclicBarrier(2);          // uhhhhh ?
		final AtomicBoolean flag = new AtomicBoolean(true);
		serverThread.submit(new Runnable() {
			ExecutorService clientHandler = Executors.newFixedThreadPool(4);
			public void run(){
				try {
					// bind to the default address
					ServerSocket server = new ServerSocket(8080);
					server.setSoTimeout((int)1e4);
                    //cb.await();
					while(flag.get()) {
						Socket client = server.accept();
						clientHandler.submit(new Request(client));
					}
				}catch(IOException e){/* OH NOOOOs! */
                    System.out.println("IOException: "+e);
                    clientHandler.shutdown();
                }/*catch(BrokenBarrierException e){
                    System.out.println("broken barrier: "+e);
                }catch(InterruptedException e){
                    System.out.println("Innterrupted Exception : " +e);
                    clientHandler.shutdown();
                }*/
			}
		});

        //cb.await();
		Socket server = new Socket("localhost", 8080);
		try {
			new PrintStream(server.getOutputStream()).println("Hello world!");
			server.shutdownOutput();
			BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream()));
			System.out.println(br.readLine());
			int eof = server.getInputStream().read();
			if(eof != -1) {
			  System.out.println("Darn, something went wrong");
			}
		} finally { 
			server.close();
			while(! flag.compareAndSet(flag.get(), false) );
			System.gc();
		}
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

			// echo until the client sends eof (ie. server.shutdownOutput(); )
			int bite;
			while((bite = client.getInputStream().read()) >= 0){
				client.getOutputStream().write(bite);
			}
			
			if(bite < 0) {
				// got end of file
			}
			
            client.shutdownOutput();
            //hang around for a while
            //  until we're sure the otherside is done talking
            //client.setSoLinger(true, 10000);

            client.shutdownInput();


        } catch(IOException e) {
            //what happend? Socket error? Time-out? 
			
        } finally {
			try {
				//We'd rather the client does this
				// only do it if forced by error / client unresponsive
				client.close();
			}catch(IOException e){}
		}
    }
}