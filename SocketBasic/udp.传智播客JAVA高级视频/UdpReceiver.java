

import java.net.*;

/**
 * @author qiuzj
 *
 */
public class UdpReceiver {

	/**
	 * @param args
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(3000); // 接收端口为3000
		byte[] bt = new byte[1024 * 1024];
		DatagramPacket dp = new DatagramPacket(bt, 0, bt.length);
		ds.receive(dp);
		// new String(dp.getData())这样是将缓冲区中所有内容..., 而不是实际发送的长度
		// 故用new String(dp.getData(), 0, dp.getLength())
		System.out.println(new String(dp.getData(), 0, dp.getLength()));
		ds.close();
	}

}
