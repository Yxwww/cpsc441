package cpsc441.assign_1;

import java.net.*;
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

/**
 * Created by Yx on 9/30/2014.
 */
public class HTTrack {
    static final String CRLF = "\r\n";
    public static void main(String[] args){
        String aURL = "people.ucalgary.ca/~mghaderi/cpsc441/test/index.html";
        HTMLSocket socket = new HTMLSocket(aURL);
        socket.getFile();

        System.out.println("HTTrack shutingdown... ");
    }
}
