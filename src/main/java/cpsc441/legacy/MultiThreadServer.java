package cpsc441.legacy;

import java.io.*;
import java.net.*;

public class MultiThreadServer implements Runnable {
  Socket csocket;

  MultiThreadServer(Socket csocket) {
    this.csocket = csocket;
  }

  public static void main_(String args[]) throws Exception {
    ServerSocket ssock = new ServerSocket(1234);
    System.out.println("Listening");
    while (true) {
      Socket sock = ssock.accept();
      System.out.println("Connected");
      new Thread(new MultiThreadServer(sock)).start();
    }
  }

  public void run() {
    try {
      PrintStream pstream = new PrintStream(csocket.getOutputStream());
      for (int i = 100; i >= 0; i--) {
        pstream.println(i + " counts");
      }
      pstream.close();
      csocket.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}

