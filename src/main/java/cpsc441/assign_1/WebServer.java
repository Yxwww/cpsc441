package cpsc441.assign_1;

/**
 * Created by YX on 10/10/2014.
 * Web server powered by pool threading. (learned from : Source: http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html)
 */
import cpsc441.testing.WorkerRunnable;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(256);

    public WebServer(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        serverStarts();
        System.out.println("Server starts at port: "+this.serverPort);
        while(!this.isStopped()){
            //creates clients socket
            Socket clientSocket = null;
            // creates IO
            try {
                // accept client socket
                clientSocket = this.serverSocket.accept();
                //inputStream.close();

            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            // handles creatd... for each client
            this.threadPool.execute(
                    new clientHandler(clientSocket,
                            "Thread Pooled Server " + this.threadPool));

        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void serverStarts() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Can not open server on port :"+this.serverPort, e);
        }
    }
}