package com.actimel.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.actimel.models.Session;
import com.actimel.models.User;
import com.actimel.utils.Utils;

public class SessionController {
	private String SESSION_SALT = "a8b8c9d9ce0a9c8f8c9088ff9a9d908f9c7a6a9c08f79ca909c8f7f6a8fb9abc9088a7";
	
	private HashMap<String, Session> sessions = new HashMap<String, Session>();
	
	public SessionController() {
		
	}
	
	public Session createSession(User user, String ip) {
		String key = createSessionKey(user.getName(), ip, SESSION_SALT);
		Session session = new Session(user, key);
		sessions.put(key, session);	
		return session;
	}
	
	public Session getSessionByKey(String key) {
		for(Entry<String, Session> entry : sessions.entrySet()) {
			if(entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}
	public boolean destroySession(String key) {
		if(sessions.containsKey(key)) {
			sessions.remove(key);
		}
		return false;
	}
	
	public boolean isSessionKeyValidForUser(String key, User user, String ip) {
		if(key == null || user == null || ip == null) return false;
		String serverKey = createSessionKey(user.getName(), ip, SESSION_SALT);		
		return key.equals(serverKey) && sessions.containsKey(key);
	}
	
	public static String createSessionKey(String username, String ip, String salt) {
		try {
			return Utils.sha256(salt + ip + username);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
