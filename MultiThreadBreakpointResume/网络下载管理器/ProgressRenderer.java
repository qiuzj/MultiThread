
/*
 * �������� 2005-4-5
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
//import javax.swing.table.*;
/**
 * @author ������
 *
 * TODO Downloadʵ������������ǰ��ɽ���
 * ��չ�˲���JProgressBar
 */
class ProgressRenderer extends JProgressBar implements TableCellRenderer {
	//���캯��
	public ProgressRenderer(int min,int max){
		super(min,max);
	}
	//�����������������������Table Cell
	public Component getTableCellRendererComponent(
			JTable table,Object value,boolean isSelected,
			boolean hasFocus,int row,int column){
		//���õ�ǰ���Ȱٷֱ�
		setValue((int)((Float)value).floatValue());
		//���ر�����ʵ��
		return this;
	}
}
