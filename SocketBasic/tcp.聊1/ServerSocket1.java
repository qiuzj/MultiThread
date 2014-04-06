
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanjia
 * 
 */
public class ServerSocket1 {

	private ServerSocket ss;

	private boolean isConnected;

	private List<ClientRun> list = new ArrayList<ClientRun>(); // 各客户端广播线程列表

	public static void main(String[] args) {
		new ServerSocket1().startServer();
	}

	public void startServer() {
		try {
			ss = new ServerSocket(10000);
			isConnected = true;
		} catch (BindException be) {
			System.out.println("端口正在使用!");
		} catch (IOException ioe) {
			System.out.println("服务器连接失败!");
		}
		try {
			while (isConnected) {
				Socket s = ss.accept();
				
				// 为每一个连接的客户端启动一个线程
				ClientRun clientThread = new ClientRun(s);
				new Thread(clientThread).start();
				
				list.add(clientThread);
				System.out.println("有人连接");
			}
		} catch (IOException e) {
			System.out.println("Client close!");
		}
	}

	/**
	 * 用于广播信息的线程. 每个线程保存着一个客户端对应的Socket服务端实例
	 * 
	 * @author qiuzj
	 *
	 */
	class ClientRun implements Runnable {

		private Socket socket;

		private DataInputStream dis;

		private DataOutputStream dos;

		private boolean isReady;

		public ClientRun(Socket s) throws IOException {
			this.socket = s;
			this.dis = new DataInputStream(s.getInputStream());
			this.dos = new DataOutputStream(s.getOutputStream());
			this.isReady = true;
		}

		public void run() {
			try {
				// 每一个客户端发送信息后, 此处将该信息广播给list中的所有客户端, 包括发送的客户端自身
				while (this.isReady) { // 如果while语句放在try外面, 那可就乱了. 因为老早就把dos, dis给关闭了
					String str = dis.readUTF();
					for (ClientRun client : list) {
						client.dos.writeUTF(str); // System.out.println("OK");
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.out.println(socket.getInetAddress() + "离开了!");
			} finally {
				try {
					if (dos != null)
						dos.close();
					if (dis != null)
						dis.close();
				} catch (Exception e) {

				}
			}

		}
	}
}
