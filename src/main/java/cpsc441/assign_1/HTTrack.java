package cpsc441.assign_1;

import javax.swing.text.html.HTML;

/**
 * Created by Yx on 9/30/2014.
 */
public class HTTrack {
    static final String CRLF = "\r\n";
    public static void main(String[] args){
        if(args.length==0) {
            //System.out.println("command line input: "+args[0]);
            //String aURL = args[0];
            ConnectionWrapper socket_1 = new ConnectionWrapper("http://pages.cpsc.ucalgary.ca/~nziragch/test/index.html");
            System.out.println(socket_1.content.header);
            socket_1.saveFile();
            socket_1.scan_download();

            ConnectionWrapper socket_2 = new ConnectionWrapper("http://people.ucalgary.ca/~mghaderi/index.html");
            System.out.println(socket_2.content.header);
            socket_2.saveFile();
            socket_2.scan_download();

            ConnectionWrapper socket_3 = new ConnectionWrapper("http://people.ucalgary.ca/~mghaderi/cpsc441/test/index.html");
            System.out.println(socket_3.content.header);
            socket_3.saveFile();
            socket_3.scan_download();

            System.out.println("HTTrack shutingdown... ");
        }  else{
                System.out.println("command empty");
        }
    }
}
