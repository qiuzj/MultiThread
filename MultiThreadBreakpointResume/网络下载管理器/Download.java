/*
 * 创建日期 2005-4-5
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * @author 姜周扬
 *
 * TODO 实际完成下载工作的类,使用一个线程下载一个任务
 * 同时可执行多个下载任务
 * 从指定URL下载文件
 */
class Download extends Observable implements Runnable {
	//下载缓冲区最大限制
	private static final int MAX_BUFFER_SIZE = 1024;
	//下载状态字
	public static final String STATUSES[]={
			"下载",
			"暂停",
			"完成",
			"取消",
			"错误"
	};
	//下载状态字内部码
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	//下载地址URL
	private URL url;
	//下载文件字节数大小
	private int size;
	//已经下载的字节数大小
	private int downloaded;
	//当前下载状态
	private int status;
	//DOWNLOAD构造函数
	public Download(URL url){
		this.url = url;
		size = -1;//文件大小未知
		downloaded = 0;
		status = DOWNLOADING;//设为下载状态
		//开始下载
		download();
	}
	//获得下载地址URL
	public String getUrl(){
		return url.toString();
	}
	//获得下载文件大小
	public int getSize(){
		return size;
	}
	//获得已下载文件的比例大小
	public float getProgress(){
		return ((float)downloaded/size)*100;
	}
	//获得此下载状态
	public int getStatus(){
		return status;
	}
	//暂停下载
	public void pause(){
		status = PAUSED;
		stateChanged();
	}
	//继续开始下载
	public void resume(){
		status = DOWNLOADING;
		stateChanged();
		download();
	}
	//取消下载
	public void cancel(){
		status = CANCELLED;
		stateChanged();
	}
	//标记此下载发生错误
	public void error(){
		status = ERROR;
		stateChanged();
	}
	//开始或重新开始下载
	private void download(){
		Thread thread = new Thread(this);
		thread.start();
	}
	//获得下载文件名
	private String getFileName(URL url){
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/')+1);
	}
	//下载文件线程开始
	public void run(){
		RandomAccessFile file = null;
		InputStream stream = null;
		
		try{
			//打开URL连接
			//Http1.1支持断点续传:Http1.0不支持
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//设置连接请求属性:断点续传关键所在
			//Range 为:开始字节数-终止字节数
			connection.setRequestProperty("Range","bytes=" + downloaded + '-');
			//连接到服务器
			connection.connect();
			//服务器返回100的整数为反应:200表示成功连接
			if(connection.getResponseCode()/100 != 2){
				error();
			}
			//获得下载文件的长度
			int contentLength = connection.getContentLength();
			if(contentLength < 1){
				error();
			}
			//设置size的值,如果还没设置的话
			if(size == -1){
				size = contentLength;
				stateChanged();
			}
			//打开文件,并且找到文件的末尾
			file = new RandomAccessFile(getFileName(url),"rw");
			file.seek(downloaded);
			//获取网络文件输入流
			while(status == DOWNLOADING){
				/* 缓冲区的大小设置为未下载的大小
				 * 如果文件大小超过缓冲区,则设置为最大限定值
				 */
				byte buffer[];
				if(size - downloaded > MAX_BUFFER_SIZE){
					buffer = new byte[MAX_BUFFER_SIZE];
				}
				else{
					buffer = new byte[size - downloaded];
				}
				//从服务器向缓冲区读数据
				int read = stream.read(buffer);
				if(read == -1)
					break;
				//将缓冲区中数据写入文件
				file.write(buffer,0,read);
				downloaded += read;
				stateChanged();
			}
			/* 如果文件下载完毕,退出循环时状态仍为DOWNLOADING
			 * 设状态为完成态
			 */
			if(status == DOWNLOADING){
				status = COMPLETE;
				stateChanged();
			}
		}
		catch(Exception e){
			error();
		}
		finally{
			//关闭文件
			if(file != null){
				try{
					file.close();
				}catch(Exception e){}
			}
			//关闭连接
			if(stream != null){
				try{
					stream.close();
				}catch(Exception e){}
			}
		}
	}
	//向已经注册到这个下载实例的类发送状态改变消息
	private void stateChanged(){
		setChanged();
		notifyObservers();
	}
	
}
