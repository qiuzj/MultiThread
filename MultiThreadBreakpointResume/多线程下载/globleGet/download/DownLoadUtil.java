package globleGet.download;

/**
 *功能：测试URL有效性，获取URL资源信息
*/
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class DownLoadUtil
{
	     /**
	      *功能：建立URL连接，将连接结果写入TestResult类型的类对象中
	      *返回类型：TestResult(自定义类)
	     */
	     public static TestResult TestURL(String tmp)
	     {
	     	      TestResult r=new TestResult();
	     	      URL tmp_URL;
	     	      try
	     	      {
	     	      	    System.out.println("正在连接URL["+tmp+"]");
	     	      	    tmp_URL=new URL(tmp);
	     	      	    HttpURLConnection http=(HttpURLConnection)tmp_URL.openConnection();
	     	      	    if (http.getResponseCode()>=400)
	     	      	    {
	     	      	    	  System.out.println("资源["+tmp_URL+"]服务器响应错误！");
	     	      	    	  r.myUrl=null;
	     	      	        r.myResult=-1;
	     	      	        return r;
	     	      	    }
	     	      	    else
	     	      	    {
	     	      	    	  System.out.println("资源["+tmp_URL+"]连接成功！");
	     	      	    	  r.myUrl=tmp_URL;
	     	      	        r.myResult=1;
	     	      	        //获取文件长度
	     	      	        r.fileLength=http.getContentLength();
	     	      	        if (r.fileLength==-1)
	     	      	        {
	     	      	        	  System.out.println("资源["+tmp+"]无法获知的文件长度！"); 
	     	      	        }
	     	      	        else
	     	      	        {
	     	      	        	  System.out.println("资源["+tmp+"]文件大小为"+Math.round(r.fileLength/1024*100)/100.0+"k。");
	     	      	        }
	     	      	        //获取文件名
	     	      	        r.fileName=getFileName(http);	
	     	      	        System.out.println("资源["+tmp+"]文件名为"+r.fileName);
	     	      	        //获取MIME列表
	     	      	        r.http_MIME=getMIME(http);	
	     	      	        return r;
	     	      	    }		
	     	      }
	     	      catch(MalformedURLException e1)
	     	      {
	     	      	    System.out.println("资源["+tmp+"]格式错误！");
	     	      	    r.myUrl=null;
	     	      	    r.myResult=-2;
	     	      	    return r;
	     	      }
	     	      catch(IOException e2)
	     	      {
	     	      	    System.out.println("资源["+tmp+"]连接错误！");
	     	      	    r.myUrl=null;
	     	      	    r.myResult=-3;
	     	      	    return r;
	     	      }			
	     }
	     
	     /**
	      *功能：获取下载文件名长度
	      *返回值：String
	     */
	     private static String getFileName(HttpURLConnection http)
	     {
	     	       String filename=http.getURL().getFile();
	     	       return filename;//.substring(filename.lastIndexOf("/")+1);
	     }
	     
	     /**
	      *功能：获取URL连接时的MIME信息
	      *返回值：HashMap<String,String>
	     */
	     private static HashMap<String,String> getMIME(HttpURLConnection http)
	     {
	     	      HashMap<String,String> http_MIME=new HashMap<String,String>();
	     	      System.out.println("资源["+http.getURL()+"]MIME信息！");
	     	      System.out.println("=====================信息开始=======================");
	     	      for(int i=0;;i++)
	     	      { 
                  String mine=http.getHeaderField(i); 
                  if(mine==null)break;                  
                  System.out.println(http.getHeaderFieldKey(i)+":"+mine); 
                  http_MIME.put(http.getHeaderFieldKey(i),http.getHeaderField(i));
              } 
              System.out.println("=====================信息结束=======================");
              return http_MIME;
	     }			
}	

/**
 *功能：记录URL连接测试结果信息 
*/
class TestResult implements Serializable
{
	    public URL myUrl;
	    public int myResult;
	    public int fileLength;
	    public String fileName;
	    public HashMap<String,String> http_MIME;
}	