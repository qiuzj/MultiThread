package globleGet.download;

/**
 *���ܣ����������ļ���Ϣ�����������߳�
*/
import java.io.*;
import java.net.*;

public class MultiDownLoad
{
	     
	     public DownLoadInfo myDownInfo;
	     private DownLoadThread[] down;	     
	     
	     public MultiDownLoad(DownLoadInfo myDownInfo)
	     {
	     	      this.myDownInfo=myDownInfo;
	     }	
	     
	     /**
	      *���ܣ����������߳�
	      *����ֵ����
	     */
	     private void startDownFile()
	     {
	     	      //��ʼ����
	     	      down=new DownLoadThread[myDownInfo.getThreadNum()];
	     	      for(int i=0;i<down.length;i++)
	     	      {
	     	      	  down[i]=new DownLoadThread(myDownInfo,i); 
                  down[i].setPriority(7); 
                  down[i].start(); 
	     	      }	
	     }
	     
	     /**
	      *���ܣ����ȶ��߳�����
	      *����ֵ����
	     */
	     public void MultiDown()
	     {
	     	      //�������ؽ���
	     	      startDownFile();
	     	      //���������߳�
	     	      new ListenThread(myDownInfo,down); 	     	      
	     }	     
}	