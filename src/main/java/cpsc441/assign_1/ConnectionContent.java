package cpsc441.assign_1;

import java.sql.Connection;
import java.util.*;
/**
 * Created by Yx on 10/1/2014.
 */
public class ConnectionContent {
    public String html;
    public byte[] buffer;// BUFFERSIZE
    public String header;
    //public String[] headerArray;
    public String data;
    public String contentType;
    public List<String> links = new LinkedList<String>();
    public int contentSize;
   /* public ConnectionContent(int size){
        System.out.println("in the content buffer size->"+size);
        this.html="";
        this.buffer = new byte[size];
    }*/
    /*
    * Constructor initialize connection content and data
    * */
    public ConnectionContent(String header, String data){
        this.header = header;
        parseHeader();
        this.data = data;
    }
    private void detectLinks(String content){
        String contentDUP = content;
        while(contentDUP.contains("href=\"")){
            int start = contentDUP.indexOf("href=\"")+6;
            int end = contentDUP.indexOf("\"",start);
            //System.out.println(start+"-"+end+" < within "+ contentDUP.length());
            links.add(contentDUP.substring(start,end));
            contentDUP = contentDUP.substring(end,contentDUP.length());
        }

    }
    // setter for HTML content and detects all the links in the content
    public void setContent(String content){
        this.html = content;
        if(!this.links.isEmpty()){
            this.links.clear();
        }
        detectLinks(this.html);          // detect the links from the HTML content.
    }
    ///
    public String bufferToString(){
        return new String(this.buffer);
    }
    public void printBuffer(){
        System.out.println(new String(this.buffer));
    }

    public void parseHeader(){
        String lines[] = this.header.split("\\r?\\n");
        //this.headerArray = new String[lines.length];
        for(int i=0;i<lines.length;i++){
            System.out.println(lines[i]);
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
        System.out.println("Parsing content type: "+ this.contentType);
    }
    public void contentSizeParser(String contentSize){
        this.contentSize = Integer.parseInt(contentSize.trim());
        System.out.println("Parsing content size: "+ this.contentSize);
    }

}
