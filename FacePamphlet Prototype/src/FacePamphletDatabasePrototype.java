/*
 * File: FacePamphletDatabase.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class FacePamphletDatabasePrototype implements FacePamphletConstantsPrototype {
	private HashMap<String, FacePamphletProfilePrototype> databaseHashMap;
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the database.
	 */
	public FacePamphletDatabasePrototype() {
		databaseHashMap = new HashMap<String, FacePamphletProfilePrototype>();
		loadDatabase();
		
	}
	
	private void loadDatabase() {
		try {
			BufferedReader br = new BufferedReader(
					// search on how to make accessing directories shorter
					new FileReader("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\src\\FacePamphletDatabase.txt"));
		
			String userCompleteInfo;
			String userName;
			String userStatus;
			String userFriendsList;
			String userImageName;
			int endOfName;
			
			FacePamphletProfilePrototype userProfile;
			
			while (true) {				
				userCompleteInfo = br.readLine();
				if (userCompleteInfo == null) break;
				
				// process the userName from the line of read String
				endOfName = userCompleteInfo.indexOf("(");
				userName = userCompleteInfo.substring(0, endOfName-1);

				// process the userStatus from the line of read String
				userStatus = userCompleteInfo.substring(endOfName+1, userCompleteInfo.indexOf(")"));
				
				// process the friendOfUser from the line of read String
				userFriendsList = userCompleteInfo.substring(userCompleteInfo.indexOf(":")+1, userCompleteInfo.indexOf("["));
				
				// process user's profile picture
				userImageName = userCompleteInfo.substring(userCompleteInfo.indexOf("[")+1, userCompleteInfo.indexOf("]"));
				
				// set a user's entire profile from the textfile database
				// to the all-users' hashmap
				userProfile = new FacePamphletProfilePrototype(userName);
				userProfile.setStatus(userStatus);
				
				// set user's friends list
				if (!userFriendsList.equals("")) { // if there is a friend
					StringTokenizer st = new StringTokenizer(userFriendsList,",");
					
					while(st.hasMoreTokens()) {
						userProfile.addFriend(st.nextToken()); // this returns boolean
					}
				}
				
				
				Image userProfileImage;
				try {
					userProfileImage = ImageIO.read(new File("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\images\\" + userImageName));
				}
				catch(Exception exc) {
					System.out.println("image does not exist");
					userProfileImage = null;
				}
				userProfile.setImage(userProfileImage, userImageName);
				
				databaseHashMap.put(userName, userProfile);
			}
			
			br.close();
		}
		catch(IOException ex) { System.out.print("error on: " + ex); }
	}
	
	
	/** 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 */
	public void addProfile(FacePamphletProfilePrototype profile) {		
		databaseHashMap.put(profile.getName(), profile);
		
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\src\\FacePamphletDatabase.txt", true)); // true for append
		
			bw.write(profile.toString());
			bw.newLine();
			bw.close();
		}
		catch(IOException ex) { System.out.print("error on: " + ex); }
	}

	
	/** 
	 * This method returns the profile associated with the given name 
	 * in the database.  If there is no profile in the database with 
	 * the given name, the method returns null.
	 */
	public FacePamphletProfilePrototype getProfile(String name) {
		return databaseHashMap.get(name);
	}
	
	
	/** 
	 * This method removes the profile associated with the given name
	 * from the database.  It also updates the list of friends of all
	 * other profiles in the database to make sure that this name is
	 * removed from the list of friends of any other profile.
	 * 
	 * If there is no profile in the database with the given name, then
	 * the database is unchanged after calling this method.
	 */
	public void deleteProfile(String name) {
		databaseHashMap.remove(name);
		
		// iterate through the entire hashmap
		Iterator<String> it = databaseHashMap.keySet().iterator(); 
		
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\src\\FacePamphletDatabase.txt"));
		
			// put all userprofiles from the hashmap to the textfile
			while (it.hasNext()) {
				bw.write(databaseHashMap.get(it.next()).toString());
				bw.newLine();				
			}
			bw.close();
		}
		catch(IOException ex) { System.out.print("error on: " + ex); }
	}

	
	/** 
	 * This method returns true if there is a profile in the database 
	 * that has the given name.  It returns false otherwise.
	 */
	public boolean containsProfile(String name) {
		Iterator<String> it = databaseHashMap.keySet().iterator(); 
		
		while (it.hasNext()) {
			if (name.equals(it.next())) {return true;}
		}
		return false;
	}

}
