import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 
 */

/**
 * @author qiuzj
 *
 */
public class UdpSender {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket();
		
		String msg = "I love 中国人!";
		// 用msg.length()取得的是字符的长度, 而不是字节长度, 错
		// DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName("10.11.1.68"), 3000);
		DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName("localhost"), 3000);
		ds.send(dp);
		ds.close();
	}

}
