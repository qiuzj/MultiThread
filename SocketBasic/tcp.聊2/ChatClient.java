import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

	public static void main(String[] args) {
		new ChatClient().start();
	}

	public void start() {
		DataOutputStream dos = null;
		try {
			Socket s = new Socket("localhost", 10000);
			new ClientRun(s).start();

			dos = new DataOutputStream(s.getOutputStream());
			for (int i = 0; i < 50; i++) {
				dos.writeUTF("I'm ok" + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ClientRun extends Thread {

		private DataInputStream dis;

		public ClientRun(Socket s) throws IOException {
			dis = new DataInputStream(s.getInputStream());
		}

		public void run() {
			try {
				while (true) {
					System.out.println(dis.readUTF());
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
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
