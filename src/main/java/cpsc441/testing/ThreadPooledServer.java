// Source: http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html

package cpsc441.testing;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThreadPooledServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(256);

    public ThreadPooledServer(int port){
        this.serverPort = port;
        System.out.println("Initialize server on port: "+ this.serverPort);
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
                    new WorkerRunnable(clientSocket,
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