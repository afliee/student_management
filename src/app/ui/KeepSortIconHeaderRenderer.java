package app.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
public class KeepSortIconHeaderRenderer implements TableCellRenderer {
	
	private TableCellRenderer defaultRenderer;
	
	public KeepSortIconHeaderRenderer(TableCellRenderer defaultRenderer) {
		this.defaultRenderer = defaultRenderer;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (comp instanceof JLabel) {
			JLabel labelHeader = (JLabel) comp;
			labelHeader.setFont(new Font("Consolas", Font.BOLD, 14));
			labelHeader.setForeground(Color.BLUE);
			labelHeader.setBorder(BorderFactory.createEtchedBorder());
		}
		
		return comp;
	}

}