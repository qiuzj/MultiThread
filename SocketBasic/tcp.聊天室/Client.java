
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

class Client {
	public static void main(String[] args) {
		new MyFrame();
	}
}

class MyFrame extends Frame {
	Panel p1;

	Panel p2;

	Panel p3;

	TextArea show;

	TextField input;

	Label port;

	Label ip;

	// Label name;
	TextField tPort;

	TextField tIp;

	// TextField tName;
	Button submit;

	Button login;

	Socket s = null;

	DataOutputStream dos = null;

	DataInputStream dis = null;

	private boolean bConnect = false;

	public MyFrame() {
		init();
	}

	public void init() {
		/**
		 * *********************************show
		 * begin***************************************************
		 */
		p1 = new Panel();
		show = new TextArea(15, 80);
		show.setEditable(false);
		p1.add(show);
		add(p1, BorderLayout.CENTER);
		/**
		 * *********************************show
		 * end***************************************************
		 */

		/**
		 * *********************************Panel2
		 * begin***************************************************
		 */
		p2 = new Panel(new FlowLayout(FlowLayout.LEFT));
		port = new Label("PORT");
		tPort = new TextField(7);
		ip = new Label("IP");
		tIp = new TextField(25);
		// name = new Label("NAME");
		// tName = new TextField(7);
		login = new Button("login");
		p2.add(port);
		p2.add(tPort);
		p2.add(ip);
		p2.add(tIp);
		// p2.add(name);
		// p2.add(tName);
		p2.add(login);
		login.addActionListener(new submitAction());
		add(p2, BorderLayout.NORTH);
		/**
		 * *********************************Panel2
		 * end***************************************************
		 */

		/**
		 * *********************************Panel3
		 * begin***************************************************
		 */
		p3 = new Panel(new FlowLayout(FlowLayout.LEFT));
		input = new TextField(70);
		submit = new Button("submit");
		p3.add(input);
		p3.add(submit);
		input.addActionListener(new inputAction());
		submit.addActionListener(new submitAction());
		add(p3, BorderLayout.SOUTH);
		/**
		 * *********************************Panel3
		 * end***************************************************
		 */

		/**
		 * ******************************fram
		 * begin***********************************************
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// disConnect();
				System.exit(0);
			}
		});
		this.setSize(300, 300);
		setLocation(100, 100);
		pack();
		setVisible(true);
		/**
		 * ******************************fram
		 * end***********************************************
		 */
	}

	class submitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ("submit".equals(e.getActionCommand())) {
				String str = input.getText().trim();
				// show.setText(show.getText() + str + '\n');
				input.setText("");
				faSong(str);

			} else if ("login".equals(e.getActionCommand())) {
				connect();
			}
		}
	}

	/** **************************************回车输出事件**************************************************** */
	class inputAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = input.getText().trim();
			// show.setText(show.getText() + str + '\n');
			input.setText("");
			faSong(str);
		}
	}

	/** *****************************向服务端发送信息****************************************************** */
	public void faSong(String str) {
		try {
			dos.writeUTF(str);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** *****************************关闭资源****************************************************** */
	/*
	 * public void disConnect() { try { dos.close(); } catch (IOException e) {
	 * //e.printStackTrace(); System.out.println("系统错误"); }
	 *  }
	 */
	/** *****************************************接收服务器发送的信息***************************************************** */
	private class RecvClient implements Runnable {
		public void run() {
			try {
				while (bConnect) {
					String str = dis.readUTF();
					show.setText(show.getText() + str + '\n');
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * *****************************connection
	 * begin******************************************************
	 */
	public void connect() {
		try {
			s = new Socket(tIp.getText(), Integer.parseInt(tPort.getText()));
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			bConnect = true;
			new Thread(new RecvClient()).start();
			System.out.println("我连进来啦！~哈哈~~");
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
