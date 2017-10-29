package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class AssignmentListingGUI extends JFrame{
	private JPanel contentPane;
	private JScrollPane scroll;
	private JPanel releasedPanel;
	private JPanel unreleasedPanel;
	private List<File> assignments;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AssignmentListingGUI frame = new AssignmentListingGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public AssignmentListingGUI() {
				
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 731);
		setTitle("WebWork");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0.9919f, 0.97f, 0.99159f));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		
		// Welcome label.
		JLabel lblWelcome = new JLabel("Welcome,");
		lblWelcome.setFont(new Font("Segoe UI Light", Font.PLAIN, 25)); // Light pink
		lblWelcome.setBounds(62, 15, 80, 50);
		lblWelcome.setSize(lblWelcome.getPreferredSize());
		contentPane.add(lblWelcome);
	
		
		// User's Name label.
		JLabel lblName = new JLabel("Insert Name");
		lblName.setFont(new Font("Segoe UI Light", Font.PLAIN, 55));
		lblName.setBounds(62, 45, 350, 70);
		lblName.setSize(lblName.getPreferredSize());
		contentPane.add(lblName);
		
		// Every existing assignment copied into an ArrayList.
		assignments = gatherExistingAssignments();
		

		/*
		 * Released Assignments Section
		 */
		
		// Released Assignments Panel
		releasedPanel = new JPanel();
		releasedPanel.setBackground(new Color(0.9919f, 0.97f, 0.99159f));
		contentPane.add(releasedPanel);
		releasedPanel.setLayout(null);
		
		// Released label.
		JLabel lblReleased = new JLabel("Released");
		lblReleased.setFont(new Font("Segoe UI Light", Font.PLAIN, 35));
		lblReleased.setBounds(0, 0, 350, 70);
		lblReleased.setSize(lblReleased.getPreferredSize());
		releasedPanel.add(lblReleased);
		
		JLabel lblAssignment;
		JLabel lblDeadline;
		// Make a JPanel for every existing assignment.
		int i = 0;
		for(File file: assignments) {
			JPanel assignReleasedPanel = new JPanel();
			assignReleasedPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			assignReleasedPanel.setLayout(null);
			String fileName = file.getName();
			String[] info = getAssignmentInfo(fileName);
			if(info[0].equals("Released")) {
				assignReleasedPanel.setBounds(0, 55 + i, 765, 85);
				assignReleasedPanel.setBackground(Color.WHITE);
				
				lblAssignment = new JLabel(fileName.replaceFirst("[.][^.]+$", "")); // Strips the .csv extension.
				lblAssignment.setFont(new Font("Segoe UI Light", Font.BOLD, 20));
				lblAssignment.setBounds(50, 0, 350, 70);
				assignReleasedPanel.add(lblAssignment);
				
				lblDeadline = new JLabel("Due " + info[2]);
				lblDeadline.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
				lblDeadline.setBounds(50, 22, 350, 70);
				lblDeadline.setBackground(Color.BLACK);
				assignReleasedPanel.add(lblDeadline);
			
				i += 90;
				
			}
			//assignmentPanel.setLayout(null);
			
			releasedPanel.add(assignReleasedPanel);
		
			
		}

		// Unreleased label.
		JLabel lblUnreleased = new JLabel("Unreleased");
		lblUnreleased.setFont(new Font("Segoe UI Light", Font.PLAIN, 35));
		lblUnreleased.setSize(lblUnreleased.getPreferredSize());
		lblUnreleased.setBounds(0,  75 + i , lblUnreleased.getWidth(), 
				lblUnreleased.getHeight());
		
		releasedPanel.add(lblUnreleased);
		
		

		for(File file: assignments) {
			JPanel assignUnreleasedPanel = new JPanel();
			assignUnreleasedPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			assignUnreleasedPanel.setLayout(null);
			String fileName = file.getName();
			String[] info = getAssignmentInfo(fileName);
			if(info[0].equals("Unreleased")) {
				assignUnreleasedPanel.setBounds(0, 130 + i, 765, 85);
				assignUnreleasedPanel.setBackground(Color.WHITE);

				lblAssignment = new JLabel(fileName.replaceFirst("[.][^.]+$", "")); // Strips the .csv extension.
				lblAssignment.setFont(new Font("Segoe UI Light", Font.BOLD, 20));
				lblAssignment.setBounds(50, 0, 350, 70);
				assignUnreleasedPanel.add(lblAssignment);
				
				lblDeadline = new JLabel("Due " + info[2]);
				lblDeadline.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
				lblDeadline.setBounds(50, 22, 350, 70);
				assignUnreleasedPanel.add(lblDeadline);
				
				i += 90;
			}
			
			releasedPanel.add(assignUnreleasedPanel);
	
		}
		
		
		releasedPanel.setBounds(62, 145, 765, 150 + i);
		contentPane.add(releasedPanel);

	
	}
	
	private ArrayList<File> gatherExistingAssignments(){
		
		ArrayList<File> assignments = new ArrayList<>();
		Pattern pattern = Pattern.compile("Assignment*\\d");
	    Matcher matcher;
	    
	    File[] files = new File(".").listFiles(); // All files in current directory.
	    for(File file: files) {
	    	if(file.isFile()) {
	    		matcher = pattern.matcher(file.getName());
	    		if(matcher.find()) { // If file name matches the regex expression in pattern.
	    			assignments.add(file);
	    		}
	    	}
	    }  
	    return assignments;
	}
	
   /**
    * Returns true if the first cell of assignment#.csv is "Released"
    * @param fileName: The file where release status is to be checked
    * @return: True if first cell is "Released"
    */
    @SuppressWarnings("resource")
	public static boolean isReleased(String fileName){

    	try {
    		FileReader fr = new FileReader(fileName);
    		BufferedReader br = new BufferedReader(fr);
    		// Check first cell for unreleased
    		String line = br.readLine();
    		if (line.equals("Unreleased")){
    			return false;
    		}
    		br.close();
    		fr.close();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
        }
        return true;
    }
    
    /*
     * [(Un)released/Due-date/date of creation]
     */
    public String[] getAssignmentInfo(String fileName) {
    	
    	String[] info = new String[3];
    	
    	try {
    		FileReader fr = new FileReader(fileName);
    		BufferedReader br = new BufferedReader(fr);
    		// Check first cell for unreleased
    		String line = br.readLine();
    		info = line.split(",");
    		br.close();
    		fr.close();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
        }
    	
    	return info;

    }
}