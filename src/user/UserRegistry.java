package user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import main.NotePanel;
import main.PasswordUtils;
import panels.GT_Notes;

public class UserRegistry {
	private ArrayList<User> users;
	
	private int current = -1;
	
	private static String[] openedNotes;
	
	public UserRegistry() {
		users = new ArrayList<User>();
		openedNotes = new String[100];
		for(int i = 0; i < openedNotes.length; i++)openedNotes[i] = "";
	}
	
	public void addUser(String name, String email, String passwd) {
		User u = new User(name, email, passwd, false);
		users.add(u);
	}
	
	public void removeUser(User u) {
		users.remove(u);
	}
	
	public boolean login(String name, String passwd) {
		for(User u: users) {
			if(u.getName().equals(name)) {
				if(PasswordUtils.verifyPassword(passwd, u.getPassword().getHash(), u.getPassword().getSalt())) {
					setCurrentUser(users.indexOf(u));
					loadNotesFromCurrentUser();
					GT_Notes.btnUser.setText(name);
					int numberOfFiles = 0;
					if(openedNotes != null) {
						for(String x: openedNotes) {
							if(x != null && !x.equals("")) {
								NotePanel p = new NotePanel(u.darkMode());
								p.autoOpen(x);
								numberOfFiles++;
							}
						}
					}
					if(numberOfFiles > 0 && GT_Notes.getCurrentNote().isNewFile())GT_Notes.removeCurrentNote(true);
					GT_Notes.setDarkMode(u.darkMode());
					System.out.println("User " + getCurrentUser().getName() + " has connected succesfully.");
					
					return true;
				}
			}
		}
		System.err.println("User connection has failed.");
		return false;
	}
	
	public void saveUsers() {
		try {
			File a = new File("users");
			if(!a.exists())a.mkdir();
			for(User u: users) {
				File f = new File("users" + File.separator + u.getName() + ".gt");
				if(!f.exists())f.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				
				oos.writeObject(u);
				
				oos.close();
				bos.close();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadUsers() {
		try {
			File f = new File("users");
			if(!f.exists())f.mkdir();
			
				for(File x: f.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".gt");
				    }
				})) {
					
					FileInputStream fis = new FileInputStream(x);
					BufferedInputStream bis = new BufferedInputStream(fis);
					ObjectInputStream ois = new ObjectInputStream(bis);
					
					User u = (User)ois.readObject();
					users.add(u);
					
					ois.close();
					bis.close();
					fis.close();
				}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveNotesFromCurrentUser() {
		if(getCurrentUser() == null)return;
		try {
			File f = new File("users" + File.separator + getCurrentUser().getName() + ".notes");
			if(!f.exists())f.createNewFile();
			
			FileWriter fw = new FileWriter(f);
			 
			for (int i = 0; i < 100; i++) {
				fw.append(openedNotes[i]);
				fw.append(System.lineSeparator());
			}
		 
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadNotesFromCurrentUser() {
		if(getCurrentUser() == null)return;
		try {
			File f = new File("users" + File.separator + getCurrentUser().getName() + ".notes");
			if(!f.exists())f.createNewFile();
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String line = br.readLine();
			while(line != null) {
				if(!line.equals(""))addNoteToCurrentUser(line);
				line = br.readLine();
			}
			
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addNoteToCurrentUser(String path) {
		int i = 0;
		if(openedNotes == null)openedNotes = new String[100];
		do {
			if(openedNotes[i] == null || openedNotes[i].equals("") || openedNotes[i].equals(path)) {
				openedNotes[i] = path;
				break;
			}
			i++;
		} while (i < 100);
		if(i == 100)System.out.println("The user has too many notes!");
	}
	
	public String[] getNotesFromCurrentUser() {
		return openedNotes;
	}
	
	public void setCurrentUser(int i) {
		current = i;
	}
	
	public User getCurrentUser() {
		if(current >= 0 &&  users.size() >= 1)return users.get(current);
		else return null;
	}
	
	public String getNoteFromCurrentUserAt(int position) {
		return openedNotes[position];
	}
	
	public void removeNoteFromCurrentUser(String path) {
		if(openedNotes == null)openedNotes = new String[100];
		for(int i = 0; i < openedNotes.length; i++) {
			if(openedNotes[i] != null && !openedNotes[i].equals("") && openedNotes[i].equals(path))openedNotes[i] = "";
		}
	}
}
