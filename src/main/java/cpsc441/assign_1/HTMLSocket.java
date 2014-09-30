package cpsc441.assign_1;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.*;
/**
 * Created by Yx on 9/30/2014.
 */


public class HTMLSocket {
    public URL url;
    public String HTMLcontent;

    //public String dir;
    private Socket socket;

    // Constructor to create a HTMLsocket Object
    public HTMLSocket(String aURL){
        String validatedURL = validateURL(aURL.trim());
        try{
            System.out.println(validatedURL);
            this.url   = new URL(validatedURL);
            System.out.println(this.url.toString());
            this.socket = new Socket(this.url.getHost(),this.url.getPort());

            System.out.println("Initialized a socket... Wait for connection..\n\n");
        }catch(UnknownHostException e){
            System.out.println("Unknown host exception thrown for: "+this.url.getHost());
            System.exit(0);
        }catch(IOException e){
            System.out.println("IO Exception Thrown: "+ e);
            System.exit(0);
        }
    }
    //Method that validate the URL
    public String validateURL(String aURL){
        if(aURL.indexOf("https://")==-1&&aURL.indexOf("http://")==-1){
            System.out.println("URL doesn't contain protocol... appending 'http://' to URL");
            String correctedURL = "http://"+aURL;
            return validateURL(correctedURL);
        }else if(aURL.indexOf(":",7)==-1){
            System.out.println("URL doesn't contain port number ... appending default 80");
            System.out.println(aURL);
            int afterHost = aURL.indexOf('/',7);
            String correctedURL = aURL.substring(0, afterHost) + ":80" + aURL.substring(afterHost, aURL.length());
            return validateURL(correctedURL);
        }else{
            System.out.println("URL correct , returning ..." + aURL);
            aURL = aURL.replaceFirst("^\uFFFE", "");
            return aURL;
        }
    }
    /*
    * Summary: function that creates a socket establish the connection and grab the page
    * Param: dir - String   the file directory ...
    * */
    public void getFile(){

        try {
            // Input & output from/to the server
            OutputStream outputStream = this.socket.getOutputStream();
            InputStream inputStream = this.socket.getInputStream();

            // Encoding the search term
            String searchTerm = URLEncoder.encode("Hello World", "utf-8");

            // Working with reader/writers is easier when dealing with strings
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            // Send the HTTP request
            //writer.append("GET /search?q=" + searchTerm);
            writer.append("GET "+this.url.getFile()+" HTTP/1.0\r\n\r\n");
            writer.flush();

            // Read the response & print it to the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
            writer.close();

            System.out.println("______________________________________________");
        }catch(IOException e){
            System.out.println("IO Exception Thrown: "+ e);
        }
    }
}
