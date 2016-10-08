package com.whyq.session;

import java.util.HashMap;

public class Storage {
	private static Storage instance = null;
	private static HashMap<String, SessionData> map = new HashMap<String, SessionData>();

	private Storage() {
	}

	public static synchronized Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
		}

		return instance;
	}

	public static boolean hasObject(String key) {
		if (map.get(key) != null) {
			return true;
		} else {
			return false;
		}
	}

	public static SessionData getObject(String key) {

		return map.get(key);

	}

	/**
	 * @return the map
	 */
	public static HashMap<String, SessionData> getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public static void setMap(HashMap<String, SessionData> map) {
		Storage.map = map;
	}

	/**
	 * @param instance
	 *            the instance to set
	 */
	public static void setInstance(Storage instance) {
		Storage.instance = instance;
	}

	public static void addElementToMap(String key, SessionData sessionData) {
		map.put(key, sessionData);
	}

}
