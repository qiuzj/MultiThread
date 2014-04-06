package globleGet.download;

/**
 *���ܣ������ļ���Ϣ��ʵ����
*/
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class DownLoadInfo implements Serializable
{
	     private String saveFileName;//�����ļ���
	     private String downURL;//�����ļ���Դ����
       private TestResult tr; //��Դ���ӽ��
	     private int threadNum; //�߳���Ŀ
	     private boolean isValidate;//��������Ϣ�ܷ���Ч
	     private long perdownLength;//�ϴ��ļ����س���   
	     private long downLength;//��ǰ�ļ������س���	     
	     private float downSpeed;//�����ٶ�
	     private long[][] downPos;//�����߳�����ָ��
	     private boolean finished;//�ļ�������ϱ�־
	     private boolean err;//�ļ��Ƿ����س���,
	     
	     /**
	     *���캯��
	     *@param 1��urlStr��Դ��ַ��2��saveFileName�����ļ�����·����3��num���������߳���Ŀ
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
	      *���ܣ���ȡ������Դ����
	      *�������ͣ�String
	     */
	     public String getDownURL()
	     {
	     	      return downURL;
	     }
	     	     	     
	     /**
	     *���ܣ���ȡ�����ļ�����
	     *�������ͣ�String
	     */
	     public String getSaveFileName()
	     {
	     	      return saveFileName;
	     }	
	     	     	     
	     /**
	     *���ܣ������ļ����ơ����óɹ�����true,���򷵻�false
	     *�������ͣ�boolean
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
	     *���ܣ����������߳���Ŀ
	     *�������ͣ�int
	     */
	     public int getThreadNum()
	     {
	     	      return threadNum;
	     }
	     
	     /**
	     *���ܣ����������߳���Ŀ��1-10֮������������������Χ����ǿ������Ϊ3
	     *�������ͣ�boolean
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
	     *���ܣ���ȡ������Ϣ�Ƿ���Ч
	     *�������ͣ�boolean
	     */
	     public boolean getIsValidate()
	     {
	     	      return isValidate;
	     }	
	          
	     /**
	     *���ܣ���ȡ�����ļ�����
	     *�������ͣ�long
	     */
	     public long getFileLength()
	     {
	     	          
	     	      return tr.fileLength;
	     }
	     
	     /**
	     *���ܣ���ȡ�����ļ���
	     *�������ͣ�String
	     */
	     public String getFileName()
	     {
	     	      return tr.fileName;
	     }	
	     
	     /**
	     *���ܣ���ȡ��Դ���ӵ�MIME��Ϣ
	     *�������ͣ�Map<String,String>
	     */
	     public HashMap<String,String> getMIME()
	     {
	     	      return tr.http_MIME;
	     }	
	     
	     /**
	     *���ܣ���ȡ�ļ����س���
	     *�������ͣ�long
	     */
	     public long getDownLength()
	     {
	     	      return downLength;
	     }
	     
	     /**
	     *���ܣ������ļ����س���	     
	     */
	     public void setDownLength(long len)
	     {
	     	      downLength=downLength+len;
	     }	
	     
	     /**
	     *���ܣ���ȡ�ļ������ٶ�
	     *�������ͣ�float
	     */
	     public float getDownSpeed()
	     {
	     	      return downSpeed;
	     }
	     
	     /**
	     *���ܣ������ļ������ٶ�
	     *�������ͣ�Map<String,String>
	     */
	     public void setDownSpeed()
	     {
	     	      downSpeed=(downLength-perdownLength)/1024.0f;
	     	      perdownLength=downLength;
	     	      downSpeed=Math.round(downSpeed*100)/100.0f;	     	      
	     }
	     	     
	     /**
	     *���ܣ���ȡ�ļ����ؽ���
	     *�������ͣ�Map<String,String>
	     */
	     public int getDownProgress()
	     {
	     	      return Math.round(100.0f*this.getDownLength()/this.getFileLength());
	     }
	     
	     /**
	     *���ܣ���ȡ�ļ������߳̽�����Ϣ
	     *�������ͣ�long[][]
	     */
	     public long[][] getDownPos()
	     {
	     	      return downPos;
	     }	
	     
	     /**
	     *���ܣ������ļ��߳����ؽ�����Ϣ	     
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
	     *���ܣ���ȡ�ļ��Ƿ����ؽ�����Ϣ
	     *�������ͣ�boolean
	     */
	     public boolean isFinished()
	     {
	     	      return finished;
	     }
	     
	     /**
	     *���ܣ������ļ��Ƿ����ؽ�����־
	     */
	     public void setFinished(boolean f)
	     {
	     	      finished=f;
	     }										
	     
	     /**
	     *���ܣ���ȡ�ļ��Ƿ����ش�����Ϣ
	     *�������ͣ�boolean
	     */
	     public boolean isError()
	     {
	     	      return err;
	     }				
	     
	     /**
	     *���ܣ������ļ��Ƿ����ش����־
	     */
	     public void setError(boolean e)
	     {
	     	      err=e;
	     }	
	     
	     /**
	     *���ܣ� ��дtoString����
	     */
	     public String toString()
	     {
	     	      return saveFileName;
	     }	
}	