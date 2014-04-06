package globleGet.download;

/**
 *功能：根据下载文件信息，创建下载线程
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
	      *功能：创建下载线程
	      *返回值：无
	     */
	     private void startDownFile()
	     {
	     	      //开始下载
	     	      down=new DownLoadThread[myDownInfo.getThreadNum()];
	     	      for(int i=0;i<down.length;i++)
	     	      {
	     	      	  down[i]=new DownLoadThread(myDownInfo,i); 
                  down[i].setPriority(7); 
                  down[i].start(); 
	     	      }	
	     }
	     
	     /**
	      *功能：调度多线程下载
	      *返回值：无
	     */
	     public void MultiDown()
	     {
	     	      //创建下载进程
	     	      startDownFile();
	     	      //创建监视线程
	     	      new ListenThread(myDownInfo,down); 	     	      
	     }	     
}	