/**
* OLAT - Online Learning and Training<br>
* http://www.olat.org
* <p>
* Licensed under the Apache License, Version 2.0 (the "License"); <br>
* you may not use this file except in compliance with the License.<br>
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing,<br>
* software distributed under the License is distributed on an "AS IS" BASIS, <br>
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
* See the License for the specific language governing permissions and <br>
* limitations under the License.
* <p>
* Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
* University of Zurich, Switzerland.
* <hr>
* <a href="http://www.openolat.org">
* OpenOLAT - Online Learning and Training</a><br>
* This file has been modified by the OpenOLAT community. Changes are licensed
* under the Apache 2.0 license as the original file.  
* <p>
*/ 

package org.olat.instantMessaging;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.id.Identity;
import org.olat.core.id.OLATResourceable;
import org.olat.core.util.resource.OresHelper;
import org.olat.instantMessaging.groupchat.GroupChatManagerController;
import org.olat.instantMessaging.ui.ConnectedUsersListEntry;

/**
 * Initial Date: 18.01.2005
 * @author guido<br />
 * Interface Instant Messaging
 * interface for public accessible functions used in the
 * framework outside the package <P>
 */
public interface InstantMessaging {
	/**
	 * Category of events which trigger close/open chat from extern
	 */
	public static final OLATResourceable TOWER_EVENT_ORES = OresHelper.createOLATResourceableType("InstantMessagingTower");
	
	
	/**
	 * called when OLAT server is started and needs to sync the learinggroups with the IM server.
	 */
	public boolean synchronizeBusinessGroupsWithIMServer();

	/**
	 * This method should only be called once as it creates the main controller and the groupchat controller for a single user
	 * @param ureq
	 * @param wControl
	 * @return a controller that represents that status changer and the buddy list
	 */
	public Controller createClientController(UserRequest ureq, WindowControl wControl);
	
	/**
	 * @param ores
	 * @return a String like BusinessGroup-123456
	 */
	public String createChatRoomString(OLATResourceable ores);
	
	/**
	 * @param ores
	 * @return a Jabber ID like grouid@conference.jabber.olat.uzh.ch
	 */
	public String createChatRoomJID(OLATResourceable ores);
	
	/**
	 * get the controller for creating and managing groupChats
	 * @return
	 */
	public GroupChatManagerController getGroupChatManagerController(UserRequest ureq);
	
	/**
	 * Sync a group
	 * @param groupId
	 * @param groupname
	 * @param addedUsers
	 * @param removedUsers
	 */
	public boolean syncFriendsRoster(String groupId, String groupname, Collection<String> addedUsers, Collection<String> removedUsers);

	/**
	 * Delete roster group from instant messaging server
	 * 
	 * @param groupId
	 */
	public boolean deleteRosterGroup(String groupId);
	
	/**
	 * rename roster group on instant messaging server
	 * 
	 * @param groupId
	 * @param displayName
	 */
	public boolean renameRosterGroup(String groupId, String displayName);

	/**
	 * send the message to: all members of the VisibilityGroup where 'username' is
	 * member.
	 * 
	 * @param username an OLAT unique username
	 * @param message
	 */
	public void sendStatus(String username, String message);

	/**
	 * @param username
	 * @return the instant messaging password (differs form the olat password)
	 */
	public String getIMPassword(String username);

	/**
	 * @return Map with client Objects (InstantMessagingClient)
	 */
	public Map<String, InstantMessagingClient> getClients();

	/**
	 * @return a Set with the users connected to the IM server
	 */
	public Set<String> getUsernamesFromConnectedUsers();

	/**
	 * enable the chat and groupchat possibilities
	 * 
	 * @param username
	 */
	public void enableChat(String username);

	/**
	 * disable the chat and groupchat possibilities
	 * 
	 * @param username
	 * @param reason A reason why the user is not able to chat.
	 */
	public void disableChat(String username, String reason);

	/**
	 * @return number of connected instant messaging users
	 */
	public int countConnectedUsers();
	
	/**
	 * 
	 * @return a list with information for each user which is connected to the instant messaging server
	 * either locally or clusterwide
	 */
	public List<ConnectedUsersListEntry> getAllConnectedUsers(Identity currentUser);
	
	/**
	 * creates an account on the instant messaging server
	 * @param username
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @return true if successful
	 */
	public boolean createAccount(String username, String password, String fullname, String email);

	/**
	 * delete an account on the im server
	 * @param username
	 */
	public boolean deleteAccount(String username);
	
	/**
	 * with the client manager you have access to the IM client for sending messages/presence information
	 * @return
	 */
	public ClientManager getClientManager();
	
	/**
	 * provides access to the IM settings like admin user/password and servername...
	 * @return
	 */
	public IMConfig getConfig();

	/**
	 * check whether accounts exists on IM server
	 * @param username
	 * @return
	 */
	public boolean hasAccount(String username);

	/**
	 * 
	 * @param username
	 * @return a jabber id like admin@jabber.olat.uzh.ch
	 */
	public String getUserJid(String username);

	/**
	 * 
	 * @param from
	 * @return only the username part from a jid
	 */
	public String getUsernameFromJid(String from);

	/**
	 * 
	 * @param name
	 * @return the username which is save for the IM server and may contains an instance id
	 * if IM server is used in a multi domain setup
	 */
	public String getIMUsername(String name);
	
	/**
	 * 
	 * @param nameHelper
	 */
	public void setNameHelper(IMNameHelper nameHelper);

	/**
	 * @return the IMNameHelper for building multipleinstance-save user/group-names
	 */
	public IMNameHelper getNameHelper();
	
	/**
	 * reset and reconnect the admin connection to the IM server. Upon failures the admin connection reconnects automatically
	 * but not on resource conflicts and manual connection close.
	 */
	public void resetAdminConnection();
	
	/**
	 * use rarely, as this is normally linked by spring!
	 * @return AdminUserConnection the connection object
	 */
	public AdminUserConnection getAdminUserConnection();

	/**
	 * check wheter the server plugin is running and the correct version is
	 * @return string with information about the plugin.
	 */
	public String checkServerPlugin();
		
	/**
	 * can be used to synchronize all OLAT users with IM-server
	 * will also recreate the buddy-roaster 
	 * it tries to find former invalid users, double wrapped (account_instanceID_instanceID) and delete them
	 */
	public String synchronizeAllOLATUsers();
	
}