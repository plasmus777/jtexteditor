package main;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import panels.GT_Notes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NotePanel extends JPanel {

	private boolean isNewFile = true;
	public boolean hasChanges = false;
	private String filePath;
	private String title;
	
	private UndoManager um = new UndoManager();
	
	private static final long serialVersionUID = 1L;
	
	public JTextArea textArea = new JTextArea();
	JScrollPane scroll = new JScrollPane(textArea);
	
	public NotePanel(boolean dark) {
		setDark(dark);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textArea.getHighlighter().removeAllHighlights();
			}
		});
		
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Calibri", Font.PLAIN, 18));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		Document doc = textArea.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
		    @Override
		    public void undoableEditHappened(UndoableEditEvent e) {
		        um.addEdit(e.getEdit());
		    }
		});
		
		//Atalhos Ctrl+S para Salvar
				textArea.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						processKeyShortcuts(arg0);
					}
				});
		
		add(scroll);
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void novo() {
		if(hasChanges) {
			int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_OPTION);
			if(option == 0) {
				salvar();
			}
		}
		filePath = "";
		textArea.setText("");
		isNewFile = true;
		hasChanges = false;
		GT_Notes.ur.saveUsers();
		GT_Notes.ur.saveNotesFromCurrentUser();
	}
	
	public void autoOpen(String path) {
		if(path == null || path.equals(""))return;
		try {
			File f = new File(path);
			if(!f.exists())return;
			setTitle(f.getName());
			hasChanges = false;
			isNewFile = false;
			filePath = path;
			textArea.setText(FileOperations.loadDocument(path));
			GT_Notes.addNote(this, f.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void abrir() {
		try {
			if(hasChanges) {
				int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_OPTION);
				if(option == 0) {
					salvar();
				}
			}
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter f = new FileNameExtensionFilter("Text files","txt");
			fc.setFileFilter(f);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.showOpenDialog(null);
			String path = fc.getSelectedFile().getAbsolutePath();
			textArea.setText(FileOperations.loadDocument(path));
			isNewFile = false;
			filePath = fc.getSelectedFile().getAbsolutePath();
			this.setTitle(fc.getSelectedFile().getName());
			hasChanges = false;
			if(GT_Notes.getCurrentNote() != null) {
				GT_Notes.changeCurrentNoteTitle(fc.getSelectedFile().getName());
			}
			if(GT_Notes.ur.getCurrentUser() != null)GT_Notes.ur.addNoteToCurrentUser(filePath);
			GT_Notes.ur.saveUsers();
			GT_Notes.ur.saveNotesFromCurrentUser();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void salvar() {
		try {
			if(isNewFile) {
				salvarComo();
			} else {
				FileOperations.saveDocument(textArea.getText(), filePath);
				hasChanges = false;
				if(GT_Notes.ur.getCurrentUser() != null)GT_Notes.ur.addNoteToCurrentUser(filePath);
				GT_Notes.ur.saveUsers();
				GT_Notes.ur.saveNotesFromCurrentUser();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void salvarComo() {
		try {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter f = new FileNameExtensionFilter("Text files","txt");
			fc.setFileFilter(f);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.showSaveDialog(null);
			String path = fc.getSelectedFile().getAbsolutePath();
			
			FileOperations.saveDocument(textArea.getText(), path + ".txt");
			isNewFile = false;
			filePath = fc.getSelectedFile().getAbsolutePath() + ".txt";
			setTitle(fc.getSelectedFile().getName() + ".txt");
			hasChanges = false;
			if(GT_Notes.getCurrentNote() != null) {
				GT_Notes.changeCurrentNoteTitle(fc.getSelectedFile().getName());
			}
			if(GT_Notes.ur.getCurrentUser() != null)GT_Notes.ur.addNoteToCurrentUser(filePath);
			GT_Notes.ur.saveUsers();
			GT_Notes.ur.saveNotesFromCurrentUser();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setFont(Font f) {
		if(textArea != null)textArea.setFont(f);
	}
	
	public void undo() {
		try {
			 if(um.canUndo()) {
				 um.undo();
			 }
		 } catch (CannotUndoException exp) {
			 exp.printStackTrace();
	     }
	}
	
	public void redo() {
		try {
			 if(um.canRedo()) {
				 um.redo();
			 }
		 } catch (CannotRedoException exp) {
			 exp.printStackTrace();
	     }
	}
	
	public void delete() {
		textArea.replaceSelection("");
	}
	
	public boolean isNewFile() {
		return isNewFile;
	}
	
	public void setDark(boolean dark) {
		if(dark) {
			textArea.setBackground(Color.DARK_GRAY);
			textArea.setDisabledTextColor(Color.WHITE);
			textArea.setSelectedTextColor(Color.BLACK);
			textArea.setCaretColor(Color.WHITE);
			textArea.setSelectionColor(Color.WHITE);
			textArea.setForeground(Color.WHITE);
			GT_Notes.tabbedPane.setBackground(Color.BLACK);
			
		} else {
			textArea.setBackground(Color.WHITE);
			textArea.setDisabledTextColor(Color.GRAY);
			textArea.setSelectedTextColor(Color.WHITE);
			textArea.setCaretColor(Color.BLACK);
			textArea.setSelectionColor(Color.BLACK);
			textArea.setForeground(Color.BLACK);
			GT_Notes.tabbedPane.setBackground(Color.WHITE);
		}
	}
	
	public void processKeyShortcuts(KeyEvent arg0) {
		textArea.getHighlighter().removeAllHighlights();
		if(arg0.isControlDown()) {
			switch(arg0.getKeyCode()) {
			case KeyEvent.VK_S:
				if(arg0.isShiftDown())salvarComo();
				else salvar();
				return;
			case KeyEvent.VK_O:
				abrir();
				return;
			case KeyEvent.VK_N:
				novo();
				return;
			case KeyEvent.VK_T:
				GT_Notes.addNote(new NotePanel(GT_Notes.darkMode()), "New Document");
				return;
			case KeyEvent.VK_W:
				if(GT_Notes.getCurrentNote().hasChanges) {
					int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_OPTION);
					if(option == 0) {
						if(GT_Notes.getCurrentNote() != null)GT_Notes.getCurrentNote().salvar();
					}
				}
				if(GT_Notes.ur.getCurrentUser() != null)GT_Notes.ur.removeNoteFromCurrentUser(GT_Notes.getCurrentNote().filePath);
				GT_Notes.removeCurrentNote(true);
				GT_Notes.ur.saveUsers();
				return;
			case KeyEvent.VK_Z:
				 undo();
				return;
			case KeyEvent.VK_Y:
				 redo();
				return;
			case KeyEvent.VK_F:
				GT_Notes.search.setVisible(true);
				return;
			default:
				return;
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_DELETE) {
			delete();
		}
		
		if(!arg0.isActionKey()) {
			hasChanges = true;
		}
	}
}
