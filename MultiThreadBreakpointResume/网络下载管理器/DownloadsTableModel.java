import java.util.Observer;
/*
 * �������� 2005-4-6
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
/**
 * @author Administrator
 *
 * TODO ���ع������б�
 * �������ر������
 */
class DownloadsTableModel extends AbstractTableModel implements Observer {
	//���ر�����
	private static final String[] columnNames = {
			"URL��ַ",
			"��С",
			"����",
			"״̬"
	};
	//ÿһ���е�����
	private static final Class[] columnClasses = {
			String.class,
			String.class,
			JProgressBar.class,
			String.class
	};
	//���ر��嵥
	private ArrayList downloadList = new ArrayList();
	//���б����һ����������
	public void addDownload(Download download){
		//��ʵ��ִ�������������ע��,�Ա��������״̬�ı仯֪ͨ
		download.addObserver(this);
		downloadList.add(download);
		//���м���һ��
		fireTableRowsInserted(getRowCount()-1,getRowCount()-1);
	}
	//��ָ���л��һ����������
	public Download getDownload(int row){
		return (Download) downloadList.get(row);
	}
	//�ӱ����Ƴ�һ����������
	public void clearDownload(int row){
		//���б���ɾ��
		downloadList.remove(row);
		//�ӱ����ɾ��
		fireTableRowsDeleted(row,row);
	}
	//��ȡ��������
	public int getColumnCount(){
		return columnNames.length;
	}
	//��ȡĳһ������
	public String getColumnName(int col){
		return columnNames[col];
	}
	//���ĳһ������
	public Class getColumnClass(int col){
		return columnClasses[col];
	}
	//��ñ��е�����
	public int getRowCount(){
		return downloadList.size();
	}
	//���ָ����,���е�һ��ֵ����
	public Object getValueAt(int row,int col){
		Download download = (Download) downloadList.get(row);
		switch(col){
		case 0://URL
			return download.getUrl();
		case 1://��С
			int size = download.getSize();
			return (size == -1) ? "" : Integer.toString(size);
		case 2://����
			return new Float(download.getProgress());
		case 3://״̬
			return Download.STATUSES[download.getStatus()];
		}
		return "";
	}
	/* ��һ������ʵ�������ı�ʱ,���ô˷���,������ʾ
	 * �˷���ΪDownload����չ��Observable���Զ����� 
	 */
	public void update(Observable o,Object arg){
		int index = downloadList.indexOf(o);
		//������ʾ����Ѹ��µ���������
		fireTableRowsUpdated(index,index);
	}
}
