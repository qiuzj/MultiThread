package globleGet.download;

/**
 *功能：监听文件线程下载状态，该线程创建定时器，每秒执行一次，用于计算下载速度
*/
import java.util.Timer;
import java.util.TimerTask;

public class ListenThread 
{
       private DownLoadInfo downInfo;
       private Timer tmr;
       private boolean isFinished;
       private boolean isError;
       private DownLoadThread[] down;           
          
       public ListenThread(DownLoadInfo downInfo,DownLoadThread[] down)
       {
       	      this.downInfo=downInfo;
       	      this.down=down;       	      
       	      tmr=new Timer();
       	      tmr.schedule(new MyTimerTask(),0,1000);
       }	
                    
       /**
        *功能：监听下载线程是否结束或出错
        *返回值:无
       */
       final private void listenFinished()
       {
       	      boolean flag=true; 
	     	      for(int i=0;i<down.length;i++)
	     	      {
	     	      	  if(down[i].isFinished()==false)
	     	      	  {
	     	      	    	flag=false;	     	      	    	
	     	      	  }
	     	      	  if(down[i].isError()==true)
	     	      	  {
	     	      	  	 isError=true;
	     	      	  	 break; 
	     	      	  }		
	     	      }	
	     	      isFinished=flag;	     	           	      	
       }	
       
       /**
        *功能：内部类实现定时器
       */
       class MyTimerTask	extends TimerTask
       {
       	     public void run()
       	     {
       	     	      //获取线程状态信息
       	     	      listenFinished();
       	     	      if (isFinished)//如果下载结束，关闭定时器，更新文件下载信息标志
       	     	      {
       	     	      	  tmr.cancel();
       	     	      	  downInfo.setFinished(true);       	     	      	  
       	     	      	  System.out.println("资源["+downInfo.getDownURL()+"]下载完毕！,文件保存在"+downInfo.getSaveFileName());
       	     	      }
       	     	      else //如果还在下载中，察看线程下载是否出错
       	     	      {
       	     	      	  if(isError)//如果出错，关闭定时器，更新文件下载标志，以便结束所有下载线程
       	     	      	  {
       	     	      	  	   tmr.cancel();
       	     	      	  	   downInfo.setError(true);       	     	      	  
       	     	      	       System.out.println("资源["+downInfo.getDownURL()+"]下载错误！");   
       	     	      	  }
       	     	      	  else//如果没出错，计算下载速度
       	     	      	  {	
       	     	      	      downInfo.setDownSpeed();       	     	      	  
       	     	      	      System.out.println("资源["+downInfo.getDownURL()+"]文件长度="+downInfo.getFileLength()+"byte  已下载长度="+downInfo.getDownLength()+"byte  下载速度="+downInfo.getDownSpeed() +"k  下载进度="+downInfo.getDownProgress());
       	     	          }   
       	     	      }	       	     	      
       	     }	
       }	
}
	
