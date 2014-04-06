package globleGet.download;

/**
 *���ܣ������ļ��߳�����״̬�����̴߳�����ʱ����ÿ��ִ��һ�Σ����ڼ��������ٶ�
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
        *���ܣ����������߳��Ƿ���������
        *����ֵ:��
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
        *���ܣ��ڲ���ʵ�ֶ�ʱ��
       */
       class MyTimerTask	extends TimerTask
       {
       	     public void run()
       	     {
       	     	      //��ȡ�߳�״̬��Ϣ
       	     	      listenFinished();
       	     	      if (isFinished)//������ؽ������رն�ʱ���������ļ�������Ϣ��־
       	     	      {
       	     	      	  tmr.cancel();
       	     	      	  downInfo.setFinished(true);       	     	      	  
       	     	      	  System.out.println("��Դ["+downInfo.getDownURL()+"]������ϣ�,�ļ�������"+downInfo.getSaveFileName());
       	     	      }
       	     	      else //������������У��쿴�߳������Ƿ����
       	     	      {
       	     	      	  if(isError)//��������رն�ʱ���������ļ����ر�־���Ա�������������߳�
       	     	      	  {
       	     	      	  	   tmr.cancel();
       	     	      	  	   downInfo.setError(true);       	     	      	  
       	     	      	       System.out.println("��Դ["+downInfo.getDownURL()+"]���ش���");   
       	     	      	  }
       	     	      	  else//���û�������������ٶ�
       	     	      	  {	
       	     	      	      downInfo.setDownSpeed();       	     	      	  
       	     	      	      System.out.println("��Դ["+downInfo.getDownURL()+"]�ļ�����="+downInfo.getFileLength()+"byte  �����س���="+downInfo.getDownLength()+"byte  �����ٶ�="+downInfo.getDownSpeed() +"k  ���ؽ���="+downInfo.getDownProgress());
       	     	          }   
       	     	      }	       	     	      
       	     }	
       }	
}
	
