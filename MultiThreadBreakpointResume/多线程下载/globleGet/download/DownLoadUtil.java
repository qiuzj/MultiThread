package globleGet.download;

/**
 *���ܣ�����URL��Ч�ԣ���ȡURL��Դ��Ϣ
*/
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class DownLoadUtil
{
	     /**
	      *���ܣ�����URL���ӣ������ӽ��д��TestResult���͵��������
	      *�������ͣ�TestResult(�Զ�����)
	     */
	     public static TestResult TestURL(String tmp)
	     {
	     	      TestResult r=new TestResult();
	     	      URL tmp_URL;
	     	      try
	     	      {
	     	      	    System.out.println("��������URL["+tmp+"]");
	     	      	    tmp_URL=new URL(tmp);
	     	      	    HttpURLConnection http=(HttpURLConnection)tmp_URL.openConnection();
	     	      	    if (http.getResponseCode()>=400)
	     	      	    {
	     	      	    	  System.out.println("��Դ["+tmp_URL+"]��������Ӧ����");
	     	      	    	  r.myUrl=null;
	     	      	        r.myResult=-1;
	     	      	        return r;
	     	      	    }
	     	      	    else
	     	      	    {
	     	      	    	  System.out.println("��Դ["+tmp_URL+"]���ӳɹ���");
	     	      	    	  r.myUrl=tmp_URL;
	     	      	        r.myResult=1;
	     	      	        //��ȡ�ļ�����
	     	      	        r.fileLength=http.getContentLength();
	     	      	        if (r.fileLength==-1)
	     	      	        {
	     	      	        	  System.out.println("��Դ["+tmp+"]�޷���֪���ļ����ȣ�"); 
	     	      	        }
	     	      	        else
	     	      	        {
	     	      	        	  System.out.println("��Դ["+tmp+"]�ļ���СΪ"+Math.round(r.fileLength/1024*100)/100.0+"k��");
	     	      	        }
	     	      	        //��ȡ�ļ���
	     	      	        r.fileName=getFileName(http);	
	     	      	        System.out.println("��Դ["+tmp+"]�ļ���Ϊ"+r.fileName);
	     	      	        //��ȡMIME�б�
	     	      	        r.http_MIME=getMIME(http);	
	     	      	        return r;
	     	      	    }		
	     	      }
	     	      catch(MalformedURLException e1)
	     	      {
	     	      	    System.out.println("��Դ["+tmp+"]��ʽ����");
	     	      	    r.myUrl=null;
	     	      	    r.myResult=-2;
	     	      	    return r;
	     	      }
	     	      catch(IOException e2)
	     	      {
	     	      	    System.out.println("��Դ["+tmp+"]���Ӵ���");
	     	      	    r.myUrl=null;
	     	      	    r.myResult=-3;
	     	      	    return r;
	     	      }			
	     }
	     
	     /**
	      *���ܣ���ȡ�����ļ�������
	      *����ֵ��String
	     */
	     private static String getFileName(HttpURLConnection http)
	     {
	     	       String filename=http.getURL().getFile();
	     	       return filename;//.substring(filename.lastIndexOf("/")+1);
	     }
	     
	     /**
	      *���ܣ���ȡURL����ʱ��MIME��Ϣ
	      *����ֵ��HashMap<String,String>
	     */
	     private static HashMap<String,String> getMIME(HttpURLConnection http)
	     {
	     	      HashMap<String,String> http_MIME=new HashMap<String,String>();
	     	      System.out.println("��Դ["+http.getURL()+"]MIME��Ϣ��");
	     	      System.out.println("=====================��Ϣ��ʼ=======================");
	     	      for(int i=0;;i++)
	     	      { 
                  String mine=http.getHeaderField(i); 
                  if(mine==null)break;                  
                  System.out.println(http.getHeaderFieldKey(i)+":"+mine); 
                  http_MIME.put(http.getHeaderFieldKey(i),http.getHeaderField(i));
              } 
              System.out.println("=====================��Ϣ����=======================");
              return http_MIME;
	     }			
}	

/**
 *���ܣ���¼URL���Ӳ��Խ����Ϣ 
*/
class TestResult implements Serializable
{
	    public URL myUrl;
	    public int myResult;
	    public int fileLength;
	    public String fileName;
	    public HashMap<String,String> http_MIME;
}	