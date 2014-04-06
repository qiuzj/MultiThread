package globleGet.gui;

/**
 *功能：添加下载任务对话框
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.File;

/**
*功能：对话框主窗口
*/
public class TaskDialog 
{
      private boolean btnOK;//是否按下“确定”按钮
      private String urlStr;
      private String fileStr;
      private int threadId;
      private Top_pane topPan;
      private Tool_pane botPan;
      private JDialog d;
      
      public TaskDialog(JFrame f)
	    {
	           
	           d=new JDialog(f,"添加下载任务",true);	           
	           setGUI();
	           setEvent();
	           d.setVisible(true);	
	    }
	           
	    /**
	     *功能：设置对话框界面
	    */
	    private void setGUI()
	    {
	           topPan=new Top_pane();
	           botPan=new Tool_pane();
	           
	           d.setLayout(new BorderLayout());
	           d.getContentPane().add(topPan,BorderLayout.CENTER);
	           d.getContentPane().add(botPan,BorderLayout.SOUTH);
	           
	           d.setSize(350,200);
	           d.setLocation(400,350);
	    }	
	    
	    /**
	     *功能：设置组件的监听事件
	    */
	    private void setEvent()
	    {
	    	     d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    	     botPan.btnClose.addActionListener(new BtnCancelListener());
	    	     botPan.btn_OK.addActionListener(new BtnOKListener());
	    	     topPan.btn_saveDialog.addActionListener(new OpenChooserListener());
	    }	  
	    
	    /**
	     *功能：获取用户是否点击对话框的确定按钮
	     *返回类型：boolean
	    */
	    public boolean getBtn()
	    {
	    	     return btnOK;
	    }	
	    
	    /**
	     *功能：获取用户设置的URL地址
	     *返回类型：String
	    */
	    public String getUrlStr()
	    {
	    	     return urlStr;
	    }	
	    
	    /**
	     *功能：获取保存文件名称
	     *返回类型：String
	    */
	    public String getFileStr()
	    {
	    	     return fileStr;
	    }	
	    
	    /**
	     *功能：获取下载线程数
	     *返回类型：String
	    */
	    public int getThreadId()
	    {
	    	     return threadId;
	    }	
	    
	    /*测试代码
	    public static void main(String[] args)
	    {
	    	    new TaskDialog();
	    }	
	    */ 
	    
	    //事件监听器代码
	    
	    /**
	     *功能：单击打开保存对话框监听器
	    */ 
	    class OpenChooserListener implements ActionListener
	    {
	    	    public void actionPerformed(ActionEvent e)
	    	    {
	    	    	     //调用打开对话框
                   JFileChooser chooser=new JFileChooser() ;
                   int result=chooser.showSaveDialog(d) ;
                   if (result==JFileChooser.CANCEL_OPTION ) return;
                   topPan.txt_savefile.setText(chooser.getSelectedFile().getAbsolutePath()); 
	    	    }	
	    }	 
	    
	    /**
	    *功能：单击确定按钮监听器
	    */
	    class BtnOKListener implements ActionListener
	    {
	    	    public void actionPerformed(ActionEvent e)
	    	    {
	    	    	     //输入验证
	    	    	     if((topPan.txt_url.getText().equals(""))||(topPan.txt_savefile.getText().equals("")))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"请输入URL和下载文件名！");
	    	    	     	  return;
	    	    	     }	
	    	    	     //验证URL地址格式是否正确
	    	    	     if (!isURL(topPan.txt_url.getText()))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"URL格式错误！");
	    	    	     	  return;
	    	    	     }	
	    	    	     //验证保存文件名格式
	    	    	     if (!isFile(topPan.txt_savefile.getText()))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"保存文件名错误！");
	    	    	     	  return;
	    	    	     }
	    	    	     urlStr=topPan.txt_url.getText();
	    	    	     fileStr=topPan.txt_savefile.getText();
	    	    	     Integer tmp=(Integer)(topPan.model.getValue());
	    	    	     threadId=tmp.intValue();
	    	    	     d.dispose();
	    	    	     btnOK=true;
	    	    }
	    	    
	    	    /**
	    	     *功能：验证URL格式
	    	     *返回值：boolean
	    	    */
	    	    private boolean isURL(String tmp_url)
	    	    {
	    	    	      try
	    	    	      {
	    	    	      	   URL url1=new URL(tmp_url);
	    	    	      	   return true;
	    	    	      }
	    	    	      catch(MalformedURLException e)
	    	    	      {
	    	    	      	   return false;
	    	    	      }		
	    	    }
	    	    
	    	    /**
	    	     *功能：验证文件名格式
	    	     *返回值：boolean
	    	    */
	    	    private boolean isFile(String filename)
	    	    {
	    	    	      File tmp_file=new File(filename);
	    	    	      if(tmp_file.isDirectory())
	    	    	      {
	    	    	      	  return false;
	    	    	      }
	    	    	      else
	    	    	      {
	    	    	      	  topPan.txt_savefile.setText(tmp_file.getAbsolutePath());
	    	    	      	  return true;
	    	    	      }	    	    	      
	    	    }	
	    }
	    
	    /**
	     *功能：单击取消按钮监听器
	    */ 
	    class BtnCancelListener implements ActionListener
	    {
	    	    public void actionPerformed(ActionEvent e)
	    	    {
	    	    	     btnOK=false;
	    	    	     urlStr="";
	    	    	     fileStr="";
	    	    	     d.dispose();
	    	    }
	    }	 	     
}
	
