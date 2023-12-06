package panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("rawtypes")
public class FontConfig extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	JComboBox comboBox = new JComboBox();
	JComboBox comboBox_1 = new JComboBox();
	JLabel label = new JLabel("Text Sample");
	
	@SuppressWarnings({ "unchecked"})
	public FontConfig() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Display Configuration");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				label.setFont(getFont());
				if(GT_Notes.getCurrentNote() != null)GT_Notes.getCurrentNote().setFont(getFont());
			}
		});
		
		comboBox.setFont(new Font("Calibri", Font.PLAIN, 18));
		comboBox.setModel(new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		comboBox.setToolTipText("Fonte do texto");
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				label.setFont(getFont());
				if(GT_Notes.getCurrentNote() != null)GT_Notes.getCurrentNote().setFont(getFont());
			}
		});
		
		comboBox_1.setFont(new Font("Calibri", Font.PLAIN, 18));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "22", "24", "26", "28", "36", "48", "72"}));
		comboBox_1.setSelectedIndex(8);
		
		comboBox.setToolTipText("Fonte do texto");
		comboBox.setBounds(10, 57, 300, 38);
		contentPane.add(comboBox);
		
		comboBox_1.setBounds(332, 57, 80, 38);
		contentPane.add(comboBox_1);
		
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Calibri", Font.PLAIN, 18));
		label.setBounds(10, 106, 414, 144);
		contentPane.add(label);
		
		JLabel lblConfiguraesDeExibio = new JLabel("Editor display configuration");
		lblConfiguraesDeExibio.setFont(new Font("Calibri", Font.PLAIN, 18));
		lblConfiguraesDeExibio.setBounds(10, 11, 414, 35);
		contentPane.add(lblConfiguraesDeExibio);
	}
	
	 public Font getFont() {
			Font fp = new Font(comboBox.getSelectedItem().toString(), Font.PLAIN, Integer.valueOf(comboBox_1.getSelectedItem().toString()));
			//Font fb = new Font(comboBox.getSelectedItem().toString(), Font.BOLD, Integer.valueOf(comboBox_1.getSelectedItem().toString()));
			//Font fi = new Font(comboBox.getSelectedItem().toString(), Font.ITALIC, Integer.valueOf(comboBox_1.getSelectedItem().toString()));
			
			return fp;
		}
}
