package panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JButton btnCadastrar;
    private boolean succeeded;
    private int attempts = 0;

  public LoginDialog(Frame parent) {
    super(parent, "Sign In", true);
    //
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    lbUsername = new JLabel("Username: ");
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(lbUsername, cs);

    tfUsername = new JTextField(20);
    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(tfUsername, cs);

    lbPassword = new JLabel("Password: ");
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(lbPassword, cs);

    pfPassword = new JPasswordField(20);
    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    pfPassword.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) login();
		}
	});
    panel.add(pfPassword, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    btnLogin = new JButton("Sign in");

    btnLogin.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        login();
      }
    });
    btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    
    btnCadastrar = new JButton("Sign up...");
    btnCadastrar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CadastroDialog cadastroDlg = new CadastroDialog(parent);
        cadastroDlg.setVisible(true); 
      }
    });
    
    
    JPanel bp = new JPanel();
    bp.add(btnLogin);
    bp.add(btnCancel);
    bp.add(btnCadastrar);

    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(bp, BorderLayout.PAGE_END);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }

  public String getUsername() {
    return tfUsername.getText().trim();
  }

  public String getPassword() {
    return new String(pfPassword.getPassword());
  }

  public boolean isSucceeded() {
    return succeeded;
  }
  
  private void login() {
	  if (GT_Notes.ur.login(getUsername(), getPassword())) {
          succeeded = true;
          dispose();
        } else {
          JOptionPane.showMessageDialog(LoginDialog.this, "Invalid username and/or password.", "Authentication error",
              JOptionPane.ERROR_MESSAGE);
          // reset username and password
          tfUsername.setText("");
          pfPassword.setText("");
          succeeded = false;
          attempts++;
          if(attempts >= 3) {
            JOptionPane.showMessageDialog(LoginDialog.this, attempts + " incorrect attempts. (Waiting foi " + attempts + "seconds.", "Too many incorrect attempts",
                JOptionPane.ERROR_MESSAGE);
            try {
              Thread.sleep(attempts * 1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } 
          } 
        }
  }
}