/**	     
 *功能：对话框上方窗口中的panel
*/ 
class Top_pane extends JPanel
{
      JTextField txt_url;
	    JTextField txt_savefile;
	    JButton btn_saveDialog;
	    SpinnerNumberModel model;
	     	     
      public Top_pane()
	    {
	    	      super();
	    	      ini();
	    }	
	     	     
	    private void ini()
	    {
	    	      JLabel lab_url=new JLabel("URL:");
	     	      JLabel lab_savefile=new JLabel("文件名:");
	     	      JLabel lab_threadId=new JLabel("线程数：");
	     	     	      
	     	      txt_url=new JTextField();
	     	      txt_url.setPreferredSize(new Dimension(200,25) ) ;
	     	      txt_savefile=new JTextField();
	     	     	txt_savefile.setPreferredSize(new Dimension(150,25) ) ;   
	     	     	//txt_savefile.setEnabled(false);
	     	     	   
	     	      btn_saveDialog=new JButton("...");
	     	     	
	     	     	model=new SpinnerNumberModel(3,1,10,1);
	     	     	JSpinner spn_threadId=new JSpinner(model);
	     	     	spn_threadId.setPreferredSize(new Dimension(200,25) ) ;
	     	     	      
	     	      GridBagLayout gl=new GridBagLayout();
	     	      this.setLayout(gl);
	     	      
	     	      GridBagConstraints gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=1;
	     	      gbc.gridy=1;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_url,gbc);
	     	      this.add(lab_url);	
	     	            
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=1;
	     	      gbc.gridy=2;	     	      
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_savefile,gbc);
	     	      this.add(lab_savefile); 
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=1;
	     	      gbc.gridy=3;	     	      
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_threadId,gbc);
	     	      this.add(lab_threadId); 
	     	            
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=1;
	     	      gbc.gridwidth=2;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(txt_url,gbc);
	     	      this.add(txt_url); 
	     	            
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=2;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(txt_savefile,gbc);
	     	      this.add(txt_savefile); 
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=3;
	     	      gbc.gridy=2;
	     	      gbc.insets.set(3,0,3,4);
	     	      gl.setConstraints(btn_saveDialog,gbc);
	     	      this.add(btn_saveDialog);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=3;
	     	      gbc.gridwidth=2;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(spn_threadId,gbc);
	     	      this.add(spn_threadId);
	     	       
	     	      this.setBorder(BorderFactory.createEtchedBorder());  	      
	     }	
}	
	     
/**
 *功能：对话框下方按钮工具条
*/ 
class Tool_pane extends JPanel
{
	    JButton btn_OK;
	    JButton btnClose;	    
	    public Tool_pane()
	    {
	           super();
	     	     ini();
	    }
	     	     
	    private void ini()
	    {
	     	      btn_OK=new JButton("确定");
	     	      btnClose=new JButton("取消");	     	            
	     	      
	     	      JPanel tmp_pane =new JPanel();
	     	      tmp_pane.setLayout(new FlowLayout(FlowLayout.RIGHT,10,4));
	     	      tmp_pane.add(btn_OK);
	     	      tmp_pane.add(btnClose);		     	      
	     	      this.setLayout(new FlowLayout(FlowLayout.RIGHT,30,2));
	     	      this.add(tmp_pane);  
	     	      this.setBorder(BorderFactory.createEtchedBorder());  
	    }		
}			 