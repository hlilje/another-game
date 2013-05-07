package game;

import java.util.HashMap;

/**
 * Inventory class used to keep track of the player items.
 * Some methods are implemented using keys, other using the
 * actual item itself.
 * 
 * @author Hampus Liljekvist
 * @version 2012-12-22
 */
public class Inventory {
	private HashMap<String, Item> itemMap;
	
	/**
	 * Constructor.
	 */
	public Inventory() {
		itemMap = new HashMap<String, Item>();
	}
	
	/**
	 * Add new item.
	 * 
	 * @param item The item to add
	 */
	public void addItem(Item item) {
		itemMap.put(item.getID(), item);
	}

	/**
	 * Remove an item using it's id.
	 * 
	 * @param key Should be the item id
	 */
	public void removeItem(String key) {
		itemMap.remove(key);
	}
	
	/**
	 * Check if the inventory contains the item.
	 * 
	 * @param key The id of the item to check
	 * @return true if true
	 */
	public boolean hasItem(String key) {
		return itemMap.containsKey(key);
	}
	
	/**
	 * Get the HashMap containing all the items in the
	 * inventory.
	 * 
	 * @return itemMap The HashMap containing all items
	 */
	public HashMap<String, Item> getAllItems() {
		return itemMap;
	}
}
