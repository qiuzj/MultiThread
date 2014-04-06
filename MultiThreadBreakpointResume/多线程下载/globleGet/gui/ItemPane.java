package globleGet.gui;

/**
 *���ܣ������ļ�������� 
*/
import javax.swing.*;
import java.awt.*;

public class ItemPane extends JPanel
{
	     public JLabel lab_filename;
	     public JLabel lab_filesize;
	     public DefaultBoundedRangeModel  pbar_model;
	     public JProgressBar pbar;
	     public JLabel lab_downspeed;
	     public JLabel lab_threadnum;
	     public JLabel lab_filepath;
	     public JLabel lab_url;
	     public JLabel lab_filestate; 
	     
	     public ItemPane()
	     {
	     	      super();
	     	      ini();
	     }	
	     
	     /**
	      *���ܣ���ʼ����岼��
	     */
	     private void ini()
	     {
	     	      JLabel lab_Lfilename=new JLabel("�ļ����ƣ�");
	     	      JLabel lab_Lfilesize=new JLabel("�ļ���С��");
	     	      JLabel lab_Lfileprogress=new JLabel("���ؽ��ȣ�");
	     	      JLabel lab_Ldownspeed=new JLabel("�����ٶȣ�");
	            JLabel lab_Lthreadnum=new JLabel("�߳���Ŀ��");
	     	      JLabel lab_Lfilepath=new JLabel("���λ�ã�");
	     	      JLabel lab_Lurl=new JLabel("������Դ��");
	     	      JLabel lab_Lfilestate=new JLabel("����״̬��");
	     	      
	     	      lab_filename=new JLabel("δ֪�ļ�");
	     	      lab_filesize=new JLabel("0k");
	     	      pbar_model=new DefaultBoundedRangeModel(100,0,0,100);
	     	      pbar=new JProgressBar(pbar_model);
	     	      pbar.setBackground (Color.white);
              //pbar.setForeground (Color.blue);
              pbar.setStringPainted(true);
	     	      pbar.setString("���ؽ���"+pbar.getValue()+"%");
	     	      lab_downspeed=new JLabel("0s/k");
	            lab_threadnum=new JLabel("3");
	     	      lab_filepath=new JLabel("c:\\download");
	     	      //lab_filepath.setMaximumSize(new Dimension(260,50));
	     	      lab_url=new JLabel("");
	     	      //lab_filepath.setMaximumSize(new Dimension(260,50));
	     	      lab_filestate=new JLabel("ֹͣ");	     	      
	     	      
	     	      GridBagLayout gl=new GridBagLayout();
	     	      this.setLayout(gl);
	     	      
	     	      GridBagConstraints gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=1;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lfilename,gbc);
	     	      this.add(lab_Lfilename);	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=1;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_filename,gbc);
	     	      this.add(lab_filename);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=2;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lfilesize,gbc);
	     	      this.add(lab_Lfilesize);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=2;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_filesize,gbc);
	     	      this.add(lab_filesize);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=3;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lfileprogress,gbc);
	     	      this.add(lab_Lfileprogress);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=3;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(pbar,gbc);
	     	      this.add(pbar);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=4;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Ldownspeed,gbc);
	     	      this.add(lab_Ldownspeed);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=4;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_downspeed,gbc);
	     	      this.add(lab_downspeed);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=5;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lthreadnum,gbc);
	     	      this.add(lab_Lthreadnum);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=5;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_threadnum,gbc);
	     	      this.add(lab_threadnum);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=6;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lfilepath,gbc);
	     	      this.add(lab_Lfilepath);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=6;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_filepath,gbc);
	     	      this.add(lab_filepath);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=7;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lurl,gbc);
	     	      this.add(lab_Lurl);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=7;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_url,gbc);
	     	      this.add(lab_url);
	     	      
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.CENTER;
	     	      gbc.gridx=1;
	     	      gbc.gridy=8;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_Lfilestate,gbc);
	     	      this.add(lab_Lfilestate);
	     	      gbc=new GridBagConstraints();
	     	      gbc.anchor=GridBagConstraints.WEST;
	     	      gbc.gridx=2;
	     	      gbc.gridy=8;
	     	      gbc.insets.set(3,4,3,4);
	     	      gl.setConstraints(lab_filestate,gbc);
	     	      this.add(lab_filestate);	      
	     	      this.setBorder(BorderFactory.createEtchedBorder());
	     }	
}	