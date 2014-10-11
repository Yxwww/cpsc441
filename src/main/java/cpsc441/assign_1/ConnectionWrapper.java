package cpsc441.assign_1;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.*;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Yx on 9/30/2014.
 */


public class ConnectionWrapper {
    public URL url;
    public String URLDirectory;
    public String responseHeader;
    public ConnectionContent content;
    //public String dir;
    private Socket socket;

    // Constructor to create a HTMLsocket Object
    public ConnectionWrapper(String aURL){
        System.out.println("*********************************************");
        String validatedURL = validateURL(aURL.trim());
        this.URLDirectory = "";
        try{
            //System.out.println(validatedURL);
            this.url   = new URL(validatedURL);
            System.out.println(this.url.toString());
            this.socket = new Socket(this.url.getHost(),this.url.getPort());
            System.out.println("Initialized a socket... Wait for connection..\n\n");
            parseDir();
            downloadContent();      // get the file through socket
        }catch(UnknownHostException e){
            System.out.println("Unknown host exception thrown for: "+this.url.getHost());
            System.exit(0);
        }catch(IOException e){
            System.out.println("IO Exception Thrown: "+ e);
            System.exit(0);
        }
    }
    //Method validates the URL
    public String validateURL(String aURL){
        if(aURL.indexOf("https://")==-1&&aURL.indexOf("http://")==-1){
            //System.out.println("URL doesn't contain protocol... appending 'http://' to URL");
            String correctedURL = "http://"+aURL;
            return validateURL(correctedURL);
        }else if(aURL.indexOf(":",7)==-1){
            //System.out.println("URL doesn't contain port number ... appending default 80");
            //System.out.println(aURL);
            int afterHost = aURL.indexOf('/',7);
            String correctedURL;
            if(afterHost!=-1){
                correctedURL = aURL.substring(0, afterHost) + ":80" + aURL.substring(afterHost, aURL.length());
            }else{
                correctedURL = aURL+":80";
            }
            return validateURL(correctedURL);
        }else{
            //System.out.println("URL correct , returning ..." + aURL);
            aURL = aURL.replaceFirst("^\uFFFE", "");
            return aURL;
        }
    }
    /*
    * Summary: function that creates a socket establish the connection and grab the page
    * Param: dir - String   the file directory ...
    * */
    public void downloadContent() {
        try {
            OutputStream outputStream = this.socket.getOutputStream();
            InputStream inputStream = this.socket.getInputStream();
            // Encoding the search term
            String searchTerm = URLEncoder.encode("Hello World", "utf-8");

            // Working with reader/writers is easier when dealing with strings
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            // Send the HTTP request
            //writer.append("GET /search?q=" + searchTerm);
            writer.append("GET " + this.url.getFile() + " HTTP/1.0\r\n\r\n");
            writer.flush();

            byte[] buffer = new byte[1024];
            String header = "";
            LinkedList<byte[]> wholeData = new LinkedList<byte[]>();
            int length = 0;
            int totalLength = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                if ((header).indexOf("\r\n\r\n") == -1) {
                    //System.out.println(length);
                    header += new String(Arrays.copyOfRange(buffer,0,length), "UTF-8");
                } else {
                    wholeData.push(Arrays.copyOfRange(buffer, 0, length));
                    totalLength += length;
                }
            }
            //saving data to associated object
            byte[] finalData = new byte[totalLength];
            int totalLengthCounter = 0;
            for (int i = wholeData.size() - 1; i >= 0; i--) {
                for (int j = 0; j < wholeData.get(i).length; j++, totalLengthCounter++) {
                    finalData[totalLengthCounter] = wholeData.get(i)[j];
                    //System.out.println("\t"+j);
                }
                //System.out.println(i + " :: "+totalLengthCounter);
            }

            this.content = new ConnectionContent(header, finalData);
            writer.close();
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
                System.out.println("IO Exception Thrown: "+ e);
        }
    }// end of download
    public void saveFile(){
            if(this.content.header.contains("200")){
                this.content.saveToPath(this.url.getHost()+this.url.getFile());
            }else if(this.content.header.contains("404")){
                this.content.saveToPath(this.url.getHost()+this.url.getFile());
                System.out.println("page 404, I don't want to save !");
            }
    }

    // Scan all the links and downlaod them.
    public void scan_download(){
        if(this.content.contentType.contains("html")){
            // make sure links are detecteds
            if(this.content.links.size()<=0){
                // scan
                System.out.println(" detecting !");
                this.content.detectLinks();
            }
            // download each links
            List<ConnectionWrapper> newSockets = new LinkedList<ConnectionWrapper>();
            int socketCounter =0;
            for (String aLink: this.content.links){
                if(aLink.contains("http:") || aLink.contains("https:")){
                    //TODO: add external link handler
                }else{
                    //if the link is hosted by current host
                    //System.out.println(aLink);
                    String completeLink = this.url.getHost()+this.URLDirectory+aLink;
                   //System.out.println(completeLink);
                    // create a new socket
                    newSockets.add(new ConnectionWrapper(completeLink));
                    //System.out.println("inner new socket-> \n"+newSockets.get(socketCounter).url.toString());
                    newSockets.get(socketCounter).saveFile();
                    socketCounter++;// inc coutner
                }
            }
            System.out.println("Generated "+newSockets.size() +" new sockets ");

        }else{
            System.out.println(this.content.contentType+" doesn't support scan_download");
        }
    }


    // helpers
    /*private void linkHandler(){

    }*/

    public void parseDir(){
        String[] splitted = this.url.getPath().split("/");
        //System.out.println(this.url.getPath());
        //System.out.println( splitted[ splitted.length-1]);
        String directory = "";
        for(int i = 0; i< splitted.length-1;i++){
            directory+= splitted[i]+"/";
        }
        this.URLDirectory = directory;
        //System.out.println(this.URLDirectory);
    }
}
