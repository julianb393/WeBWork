package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;
import javax.swing.Timer;

import controllers.ExtractData;

/**
 * Class to display a list of assignments for an instructor.
 */
public class StudentListingGUI extends ListingGUI{

	private ArrayList<File> assignments;
	private ArrayList<File> openedAssigns;
	private ArrayList<File> closedAssigns;
	private Date today;
	private Timer timer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentListingGUI frame = new StudentListingGUI("student");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public StudentListingGUI(String email) {
		super(email);
		timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				displayAssignments();	
			}
		});
		timer.start();
	}

	@Override
	public void displayAssignments() {
		today = new Date();

		assignments = gatherExistingAssignments();
		openedAssigns = new ArrayList<File>();
		closedAssigns = new ArrayList<File>();
		
		/*
		 * Opened Assignments Section
		 */
		
		// Assignments Panel
		listAssignmentsPanel = new JPanel();
		listAssignmentsPanel.setBackground(Color.WHITE);
		contentPane.add(listAssignmentsPanel);
		listAssignmentsPanel.setLayout(null);
		
		Calendar calendar = Calendar.getInstance();
		Date due;
		String[] info, dueDate;
		boolean beforeDeadline;
		
		for(File assignment: assignments) {
			info = ExtractData.getAssignmentInfo(assignment.getName());
			dueDate = info[2].split("/");
			calendar.set(Integer.parseInt(dueDate[2]), Integer.parseInt(dueDate[0]) - 1, Integer.parseInt(dueDate[1])); 
			due = calendar.getTime();
			beforeDeadline = (due.compareTo(today) > 0);
			if(info[0].equals("Released") && beforeDeadline) {
				openedAssigns.add(assignment);
			} else if(info[0].equals("Released") && !beforeDeadline) {
				closedAssigns.add(assignment);
			}
		}
		
		// Open label.
		JLabel lblOpen = new JLabel("Open");
		lblOpen.setFont(new Font("Segoe UI Light", Font.PLAIN, 35));
		lblOpen.setBounds(0, 0, lblOpen.getWidth(), lblOpen.getHeight());
		lblOpen.setSize(lblOpen.getPreferredSize());
		listAssignmentsPanel.add(lblOpen);
		
		int yPos = 55;
		yPos += addSection(true, openedAssigns, yPos);
		
		// Closed Assignment label.
		JLabel lblClosed = new JLabel("Closed");
		lblClosed.setFont(new Font("Segoe UI Light", Font.PLAIN, 35));
		lblClosed.setBounds(0, yPos, lblClosed.getWidth(), 
				lblClosed.getHeight());
		lblClosed.setSize(lblClosed.getPreferredSize());
		listAssignmentsPanel.add(lblClosed);	
		
		yPos += 55;
		yPos = addSection(false, closedAssigns, yPos);

		listAssignmentsPanel.setBounds(62, 145, 765, yPos);
		contentPane.add(listAssignmentsPanel);
	
		contentPane.setPreferredSize(new Dimension(900, 100  + listAssignmentsPanel.getHeight()));
		setSize(900, 700);
		setLocationRelativeTo(null);
	}
	
	public String getEmail() { return this.email;}
	
	/**
	 * Return the y-position that indicates where
	 * the next section will start adding assignment.
	 * @param boolean beforeDeadline
	 * @param ArrayList<File> assignmentSection section that will be added to the
	 * 				content panel.
	 * @param int currYPos where to start added to.
	 * @return int y-position
	 */
	private int addSection(boolean beforeDeadline, ArrayList<File> assignmentSection, int currYPos) {
		int yPos = currYPos;
		for(File file: assignmentSection) {
			JPanel assignmentPanel = new JPanel();
			assignmentPanel.setLayout(null);
			assignmentPanel.setBackground(Color.decode("#F0F0F0"));
			assignmentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			assignmentPanel.setBounds(0, yPos, 765, 85);

			// Adds other components to each individual panel.
			if(beforeDeadline) {
				addToOpenedAssignmentPanel(assignmentPanel, file);
			} else {
				addToClosedAssignmentPanel(assignmentPanel, file);
			}
			yPos += 90;
			listAssignmentsPanel.add(assignmentPanel); // Adds to the main assignments panel.
		}
		return yPos;
	}
	
	/**
	 * Adds an interactive JButton to open assignments.
	 * 
	 * @param released: boolean Flag whether assignment is released or not.
	 * @param panel: JPanel for each component being added.
	 * @param file: The assignment's file. 
	 */
	private void addToOpenedAssignmentPanel(JPanel panel, File file) {
		String fileName = file.getName();
		String[] info = ExtractData.getAssignmentInfo(fileName);

		JLabel lblAssignment = new JLabel(fileName.replaceFirst("[.][^.]+$", "")); // Strips the .csv extension.
		lblAssignment.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblAssignment.setBounds(50, -3, 350, 70);
		panel.add(lblAssignment);
		
		JLabel lblDeadline = new JLabel("Due " + info[2]);
		lblDeadline.setFont(new Font("Segoe UI Regular", Font.PLAIN, 13));
		lblDeadline.setBounds(50, 22, 350, 70);
		lblDeadline.setBackground(Color.BLACK);
		panel.add(lblDeadline);
		
		// added an open button
		JButton openButton = new JButton("Open");
		openButton.setHorizontalTextPosition(SwingConstants.CENTER);
		openButton.setBounds(640, 26, 100, 35);
		openButton.setBackground(new Color(51, 204, 153));
		openButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		openButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  	String studentNumber = ExtractData.getStudentID(email);
				  	// if the studentNumber is not empty or null, open the assignment
				  	if(!studentNumber.isEmpty() && studentNumber != null) {
				  		 new AssignmentCompletionGUI(fileName, studentNumber);
				  	}
				  } 
				} );
		panel.add(openButton);
	}
	
	/**
	 * Adds an interactive JButton to view results of an assignment,
	 * and request a remark.
	 * 
	 * @param panel: JPanel for each component being added.
	 * @param file: The assignment's file. 
	 */
	private void addToClosedAssignmentPanel(JPanel panel, File file) {
		String fileName = file.getName(); 
		String[] info = ExtractData.getAssignmentInfo(fileName);
		
		JLabel lblAssignment = new JLabel(fileName.replaceFirst("[.][^.]+$", "")); // Strips the .csv extension.
		lblAssignment.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblAssignment.setBounds(50, -3, 350, 70);
		panel.add(lblAssignment);

		JLabel lblDeadline = new JLabel("Due " + info[2]);
		lblDeadline.setFont(new Font("Segoe UI Regular", Font.PLAIN, 13));
		lblDeadline.setBounds(50, 22, 350, 70);
		lblDeadline.setBackground(Color.BLACK);
		panel.add(lblDeadline);

		// Get the student's submission details for this assignment
		String studentID = ExtractData.getStudentID(email);
		HashMap<String, String> submissionDetails = ExtractData.getAssignmentSubmissionDetails(fileName, studentID);

		// If the student has a submission record, display the button for them to see the results
		if (!submissionDetails.isEmpty()) {
			JButton resultsButton = new JButton("Results");
			resultsButton.setHorizontalTextPosition(SwingConstants.CENTER);
			resultsButton.setBounds(640, 26, 100, 35);
			resultsButton.setBackground(new Color(51, 204, 153));
			resultsButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			resultsButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 

					//Creates a StudentSubmissionGetails GUI
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							StudentSubmissionDetailsGUI frame =  new StudentSubmissionDetailsGUI(fileName.substring(fileName.indexOf("t") + 1, fileName.indexOf(".")), submissionDetails);
							frame.setVisible(true);
							frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);									
						}
					});
				} 
			});
			panel.add(resultsButton);

			JButton btnRemark = new JButton("Remark");
			btnRemark.setHorizontalTextPosition(SwingConstants.CENTER);
			btnRemark.setBounds(520, 26, 100, 35);
			btnRemark.setFocusPainted(false);
			btnRemark.setBackground(Color.decode("#EC7063"));
			btnRemark.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			btnRemark.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					//Creates a RemarkRequest GUI
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							RemarkRequestGUI frame =  new RemarkRequestGUI(file, email);
							frame.setVisible(true);
							frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);									
						}
					});
				}

			});
			panel.add(btnRemark);	
		}
	}
}