package globleGet.gui;

/**
 *���ܣ��������·���ť������
*/ 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ToolPane extends JPanel
{
	     public JButton btnTask;
	     public JButton btnClose;
	     
	     public ToolPane()
	     {
	     	      super();
	     	      ini();
	     }	
	     
	     /**
	      *���ܣ���ʼ��������
	      *����ֵ����
	     */
	     private void ini()
	     {
	     	      btnTask=new JButton("�½�����");
	     	      btnClose=new JButton("�رմ���");
	     	      /*
	     	      btnClose.addActionListener(new ActionListener(){
	     	          
	     	          public void actionPerformed(ActionEvent e)
	     	          {
	     	          	     System.exit(0);
	     	          }	
	     	          	
	     	      });
	     	      */
	     	      JPanel tmp_pane =new JPanel();
	     	      tmp_pane.setLayout(new FlowLayout(FlowLayout.RIGHT,10,8));
	     	      tmp_pane.add(btnTask);
	     	      tmp_pane.add(btnClose);		     	      
	     	      this.setLayout(new FlowLayout(FlowLayout.RIGHT,30,2));
	     	      this.add(tmp_pane);  
	     	      this.setBorder(BorderFactory.createEtchedBorder());  	      
	     }	
}	