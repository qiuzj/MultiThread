package globleGet.gui;

import globleGet.download.DownLoadInfo;
import globleGet.download.MultiDownLoad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainFrame extends JFrame
{
	     private DefaultListModel model;
	     private JList lst_downfiles;
	     private ItemPane itp_fileProperity;
	     private ToolPane toolpane;
	     private int lst_pos=-1;
	     
	     public MainFrame()
	     {
	     	      super("GlobalGet 多线程下载程序");
	     	      ini();
	     	      setGUI();
	     	      setEvent();
	     	      Displace dis=new Displace();
	     	      dis.setDaemon(true);
	     	      dis.setPriority(7);
	     	      dis.start();
	     }	
	     
	     /**
	      *功能：加载序列化文件
	     */
	     private void ini()
	     {
	     	       File myDownfiles=new File(".\\downfiles.data");
	     	       
	     	       if (myDownfiles.exists())
	     	       {
	     	       	   ObjectInputStream ois=null;
	     	       	   try
	     	       	   {
	     	       	   	  ois=new ObjectInputStream(new FileInputStream(myDownfiles));
	     	       	   	  model=(DefaultListModel)(ois.readObject());
	     	       	   	  ois.close();
	     	       	   	  DownLoadInfo tmp_info;
	     	       	   	  int len=model.getSize();
	     	       	   	  for(int i=0;i<len;i++)
	     	       	   	  {
	     	       	   	  	  tmp_info=(DownLoadInfo)(model.getElementAt(i));
	     	       	   	  	  if (!tmp_info.isFinished())
	     	       	   	  	  {
	     	       	   	  	  	  MultiDownLoad md=new MultiDownLoad(tmp_info);
	     	                      md.MultiDown();
	     	       	   	  	  }	
	     	       	   	  }	
	     	       	   	  lst_pos=len-1;
	     	       	   }
	     	       	   catch(Exception e)
	     	       	   {
	     	       	   	  try
	     	       	   	   {
	     	       	   	   	   ois.close();
	     	       	   	   }
	     	       	   	   catch(Exception e1)
	     	       	   	   {}	
	     	       	   	  JOptionPane.showMessageDialog(this,"加载历史下载文件出错！"); 
	     	       	   	  return;
	     	       	   }
	     	       	   		
	     	       }
	     	       else 
	     	       {	
	     	            model=new DefaultListModel();
	     	       }     
	     }	
	     
	     /**
	      *功能：保存序列化文件
	     */
	     private void saveModel()
	     {
	     	       ObjectOutputStream oos=null;
	     	       try
	     	       {
	     	       	    oos=new ObjectOutputStream(new FileOutputStream(".\\downfiles.data"));
	     	            oos.writeObject(model);	     	            
	     	       }
	     	       catch(Exception e)
	     	       {
	     	       	    JOptionPane.showMessageDialog(this,"系统关闭时出错，可能丢失部分下载信息！"); 
	     	       }
	     	       finally
	     	       {
	     	       	    try
	     	       	    {
	     	       	    	 oos.close();
	     	       	    }
	     	       	    catch(Exception e2)
	     	       	    {}		     	       	       	       	    	
	     	       }	
	     	       	
	     }	
	     
	     public void setGUI()
	     {
	     	      //构建List列表布局	     	      
	     	      lst_downfiles=new JList(model);
	     	      lst_downfiles.setBorder(BorderFactory.createEtchedBorder());
	     	      JScrollPane sp_lst=new JScrollPane(lst_downfiles);
	     	      sp_lst.setPreferredSize(new Dimension(200,360) ) ;
	     	      
	     	      //创建文件下载属性面板
	     	      itp_fileProperity=new ItemPane();
	     	      itp_fileProperity.setPreferredSize(new Dimension(400,360) ) ;
	     	      //构建窗口上方布局	     	      
	     	      JPanel top_pane=new JPanel();
	     	      top_pane.setLayout(new BorderLayout());
	     	      top_pane.add(sp_lst,BorderLayout.WEST);
	     	      top_pane.add(itp_fileProperity,BorderLayout.CENTER);
	     	      top_pane.setBorder(BorderFactory.createEtchedBorder());
	     	      
	     	      //创建toolbar面板
	     	      toolpane=new ToolPane();
	     	      
	     	      //构建Frame布局
	     	      this.setLayout(new BorderLayout());
	     	      this.getContentPane().add(top_pane,BorderLayout.CENTER);
	     	      this.getContentPane().add(toolpane,BorderLayout.SOUTH);
	     	      
	     	      //设置Frame显示属性
	     	      this.setSize(600,400);
	     	      this.setLocation(200,200);
	     	      this.setVisible(true);
	     	      this.setResizable(false);
	     }	
	     
	     public void setEvent()
	     {
	     	      //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     	      this.toolpane.btnTask.addActionListener(new AddTaskListener(this));
	     	      this.toolpane.btnClose.addActionListener(new CloseListener());
	     	      this.lst_downfiles.addListSelectionListener(new SelectTaskListener());
	     	      this.addWindowListener(new JFrameCloseListener());	     	      
	     }	
	     
	     public static void main(String[] args)
	     {
	     	      new MainFrame();	     	      
	     }	
	     
	     //事件监听器程序
	     
	     //添加下载任务监听器类
	     class AddTaskListener implements ActionListener
	     {
	     	     MainFrame f;
	     	     public AddTaskListener(MainFrame f)
	     	     {
	     	     	      this.f=f;
	     	     }	
	     	     
	     	     public void actionPerformed(ActionEvent e)
	     	     {
	     	     	      //调用添加任务对话框
	     	     	      TaskDialog taskInfo=new TaskDialog(f);
	     	     	      //创建下载任务
	     	     	      
	     	     	      if (taskInfo.getBtn())
	     	     	      {
	     	     	         addTask(taskInfo);
	     	     	      }
	     	     }	
	     }	
	     
	     //点击关闭按钮监听器类
	     class CloseListener implements ActionListener
	     {
	     	     public void actionPerformed(ActionEvent e)
	     	     {
	     	     	      saveModel();
	     	     	      System.exit(0);
	     	     }	
	     }	
	     
	     //点击JFrame关闭窗口监听器类
	     class JFrameCloseListener extends WindowAdapter
	     {
	     	     public void windowClosing(WindowEvent e)
	     	     {
	     	     	      saveModel();
	     	     	      System.exit(0);
	     	     }	
	     }	
	     
	     //列表框选择内容改变监听器
	     class SelectTaskListener implements ListSelectionListener
	     {
	     	     public void valueChanged(ListSelectionEvent e)
	     	     {
	     	     	      int index=lst_downfiles.getSelectedIndex();
	     	     	      setFileProperity(index);
	     	     	      lst_pos=index;
	     	     }	
	     }
	     
	     
	     
	     //设置下载文件属性
	     private void setFileProperity(int index)
	     {                                                 
	     	       DownLoadInfo tmp_info=(DownLoadInfo)(model.elementAt(index));
	     	       String tmp_file=tmp_info.getSaveFileName();
	     	       float tmp_len=tmp_info.getFileLength();
	     	       itp_fileProperity.lab_filename.setText(tmp_file.substring(tmp_file.lastIndexOf("\\")+1));
	     	       
	     	       if (tmp_len<500)
	     	       {
	     	           itp_fileProperity.lab_filesize.setText(tmp_len+"bytes");
	     	       }
	     	       else if(tmp_len<1024*1024)  
	     	       {
	     	       	   tmp_len=Math.round(tmp_len/1024*100)/100.0f;
	     	       	   itp_fileProperity.lab_filesize.setText(tmp_len+"k");
	     	       }
	     	       else
	     	       {
	     	       	   tmp_len=Math.round(tmp_len/1024/1024*100)/100.0f;
	     	       	   itp_fileProperity.lab_filesize.setText(tmp_len+"M");
	     	       }		
	     	       
	     	       itp_fileProperity.pbar_model.setValue(tmp_info.getDownProgress());
	     	       itp_fileProperity.pbar.setString("下载进度"+itp_fileProperity.pbar.getValue()+"%");
	     	       
	     	       tmp_len=tmp_info.getDownSpeed();	     	       
	     	       itp_fileProperity.lab_downspeed.setText(tmp_len+"k/s");
	     	       
	     	       itp_fileProperity.lab_threadnum.setText(tmp_info.getThreadNum()+"");
	     	       
	     	       String tmp_str=tmp_file;
	     	       if (tmp_file.length()>20)
	     	       {
	     	       	  tmp_str=tmp_file.substring(0,19)+"...";
	     	       }	
	     	       itp_fileProperity.lab_filepath.setText(tmp_str);
	     	       itp_fileProperity.lab_filepath.setToolTipText(tmp_file);
	     	       
	     	       if(tmp_info.getDownURL().length()>20)
	     	       {
	     	       	  tmp_str=tmp_info.getDownURL().substring(0,19)+"...";
	     	       }	
	     	       itp_fileProperity.lab_url.setText(tmp_str);
	     	       itp_fileProperity.lab_url.setToolTipText(tmp_info.getDownURL());
	     	       
	     	       if(tmp_info.isFinished())
	     	       {
	     	       	    itp_fileProperity.lab_filestate.setText("下载完成");
	     	       }
	     	       else if(tmp_info.isError())
	     	       {
	     	       	    itp_fileProperity.lab_filestate.setText("下载错误");
	     	       }
	     	       else
	     	       {
	     	       	    itp_fileProperity.lab_filestate.setText("正在下载中...");
	     	       }			
	     }
	     
	     /**
	      *功能：创建一个下载任务
	      *返回值：无
	     */
	     private void addTask(TaskDialog taskInfo)
	     {
	     	       
	     	       DownLoadInfo tmp_info=new DownLoadInfo(taskInfo.getUrlStr(),taskInfo.getFileStr(),3);
	     	       if (!tmp_info.getIsValidate())
	     	       {
	     	       	   JOptionPane.showMessageDialog(this,"文件无法下载！");
	     	       	   return;
	     	       }
	     	       MultiDownLoad md=new MultiDownLoad(tmp_info);
	     	       md.MultiDown();
	     	       
	     	       model.addElement(tmp_info);	
	     	       
	     	       int index=model.getSize()-1;
	     	       
	     	       setFileProperity(index);
	     	       lst_pos=index;	     	       
	     }
	     
	     class Displace extends Thread
	     {
	     	     public void run()
	     	     {
	     	     	      while(true)
	     	     	      {
	     	     	           if (lst_pos!=-1)
	     	     	           {
	     	     	               setFileProperity(lst_pos);
	     	     	           }
	     	     	           try
	     	     	           {
	     	     	           	   Thread.sleep(100);
	     	     	           }
	     	     	           catch(InterruptedException e)
	     	     	           {}
	     	     	           	    
	     	     	      }     
	     	     }	
	     }				
}	