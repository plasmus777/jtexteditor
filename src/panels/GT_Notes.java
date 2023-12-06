package panels;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicMenuBarUI;

import main.NotePanel;
import user.UserRegistry;
import java.awt.Dimension;
import javax.swing.JCheckBoxMenuItem;

public class GT_Notes extends JFrame {
	private static final long serialVersionUID = 1L;

	public static UserRegistry ur = new UserRegistry();

	private static JPanel contentPane;
	private static JMenuBar toolBar;

	private static GT_Notes frame = new GT_Notes();
	private static FontConfig font = new FontConfig();
	public static Search search = new Search();
	public LoginDialog login = new LoginDialog(this);
	//

	//
	private final JMenu mnArquivo = new JMenu("File");
	private final JMenuItem mntmNewMenuItem = new JMenuItem("New");
	private final JMenuItem mntmNewMenuItem_1 = new JMenuItem("New tab");
	private final JMenuItem mntmNewMenuItem_2 = new JMenuItem("Open...");
	private final JMenuItem mntmNewMenuItem_3 = new JMenuItem("Save");
	private final JMenuItem mntmNewMenuItem_4 = new JMenuItem("Save as...");
	private final JMenuItem mntmNewMenuItem_7 = new JMenuItem("Exit");
	private final JMenu mnEditar = new JMenu("Edit");
	private final JMenu mnFormatar = new JMenu("Format");
	private final JMenu mnNewMenu = new JMenu("Display");
	private final JMenu mnAjuda = new JMenu("Help");
	public static JTabbedPane tabbedPane;
	public static JButton btnUser;
	private JMenuItem mntmEditarFonte = new JMenuItem("Edit font");
	private Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
	private static JCheckBoxMenuItem chckbxmntmDarkTheme;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					font.setVisible(false);
					search.setVisible(false);
					ur.loadUsers();
					setDarkMode(false);
					
