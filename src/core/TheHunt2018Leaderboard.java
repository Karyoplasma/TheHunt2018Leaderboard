package core;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

/**
 * @author Karyoplasma
 *
 */
public class TheHunt2018Leaderboard implements Observer, ActionListener {
	
	private final String[] columnNames = new String[] {"Position", "Name", "Points"};
	private JFrame frmTheHunt;
	private JButton btnUpdate;
	private JTable table;
	private JButton btnCopy;
	private JMenuItem mntmUpdateInterval;
	private JMenuItem mntmCopyThreshold;
	private int copyEntries;
	private long delay;
	private Timer update;
	private TimerTask updateTask;
	private SpreadsheetReader reader;
	private JMenuItem mntmCancelAutoupdate;
	private JTextField textField_update;
	private JMenu mnShowCurrentSettings;
	private JTextField txtUpdateInterval;
	private JTextField txtCopyThreshold;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TheHunt2018Leaderboard window = new TheHunt2018Leaderboard();
					window.frmTheHunt.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TheHunt2018Leaderboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		//initialize the variables
		this.copyEntries = 10;
		
		this.delay = 10L * 60 * 1000;
		this.updateTask = this.getUpdateTask();
		update = new Timer();
		//start the timer
		update.scheduleAtFixedRate(updateTask, delay, delay);
		
