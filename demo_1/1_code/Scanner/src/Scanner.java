import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jcraft.jsch.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;

public class Scanner {

	private JFrame frmScanner;
	private JTextField txtenterIdHere;
	static String netID = null;
    static char[] password = null;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Scanner window = new Scanner();
					window.frmScanner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Scanner() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmScanner = new JFrame();
		frmScanner.setTitle("Scanner");
		frmScanner.setBounds(100, 100, 425, 350);
		frmScanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//ScarletAccount Login Panel
		while(!isValid(netID, password)) {
			JPanel panel = new JPanel(new GridLayout(0, 1));
			JLabel uLabel = new JLabel("NetID", SwingConstants.CENTER);
			JTextField user = new JTextField(10);
			JLabel pLabel = new JLabel("Password", SwingConstants.CENTER);
			JPasswordField pass = new JPasswordField(10);
			panel.add(uLabel);
			panel.add(user);
			panel.add(pLabel);
			panel.add(pass);
			String[] options = new String[]{"OK", "Cancel"};
			int option = JOptionPane.showOptionDialog(null, panel, "", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(option == 0)
			{
				netID = user.getText();
			    password = pass.getPassword();
			}
			else if(option == 1) System.exit(0);
		}
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(16, 88, 391, 223);
		frmScanner.getContentPane().add(textArea);
		
		JButton btnValidate = new JButton("Submit");
		btnValidate.setBounds(331, 17, 76, 60);
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					textArea.setText(null);
					textArea.setText(process(txtenterIdHere.getText()));
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
		});
		
		txtenterIdHere = new JTextField();
		txtenterIdHere.setBounds(16, 16, 305, 60);
		txtenterIdHere.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtenterIdHere.setText("");
			}
		});
		frmScanner.getContentPane().setLayout(null);
		txtenterIdHere.setHorizontalAlignment(SwingConstants.CENTER);
		frmScanner.getContentPane().add(txtenterIdHere);
		frmScanner.getContentPane().add(btnValidate);
		
		frmScanner.getRootPane().setDefaultButton(btnValidate);
	}
	
	@SuppressWarnings("resource")
	private static String process(String bc) throws ClassNotFoundException, SQLException, ParseException {
		
		if(bc.compareTo("") == 0) return null;
		
		int rport = 3306;
        String rhost = "coewww.rutgers.edu";
        String dbUsername = "zippypark";
        String dbPassword = "goyhVynIiLcJgHpN";
        Session session = null;
        Connection conn = null;
        String output = new String("");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(netID, rhost, 22);
            session.setPassword(new String(password));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            int assigned_port = session.setPortForwardingL(0, "localhost", rport);
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost:" + assigned_port + "/zippypark?user=" + dbUsername + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8");
	    	
	    	//Remove Passed Reservations where no spot was assigned (Data Cleanup)
	    	Statement stmt = conn.createStatement();
	    	Date currentDate = new Date();
            SimpleDateFormat ToD = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat Day = new SimpleDateFormat("yyyy-MM-dd");
            
	    	stmt.executeUpdate("DELETE FROM Reservations WHERE Date < \"" + Day.format(currentDate) + "\" AND AssignedSpot = -1");
	    	stmt.executeUpdate("DELETE FROM Reservations WHERE Date = \"" + Day.format(currentDate) + "\" AND EndTime < \"" + ToD.format(currentDate) + "\" AND AssignedSpot = -1");
	    	
	    	//Get CustomerInfo
            ResultSet rs = stmt.executeQuery("SELECT FirstName, LastName, Handicapped FROM CustomerInfo WHERE Barcode = " + bc);
            int Handicapped = 0;
            if(rs.next()) {
	            String FirstName = rs.getString("FirstName");
	            String LastName = rs.getString("LastName");
	            Handicapped = rs.getInt("Handicapped");
	            output += "Hi " + FirstName + " " + LastName + ".\n";
            }
            else return("Barcode Does Not Exist\n");
            
            //Get Reservations
            rs = stmt.executeQuery("SELECT Date, StartTime, EndTime, AssignedSpot, Charge, rID FROM Reservations WHERE Barcode = " + bc + " ORDER BY `Date`, `StartTime` ASC");
            String Date = "";
            String StartTime = "";
            String EndTime = "";
            float Charge = 0;
            int rID = 0;
            int AssignedSpot = 0;
            if(rs.next()) {
	            Date = rs.getString("Date");
	            StartTime = rs.getString("StartTime");
	            EndTime = rs.getString("EndTime");
	            AssignedSpot = rs.getInt("AssignedSpot");
	            Charge = rs.getFloat("Charge");
	            rID = rs.getInt("rID");
            }
            
            //Get Walk-in
            rs = stmt.executeQuery("SELECT Date, StartTime, AssignedSpot, Charge, wID FROM WalkIns WHERE Barcode = " + bc + " ORDER BY `Date`, `StartTime` ASC");
            String wDate = "";
            String wStartTime = "";
            int wID = 0;
            int wAssignedSpot = 0;
            float wCharge = 0;
            if(rs.next()) {
	            wDate = rs.getString("Date");
	            wStartTime = rs.getString("StartTime");
	            wAssignedSpot = rs.getInt("AssignedSpot");
	            wCharge = rs.getFloat("Charge");
	            wID = rs.getInt("wID");
            }
            
            //Setting Current, Entry, and Exit Time Variables
            SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            String enterTime = Date + " " + StartTime;
            String wEnterTime = wDate + " " + wStartTime;
            String exitTime = Date + " " + EndTime;
        	String currentTime = fullDate.format(currentDate);
            
        	//Process Walk-In Entry
        	if((AssignedSpot == 0 || currentTime.compareTo(enterTime) < 0) && wAssignedSpot == 0) {
        		int spot = 0;
        		rs = stmt.executeQuery("SELECT * FROM `Parking Spots` WHERE Type = \"walkin\" AND Status = \"unoccupied\"");
        		if(rs.next()) {
        			spot = rs.getInt("SpotNum");
        			stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"" + bc + "\" WHERE SpotNum = " + spot);
        			stmt.executeUpdate("INSERT INTO `WalkIns` (`Date`, `StartTime`, `Barcode`, `AssignedSpot`, `Charge`) "
        					+ "VALUES ('" + dayFormat.format(currentDate) + "', '" + hourFormat.format(currentDate) + "', '" + bc + "', '" + spot + "', '35')");
        			output += "No reservation found for this time...creating walk-in.\n";
        			output += ("\nYour Walk-in Information\nDate:\t" + dayFormat.format(currentDate) + "\nStart Time:\t" + hourFormat.format(currentDate) 
        			+ "\nCharge:\t$35/hr" + "\nAssigned Spot:\t" + spot + "\n");
        		}
        		else
        			output += "\nThere are no walkin spots available.\n";
        	}
        	//Exit Walk-in
        	else if(wAssignedSpot > 0) {
        		long elapsedMillis = currentDate.getTime() - fullDate.parse(wEnterTime).getTime();
        		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedMillis),
        	            TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedMillis)),
        	            TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));
        		float cost = (elapsedMillis / 3600000 + 1) * wCharge;
        		stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"unoccupied\" WHERE SpotNum = " + wAssignedSpot);
                stmt.executeUpdate("INSERT INTO `Records` (`Date`, `StartTime`, `EndTime`, `Barcode`, `AssignedSpot`, `Charge`, `Deleted`, `rID`, `wID`) "
                		+ "SELECT Date, StartTime, '" + hourFormat.format(currentDate) + "', Barcode, AssignedSpot, '" + cost + "', 'Yes', '-1', wID FROM WalkIns WHERE wID = " + wID);
                stmt.executeUpdate("DELETE FROM `WalkIns` WHERE wID = " + wID);
                output += ("\nYour Walk-in Information\nDate:\t" + wDate + "\nStart Time:\t" + wStartTime + "\nWalk-in ID:\t" + wID + "\nAssigned Spot:\t" + wAssignedSpot 
                		+ "\nTime Elapsed:\t" + hms + "\nTotal Cost:\t$" + cost + "\n");
                output += "\nThank you for choosing ZippyPark!\n";
        	}
            //Reservation Entrance
        	else if(AssignedSpot == -1) {
        		output += ("\nYour Reservation Information\nDate:\t" + Date + "\nStart Time:\t" + StartTime + "\nEnd Time:\t" + EndTime + "\nCharge:\t$" + Charge + "\nReservation ID:\t" + rID + "\n");
            	//OnTime
            	if(exitTime.compareTo(currentTime) >= 0 && enterTime.compareTo(currentTime) < 0) {
            		//Pick from regular spots
	            	int spot = 0;
	            	if(Handicapped == 0) {
	            		rs = stmt.executeQuery("SELECT * FROM `Parking Spots` WHERE Type = \"reserved\" AND Status = \"unoccupied\"");
	            		if(rs.next()) {
	            			spot = rs.getInt("SpotNum");
	            			stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"" + bc + "\" WHERE SpotNum = " + spot);
	            			stmt.executeUpdate("UPDATE `Reservations` SET AssignedSpot = " + spot + " WHERE rID  = " + rID);
	            			stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points + 5 WHERE Barcode  = " + bc);
	            			output += "Assigned Spot:\t" + spot + "\n";
	            		}
	            		else output += "\nThere are no regular spots available.\n";
	            	}
	            	//Pick from handicapped spots
	            	else {
	            		rs = stmt.executeQuery("SELECT * FROM `Parking Spots` WHERE Type = \"handicap\" AND Status = \"unoccupied\"");
	            		if(rs.next()) {
	            			spot = rs.getInt("SpotNum");
	            			stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"" + bc + "\" WHERE SpotNum = " + spot);
	            			stmt.executeUpdate("UPDATE `Reservations` SET AssignedSpot = " + spot + " WHERE rID  = " + rID);
	            			stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points + 5 WHERE Barcode  = " + bc);
	            			output += "Assigned Spot:\t" + spot + "\n";
	            		}
	            		else
	            			output += "\nThere are no handicap spots available.\n";
	            	}
            	}
            	//Early: should make Walk-in instead
            	else if(enterTime.compareTo(currentTime) > 0)
            		output += "\nYou are too early for your reservation.\n";
            	//This case should be cleaned up beforehand
            	else if(exitTime.compareTo(currentTime) < 0)
            		output += "\nYou have missed your reservation.\n";
            }
            //Exiting Reservation
            else if (AssignedSpot > 0){
            	long elapsedMillis = currentDate.getTime() - fullDate.parse(enterTime).getTime();
        		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedMillis),
        	            TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedMillis)),
        	            TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));
        		output += ("\nYour Reservation Information\nDate:\t" + Date + "\nStart Time:\t" + StartTime + "\nEnd Time:\t" + EndTime 
        				+ "\nReservation ID:\t" + rID + "\nAssigned Spot:\t" + AssignedSpot + "\nTime Elapsed:\t" + hms + "\n");
            	//Not Late
            	if(exitTime.compareTo(currentTime) >= 0 && enterTime.compareTo(currentTime) < 0) {
                    stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"unoccupied\" WHERE SpotNum = " + AssignedSpot);
                    stmt.executeUpdate("INSERT INTO `Records` (`Date`, `StartTime`, `EndTime`, `Barcode`, `AssignedSpot`, `Charge`, `Deleted`, `rID`, `wID`) "
                    		+ "SELECT Date, StartTime, EndTime, Barcode, AssignedSpot, Charge, 'Yes', rID, '-1' FROM Reservations WHERE rID = " + rID);
                    stmt.executeUpdate("DELETE FROM `Reservations` WHERE rID = " + rID);
                    output += "Total Cost:\t$" + Charge + "\n";
                    output += "\nThank you for choosing Zippypark!\n";
            	}
            	//Late
            	else if (exitTime.compareTo(currentTime) < 0){
            		output += "\nYou have exited late.\n";
            		long elapsedMillisLate = currentDate.getTime() - fullDate.parse(exitTime).getTime();
            		float lateCost = Charge + (elapsedMillisLate / 3600000 + 1) * 25;
            		String hmsLate = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedMillisLate),
            	            TimeUnit.MILLISECONDS.toMinutes(elapsedMillisLate) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedMillisLate)),
            	            TimeUnit.MILLISECONDS.toSeconds(elapsedMillisLate) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillisLate)));
            		stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"unoccupied\" WHERE SpotNum = " + AssignedSpot);
            		stmt.executeUpdate("INSERT INTO `Records` (`Date`, `StartTime`, `EndTime`, `Barcode`, `AssignedSpot`, `Charge`, `Deleted`, `rID`, `wID`) "
                    		+ "SELECT Date, StartTime, EndTime, Barcode, AssignedSpot, '" + lateCost + "', 'Yes', rID, '-1' FROM Reservations WHERE rID = " + rID);
                    stmt.executeUpdate("DELETE FROM `Reservations` WHERE rID = " + rID);
                    stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points - 5 WHERE Barcode = " + bc);
            		output += "Time Over:\t" + hmsLate + "\n";
                    output += "Total Cost:\t$" + lateCost + "\n";
                    //Notification of Extra Payment Required Here
            	}
            	//Early to Reservation (should not occur)
            	else output += "Your parking reservation has not begun yet.\n";	
            }
            
        }catch (JSchException e) {
            e.printStackTrace();
        }finally {
	    	if(conn != null && !conn.isClosed()){
	    		conn.close();
	    	}
	    	if(session !=null && session.isConnected()){
	    		session.disconnect();
	    	}
	    }
		return output;
	}
	
	private static Boolean isValid(String user, char[] pass) {
		if(user == null || user.isEmpty() || pass == null) return false;
		JSch jsch = new JSch();
        Session session;
		try {
			session = jsch.getSession(user, "coewww.rutgers.edu", 22);
			session.setPassword(new String(pass));
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();
	    	if(session !=null && session.isConnected()) session.disconnect();
	        return true;
		} catch (JSchException e) {
			return false;
		}
	}
}
