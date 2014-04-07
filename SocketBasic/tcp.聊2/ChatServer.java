import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private ServerSocket server;
	private List<ClientRun> clients = new ArrayList<ClientRun>();

	public static void main(String[] args) {
		new ChatServer().start();
	}

	public void start() {
		try {
			server = new ServerSocket(10000);
			boolean connected = true;
			while (connected) {
				Socket s = server.accept();
				ClientRun cli = new ClientRun(s);
				new Thread(cli).start();
				clients.add(cli);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ClientRun implements Runnable {
		private DataInputStream dis;
		private DataOutputStream dos;

		public ClientRun(Socket s) throws IOException {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		}

		public void run() {
			try {
				while (true) {
					String str = dis.readUTF();
					for (ClientRun cli : clients) {
						cli.dos.writeUTF(str);
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
