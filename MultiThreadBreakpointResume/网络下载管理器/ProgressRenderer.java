
/*
 * 创建日期 2005-4-5
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
//import javax.swing.table.*;
/**
 * @author 姜周扬
 *
 * TODO Download实例中下载任务当前完成进度
 * 扩展了不起JProgressBar
 */
class ProgressRenderer extends JProgressBar implements TableCellRenderer {
	//构造函数
	public ProgressRenderer(int min,int max){
		super(min,max);
	}
	//返回这个进度条给调用它的Table Cell
	public Component getTableCellRendererComponent(
			JTable table,Object value,boolean isSelected,
			boolean hasFocus,int row,int column){
		//设置当前进度百分比
		setValue((int)((Float)value).floatValue());
		//返回本进度实例
		return this;
	}
}
