import java.util.Observer;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/*
 * �������� 2005-4-6
 *
 * TODO 
 */

/**
 * @author Administrator
 *
 * TODO ���ع�����������
 * ����������ʾ
 */
class DownloadManager extends JFrame implements Observer {
	//�����ļ�URL�ı���
	private JTextField addTextField;
	//�����б�����ģ��
	private DownloadsTableModel tableModel;
	//���������б�
	private JTable table;
	//����ѡ�е���������
	private JButton pauseButton,resumeButton;
	private JButton cancelButton,clearButton;
	//��ǰѡ�е���������
	private Download selectedDownload;
	//��ǩ:ָʾ���ѡ���Ƿ����
	private boolean clearing;
	//���캯��
	public DownloadManager(){
		//���ó������
		setTitle("�������ع�����");
		//���ô����С
		setSize(640,480);
		setLocation(200,50);
		//����ر��¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				actionExit();
			}
		});
		//�����ļ��˵���
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("�ļ�");
		//��ݼ�
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem fileExitMenuItem = new JMenuItem("�˳�",KeyEvent.VK_X);
		fileExitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionExit();
			}
		});
		fileMenu.add(fileExitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		//����URL������
		JPanel addPanel = new JPanel();
		addTextField = new JTextField(30);
		addPanel.add(addTextField);
		JButton addButton = new JButton("�������");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionAdd();
			}
		});
		addPanel.add(addButton);
		//���������б�
		tableModel = new DownloadsTableModel();
		table = new JTable(tableModel);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				tableSelectionChanged();
			}
		});
		//ֻ��ͬʱѡ��һ��
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���ý�����
		ProgressRenderer renderer = new ProgressRenderer(0,100);
		//������ʾ�ַ���
		renderer.setStringPainted(true);
		table.setDefaultRenderer(JProgressBar.class,renderer);
		//���ñ��Ŀ����߶� ʹ�ʺϽ������ĸ߶�
		table.setRowHeight((int)renderer.getPreferredSize().getHeight());
		
		//�������ر����
		JPanel downloadsPanel = new JPanel();
		downloadsPanel.setBorder(BorderFactory.createTitledBorder("�����б�"));
		downloadsPanel.setLayout(new BorderLayout());
		downloadsPanel.add(new JScrollPane(table),BorderLayout.CENTER);
		
		//���ð�ť���
		JPanel buttonsPanel = new JPanel();
		pauseButton = new JButton("��ͣ");
		pauseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionPause();
			}
		});
		pauseButton.setEnabled(false);
		buttonsPanel.add(pauseButton);
		//*******************************************************
		
		resumeButton = new JButton("�ָ�");
		resumeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionResume();
			}
		});
		resumeButton.setEnabled(false);
		buttonsPanel.add(resumeButton);
		//********************************************************
		cancelButton = new JButton("ȡ��");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionCancel();
			}
		});
		cancelButton.setEnabled(false);
		buttonsPanel.add(cancelButton);
		//*******************************************************
		clearButton = new JButton("���");
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionClear();
			}
		});
		clearButton.setEnabled(false);
		buttonsPanel.add(clearButton);
		//*****************************************************
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(addPanel,BorderLayout.NORTH);
		getContentPane().add(downloadsPanel,BorderLayout.CENTER);
		getContentPane().add(buttonsPanel,BorderLayout.SOUTH);
	}
	//�˳�����
	private void actionExit(){
		System.exit(0);
	}
	//���һ��������
	private void actionAdd(){
		URL verifiedUrl = verifyUrl(addTextField.getText());
		if(verifiedUrl != null){
			tableModel.addDownload(new Download(verifiedUrl));
			addTextField.setText("");
		}
		else{
			JOptionPane.showMessageDialog(this,"URL��ַ��Ч!","����",JOptionPane.ERROR_MESSAGE);
		}
	}
	//���URL��ַ
	private URL verifyUrl(String url){
		//ֻ����HTTP URLs
		if(!url.toLowerCase().startsWith("http://"))
			return null;
		//��ʽ��URL
		URL verifiedUrl = null;
		try{
			verifiedUrl = new URL(url);
		}
		catch(Exception e){
			return null;
		}
		//ȷ��URL���ӵ�һ���ļ�
		if(verifiedUrl.getFile().length()< 2)
			return null;
		return verifiedUrl;
	}
	//�����ѡ��仯ʱ����
	private void tableSelectionChanged(){
		//���ԭ��ѡ���������¼���ע��
		if(selectedDownload != null){
			selectedDownload.deleteObserver(DownloadManager.this);
		}
		//���û����Ļ�,ע�������¼�
		if(!clearing){
			selectedDownload = tableModel.getDownload(table.getSelectedRow());
			selectedDownload.addObserver(DownloadManager.this);
			updateButtons();
		}
	}
	//��ͣ����
	private void actionPause(){
		selectedDownload.pause();
		updateButtons();
	}
	//��������
	private void actionResume(){
		selectedDownload.resume();
		updateButtons();
	}
	//ȡ������
	private void actionCancel(){
		selectedDownload.cancel();
		updateButtons();
	}
	//�������
	private void actionClear(){
		clearing = true;
		tableModel.clearDownload(table.getSelectedRow());
		clearing = false;
		selectedDownload = null;
		updateButtons();
	}
	//��״̬�ı�ʱ�ı䰴ť�Ŀ���״̬
	private void updateButtons(){
		if(selectedDownload != null){
			int status = selectedDownload.getStatus();
			switch(status){
			case Download.DOWNLOADING:
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
			case Download.PAUSED:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
			case Download.ERROR:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				break;
		    default://��ɻ�ȡ��
		    	pauseButton.setEnabled(false);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				break;
			}
		}
		else{
			//û��ѡ������
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			clearButton.setEnabled(false);
		}
	}
	//����������ı�ʱ,֪ͨ�������
	public void update(Observable o,Object arg){
		//���°�ť,���ѡ������ظı���
		if(selectedDownload != null && selectedDownload.equals(o))
			updateButtons();
	}
}
