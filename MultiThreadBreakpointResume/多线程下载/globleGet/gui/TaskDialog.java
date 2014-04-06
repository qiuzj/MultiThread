package globleGet.gui;

/**
 *���ܣ������������Ի���
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.File;

/**
*���ܣ��Ի���������
*/
public class TaskDialog 
{
      private boolean btnOK;//�Ƿ��¡�ȷ������ť
      private String urlStr;
      private String fileStr;
      private int threadId;
      private Top_pane topPan;
      private Tool_pane botPan;
      private JDialog d;
      
      public TaskDialog(JFrame f)
	    {
	           
	           d=new JDialog(f,"�����������",true);	           
	           setGUI();
	           setEvent();
	           d.setVisible(true);	
	    }
	           
	    /**
	     *���ܣ����öԻ������
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
	     *���ܣ���������ļ����¼�
	    */
	    private void setEvent()
	    {
	    	     d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    	     botPan.btnClose.addActionListener(new BtnCancelListener());
	    	     botPan.btn_OK.addActionListener(new BtnOKListener());
	    	     topPan.btn_saveDialog.addActionListener(new OpenChooserListener());
	    }	  
	    
	    /**
	     *���ܣ���ȡ�û��Ƿ����Ի����ȷ����ť
	     *�������ͣ�boolean
	    */
	    public boolean getBtn()
	    {
	    	     return btnOK;
	    }	
	    
	    /**
	     *���ܣ���ȡ�û����õ�URL��ַ
	     *�������ͣ�String
	    */
	    public String getUrlStr()
	    {
	    	     return urlStr;
	    }	
	    
	    /**
	     *���ܣ���ȡ�����ļ�����
	     *�������ͣ�String
	    */
	    public String getFileStr()
	    {
	    	     return fileStr;
	    }	
	    
	    /**
	     *���ܣ���ȡ�����߳���
	     *�������ͣ�String
	    */
	    public int getThreadId()
	    {
	    	     return threadId;
	    }	
	    
	    /*���Դ���
	    public static void main(String[] args)
	    {
	    	    new TaskDialog();
	    }	
	    */ 
	    
	    //�¼�����������
	    
	    /**
	     *���ܣ������򿪱���Ի��������
	    */ 
	    class OpenChooserListener implements ActionListener
	    {
	    	    public void actionPerformed(ActionEvent e)
	    	    {
	    	    	     //���ô򿪶Ի���
                   JFileChooser chooser=new JFileChooser() ;
                   int result=chooser.showSaveDialog(d) ;
                   if (result==JFileChooser.CANCEL_OPTION ) return;
                   topPan.txt_savefile.setText(chooser.getSelectedFile().getAbsolutePath()); 
	    	    }	
	    }	 
	    
	    /**
	    *���ܣ�����ȷ����ť������
	    */
	    class BtnOKListener implements ActionListener
	    {
	    	    public void actionPerformed(ActionEvent e)
	    	    {
	    	    	     //������֤
	    	    	     if((topPan.txt_url.getText().equals(""))||(topPan.txt_savefile.getText().equals("")))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"������URL�������ļ�����");
	    	    	     	  return;
	    	    	     }	
	    	    	     //��֤URL��ַ��ʽ�Ƿ���ȷ
	    	    	     if (!isURL(topPan.txt_url.getText()))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"URL��ʽ����");
	    	    	     	  return;
	    	    	     }	
	    	    	     //��֤�����ļ�����ʽ
	    	    	     if (!isFile(topPan.txt_savefile.getText()))
	    	    	     {
	    	    	     	  JOptionPane.showMessageDialog(d,"�����ļ�������");
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
	    	     *���ܣ���֤URL��ʽ
	    	     *����ֵ��boolean
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
	    	     *���ܣ���֤�ļ�����ʽ
	    	     *����ֵ��boolean
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
	     *���ܣ�����ȡ����ť������
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
 *���ܣ��Ի����Ϸ������е�panel
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
	     	      JLabel lab_savefile=new JLabel("�ļ���:");
	     	      JLabel lab_threadId=new JLabel("�߳�����");
	     	     	      
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
 *���ܣ��Ի����·���ť������
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
	     	      btn_OK=new JButton("ȷ��");
	     	      btnClose=new JButton("ȡ��");	     	            
	     	      
	     	      JPanel tmp_pane =new JPanel();
	     	      tmp_pane.setLayout(new FlowLayout(FlowLayout.RIGHT,10,4));
	     	      tmp_pane.add(btn_OK);
	     	      tmp_pane.add(btnClose);		     	      
	     	      this.setLayout(new FlowLayout(FlowLayout.RIGHT,30,2));
	     	      this.add(tmp_pane);  
	     	      this.setBorder(BorderFactory.createEtchedBorder());  
	    }		
}			 