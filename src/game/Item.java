package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

/**
 * Item class used to represent objects which can be
 * picked up or interacted with.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class Item extends Rectangle {
	// Generated value used for serialisation
	private static final long serialVersionUID = 4734743661979418008L;
	private Image image;
	private String id;
	private Sound sound;
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Item(float x, float y, float width, float height, String id) {
		// Set the position and size
		super(x, y, width, height);
		this.id = id;
	}
	
	/**
	 * Set the image used by this class.
	 * 
	 * @param filePath The file path the image
	 * @throws SlickException
	 */
	public void setImage(String filePath) throws SlickException {
		try {
			image = new Image(filePath);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the image of this item.
	 * 
	 * @return image This item's image
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Setter for item ID. 
	 * 
	 * @return id The ID
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Set new id.
	 * 
	 * @param id The new id to set
	 */
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Set sound.
	 * 
	 * @param filePath
	 * @throws SlickException
	 */
	public void setSound(String filePath) throws SlickException {
		try {
			sound = new Sound(filePath);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter for sound. 
	 * 
	 * @return sound The sound
	 */
	public Sound getSound() {
		return sound;
	}
}