		frmTheHunt = new JFrame();
		frmTheHunt.setTitle("The Hunt 2018 Leaderboard");
		frmTheHunt.setResizable(false);
		frmTheHunt.setBounds(100, 100, 401, 530);
		frmTheHunt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTheHunt.getContentPane().setLayout(null);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(this);
		btnUpdate.setBounds(295, 450, 89, 23);
		frmTheHunt.getContentPane().add(btnUpdate);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 11, 374, 428);
		frmTheHunt.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Position", "Name", "Points"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(255);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);
		table.setDefaultRenderer(Integer.class, centerRenderer);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		
		btnCopy = new JButton("Copy");
		btnCopy.setBounds(10, 450, 89, 23);
		btnCopy.addActionListener(this);
		btnCopy.setEnabled(false);
		frmTheHunt.getContentPane().add(btnCopy);
		
		textField_update = new JTextField();
		textField_update.setEditable(false);
		textField_update.setHorizontalAlignment(SwingConstants.CENTER);
		textField_update.setBounds(109, 451, 176, 20);
		frmTheHunt.getContentPane().add(textField_update);
		textField_update.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		frmTheHunt.setJMenuBar(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		mntmUpdateInterval = new JMenuItem("Update interval");
		mntmUpdateInterval.addActionListener(this);
		mnSettings.add(mntmUpdateInterval);
		
		mntmCopyThreshold = new JMenuItem("Copy threshold");
		mntmCopyThreshold.addActionListener(this);
		mnSettings.add(mntmCopyThreshold);
		
		mntmCancelAutoupdate = new JMenuItem("Cancel Auto-Update");
		mntmCancelAutoupdate.addActionListener(this);
		mnSettings.add(mntmCancelAutoupdate);
		
		
		mnShowCurrentSettings = new JMenu("Show current settings");
		menuBar.add(mnShowCurrentSettings);
		
		//Override the size function because the layout manager is bad
		txtUpdateInterval = new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(180,25);
			}
		};
		txtUpdateInterval.setEditable(false);
		txtUpdateInterval.setText("Update interval: 10 minutes");
		mnShowCurrentSettings.add(txtUpdateInterval);
		txtUpdateInterval.setColumns(10);
		
		//see above
		txtCopyThreshold = new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(180,25);
			}
		};
		txtCopyThreshold.setEditable(false);
		txtCopyThreshold.setMinimumSize(new Dimension(959,25));
		txtCopyThreshold.setText("Copy threshold: 10");
		mnShowCurrentSettings.add(txtCopyThreshold);
		txtCopyThreshold.setColumns(10);
		
	
		reader = new SpreadsheetReader(this);
		
		
		
		
	}
	
	/**
	 * Not sure if that is needed. I did it in an attempt to fix random errors and found out that the culprit is the actual Timer afterwards. Still I left it in
	 * @return a new TimerTask that updates the leaderboard
	 */
	private TimerTask getUpdateTask() {
		TimerTask updateTask = new TimerTask(){

			@Override
			public void run() {
				reader.gatherLeaderboardData();
				btnCopy.setEnabled(true);
				setTimestamp();
			}
			
		};
		return updateTask;
	}
	
	/* 
	 * Override the update method for the Observer
	 */
	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	public void update(Observable o, Object arg) {
		// the only thing that sends an update is the SpreadsheetReader.gatherLeaderboardData, so we can cast it without checking
		PriorityQueue<Participant> leaderboard = (PriorityQueue<Participant>) arg;
		//create a new table with x rows and 3 columns
		Object[][] data = new Object[leaderboard.size()][3];
		//write the data from the PriorityQueue into the table object
		int index = 0;
		while (!leaderboard.isEmpty()) {
			Participant p = leaderboard.poll();
			data[index][0] = index + 1;
			data[index][1] = p.getName();
			data[index][2] = p.getPoints();
			index++;
		}
		//put the data in a new model and set the result to the table to update the leaderboard
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Integer.class, String.class, Integer.class
				};
				public Class<?> getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(255);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		
	}
	
	/**
	 * Put a timestamp in the text field on the bottom
	 */
	private void setTimestamp() {
		//get local time
		DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
		//format it
		String formattedDate = f.format(new Date());
		//split it up so because I wanted to put time of day before day of year
		String[] dateSplit = formattedDate.split(" ");
		//fancy --- separator
		textField_update.setText(String.format("%s --- %s", dateSplit[1], dateSplit[0]));
	}
	
	/**
	 * Gets an input from the user via a pop-up message
	 * @param message The message displayed for the user
	 * @return The user input as a String
	 */
	private String getUserInput(String message) {
		String ret = JOptionPane.showInputDialog(message);
		if (ret.isEmpty()) {
			return null;
		} else {
			return ret;
		}
	}
	
	/**
	 * Checks if a String is a positive numeric
	 * @param x The String to check
	 * @return Truth value of the check
	 */
	private boolean isNumeric(String x) {
		char[] y = x.toCharArray();
		for (char c : y) {
			if (Character.isDigit(c)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	/* 
	 * Method to classify this class as an ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//check which UI element triggered this method
		
		//Update button
		if (e.getSource() == btnUpdate) {
			//update the leaderboard and set a timestamp
			reader.gatherLeaderboardData();
			setTimestamp();
			//enable the copy button
			btnCopy.setEnabled(true);
			
		}
		//copy button
		if (e.getSource() == btnCopy) {
			//instantiate a StringBuilder
			StringBuilder builder = new StringBuilder();
			//check if the copy threshold is set to a ridiculous number and cut it down to the amount of entries in the leaderboard if needed
			this.copyEntries = Math.min(this.copyEntries, table.getRowCount());
			
			//build the String
			for (int x = 0; x < this.copyEntries; x++) {
				builder.append(table.getModel().getValueAt(x, 0)).append(".) ");
				builder.append(table.getModel().getValueAt(x, 1)).append(" with ");
				builder.append(table.getModel().getValueAt(x, 2)).append(" points").append(System.getProperty("line.separator"));
			}
			
			//copy the String to clipboard
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(builder.toString()), null);
		}
		//Copy threshold menu entry
		if (e.getSource() == mntmCopyThreshold) {
			//get user input
			String input = getUserInput("Set copy entries (default is 10)");
			//check if user cancelled the input or wrote a non-digit
			if (input == null || !this.isNumeric(input)) {
				return;
			} else {
				//update the variable and the textbox in the Show menu
				this.copyEntries = Integer.parseInt(input);
				txtCopyThreshold.setText(String.format("%s: %d", "Copy threshold", this.copyEntries));
			}
		}
		//Update interval menu entry
		if (e.getSource() == mntmUpdateInterval) {
			//get user input
			String input = getUserInput("Set update interval in minutes (default is 10 minutes)");
			//check if user cancelled the input or wrote a non-digit
			if (input == null || !this.isNumeric(input)){
				return;
			} else {
				long time = Long.parseLong(input);
				//0 minutes is not an appropriate update interval, so discard
				if (time == 0L) {
					return;
				} else {
					this.delay = time * 60 * 1000;
				}
				//cancel the Timer, not sure what purge does, but it sounds good
				update.cancel();
				update.purge();
				//make a new Timer, cancelled Timers throw seemingly random errors and this fixed it (probably bad)
				update = new Timer();
				this.updateTask = this.getUpdateTask();
				update.scheduleAtFixedRate(this.updateTask, this.delay, this.delay);
				//update the text field in the Show menu
				txtUpdateInterval.setText(String.format("%s: %d %s", "Update interval", this.delay/60/1000, (this.delay == 60000) ? "minute" : "minutes"));
				
			}
		}
		//Cancel Auto-Update menu entry
		if (e.getSource() == mntmCancelAutoupdate) {
			//cancel the Timer and make a new one, update the text field in the Show menu
			update.cancel();
			update.purge();
			update = new Timer();
			txtUpdateInterval.setText("Auto-update cancelled");
		}
	}
}
