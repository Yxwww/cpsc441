package cpsc441.legacy;

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
 * A simple HTTP client that connects to the google server and searches for a
 * term. It prints the result to the System.out.
 * 
 * @author <a href="mailto:mmollano@ucalgary.ca">Mohsen Mollanoori</a>
 */
public class TCPClient {
	// Carriage return + line feed
	static final String CRLF = "\r\n";

	public static void main(String[] args) throws UnknownHostException, IOException {
		// Client socket, connects to port 80 of the google server. This is the
		// default HTTP server port.
		Socket socket = new Socket("people.ucalgary.ca", 80);

		// Input & output from/to the server
		OutputStream outputStream = socket.getOutputStream();
		InputStream inputStream = socket.getInputStream();

		// Encoding the search term
		String searchTerm = URLEncoder.encode("Hello World", "utf-8");
		
		// Working with reader/writers is easier when dealing with strings
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		
		// Send the HTTP request
		//writer.append("GET /search?q=" + searchTerm);
        writer.append("GET /~mghaderi/cpsc441/test/index.html");
		// We need 2 CRLFs at the end of a HTTP request
		writer.append(CRLF);
		writer.append(CRLF);
		writer.flush();

		// Read the response & print it to the output
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		reader.close();
		writer.close();

        System.out.println("\n\n______________________________________________");
        //listenSocket();
	}


    public static void listenSocket(){
        //Create socket connection
        try{
            Socket socket = new Socket("google.ca", 80);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            System.out.print("Echoing:\t"+in.readLine()+CRLF);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: google.ca");
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

}
