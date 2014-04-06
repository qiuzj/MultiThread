package globleGet.download;

/**
 *���ܣ������߳���
*/

import java.io.*;
import java.net.*;

public class DownLoadThread extends Thread
{
       private InputStream in; //���������ļ�������
       private RandomAccessFile out; //�����ļ������
       private URL downURL;
       private long block; 
       private int threadId=-1; 
       private boolean finished=false;
       private boolean err=false;
       private DownLoadInfo downInfo;
       private long startP;
                     
       public DownLoadThread(DownLoadInfo downInfo,int threadId)
       {
       	      
              this.block=downInfo.getDownPos()[threadId][1]; 
              this.threadId=threadId;
              this.downInfo=downInfo;  
              startP=downInfo.getDownPos()[threadId][0];  	      
       }
       
       /**
        *���ܣ���ȡ�߳������Ƿ������־
        *����ֵ:boolean
       */
       public boolean isFinished()
       {
       	      return finished;
       }	
       
       /**
        *���ܣ���ȡ�߳������Ƿ�����־
        *����ֵ:boolean
       */
       public boolean isError()
       {
       	      return err;
       }	
       
       public void run()
       {
       	      HttpURLConnection http;
       	      try
       	      { 
                 downURL=new URL(downInfo.getDownURL());
                 http=(HttpURLConnection)downURL.openConnection(); 
                 String sProperty = "bytes=" + startP + "-";
                 http.setRequestProperty("Range",sProperty); 
                 in=http.getInputStream(); 
                 out=new RandomAccessFile(downInfo.getSaveFileName(),"rw"); 
              } 
              catch(IOException e)
              { 
                 System.err.println("��Դ�ļ�["+downInfo.getDownURL()+"]�߳�"+threadId+"�����������");
                 err=true;
                 return; 
              } 
              
              byte [] buffer=new byte[1024]; 
              int offset=0; 
              long localSize=0; 
              long step=0;
              System.out.println("��Դ�ļ�["+downInfo.getDownURL()+"]�߳�"+threadId+"��ʼ����"); 
              try 
              { 
              	    out.seek(startP);
              	    step=(block>1024)?1024:block;
                    while ((offset = in.read(buffer,0,(int)step)) >0&&localSize<=block) 
                    { 
                           if(downInfo.isError())break;//�ж������߳��Ƿ���������������ѭ�������������߳�
                           out.write(buffer,0,offset); 
                           localSize+=offset; 
                           //�޸��ļ������߳̽�����Ϣ
                           {
                             downInfo.getDownPos()[threadId][0]=startP+localSize;
                             downInfo.getDownPos()[threadId][1]=block-localSize;
                           }  
                           step=block-localSize;
                           step=(step>1024)?1024:step;
                           //���������ļ�����
                           downInfo.setDownLength(offset);
                    } 
                    out.close(); 
                    in.close(); 
                    http.disconnect();
                    if (!downInfo.isError())
                    {
                       finished=true;
                       System.out.println("��Դ�ļ�["+downInfo.getDownURL()+"]�߳�"+threadId+"�������");                    
                    }
              } 
              catch(Exception e)
              { 
                    System.out.println("��Դ�ļ�["+downInfo.getDownURL()+"]�߳�"+threadId+"���س���"); 
                    err=true;
                    finished=true;
                    try
                    {
                    	  http.disconnect();                    	  
                        in.close(); 
                        out.close(); 
                    }
                    catch(Exception e1)
                    {}		
              }               
       }	      	
}
	