					if(args != null && args.length >= 1) {
						NotePanel n = new NotePanel(darkMode());
						n.abrirAuto(args[0]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GT_Notes() {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				while(getCurrentNote() != null) {
					if(getCurrentNote().hasChanges) {
						int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_CANCEL_OPTION);
						switch(option) {
							case 0:
								getCurrentNote().salvar();
							case 2:
								return;
						}
					}
					
					if(tabbedPane.getTabCount() == 1) {
						removeCurrentNote(false);
						break;
					} else {
						removeCurrentNote(false);
					}
				}
				
				ur.saveUsers();
				ur.saveNotesFromCurrentUser();
				System.out.println("Window closed.");
				System.exit(0);	
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				//windows -14
				toolBar.setBounds(0, 0, frame.getWidth() - 14, 30);
				//windows -14 -66
				tabbedPane.setBounds(0 , 30, frame.getWidth() - 14, frame.getHeight() - 66);
				
				System.gc();
				//linux	toolBar.setBounds(0, 0, frame.getWidth(), 30);
				//linux tabbedPane.setBounds(0 , 30, frame.getWidth(), frame.getHeight() - 53);
			}
		});
		ur.loadUsers();
		setTitle("Notes - version 1.0");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		
		setupTabTraversalKeys(tabbedPane);
		tabbedPane.setBorder(new EmptyBorder(0, 0, 1, 1));
		tabbedPane.add(new NotePanel(darkMode()) , "New Document");
		contentPane.add(tabbedPane);
		
		toolBar = new JMenuBar();
		toolBar.setBounds(0, 0, 510, 30);
		contentPane.add(toolBar);
		
		toolBar.add(mnArquivo);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getCurrentNote() != null)getCurrentNote().novo();
			}
		});
		
		mnArquivo.add(mntmNewMenuItem);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.add(new NotePanel(darkMode()), "New Document");
			}
		});
		
		//Abrir
		mnArquivo.add(mntmNewMenuItem_1);
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(getCurrentNote() != null)getCurrentNote().abrir();
			}
		});
		
		JMenuItem mntmFecharAba = new JMenuItem("Close tab");
		mntmFecharAba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getCurrentNote().hasChanges) {
					int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_OPTION);
					if(option == 0) {
						if(getCurrentNote() != null)getCurrentNote().salvar();
					}
				}
				tabbedPane.remove(tabbedPane.getSelectedIndex());
				ur.saveUsers();
				ur.saveNotesFromCurrentUser();
			}
		});
		mnArquivo.add(mntmFecharAba);
		
		//Salvar
		mnArquivo.add(mntmNewMenuItem_2);
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getCurrentNote() != null)getCurrentNote().salvar();
			}
		});
		
		//Salvar como
		mnArquivo.add(mntmNewMenuItem_3);
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getCurrentNote() != null)getCurrentNote().salvarComo();
			}
		});
		
		mnArquivo.add(mntmNewMenuItem_4);
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getCurrentNote() != null) {
					if(getCurrentNote().hasChanges) {
						int option = JOptionPane.showConfirmDialog(null, "Do you want to save all changes before exiting?", "Document not saved", JOptionPane.YES_NO_OPTION);
						if(option == 0) {
							getCurrentNote().salvar();
						}
					}
				}
				ur.saveUsers();
				ur.saveNotesFromCurrentUser();
				System.exit(0);
			}
		});
		
		mnArquivo.add(mntmNewMenuItem_7);
		
		toolBar.add(mnEditar);
		
		JMenuItem mntmDesfazer = new JMenuItem("Undo");
		mntmDesfazer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getCurrentNote() != null)getCurrentNote().undo();
			}
		});
		mnEditar.add(mntmDesfazer);
		
		toolBar.add(mnFormatar);
		
		toolBar.add(mnNewMenu);
		mntmEditarFonte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				font.setVisible(true);
			}
		});
		
		chckbxmntmDarkTheme = new JCheckBoxMenuItem("Dark theme");
		mnNewMenu.add(mntmEditarFonte);
		chckbxmntmDarkTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!chckbxmntmDarkTheme.isSelected()) {
					setDarkMode(false);
					return;
				} else {
					setDarkMode(true);
					return;
				}
			}
		});
		
		mnNewMenu.add(chckbxmntmDarkTheme);
		
		toolBar.add(mnAjuda);
		
		btnUser = new JButton("Sign in");
		btnUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login.setVisible(true);
			}
		});
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		toolBar.add(btnUser);
		
		toolBar.add(rigidArea);
	}
	
	private static void setupTabTraversalKeys(JTabbedPane tabbedPane)
	  {
	    KeyStroke ctrlTab = KeyStroke.getKeyStroke("ctrl TAB");
	    KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke("ctrl shift TAB");
	 
	    // Remove ctrl-tab from normal focus traversal
	    Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
	    forwardKeys.remove(ctrlTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
	 
	    // Remove ctrl-shift-tab from normal focus traversal
	    Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
	    backwardKeys.remove(ctrlShiftTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
	 
	    // Add keys to the tab's input map
	    InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	    inputMap.put(ctrlTab, "navigateNext");
	    inputMap.put(ctrlShiftTab, "navigatePrevious");
	  }
	
	public static NotePanel getCurrentNote() {
		if(tabbedPane.getSelectedIndex() == -1)return null;
		NotePanel note = (NotePanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
		if(note != null)return note;
		else return null;
	}
	
	public static NotePanel getNoteAt(int index) {
		if(tabbedPane.getSelectedIndex() == -1)return null;
		NotePanel note = (NotePanel) tabbedPane.getComponentAt(index);
		if(note != null)return note;
		else return null;
	}
	
	public static void addNote(NotePanel p, String name) {
		if(darkMode())p.setDark(true);
		tabbedPane.add(p, name);
		if(ur.getCurrentUser() != null)ur.addNoteToCurrentUser(p.getFilePath());
		ur.saveUsers();
		ur.saveNotesFromCurrentUser();
	}
	
	public static void removeNote(int index, boolean save) {
		if(getNoteAt(index) != null) {
			NotePanel p = getNoteAt(index);
			if(ur.getCurrentUser() != null)ur.removeNoteFromCurrentUser(p.getFilePath());
			tabbedPane.remove(index);
		}
		if(save) {
			ur.saveUsers();
			ur.saveNotesFromCurrentUser();
		}
	}
	
	public static void removeCurrentNote(boolean save) {
		if(getCurrentNote() != null) {
			NotePanel p = getCurrentNote();
			if(ur.getCurrentUser() != null)ur.removeNoteFromCurrentUser(p.getFilePath());
			tabbedPane.remove(tabbedPane.getSelectedIndex());
			if(tabbedPane.getTabCount() == 0)addNote(new NotePanel(darkMode()), "New Document");
		}
		
		if(save) {
			ur.saveUsers();
			ur.saveNotesFromCurrentUser();
		}
	}
	
	public static void changeCurrentNoteTitle(String title) {
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), title);
		ur.saveUsers();
		ur.saveNotesFromCurrentUser();
	}
	
	public static boolean darkMode() {
		if(ur.getCurrentUser() != null)return ur.getCurrentUser().darkMode();
		else if(chckbxmntmDarkTheme != null) return chckbxmntmDarkTheme.isSelected();
		else return false;
	}
	
	public static void setDarkMode(boolean dark) {
		Color c1;
		Color c2;
		Color c3;
		if(dark) {
			c1 = Color.BLACK;
			c2 = Color.WHITE;
			c3 = Color.DARK_GRAY;
		} else {
			c1 = Color.WHITE;
			c2 = Color.BLACK;
			c3 = Color.WHITE;
		}
		if(chckbxmntmDarkTheme != null)chckbxmntmDarkTheme.setSelected(dark);
		contentPane.setBackground(c3);
		GT_Notes.tabbedPane.setBackground(c1);
		GT_Notes.tabbedPane.setForeground(c2);
		customizeMenuBar(GT_Notes.toolBar, c1, c2);
		for(int i = 0; i < GT_Notes.tabbedPane.getTabCount(); i++) {
			GT_Notes.tabbedPane.setBackgroundAt(i, c1);
			getNoteAt(i).setDark(dark);
		}
		if(ur.getCurrentUser() != null) {
			ur.getCurrentUser().setDarkMode(dark);
			ur.saveUsers();
		}
	}
	
	private static void customizeMenuBar(JMenuBar menuBar, Color background, Color text) {
	    menuBar.setUI(new BasicMenuBarUI() {
	        @Override
	        public void paint(Graphics g, JComponent c) {
	            g.setColor(background);
	            g.fillRect(0, 0, c.getWidth(), c.getHeight());
	        }

	    });
	    MenuElement[] menus = menuBar.getSubElements();
	    for (MenuElement menuElement : menus) {
	        JMenu menu = (JMenu) menuElement.getComponent();
	        menu.setBackground(background);
            menu.setForeground(text);
	        menu.setOpaque(true);
	        MenuElement[] menuElements = menu.getSubElements();
	        for (MenuElement popupMenuElement : menuElements) {
	            JPopupMenu popupMenu = (JPopupMenu) popupMenuElement.getComponent();
	            popupMenu.setBorder(null);
	            MenuElement[] menuItens = popupMenuElement.getSubElements();
	            for (MenuElement menuItemElement : menuItens) {
	                JMenuItem menuItem = (JMenuItem) menuItemElement.getComponent();
	                menuItem.setBackground(background);
	                menuItem.setForeground(text);
	                menuItem.setOpaque(true);
	            }
	        }
	    }
	}
}