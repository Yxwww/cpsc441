// Source : http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html
package cpsc441.testing;

import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.LinkedList;
import java.util.*;


public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    protected String requestHeader = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.requestHeader = "";
    }

    public void run() {
        String FILE_TO_SEND = "people.ucalgary.ca\\~mghaderi\\cpsc441\\test\\a.html";

        try{
            byte[] reader = new byte[1024];
            clientSocket.getInputStream().read(reader);
            this.requestHeader = new String(reader);
            System.out.println("Accepted connection : " + this.requestHeader);
        }catch(IOException e){System.out.println(e);}
            //File[] files = new File("people.ucalgary.ca").listFiles();
            //showFiles(files);
            sendFile(FILE_TO_SEND);
            //sendFile("people.ucalgary.ca\\~mghaderi\\cpsc441\\test\\UofC.gif");

    }
//http://stackoverflow.com/questions/3154488/best-way-to-iterate-through-a-directory-in-java
    public void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());
            }
        }
    }
    private void sendFile(String filePath) {

        try {
            File fileToSend = new File(filePath);
            FileInputStream fileInputStream =  new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            OutputStream outputStream = clientSocket.getOutputStream();

            // Writer:
            byte[] writerBuffer = new byte[(int) fileToSend.length()];
            bufferedInputStream.read(writerBuffer, 0, writerBuffer.length);

            System.out.println("Sending " + filePath + "(" + writerBuffer.length + " bytes)");
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
            //System.out.println("Writing: "+new String(writerBuffer));
            outputStream.write(writerBuffer, 0, writerBuffer.length);
            outputStream.flush();

            System.out.println("Done.");

            //inputStream.close();
            fileInputStream.close();
            bufferedInputStream.close();
            outputStream.close();
        }
        catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}