/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */

package edu.emory.bmi.medicurator.general;

import edu.emory.bmi.medicurator.infinispan.ID;
import java.util.UUID;
import java.util.HashSet;
import java.io.Serializable;

/**
 * User represents an account of whom accesses MediCurator.
 * A User has a username, a password, and several ReplicaSets.
 * One can change his username/password, add or remove ReplicaSet in his account.
 */
public class User implements Serializable
{
    /**
     * the unique ID used to retrieve the User inside MediCurator
     */
    private UUID userID = UUID.randomUUID();

    /**
     * getID
     * @return userID
     */
    public UUID getID() { return userID; }

    private String username;
    private String password;

    /**
     *  store the IDs of ReplicaSets of this User
     */
    private HashSet<UUID> replicaSets;

    /**
     * lookup User
     * @param username String usernamme
     * @return User
     */
    public static User lookup(String username)
    {
	UUID userid = ID.getUserID(username);
	if (userid == null) return null;
	return ID.getUser(userid);
    }

    /**
     * create a new User with his username and password
     * @param username
     * @param password
     */
    public User(String username, String password)
    {
	this.username = username;
	this.password = password;
	replicaSets = new HashSet<UUID>();
	store();
    }

    /**
     * SetUsername
     * @param username string
     */
    public void setUsername(String username)
    {
	this.username = username;
	store();
    }

    /**
     * get username
     * @return String username
     */
    public String getUsername()
    {
	return username;
    }

    /**
     * change password
     * @param password String password
     */
    public void setPassword(String password)
    {
	this.password = password;
	store();
    }

    /**
     * compare User's password with giving String
     * @param password String password
     * @return boolean
     */
    public boolean checkPassword(String password)
    {
	return password.equals(this.password);
    }

    /**
     * get the array of IDs of ReplicaSets of the User
     * @return UUIDS of the replicaSets
     */
    public UUID[] getReplicaSets()
    {
	return (UUID[])replicaSets.toArray(new UUID[0]);
    }

    /**
     * add a ReplicaSet into this User
     * @param replicaSetID
     */
    public void addReplicaSet(UUID replicaSetID)
    {
	replicaSets.add(replicaSetID);
	store();
    }

    /**
     * remove a ReplicaSet of this User
     * @param replicaSetID UUID
     */
    public void removeReplicaSet(UUID replicaSetID)
    {
	replicaSets.remove(replicaSetID);
	store();
    }

    /**
     * store the information of this User to Infinispan
     */
    public void store()
    {
	ID.setUser(userID, this);
	ID.setUserID(username, userID);
    }
}

