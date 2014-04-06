
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author zhanjia
 * 
 */
public class SocketClient1 {

	private Socket socket;

	private DataInputStream dis;

	private DataOutputStream dos;

	private boolean isReady;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SocketClient1().start();
	}

	private void start() {
		try {
			socket = new Socket("localhost", 10000);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			this.isReady = true;
			
			// 启动接收信息线程
			new Thread(new ClientThread()).start();
			System.out.println("我连进来啦！~哈哈~~");
			
			// 测试数据发送
			for (int i = 0; i < 50; i++) {
				dos.writeUTF("I'm " + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	class ClientThread implements Runnable {
		public void run() {
			receive();
		}
	}

	/**
	 * 接收信息
	 *
	 */
	public void receive() {
		while (this.isReady) {
			try {
				System.out.println(this.dis.readUTF());
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
