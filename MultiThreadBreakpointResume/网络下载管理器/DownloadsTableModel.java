import java.util.Observer;
/*
 * 创建日期 2005-4-6
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
/**
 * @author Administrator
 *
 * TODO 下载管理器列表
 * 管理下载表格数据
 */
class DownloadsTableModel extends AbstractTableModel implements Observer {
	//下载表列名
	private static final String[] columnNames = {
			"URL地址",
			"大小",
			"进度",
			"状态"
	};
	//每一个列的类型
	private static final Class[] columnClasses = {
			String.class,
			String.class,
			JProgressBar.class,
			String.class
	};
	//下载表清单
	private ArrayList downloadList = new ArrayList();
	//向列表添加一个下载任务
	public void addDownload(Download download){
		//向实际执行下载任务的类注册,以便接收下载状态的变化通知
		download.addObserver(this);
		downloadList.add(download);
		//表中加入一行
		fireTableRowsInserted(getRowCount()-1,getRowCount()-1);
	}
	//从指定行获得一个下载任务
	public Download getDownload(int row){
		return (Download) downloadList.get(row);
	}
	//从表中移除一个下载任务
	public void clearDownload(int row){
		//从列表中删除
		downloadList.remove(row);
		//从表格中删除
		fireTableRowsDeleted(row,row);
	}
	//获取表中列数
	public int getColumnCount(){
		return columnNames.length;
	}
	//获取某一列名称
	public String getColumnName(int col){
		return columnNames[col];
	}
	//获得某一列类型
	public Class getColumnClass(int col){
		return columnClasses[col];
	}
	//获得表中的行数
	public int getRowCount(){
		return downloadList.size();
	}
	//获得指定行,列中的一个值特征
	public Object getValueAt(int row,int col){
		Download download = (Download) downloadList.get(row);
		switch(col){
		case 0://URL
			return download.getUrl();
		case 1://大小
			int size = download.getSize();
			return (size == -1) ? "" : Integer.toString(size);
		case 2://进度
			return new Float(download.getProgress());
		case 3://状态
			return Download.STATUSES[download.getStatus()];
		}
		return "";
	}
	/* 当一个下载实例发生改变时,调用此方法,更新显示
	 * 此方法为Download类扩展的Observable类自动调用 
	 */
	public void update(Observable o,Object arg){
		int index = downloadList.indexOf(o);
		//更新显示这个已更新的下载任务
		fireTableRowsUpdated(index,index);
	}
}
