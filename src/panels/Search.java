package panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Search extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	private boolean dif = false;
	private JTextField textField_1;
	private JCheckBox chckbxSubstituir;
	
	public Search() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setType(Type.UTILITY);
		setTitle("Localize text");
		setBounds(100, 100, 434, 168);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					GT_Notes.getCurrentNote().textArea.getHighlighter().removeAllHighlights();
					search(textField.getText());
				}
			}
		});
		textField.setBounds(74, 28, 245, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblLocalizar = new JLabel("Localize:");
		lblLocalizar.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblLocalizar.setBounds(10, 32, 70, 14);
		contentPane.add(lblLocalizar);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GT_Notes.getCurrentNote().textArea.getHighlighter().removeAllHighlights();
				search(textField.getText());
			}
		});
		btnNewButton.setBounds(329, 27, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnCancelar = new JButton("Cancel");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				GT_Notes.getCurrentNote().textArea.getHighlighter().removeAllHighlights();
			}
		});
		btnCancelar.setBounds(329, 53, 89, 23);
		contentPane.add(btnCancelar);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Case sensitive");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(dif == false) {
					dif = true;
					return;
				} else {
					dif = false;
					return;
				}
			}
		});
		chckbxNewCheckBox.setBounds(6, 81, 272, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JCheckBox chckbxAoRedor = new JCheckBox("Around");
		chckbxAoRedor.setBounds(6, 105, 97, 23);
		contentPane.add(chckbxAoRedor);
		
		JLabel lblDireo = new JLabel("Direction:");
		lblDireo.setBounds(284, 87, 70, 14);
		contentPane.add(lblDireo);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Up");
		rdbtnNewRadioButton.setBounds(239, 105, 80, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnAbaixo = new JRadioButton("Down");
		rdbtnAbaixo.setSelected(true);
		rdbtnAbaixo.setBounds(321, 105, 97, 23);
		contentPane.add(rdbtnAbaixo);
		
		JLabel lblSubstituirPor = new JLabel("Replace for:");
		lblSubstituirPor.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSubstituirPor.setBounds(10, 53, 93, 14);
		contentPane.add(lblSubstituirPor);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(105, 53, 214, 20);
		contentPane.add(textField_1);
		
		chckbxSubstituir = new JCheckBox("Replace");
		chckbxSubstituir.setBounds(123, 105, 97, 23);
		contentPane.add(chckbxSubstituir);
	}
	
	private void search(String text) {
		if(text.isEmpty())return;
		if(!dif)text = text.toLowerCase();
		String lctext = GT_Notes.getCurrentNote().textArea.getText().toLowerCase();
		
		Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
		
		if(dif) {
			if(GT_Notes.getCurrentNote() != null) {
				int offset = GT_Notes.getCurrentNote().textArea.getText().indexOf(text);
				int length = text.length();
				while(offset != -1){
				    try{
				    	if(!chckbxSubstituir.isSelected()) {
				    		GT_Notes.getCurrentNote().textArea.getHighlighter().addHighlight(offset, offset + length, painter);
				    	} else {
				    		GT_Notes.getCurrentNote().textArea.replaceRange(textField_1.getText(), offset, offset + length);
				    	}
				    	offset = GT_Notes.getCurrentNote().textArea.getText().indexOf(text, offset+1);
				    } catch(BadLocationException ble) { System.out.println(ble); }
				}
			}
		} else {
			if(GT_Notes.getCurrentNote() != null) {
				int offset = lctext.indexOf(text);
				int length = text.length();
				while(offset != -1){
				    try{
				    	if(!chckbxSubstituir.isSelected()) {
				    		GT_Notes.getCurrentNote().textArea.getHighlighter().addHighlight(offset, offset + length, painter);
				    	} else {
				    		GT_Notes.getCurrentNote().textArea.replaceRange(textField_1.getText(), offset, offset + length);
				    	}
				    	offset = GT_Notes.getCurrentNote().textArea.getText().indexOf(text, offset+1);
				    } catch(BadLocationException ble) { System.out.println(ble); }
				}
			}
		}
	}
}
