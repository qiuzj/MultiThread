package globleGet.download;

/**
 *功能：下载线程类
*/

import java.io.*;
import java.net.*;

public class DownLoadThread extends Thread
{
       private InputStream in; //网络下载文件输入流
       private RandomAccessFile out; //下载文件输出流
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
        *功能：获取线程下载是否结束标志
        *返回值:boolean
       */
       public boolean isFinished()
       {
       	      return finished;
       }	
       
       /**
        *功能：获取线程下载是否出错标志
        *返回值:boolean
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
                 System.err.println("资源文件["+downInfo.getDownURL()+"]线程"+threadId+"报文申请错误！");
                 err=true;
                 return; 
              } 
              
              byte [] buffer=new byte[1024]; 
              int offset=0; 
              long localSize=0; 
              long step=0;
              System.out.println("资源文件["+downInfo.getDownURL()+"]线程"+threadId+"开始下载"); 
              try 
              { 
              	    out.seek(startP);
              	    step=(block>1024)?1024:block;
                    while ((offset = in.read(buffer,0,(int)step)) >0&&localSize<=block) 
                    { 
                           if(downInfo.isError())break;//判断其它线程是否出错，如果出错，跳出循环，结束下载线程
                           out.write(buffer,0,offset); 
                           localSize+=offset; 
                           //修改文件下载线程进度信息
                           {
                             downInfo.getDownPos()[threadId][0]=startP+localSize;
                             downInfo.getDownPos()[threadId][1]=block-localSize;
                           }  
                           step=block-localSize;
                           step=(step>1024)?1024:step;
                           //更改下载文件长度
                           downInfo.setDownLength(offset);
                    } 
                    out.close(); 
                    in.close(); 
                    http.disconnect();
                    if (!downInfo.isError())
                    {
                       finished=true;
                       System.out.println("资源文件["+downInfo.getDownURL()+"]线程"+threadId+"完成下载");                    
                    }
              } 
              catch(Exception e)
              { 
                    System.out.println("资源文件["+downInfo.getDownURL()+"]线程"+threadId+"下载出错"); 
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
	