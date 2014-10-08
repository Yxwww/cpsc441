package cpsc441.assign_1;

import java.sql.Connection;
import java.util.*;
/**
 * Created by Yx on 10/1/2014.
 */
public class ConnectionContent {
    public String html;
    public byte[] buffer;// BUFFERSIZE
    public List<String> links = new LinkedList<String>();
    public ConnectionContent(int size){
        System.out.println("in the content buffer size->"+size);
        this.html="";
        this.buffer = new byte[size];
    }
    public ConnectionContent(String content){
        this.html = content;
    }
    public ConnectionContent(byte[] buffer){this.buffer = buffer;}
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

}
