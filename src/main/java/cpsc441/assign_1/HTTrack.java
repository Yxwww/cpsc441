package cpsc441.assign_1;

import javax.swing.text.html.HTML;

/**
 * Created by Yx on 9/30/2014.
 */
public class HTTrack {
    static final String CRLF = "\r\n";
    public static void main(String[] args){
        String aURL = "people.ucalgary.ca/~mghaderi/cpsc441/test/index.html";
        ConnectionWrapper socket_1 = new ConnectionWrapper(aURL);
        socket_1.saveFile();
        socket_1.scan_download();
        //System.out.println("All the links: " + HTMLsocket.content.links.toString());
        //HTMLsocket.printContent();

        /*ConnectionWrapper aSocket = new ConnectionWrapper("http://people.ucalgary.ca/~mghaderi/cpsc441/test/UofC.gif");
        //aSocket.printContent();
        //aSocket.content.printBuffer();
        aSocket.saveFile();
        */
        System.out.println("HTTrack shutingdown... ");
    }
}
