package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class CadastroDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  private JLabel lbEmail;
  private JTextField tfEmail;
  private JLabel lbNome;
  private JTextField tfNome;  
  private JLabel lbPassword;
  private JPasswordField tfPassword;
  private JLabel lbPassConf;
  private JPasswordField tfPassConf;
  
  private JButton btnCadastrar;
  private JButton btnCancelar;
  
  public CadastroDialog(Frame parent) {
    super(parent, "Sign Up", true);
    //
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    lbNome = new JLabel("Name: ");
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(lbNome, cs);

    tfNome = new JTextField(20);
    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(tfNome, cs);    
    
    
    lbEmail = new JLabel("E-mail: ");
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(lbEmail, cs);

    tfEmail = new JTextField(20);
    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    panel.add(tfEmail, cs);

    lbPassword = new JLabel("Password: ");
    cs.gridx = 0;
    cs.gridy = 2;
    cs.gridwidth = 1;
    panel.add(lbPassword, cs);

    tfPassword = new JPasswordField(20);
    cs.gridx = 1;
    cs.gridy = 2;
    cs.gridwidth = 2;
    panel.add(tfPassword, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    lbPassConf = new JLabel("Password (conf): ");
    cs.gridx = 0;
    cs.gridy = 3;
    cs.gridwidth = 1;
    panel.add(lbPassConf, cs);

    tfPassConf = new JPasswordField(20);
    cs.gridx = 1;
    cs.gridy = 3;
    cs.gridwidth = 2;
    panel.add(tfPassConf, cs);
    panel.setBorder(new LineBorder(Color.GRAY));

    btnCadastrar = new JButton("Sign up");

    btnCadastrar.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        String pass = new String(tfPassword.getPassword());
        String conf = new String(tfPassConf.getPassword());
        
        if(pass.equals(conf)) {
          GT_Notes.ur.addUser(tfNome.getText(), tfEmail.getText(), pass);
          JOptionPane.showMessageDialog(CadastroDialog.this, "Signed up succesfully!",
              "Cadastro", JOptionPane.INFORMATION_MESSAGE);
          dispose();
        }
        else
          JOptionPane.showMessageDialog(CadastroDialog.this, "Passwords do not match.",
              "Cadastro", JOptionPane.ERROR_MESSAGE);
      }
    });
    btnCancelar = new JButton("Cancel");
    btnCancelar.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    JPanel bp = new JPanel();
    bp.add(btnCadastrar);
    bp.add(btnCancelar);

    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(bp, BorderLayout.PAGE_END);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }
}
