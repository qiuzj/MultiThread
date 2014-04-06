import java.util.Observer;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/*
 * 创建日期 2005-4-6
 *
 * TODO 
 */

/**
 * @author Administrator
 *
 * TODO 下载管理器管理类
 * 负责界面的显示
 */
class DownloadManager extends JFrame implements Observer {
	//下载文件URL文本框
	private JTextField addTextField;
	//下载列表数据模型
	private DownloadsTableModel tableModel;
	//下载任务列表
	private JTable table;
	//控制选中的下载任务
	private JButton pauseButton,resumeButton;
	private JButton cancelButton,clearButton;
	//当前选中的下载任务
	private Download selectedDownload;
	//标签:指示表格选择是否被清除
	private boolean clearing;
	//构造函数
	public DownloadManager(){
		//设置程序标题
		setTitle("网络下载管理器");
		//设置窗体大小
		setSize(640,480);
		setLocation(200,50);
		//处理关闭事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				actionExit();
			}
		});
		//设置文件菜单项
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("文件");
		//快捷键
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem fileExitMenuItem = new JMenuItem("退出",KeyEvent.VK_X);
		fileExitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionExit();
			}
		});
		fileMenu.add(fileExitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		//设置URL添加面板
		JPanel addPanel = new JPanel();
		addTextField = new JTextField(30);
		addPanel.add(addTextField);
		JButton addButton = new JButton("添加下载");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionAdd();
			}
		});
		addPanel.add(addButton);
		//设置下载列表
		tableModel = new DownloadsTableModel();
		table = new JTable(tableModel);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				tableSelectionChanged();
			}
		});
		//只能同时选中一行
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//设置进度栏
		ProgressRenderer renderer = new ProgressRenderer(0,100);
		//设置显示字符串
		renderer.setStringPainted(true);
		table.setDefaultRenderer(JProgressBar.class,renderer);
		//设置表格的宽度与高度 使适合进度条的高度
		table.setRowHeight((int)renderer.getPreferredSize().getHeight());
		
		//设置下载表面板
		JPanel downloadsPanel = new JPanel();
		downloadsPanel.setBorder(BorderFactory.createTitledBorder("下载列表"));
		downloadsPanel.setLayout(new BorderLayout());
		downloadsPanel.add(new JScrollPane(table),BorderLayout.CENTER);
		
		//设置按钮面板
		JPanel buttonsPanel = new JPanel();
		pauseButton = new JButton("暂停");
		pauseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionPause();
			}
		});
		pauseButton.setEnabled(false);
		buttonsPanel.add(pauseButton);
		//*******************************************************
		
		resumeButton = new JButton("恢复");
		resumeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionResume();
			}
		});
		resumeButton.setEnabled(false);
		buttonsPanel.add(resumeButton);
		//********************************************************
		cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionCancel();
			}
		});
		cancelButton.setEnabled(false);
		buttonsPanel.add(cancelButton);
		//*******************************************************
		clearButton = new JButton("清除");
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
	//退出程序
	private void actionExit(){
		System.exit(0);
	}
	//添加一个新下载
	private void actionAdd(){
		URL verifiedUrl = verifyUrl(addTextField.getText());
		if(verifiedUrl != null){
			tableModel.addDownload(new Download(verifiedUrl));
			addTextField.setText("");
		}
		else{
			JOptionPane.showMessageDialog(this,"URL地址无效!","错误",JOptionPane.ERROR_MESSAGE);
		}
	}
	//检查URL地址
	private URL verifyUrl(String url){
		//只充许HTTP URLs
		if(!url.toLowerCase().startsWith("http://"))
			return null;
		//格式化URL
		URL verifiedUrl = null;
		try{
			verifiedUrl = new URL(url);
		}
		catch(Exception e){
			return null;
		}
		//确认URL连接到一个文件
		if(verifiedUrl.getFile().length()< 2)
			return null;
		return verifiedUrl;
	}
	//当表格选择变化时调用
	private void tableSelectionChanged(){
		//解除原先选定的下载事件的注册
		if(selectedDownload != null){
			selectedDownload.deleteObserver(DownloadManager.this);
		}
		//如果没清除的话,注册下载事件
		if(!clearing){
			selectedDownload = tableModel.getDownload(table.getSelectedRow());
			selectedDownload.addObserver(DownloadManager.this);
			updateButtons();
		}
	}
	//暂停下载
	private void actionPause(){
		selectedDownload.pause();
		updateButtons();
	}
	//重启下载
	private void actionResume(){
		selectedDownload.resume();
		updateButtons();
	}
	//取消下载
	private void actionCancel(){
		selectedDownload.cancel();
		updateButtons();
	}
	//清除下载
	private void actionClear(){
		clearing = true;
		tableModel.clearDownload(table.getSelectedRow());
		clearing = false;
		selectedDownload = null;
		updateButtons();
	}
	//当状态改变时改变按钮的可用状态
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
		    default://完成或取消
		    	pauseButton.setEnabled(false);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				break;
			}
		}
		else{
			//没有选择下载
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			clearButton.setEnabled(false);
		}
	}
	//当下载任务改变时,通知这个方法
	public void update(Observable o,Object arg){
		//更新按钮,如果选择的下载改变了
		if(selectedDownload != null && selectedDownload.equals(o))
			updateButtons();
	}
}
