package cpsc441.assign_1;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.*;
import java.io.UnsupportedEncodingException;
/**
 * Created by Yx on 9/30/2014.
 */


public class ConnectionWrapper {
    public URL url;
    public String responseHeader;
    public ConnectionContent content;
    //public String dir;
    private Socket socket;

    // Constructor to create a HTMLsocket Object
    public ConnectionWrapper(String aURL){
        String validatedURL = validateURL(aURL.trim());
        this.responseHeader  = "";
        try{
            System.out.println(validatedURL);
            this.url   = new URL(validatedURL);
            System.out.println(this.url.toString());
            this.socket = new Socket(this.url.getHost(),this.url.getPort());
            System.out.println("Initialized a socket... Wait for connection..\n\n");
            getFile();      // get the file through socket
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
            System.out.println("URL doesn't contain protocol... appending 'http://' to URL");
            String correctedURL = "http://"+aURL;
            return validateURL(correctedURL);
        }else if(aURL.indexOf(":",7)==-1){
            System.out.println("URL doesn't contain port number ... appending default 80");
            System.out.println(aURL);
            int afterHost = aURL.indexOf('/',7);
            String correctedURL;
            if(afterHost!=-1){
                correctedURL = aURL.substring(0, afterHost) + ":80" + aURL.substring(afterHost, aURL.length());
            }else{
                correctedURL = aURL+":80";
            }
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


            writer.append("GET " + this.url.getFile() + " HTTP/1.0\r\n\r\n");
            writer.flush();

            // Read the response & print it to the output

/*
            long startTime = System.currentTimeMillis();
            int read;
            int totalRead = 0;

            //InputStream clientInputStream = this.socket.getInputStream();
            byte[] buffer = new byte[1048576];
            while ((read = inputStream.read(buffer)) != -1) {
                totalRead += read;
            }
            long endTime = System.currentTimeMillis();
            System.out.println(totalRead + " bytes read in " + (endTime - startTime) + " ms.");
            this.content = new ConnectionContent(totalRead);
            for(int i=0; i<totalRead; i++){
                this.content.buffer[i] = buffer[i];
            }
            //
            String decodedDataUsingUTF8;
            try {
                decodedDataUsingUTF8 = new String(this.content.buffer, "UTF-8");  // Best way to decode using "UTF-8"
                System.out.println("Text Decryted using UTF-8 : " + decodedDataUsingUTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            //System.out.println(new String(this.content.buffer));



            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String header = "";
            String wholeContent = "";
            byte[] buffer = new byte[1048576];
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println("->"+header.indexOf("\n\n"));
                //System.out.println(line);
                if(!header.contains("\n\n")){
                    header += line+"\n";
                }else{
                    wholeContent += line;
                }
            }
            System.out.println("********************************************\n"+ header+wholeContent);
            //this.responseHeader = header;
            this.content = new ConnectionContent(header,wholeContent);
            //this.content = new ConnectionContent();
/*
            if(this.responseHeader.toLowerCase().contains("text/html")){
                if(wholeContent.contains("!DOCTYPE")){
                    //this.responseHeader = wholeContent.substring(0, wholeContent.indexOf("<!DOCTYPE"));
                    //System.out.println(this.responseHeader);
                    this.content.setContent(wholeContent.substring(wholeContent.indexOf("<!DOCTYPE"),wholeContent.length()));
                    System.out.println(this.content.html);
                }else{
                    //this.responseHeader = wholeContent.substring(0, wholeContent.indexOf("<html"));
                    //System.out.println(this.responseHeader);
                    this.content.setContent(wholeContent.substring(wholeContent.indexOf("<html"),wholeContent.length()));
                    //System.out.println(this.content.html);
                }
            }else{

            }
*/
            //this.content.buffer = data;




/*
            DataInputStream in = new DataInputStream(this.socket.getInputStream());
            short myShortStreamSize = in.readShort();
            byte[] payload = new byte[myShortStreamSize];
            in.readFully(payload);
            this.content = new ConnectionContent(myShortStreamSize);
            this.content.buffer = payload;
            System.out.println(this.content.buffer);
*/


/*
            this.responseHeader = wholeContent.substring(0,wholeContent.indexOf("\r\n\r\n")+4);
            System.out.println("responsHeader: \n"+this.responseHeader);
            //System.out.println(wholeContent.substring(0,(wholeContent.toLowerCase().indexOf("\n",wholeContent.toLowerCase().indexOf("content-type:")))).toLowerCase());
           // if(this.)
            if(this.responseHeader.toLowerCase().contains("text/html")){
                if(wholeContent.contains("!DOCTYPE")){
                    //this.responseHeader = wholeContent.substring(0, wholeContent.indexOf("<!DOCTYPE"));
                    //System.out.println(this.responseHeader);
                    this.content.setContent(wholeContent.substring(wholeContent.indexOf("<!DOCTYPE"),wholeContent.length()));
                    System.out.println(this.content.html);
                }else{
                    //this.responseHeader = wholeContent.substring(0, wholeContent.indexOf("<html"));
                    //System.out.println(this.responseHeader);
                    this.content.setContent(wholeContent.substring(wholeContent.indexOf("<html"),wholeContent.length()));
                    System.out.println(this.content.html);
                }
            }else{

            }
*/


            //reader.close();
            writer.close();

            System.out.println("______________________________________________");
        }catch(IOException e){
            System.out.println("IO Exception Thrown: "+ e);
        }
    }
    public void saveFile(){
        try{
            if(this.content.header.contains("200")){
                File file = new File(this.url.getHost()+this.url.getFile());
                file.getParentFile().mkdirs();
                /*
                PrintWriter out = new PrintWriter(fileWriter);
                out.write(this.content.buffer);
                out.close();*/
                //FileWriter fileWriter = new FileWriter(file);

                /*FileOutputStream out=new FileOutputStream(file);
                out.write(this.content.buffer);
                out.close();*/
                OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                out.write(this.content.buffer);
                out.close();
                System.out.println("-> Saved to "+this.url.getFile());
            }else{
                System.out.println("page 404, I don't want to save !");
            }
        }catch(IOException e){
            System.out.print("IO exception thrown: "+ e);
        }
    }
    public void printContent(){
        System.out.println("Printing HTML content: ");
        System.out.println(this.content.buffer);
    }
}
