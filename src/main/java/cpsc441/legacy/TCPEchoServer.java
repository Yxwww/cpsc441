package cpsc441.legacy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is a simple, multi-threaded TCP echo server. It listens to the port
 * specified by the <code>SERVER_PORT</code> to serve the clients. It echos the
 * bytes received from the client till it sees the sequence of bytes specified
 * by the <code>QUIT_COMMAND</code>. If it receive the <code>QUIT_COMMAND</code>
 * it writes a goodbye string to the client port and terminates the session.
 * 
 * @author <a href="mailto:mmollano@ucalgary.ca">Mohsen Mollanoori</a>
 */
public class TCPEchoServer {
	final static int SERVER_PORT = 2020;

	public static void main_(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(SERVER_PORT);
		while (true) {
			// Have a new client
			Socket client = server.accept();
			// Serve the new client in a new Thread
			new EchoHandler(client).start();
		}
	}

	static class EchoHandler extends Thread {
		String QUIT_COMMAND = "_quit_";
		int quitCounter = 0;
		private Socket client;

		public EchoHandler(Socket client) {
			this.client = client;
		}

		public void run() {
			try {
				// Most probably it shows the port # and the IPv6 address of the
				// connected client.
				System.out.println("Serving " + client);

				// Input from the client. Output to the client.
				// Wherever convenient you can also use higher lever stream
				// classes such as BufferedReader/BufferedWriter and
				// ObjectInputStream/ObjectOutputStream.
				InputStream inputStream = client.getInputStream();
				OutputStream outputStream = client.getOutputStream();

				// The byte read from the client input
				int b;

				// Read a single byte at a time. If the client closes the
				// connection, we will get a -1, so we stop reading.
				while ((b = inputStream.read()) != -1) {
					// echo the byte to the client's output
					outputStream.write(b);

					// The following lines simulate a kind of a simple DFA to
					// find the QUIT_COMMAND in the input sequence. The
					// quitCounter variable is the current state of the DFA.
					// Whenever we observe the QUIT_COMMAND (even in the middle
					// of a line) we stop and terminate the session.
					if (QUIT_COMMAND.charAt(quitCounter) == b) {
						quitCounter++;
					} else {
						quitCounter = 0;
					}

					// if the QUIT_COMMAND is seen
					if (quitCounter == QUIT_COMMAND.length()) {
						outputStream.write("\nbye bye :)\n".getBytes());
						outputStream.close();
						inputStream.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
