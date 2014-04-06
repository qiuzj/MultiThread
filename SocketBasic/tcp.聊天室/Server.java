
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Server {
	boolean started = false;

	ServerSocket ss = null;

	List<ClientRun> list = new ArrayList<ClientRun>();

	public static void main(String[] args) {
		new Server().start();
	}

	public void start() {
		try {
			ss = new ServerSocket(8888);
			started = true;
		} catch (BindException be) {
			System.out.println("端口使用中！");
		} catch (IOException e) {
			System.out.println("服务器连接失败！");
		}
		try {
			while (started) {
				Socket s = ss.accept();
				ClientRun cr = new ClientRun(s); // 主线程只负责接收信息，每个客户端连接进来都会开始一个新线程
				new Thread(cr).start(); // 把连接进来的Socket传到线程中。
				list.add(cr);
				System.out.println("系统提示：" + s.getInetAddress() + "已经联入");
			}
		} catch (IOException e) {
			System.out.println("Client closed!");
		}
	}

	/** *********************************多线程实现多客户端同时连接************************************************* */
	class ClientRun implements Runnable {
		private Socket s;

		private DataInputStream dis = null;

		private DataOutputStream dos = null;

		boolean bConnect = false;

		public ClientRun(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void run() {
			try {
				while (bConnect) {
					String str = dis.readUTF();
					System.out.println(str);
					for (int i = 0; i < list.size(); i++) {
						ClientRun cr = list.get(i);
						cr.send(str);
					}
				}
			} catch (Exception e) {
				System.out.println(s.getInetAddress() + "离开了!");
			} finally {
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
					if (s != null) {
						s.close();
						s = null;
					}
				} catch (IOException io) {
					io.printStackTrace();
				}
			}

		}
	}
}
