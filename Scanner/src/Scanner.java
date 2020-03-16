import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.jcraft.jsch.*;
import com.jcraft.jsch.Session;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Scanner {

	private JFrame frmScanner;
	private JTextField txtenterIdHere;
    static char[] password;
    private JPasswordField passwordField;

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
		frmScanner.setBounds(100, 100, 450, 300);
		frmScanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnValidate = new JButton("Enter");
		btnValidate.setBounds(374, 120, 76, 29);
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					process(txtenterIdHere.getText());
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				if(db.contains(txtenterIdHere.getText())) {
//					JOptionPane.showMessageDialog(null, "Valid Barcode.");
//				}
//				else
//					JOptionPane.showMessageDialog(null, "Invalid Barcode.");
			}
		});
		
		txtenterIdHere = new JTextField();
		txtenterIdHere.setBounds(41, 81, 321, 150);
		txtenterIdHere.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtenterIdHere.setText("");
			}
		});
		frmScanner.getContentPane().setLayout(null);
		txtenterIdHere.setText("ENTER BARCODE HERE");
		txtenterIdHere.setHorizontalAlignment(SwingConstants.CENTER);
		frmScanner.getContentPane().add(txtenterIdHere);
		frmScanner.getContentPane().add(btnValidate);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password = passwordField.getPassword();
			}
		});
		passwordField.setBounds(98, 29, 218, 40);
		frmScanner.getContentPane().add(passwordField);
	}
	
	@SuppressWarnings("resource")
	private static void process(String bc) throws ClassNotFoundException, SQLException {
		int rport = 3306;
        String rhost = "coewww.rutgers.edu";
        String user = "pbp71";
        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbUsername = "zippypark";
        String dbPassword = "goyhVynIiLcJgHpN";
        Session session = null;
        Connection conn = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(new String(password));
            session.setConfig("StrictHostKeyChecking", "no");
            //System.out.println("Establishing Connection...");
            session.connect();
            //System.out.println("Connection established.");
            
            int assigned_port = session.setPortForwardingL(0, "localhost", rport);
            //System.out.println("localhost:"+assigned_port+" -> "+rhost+":"+rport);
	    	//System.out.println("Port Forwarded");
	    	Class.forName(driverName);
	    	conn = DriverManager.getConnection("jdbc:mysql://localhost:" + assigned_port + "/zippypark?user=" + dbUsername + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8");
            //System.out.println ("Database connection established");
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
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
	            System.out.println("Hi " + FirstName + " " + LastName + " \nWelcome to Zippypark!");
            }
            else {
            	System.out.println("Barcode Does Not Exist");
            	return;
            }
            
            //Get Reservations
            rs = stmt.executeQuery("SELECT Date, StartTime, EndTime, AssignedSpot FROM Reservations WHERE Barcode = " + bc + " ORDER BY `Date`, `StartTime` ASC");
            String Date = "";
            String StartTime = "";
            String EndTime = "";
            int AssignedSpot = -1;
            if(rs.next()) {
	            Date = rs.getString("Date");
	            StartTime = rs.getString("StartTime");
	            EndTime = rs.getString("EndTime");
	            AssignedSpot = rs.getInt("AssignedSpot");
            }
            else {
            	System.out.println("Reservation Does Not Exist");
            	return;
            }
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String enterTime = Date + " " + StartTime;
            String exitTime = Date + " " + EndTime;
        	String currentTime = formatter.format(currentDate);
            
            //Reservation Entrance
            if(AssignedSpot == -1) {
            	//OnTime
            	if(exitTime.compareTo(currentTime) >= 0 && enterTime.compareTo(currentTime) < 0) {
            		//Pick from regular spots
	            	int spot = 0;
	            	if(Handicapped == 0) {
	            		rs = stmt.executeQuery("SELECT * FROM `Parking Spots` WHERE Type = \"reserved\" AND Status = \"unoccupied\"");
	            		if(rs.next()) {
	            			spot = rs.getInt("SpotNum");
	            			stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"" + bc + "\" WHERE SpotNum = " + spot);
	            			stmt.executeUpdate("UPDATE `Reservations` SET AssignedSpot = " + spot + " WHERE Barcode  = " + bc);
	            			stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points + 5 WHERE Barcode  = " + bc);
	            			System.out.println("You have been assigned spot #" + spot);
	            		}
	            		else
	            			System.out.println("No Regular Spot Available");
	            	}
	            	//Pick from handicapped spots
	            	else {
	            		rs = stmt.executeQuery("SELECT * FROM `Parking Spots` WHERE Type = \"handicap\" AND Status = \"unoccupied\"");
	            		if(rs.next()) {
	            			spot = rs.getInt("SpotNum");
	            			stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"" + bc + "\" WHERE SpotNum = " + spot);
	            			stmt.executeUpdate("UPDATE `Reservations` SET AssignedSpot = " + spot + " WHERE Barcode  = " + bc);
	            			stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points + 5 WHERE Barcode  = " + bc);
	            			System.out.println("You have been assigned spot #" + spot);
	            		}
	            		else
	            			System.out.println("No Handicap Spot Available");
	            	}
            	}
            	//Early
            	else if(enterTime.compareTo(currentTime) > 0) {
            		System.out.println("Too Early");
            	}
            	//This case should be cleaned up beforehand
            	else if(exitTime.compareTo(currentTime) < 0) {
            		System.out.println("Reservation Missed");
            	}
            }
            //Exiting
            else {
            	//Not Late
            	if(exitTime.compareTo(currentTime) >= 0 && enterTime.compareTo(currentTime) < 0) {
                    stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"unoccupied\" WHERE Status = " + bc);
                    stmt.executeUpdate("INSERT INTO `Records` SELECT *, \"Yes\" FROM `Reservations` WHERE EndTime = \"" + EndTime + "\"");
                    stmt.executeUpdate("DELETE FROM `Reservations` WHERE EndTime = \"" + EndTime + "\"");
                    System.out.println("User exited on time.");
            	}
            	//Late
            	else if (exitTime.compareTo(currentTime) < 0){
            		stmt.executeUpdate("UPDATE `Parking Spots` SET Status = \"unoccupied\" WHERE Status = " + bc);
            		stmt.executeUpdate("INSERT INTO `Records` SELECT *, \"Yes\" FROM `Reservations` WHERE EndTime = \"" + EndTime + "\"");
                    stmt.executeUpdate("DELETE FROM `Reservations` WHERE EndTime = \"" + EndTime + "\"");
                    stmt.executeUpdate("UPDATE `CustomerInfo` SET Points = Points - 5 WHERE Barcode = " + bc);
                    //Notification of Extra Payment Required Here
            	}
            	//Early
            	else {
            		System.out.println("Parking reservation has not begun.");
            	}
            		
            }
            
            
        }catch (JSchException e) {
            e.printStackTrace();
        }finally {
	    	if(conn != null && !conn.isClosed()){
	    		//System.out.println("Closing Database Connection");
	    		conn.close();
	    	}
	    	if(session !=null && session.isConnected()){
	    		//System.out.println("Closing SSH Connection");
	    		session.disconnect();
	    	}
	    }
	}
}
