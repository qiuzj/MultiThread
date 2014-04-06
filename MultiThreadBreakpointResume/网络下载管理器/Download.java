/*
 * �������� 2005-4-5
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * @author ������
 *
 * TODO ʵ��������ع�������,ʹ��һ���߳�����һ������
 * ͬʱ��ִ�ж����������
 * ��ָ��URL�����ļ�
 */
class Download extends Observable implements Runnable {
	//���ػ������������
	private static final int MAX_BUFFER_SIZE = 1024;
	//����״̬��
	public static final String STATUSES[]={
			"����",
			"��ͣ",
			"���",
			"ȡ��",
			"����"
	};
	//����״̬���ڲ���
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	//���ص�ַURL
	private URL url;
	//�����ļ��ֽ�����С
	private int size;
	//�Ѿ����ص��ֽ�����С
	private int downloaded;
	//��ǰ����״̬
	private int status;
	//DOWNLOAD���캯��
	public Download(URL url){
		this.url = url;
		size = -1;//�ļ���Сδ֪
		downloaded = 0;
		status = DOWNLOADING;//��Ϊ����״̬
		//��ʼ����
		download();
	}
	//������ص�ַURL
	public String getUrl(){
		return url.toString();
	}
	//��������ļ���С
	public int getSize(){
		return size;
	}
	//����������ļ��ı�����С
	public float getProgress(){
		return ((float)downloaded/size)*100;
	}
	//��ô�����״̬
	public int getStatus(){
		return status;
	}
	//��ͣ����
	public void pause(){
		status = PAUSED;
		stateChanged();
	}
	//������ʼ����
	public void resume(){
		status = DOWNLOADING;
		stateChanged();
		download();
	}
	//ȡ������
	public void cancel(){
		status = CANCELLED;
		stateChanged();
	}
	//��Ǵ����ط�������
	public void error(){
		status = ERROR;
		stateChanged();
	}
	//��ʼ�����¿�ʼ����
	private void download(){
		Thread thread = new Thread(this);
		thread.start();
	}
	//��������ļ���
	private String getFileName(URL url){
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/')+1);
	}
	//�����ļ��߳̿�ʼ
	public void run(){
		RandomAccessFile file = null;
		InputStream stream = null;
		
		try{
			//��URL����
			//Http1.1֧�ֶϵ�����:Http1.0��֧��
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//����������������:�ϵ������ؼ�����
			//Range Ϊ:��ʼ�ֽ���-��ֹ�ֽ���
			connection.setRequestProperty("Range","bytes=" + downloaded + '-');
			//���ӵ�������
			connection.connect();
			//����������100������Ϊ��Ӧ:200��ʾ�ɹ�����
			if(connection.getResponseCode()/100 != 2){
				error();
			}
			//��������ļ��ĳ���
			int contentLength = connection.getContentLength();
			if(contentLength < 1){
				error();
			}
			//����size��ֵ,�����û���õĻ�
			if(size == -1){
				size = contentLength;
				stateChanged();
			}
			//���ļ�,�����ҵ��ļ���ĩβ
			file = new RandomAccessFile(getFileName(url),"rw");
			file.seek(downloaded);
			//��ȡ�����ļ�������
			while(status == DOWNLOADING){
				/* �������Ĵ�С����Ϊδ���صĴ�С
				 * ����ļ���С����������,������Ϊ����޶�ֵ
				 */
				byte buffer[];
				if(size - downloaded > MAX_BUFFER_SIZE){
					buffer = new byte[MAX_BUFFER_SIZE];
				}
				else{
					buffer = new byte[size - downloaded];
				}
				//�ӷ������򻺳���������
				int read = stream.read(buffer);
				if(read == -1)
					break;
				//��������������д���ļ�
				file.write(buffer,0,read);
				downloaded += read;
				stateChanged();
			}
			/* ����ļ��������,�˳�ѭ��ʱ״̬��ΪDOWNLOADING
			 * ��״̬Ϊ���̬
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
			//�ر��ļ�
			if(file != null){
				try{
					file.close();
				}catch(Exception e){}
			}
			//�ر�����
			if(stream != null){
				try{
					stream.close();
				}catch(Exception e){}
			}
		}
	}
	//���Ѿ�ע�ᵽ�������ʵ�����෢��״̬�ı���Ϣ
	private void stateChanged(){
		setChanged();
		notifyObservers();
	}
	
}
