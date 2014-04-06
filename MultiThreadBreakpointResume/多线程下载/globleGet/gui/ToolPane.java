package globleGet.gui;

/**
 *功能：主窗口下方按钮工具条
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
	      *功能：初始化工具条
	      *返回值：无
	     */
	     private void ini()
	     {
	     	      btnTask=new JButton("新建任务");
	     	      btnClose=new JButton("关闭窗口");
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