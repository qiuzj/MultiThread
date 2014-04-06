package globleGet.download;

/**
 *功能：下载文件信息的实体类
*/
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class DownLoadInfo implements Serializable
{
	     private String saveFileName;//下载文件名
	     private String downURL;//下载文件资源名称
       private TestResult tr; //资源连接结果
	     private int threadNum; //线程数目
	     private boolean isValidate;//该下载信息受否有效
	     private long perdownLength;//上次文件下载长度   
	     private long downLength;//当前文件已下载长度	     
	     private float downSpeed;//下载速度
	     private long[][] downPos;//各个线程下载指针
	     private boolean finished;//文件下载完毕标志
	     private boolean err;//文件是否下载出错,
	     
	     /**
	     *构造函数
	     *@param 1、urlStr资源地址；2、saveFileName下载文件保存路径；3、num所开下载线程数目
	     */
	     public DownLoadInfo(String urlStr,String saveFileName,int num)
	     {
	     	      tr=DownLoadUtil.TestURL(urlStr);
	     	      if ((tr.myResult==1)&&(tr.fileLength>0)&&setSaveFile(saveFileName))
	     	      {
	     	      	  downURL=urlStr;
	     	      	  isValidate=true;
	     	      	  setThreadNum(3);
	     	      	  setDownPos();	
	     	      }
	     	      else
	     	      {
	     	      	  isValidate=false;
	     	      }	     	           	      
	     }
	     
	     /**
	      *功能：获取下载资源名称
	      *返回类型：String
	     */
	     public String getDownURL()
	     {
	     	      return downURL;
	     }
	     	     	     
	     /**
	     *功能：获取保存文件名称
	     *返回类型：String
	     */
	     public String getSaveFileName()
	     {
	     	      return saveFileName;
	     }	
	     	     	     
	     /**
	     *功能：设置文件名称。设置成功返回true,否则返回false
	     *返回类型：boolean
	     */
	     public boolean setSaveFile(String saveFileName)
	     {
	            File tmp_File=new File(saveFileName);
	            try
	            {
	                 RandomAccessFile out=new RandomAccessFile(tmp_File,"rw");
	            	   out.setLength(tr.fileLength);
	            	   out.close();
	            	   this.saveFileName=saveFileName;
	            	   return true;
	            }
	            catch(IOException e)
	            {
	                 return false;
	            }		            
	     }
	     
	     /**
	     *功能：返回下载线程数目
	     *返回类型：int
	     */
	     public int getThreadNum()
	     {
	     	      return threadNum;
	     }
	     
	     /**
	     *功能：设置下载线程数目。1-10之间的整数，如果超出范围，则强行设置为3
	     *返回类型：boolean
	     */
	     private void setThreadNum(int num)
	     {
	     	      if(!isValidate)
	     	      {
	     	      	 return;
	     	      }	
	     	      if (tr.fileName.equals(""))
	     	      {
	     	      	  threadNum=1;
	     	      	  return;
	     	      }	
	     	      if((num<=0)||(num>10))
	     	      {
	     	      	   threadNum=3; 
	     	      }
	     	      else
	     	      {
	     	      	   threadNum=num;
	     	      }		
	     }	
	     
	     /**
	     *功能：获取下载信息是否有效
	     *返回类型：boolean
	     */
	     public boolean getIsValidate()
	     {
	     	      return isValidate;
	     }	
	          
	     /**
	     *功能：获取下载文件长度
	     *返回类型：long
	     */
	     public long getFileLength()
	     {
	     	          
	     	      return tr.fileLength;
	     }
	     
	     /**
	     *功能：获取下载文件名
	     *返回类型：String
	     */
	     public String getFileName()
	     {
	     	      return tr.fileName;
	     }	
	     
	     /**
	     *功能：获取资源连接的MIME信息
	     *返回类型：Map<String,String>
	     */
	     public HashMap<String,String> getMIME()
	     {
	     	      return tr.http_MIME;
	     }	
	     
	     /**
	     *功能：获取文件下载长度
	     *返回类型：long
	     */
	     public long getDownLength()
	     {
	     	      return downLength;
	     }
	     
	     /**
	     *功能：设置文件下载长度	     
	     */
	     public void setDownLength(long len)
	     {
	     	      downLength=downLength+len;
	     }	
	     
	     /**
	     *功能：获取文件下载速度
	     *返回类型：float
	     */
	     public float getDownSpeed()
	     {
	     	      return downSpeed;
	     }
	     
	     /**
	     *功能：计算文件下载速度
	     *返回类型：Map<String,String>
	     */
	     public void setDownSpeed()
	     {
	     	      downSpeed=(downLength-perdownLength)/1024.0f;
	     	      perdownLength=downLength;
	     	      downSpeed=Math.round(downSpeed*100)/100.0f;	     	      
	     }
	     	     
	     /**
	     *功能：获取文件下载进度
	     *返回类型：Map<String,String>
	     */
	     public int getDownProgress()
	     {
	     	      return Math.round(100.0f*this.getDownLength()/this.getFileLength());
	     }
	     
	     /**
	     *功能：获取文件下载线程进度信息
	     *返回类型：long[][]
	     */
	     public long[][] getDownPos()
	     {
	     	      return downPos;
	     }	
	     
	     /**
	     *功能：设置文件线程下载进度信息	     
	     */
	     private void setDownPos()
	     {
	     	      downPos=new long[threadNum][2];
	     	      long len=tr.fileLength;
	     	      long block=(len/threadNum+1);
	     	      for(int i=0;i<threadNum;i++)
	     	      {
	     	      	  len=len-block;
	     	      	  downPos[i][0]=i*block;
	     	      	  downPos[i][1]=block;
	     	      	  if(len<0)downPos[i][1]=block+len;
	     	      }	
	     }	
	     
	     /**
	     *功能：获取文件是否下载结束信息
	     *返回类型：boolean
	     */
	     public boolean isFinished()
	     {
	     	      return finished;
	     }
	     
	     /**
	     *功能：设置文件是否下载结束标志
	     */
	     public void setFinished(boolean f)
	     {
	     	      finished=f;
	     }										
	     
	     /**
	     *功能：获取文件是否下载错误信息
	     *返回类型：boolean
	     */
	     public boolean isError()
	     {
	     	      return err;
	     }				
	     
	     /**
	     *功能：设置文件是否下载错误标志
	     */
	     public void setError(boolean e)
	     {
	     	      err=e;
	     }	
	     
	     /**
	     *功能： 重写toString方法
	     */
	     public String toString()
	     {
	     	      return saveFileName;
	     }	
}	