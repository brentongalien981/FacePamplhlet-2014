/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

public class FacePamphletCanvasPrototype extends JPanel
	implements FacePamphletConstantsPrototype {
	
	private Image userProfileImage;
	private String userName, friendsTitle, userStatus, programMessagePrompt, noImageString;
	private Iterator<String> friendsList;
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvasPrototype() {
		setLayout(null);		
        setDoubleBuffered(false);
        setBackground(Color.black);
        
        userProfileImage = null;
        userName = "user name";
        friendsTitle = "friends:";
        friendsList = null;
        noImageString = "no image";
        userStatus = "user status";
        programMessagePrompt = "program prompt:";
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		programMessagePrompt = "program prompt: " + msg;
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfilePrototype profile) {
		update(profile);
		repaint();
	}
	
	public void update(FacePamphletProfilePrototype profile) {
		if (profile == null) {
	        userProfileImage = null;
	        userName = "user name";
	        friendsTitle = "friends:";
	        friendsList = null;
	        noImageString = "no image";
	        userStatus = "user status";
		}
		else {
		    userProfileImage = profile.getImage();
	        userName = profile.getName();
	        friendsTitle = "friends:";
	        
	        friendsList = profile.getFriends();
	        
	        userStatus = "user status: " + profile.getStatus();
		}

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.drawString(userName, LEFT_MARGIN, TOP_MARGIN);
		
		if (userProfileImage == null) {
			g.drawRect(LEFT_MARGIN, 	TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN, 		IMAGE_WIDTH, 	IMAGE_HEIGHT);
			g.drawString(noImageString, 	LEFT_MARGIN + IMAGE_WIDTH/2, 		TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN + IMAGE_HEIGHT/2); 
		}
		else {
			g.drawImage(userProfileImage, LEFT_MARGIN, TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT, null);
		}
		
		g.drawString(userStatus, 	LEFT_MARGIN, 		TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN);
		
		g.drawString(friendsTitle, getWidth()/2, TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN);
		int h = g.getFont().getSize();
		int lastHeight = TOP_MARGIN + g.getFont().getSize() + IMAGE_MARGIN + h+5;
		if (friendsList != null) {
			while (friendsList.hasNext()) {				
				g.drawString(friendsList.next(), getWidth()/2 + 8, 		lastHeight);
				lastHeight += (h+3);
			}
		}
		
		g.drawString(programMessagePrompt, 		LEFT_MARGIN, 	getHeight()-BOTTOM_MESSAGE_MARGIN);
		
	}

	
}
