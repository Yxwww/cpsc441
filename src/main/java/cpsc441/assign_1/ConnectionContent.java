package cpsc441.assign_1;

import java.io.*;
import java.sql.Connection;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import sun.misc.BASE64Decoder;

/**
 * Created by Yx on 10/1/2014.
 */
public class ConnectionContent {
    public String html;
    public byte[] buffer;// BUFFERSIZE
    public String header;
    //public String[] headerArray;
    public byte[] data;
    public String contentType;
    public int contentSize;
    public List<String> links = new LinkedList<String>();
   /* public ConnectionContent(int size){
        System.out.println("in the content buffer size->"+size);
        this.html="";
        this.buffer = new byte[size];
    }*/
    /*
    * Constructor initialize connection content and data
    * */
    public ConnectionContent(String header, byte[] data){
        this.header = header;
        parseHeader();
        this.data = data;
        if(this.contentType.contains("html")){
            detectLinks();
        }

    }
    public void detectLinks(){
        if(this.contentType.contains("html")){
            String contentDUP = dataToString();
            while(contentDUP.contains("href=\"")){
                int start = contentDUP.indexOf("href=\"")+6;
                int end = contentDUP.indexOf("\"",start);
                //System.out.println(start+"-"+end+" < within "+ contentDUP.length());
                links.add(contentDUP.substring(start,end));
                contentDUP = contentDUP.substring(end,contentDUP.length());
            }
        }
    }
    // convert byte array data to String
    public String dataToString(){
        try{
            String dataToString = new String(this.data, "UTF-8");
            return dataToString;
        }catch (UnsupportedEncodingException e){
            System.out.println("Unable to save the link");
            return null;
        }

    }

    public void parseHeader(){
        String lines[] = this.header.split("\\r?\\n");
        //this.headerArray = new String[lines.length];
        for(int i=0;i<lines.length;i++){
            String currentLine = lines[i];
            String[] headerContent = currentLine.split(":");
            //System.out.println(Arrays.toString(headerContent));
            if(headerContent[0].trim().equalsIgnoreCase("Content-Type")){
                contentTypeParser(headerContent[1]);
            }else if(headerContent[0].trim().equalsIgnoreCase("Content-Length")){
                contentSizeParser(headerContent[1]);
            }
        }

    }
    public void contentTypeParser(String contentType){
        this.contentType = contentType.trim();
        //System.out.println("Parsing content type: "+ this.contentType);
    }
    public void contentSizeParser(String contentSize){
        this.contentSize = Integer.parseInt(contentSize.trim());
        //System.out.println("Parsing content size: "+ this.contentSize);
    }
    public void saveToPath(String path){
        try{
            System.out.println("-> Saving to "+path);
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileOutputStream out=new FileOutputStream(file);
            if(file.exists()){
                System.out.println("File exits, deleting ...");
                file.delete();
            }
            //System.out.println("data length: "+this.data.length);
            out.write(this.data,0,this.contentSize);
            out.close();

        }catch(IOException e){
            System.out.println(e);
        }
    }

    /*public static byte[] decodeImage(String imageDataString) {
        return Convert.FromBase64String(imageDataString);
    }*/
}
