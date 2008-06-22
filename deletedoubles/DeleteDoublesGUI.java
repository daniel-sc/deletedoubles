import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.util.ServiceException;
import javax.swing.JScrollPane;

public class DeleteDoublesGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextField tfUsername = null;

	private JButton jButton = null;

	private JPanel jPanel = null;

	private JTextField tfPassword = null;

	private JPanel jPanel1 = null;

	private JButton jButton1 = null;

	private JList jListCalendars = null;
	
	/**
	 * DeleteDoubles instance - if null the DeleteDoubles-Butten should be inactive.
	 */
	private DeleteDoubles dd = null;
	
	private HashMap<String, String> currCals = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel = null;

	private JProgressBar jProgressBar = null;
	
	private doFindDoubles myDoFindDoubles = null;

	private JPanel jPanel3 = null;

	private JButton jButton2 = null;

	private JScrollPane jScrollPane = null;

	/**
	 * This is the default constructor
	 */
	public DeleteDoublesGUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("delete doubles from Google-Calendar");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			jContentPane.add(getJPanel1(), BorderLayout.WEST);
			jContentPane.add(getJPanel2(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes tfUsername	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (tfUsername == null) {
			tfUsername = new JTextField();
			tfUsername.setText("d8schreiber");
		}
		return tfUsername;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("get Calendars");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						dd = new DeleteDoubles(tfUsername.getText(),tfPassword.getText());
						dd.setProgressBar(jProgressBar);
						HashMap<String, CalendarEntry> cals = dd.getCalendars();
						jListCalendars.removeAll();
						currCals = new HashMap<String, String>();
						
						for (String a : cals.keySet()) {
							currCals.put(cals.get(a).getTitle().getPlainText(), a);
						}
						
						jListCalendars.setListData(currCals.keySet().toArray());
						jListCalendars.setVisibleRowCount(currCals.size()+2);
						jListCalendars.setSize(jListCalendars.getPreferredSize());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					jButton1.setEnabled(true);
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.add(getJTextField(), null);
			jPanel.add(getJTextField1(), null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes tfPassword	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField1() {
		if (tfPassword == null) {
			tfPassword = new JTextField();
			tfPassword.setText("password");
		}
		return tfPassword;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel = new JLabel();
			jLabel.setText("JLabel");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJPanel3(), BorderLayout.SOUTH);
			jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("find doubles");
			jButton1.setEnabled(false);
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					myDoFindDoubles = new doFindDoubles(dd, currCals.get(jListCalendars.getSelectedValue()), jLabel, jButton2);
					myDoFindDoubles.start();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jListCalendars	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJList() {
		if (jListCalendars == null) {
			jListCalendars = new JList();
			jListCalendars.setPreferredSize(new Dimension(50,50));
			
			
		}
		return jListCalendars;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(jLabel, BorderLayout.WEST);
			jPanel2.add(getJProgressBar(), BorderLayout.CENTER);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new FlowLayout());
			jPanel3.add(getJButton1(), null);
			jPanel3.add(getJButton2(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("delete doubles");
			jButton2.setEnabled(false);
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doDeleteDoubles doDel = new doDeleteDoubles(myDoFindDoubles.getDoubles(),jLabel,jProgressBar);
					doDel.start();
				}
			});
		}
		return jButton2;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

}  //  @jve:decl-index=0:visual-constraint="214,50"
