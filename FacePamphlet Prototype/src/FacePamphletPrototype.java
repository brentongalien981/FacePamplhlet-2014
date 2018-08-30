import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FacePamphletPrototype extends JFrame
	implements FacePamphletConstantsPrototype, ActionListener {
	
	private JPanel northJPanel, westJPanel;
	private JTextField nameTextField;
	private FacePamphletDatabasePrototype database;
	private FacePamphletCanvasPrototype userCanvas;
	private JTextField changeStatusTextField;
	private JTextField changePictureTextField;
	private JTextField addFriendTextField;
	private FacePamphletProfilePrototype currentUserProfile;

	public FacePamphletPrototype() {
	    setTitle("FacePamphletPrototype");
	    setLayout(new BorderLayout()); /**@param -- of BorderLayout() sets the space between components*/
	    setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
	    setBackground(Color.RED);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    database = new FacePamphletDatabasePrototype();
	    userCanvas = new FacePamphletCanvasPrototype();
	    currentUserProfile = null;

	    initNorthJPanel();
	    initWestJPanel();	    
	    
	    add(northJPanel, BorderLayout.NORTH);
	    add(westJPanel, BorderLayout.WEST);
	    add(userCanvas, BorderLayout.CENTER);
	    
	    setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		String userName, msg;
		msg = "";
		
		switch(ae.getActionCommand()) {
		case "Add":	
			userName = nameTextField.getText();
			if (!userName.equals("")) { // if name textfield is not empty			
				//if ((currentUserProfile == null)	||	(!(currentUserProfile.getName().equals(userName)))) { 
				if (database.containsProfile(userName)) { 
					msg = "the username " + userName + " already exists"; 
					currentUserProfile = database.getProfile(userName);
				}
				else { 
					currentUserProfile = new FacePamphletProfilePrototype(userName);
					database.addProfile(currentUserProfile);
					msg = "added " + userName + " to the database"; 
				}
				
				userCanvas.showMessage(msg);
				userCanvas.displayProfile(currentUserProfile);
			}
			break;
		case "Delete":			
			if (!((userName = nameTextField.getText()).equals(""))) {				
				if (database.containsProfile(userName)) { 
					FacePamphletProfilePrototype tempUserProfile, updatedUserProfile;					
					
					Iterator<String> friendList = database.getProfile(userName).getFriends();
					Iterator<String> it;
					while (friendList.hasNext()) { // user-to-be-deleted's friendslist
						tempUserProfile = database.getProfile(friendList.next());
						tempUserProfile.removeFriend(userName); // remove the user-to-be-deleted to his friends' friendslist
						
						updatedUserProfile = new FacePamphletProfilePrototype(tempUserProfile.getName());
						updatedUserProfile.setStatus(tempUserProfile.getStatus());
						updatedUserProfile.setImage(tempUserProfile.getImage(), tempUserProfile.getImageName());
						
						it = tempUserProfile.getFriends();
						while (it.hasNext()) {
							updatedUserProfile.addFriend(it.next());
						}
						// update the friends' profiles
						database.deleteProfile(tempUserProfile.getName());
						database.addProfile(updatedUserProfile);
					}
					
					// if the name typed is the current user
					if (userName.equals(currentUserProfile.getName())) {
						currentUserProfile = null;
					}
					else { // if the name being deleted is not the currently displayed profile						
						
						// create a renewed version of the user profile
						currentUserProfile = new FacePamphletProfilePrototype(currentUserProfile.getName());						
						
						// get a reference of the previous version of the user profile
						tempUserProfile = database.getProfile(currentUserProfile.getName());
						
						// copy the states oof the user to be retained from
						// the previous to the current and set the user's new profiile image
						currentUserProfile.setStatus(tempUserProfile.getStatus());
						
						friendList = tempUserProfile.getFriends();
						
						while(friendList.hasNext()) {
							currentUserProfile.addFriend(friendList.next()); // this returns boolean
						}
						
						currentUserProfile.setImage(tempUserProfile.getImage(), tempUserProfile.getImageName());
						
						database.deleteProfile(tempUserProfile.getName());
						database.addProfile(currentUserProfile);
					}

					msg = "deleted " + userName;
					database.deleteProfile(userName);
					//userCanvas.displayProfile(currentUserProfile);
				}
				else {					
					msg = "no " + userName + " exists in database";
				}
				
				userCanvas.showMessage(msg);
				userCanvas.displayProfile(currentUserProfile);
			}			
			break;
		case "Lookup":
			userName = nameTextField.getText();
			if (database.containsProfile(userName)) {
				currentUserProfile = database.getProfile(userName); 
				msg = "searched " + userName;
			}
			else {
				msg = "no " + userName + " exists in database";
			}
			
			userCanvas.showMessage(msg);
			userCanvas.displayProfile(currentUserProfile);
	
			break;
		case "Change Status":
			if (currentUserProfile != null) {
				userName = currentUserProfile.getName();
				String status = changeStatusTextField.getText();
				if (!status.equals("")) { // if status text field contains text										
					FacePamphletProfilePrototype tempUserProfile = database.getProfile(userName);
					currentUserProfile = new FacePamphletProfilePrototype(userName);	
					
					// change status
					currentUserProfile.setStatus(status);
					
					// retain friends
					Iterator<String> friendList = tempUserProfile.getFriends();					
					while(friendList.hasNext()) {
						currentUserProfile.addFriend(friendList.next()); // this returns boolean
					}
					
					// retain picture
					currentUserProfile.setImage(tempUserProfile.getImage(), tempUserProfile.getImageName());

					database.deleteProfile(userName);
					database.addProfile(currentUserProfile);

					msg = userName + " changed status";
					userCanvas.showMessage(msg);
					userCanvas.displayProfile(currentUserProfile); 
				}
			}
			break;
		case "Change Picture":
			if (currentUserProfile != null) {
				String userImageName = changePictureTextField.getText();
				if (!userImageName.equals("")) {
					userName = currentUserProfile.getName();					
					Image userProfileImage;
					try {
						userProfileImage = ImageIO.read(new File("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\images\\" + userImageName));
					}
					catch(Exception exc) {
						msg = "image does not exist";
						userProfileImage = null;
					}
					
					if (!(userProfileImage == null)) {
						// create a renewed version of the user profile
						currentUserProfile = new FacePamphletProfilePrototype(userName);						
						
						// get a reference of the previous version of the user profile
						FacePamphletProfilePrototype tempUserProfile = database.getProfile(userName);
						
						// copy the states oof the user to be retained from
						// the previous to the current and set the user's new profiile image
						currentUserProfile.setStatus(tempUserProfile.getStatus());
						
						Iterator<String> friendList = tempUserProfile.getFriends();
						
						while(friendList.hasNext()) {
							currentUserProfile.addFriend(friendList.next()); // this returns boolean
						}
						
						currentUserProfile.setImage(userProfileImage, userImageName);
						
						database.deleteProfile(userName);
						database.addProfile(currentUserProfile);
						
						msg = userName + " changed profile piccture";
					}
					
					userCanvas.showMessage(msg);
					userCanvas.displayProfile(currentUserProfile);				
				}
			}
			break;
		case "Add Friend":
			// 
			if (currentUserProfile != null) {
				userName = currentUserProfile.getName();
				String friendBeingAdded = addFriendTextField.getText();
				if (database.containsProfile(friendBeingAdded)		&&		(!userName.equals(friendBeingAdded))) {	
					if (currentUserProfile.addFriend(friendBeingAdded)) {// if the friend is not already in user's friendslist
						// EDIT THE CURRENT USER
						// get a reference of the previous version of the user profile
						FacePamphletProfilePrototype tempUserProfile = database.getProfile(userName);
						
						// create a renewed version of the user profile
						currentUserProfile = new FacePamphletProfilePrototype(userName);
						
						// copy the states oof the user to be retained from
						// the previous to the current and set the user's new profiile image
						currentUserProfile.setStatus(tempUserProfile.getStatus());
						
						// refresh friends list
						Iterator<String> friendList = tempUserProfile.getFriends();					
						currentUserProfile.addFriend(friendBeingAdded); // this returns boolean
						while(friendList.hasNext()) {
							currentUserProfile.addFriend(friendList.next()); // this returns boolean
						}
						
						// retain picture
						currentUserProfile.setImage(tempUserProfile.getImage(), tempUserProfile.getImageName());
						
						database.deleteProfile(userName);
						database.addProfile(currentUserProfile);
						
						// *** EDIT THE INFO OF THE FRIEND BEING ADDED TO ***
						// ADD THE CURRENT USER TO HIS LIST OF FRIENDS
						tempUserProfile = database.getProfile(friendBeingAdded);
						FacePamphletProfilePrototype tempUserProfile2 = new FacePamphletProfilePrototype(friendBeingAdded);
						
						// status
						tempUserProfile2.setStatus(tempUserProfile.getStatus());
						
						// friends
						friendList = tempUserProfile.getFriends();					
						tempUserProfile2.addFriend(userName); // this returns boolean
						while(friendList.hasNext()) {
							tempUserProfile2.addFriend(friendList.next()); // this returns boolean
						}
						
						// picture
						tempUserProfile2.setImage(tempUserProfile.getImage(), tempUserProfile.getImageName());
						
						// update database
						database.deleteProfile(friendBeingAdded);
						database.addProfile(tempUserProfile2);

						msg = "you're now friends with " + friendBeingAdded;
						userCanvas.displayProfile(currentUserProfile);
						
					}
					else { msg = friendBeingAdded + " is already your friend"; }
					userCanvas.showMessage(msg);
				}
			}
			break;
		}
	}
	
	private void initNorthJPanel() {
		northJPanel = new JPanel();
		northJPanel.setLayout(new FlowLayout());
		
		JLabel nameLabel = new JLabel("Name");
		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		JButton addButton = new JButton("Add");
		JButton deleteButton = new JButton("Delete");
		JButton lookUpButton = new JButton("Lookup");
		
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		lookUpButton.addActionListener(this);
		
		northJPanel.add(nameLabel);
		northJPanel.add(nameTextField);
		northJPanel.add(addButton);
		northJPanel.add(deleteButton);
		northJPanel.add(lookUpButton);
	}
	
	private void initWestJPanel() {
		westJPanel = new JPanel();
		westJPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		westJPanel.setPreferredSize(new Dimension(WEST_PANEL_WIDTH, APPLICATION_HEIGHT));
		
		changeStatusTextField = new JTextField(TEXT_FIELD_SIZE);
		changePictureTextField = new JTextField(TEXT_FIELD_SIZE);
		addFriendTextField = new JTextField(TEXT_FIELD_SIZE);
		JButton changeStatusButton = new JButton("Change Status");
		JButton changePictureButton = new JButton("Change Picture");
		JButton addFriendButton = new JButton("Add Friend");
		
		changeStatusTextField.setActionCommand("Change Status");
		changePictureTextField.setActionCommand("Change Picture");
		addFriendTextField.setActionCommand("Add Friend");
		
		changeStatusTextField.addActionListener(this);
		changeStatusButton.addActionListener(this);
		changePictureTextField.addActionListener(this);
		changePictureButton.addActionListener(this);
		addFriendTextField.addActionListener(this);
		addFriendButton.addActionListener(this);
		
		westJPanel.add(changeStatusTextField);
		westJPanel.add(changeStatusButton);
		westJPanel.add(changePictureTextField);
		westJPanel.add(changePictureButton);
		westJPanel.add(addFriendTextField);
		westJPanel.add(addFriendButton);
	}
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable () {
				public void run() {
					new FacePamphletPrototype();
				}
			});
		}
		catch(Exception exc) { System.out.println("Can't create because of "+ exc); }
	}
}
