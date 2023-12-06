package panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;

import main.FileOperations;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class FilePanel extends JPanel {

	private static final long serialVersionUID = 1L;;

	String text = "Text";
	public String loadedText = "Text";
	private JTextArea t;
	
	public void setText(String text){
		this.text = text;
	}
	
	public FilePanel(JTextArea jTextArea) {
		setBounds(0, 0, 1044, 117);
		
		t = jTextArea;
		JButton btnNewButton = new JButton("Export Note");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fc.showSaveDialog(null);
					String path = fc.getSelectedFile().getAbsolutePath();
					
					FileOperations.saveDocument(text, path + ".txt");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnCarregarNota = new JButton("Load Note");
		btnCarregarNota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fc.showOpenDialog(null);
					String path = fc.getSelectedFile().getAbsolutePath();
					t.setText(FileOperations.loadDocument(path));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCarregarNota)
					.addContainerGap(838, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCarregarNota, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(42, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}

}
