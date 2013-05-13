package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

/**
 * This class is supposed to represent a player object
 * in a usable way, probably by having the player be
 * a geometric object.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-13
 */
public class Player extends Polygon {
	// Generated value used for serialisation
	private static final long serialVersionUID = 125683132315162989L;
	private Animation avatar, upAnim, downAnim, leftAnim,
	rightAnim;
	private int health, stamina, maxStamina;
	private boolean alive;
	String direction;
	/*
	 * Horrible way to make sure that all states have access to
	 * the player inventory, if a new player would be created,
	 * this inventory would be discarded.
	 */
	private static Inventory inv;
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 * @throws SlickException 
	 */
	public Player(float x, float y) throws SlickException {
		addPoints();
		// Set the position coordinates
		super.setX(x);
		super.setY(y);
		initAnimations();
		inv = new Inventory();
		health = 100;
		maxStamina = 100;
		stamina = maxStamina;
		alive = true;
	}

	/**
	 * Add points to create a square polygon. 
	 */
	private void addPoints() {
		// Assuming the player is drawn as a box
		int size = 25;
		addPoint(0, 0);
		addPoint(0, size);
		addPoint(size, size);
		addPoint(size, 0);
	}
	
	/**
	 * Initialise the images and resources used for this players
	 * animations.
	 * 
	 * @throws SlickException
	 */
	private void initAnimations() throws SlickException {
		int[] moveDur = {300, 300}; // ms
		Image[] moveUpImg = {new Image("res/img/avatarUp1.png"),
				new Image("res/img/avatarUp2.png")};
		Image[] moveDownImg = {new Image("res/img/avatarDown1.png"),
				new Image("res/img/avatarDown2.png")};
		Image[] moveLeftImg = {new Image("res/img/avatarLeft1.png"),
				new Image("res/img/avatarLeft2.png")};
		Image[] moveRightImg = {new Image("res/img/avatarRight1.png"),
				new Image("res/img/avatarRight2.png")};
		
		// False to only update when the user presses a key
		upAnim = new Animation(moveUpImg, moveDur, false);
		downAnim = new Animation(moveDownImg, moveDur, false);
		leftAnim = new Animation(moveLeftImg, moveDur, false);
		rightAnim = new Animation(moveRightImg, moveDur, false);
		// Start by showing the up animation
		avatar = upAnim;
		direction = "up";
	}
	
	/**
	 * Get the current player animation.
	 * 
	 * @return avatar The animation used as the current
	 */
	public Animation getAnimation() {
		return avatar;
	}
	
	/**
	 * Toggle the up animation.
	 */
	public void setUpAnimation() {
		avatar = upAnim;
		direction = "up";
	}
	
	/**
	 * Toggle the down animation.
	 */
	public void setDownAnimation() {
		avatar = downAnim;
		direction = "down";
	}
	
	/**
	 * Toggle the left animation.
	 */
	public void setLeftAnimation() {
		avatar = leftAnim;
		direction = "left";
	}
	
	/**
	 * Toggle the right animation.
	 */
	public void setRightAnimation() {
		avatar = rightAnim;
		direction = "right";
	}
	
	/**
	 * Get the players iventory for checking for items
	 * and such. Every action should be performed on
	 * the inventory itself and not by second getters
	 * and setters for the Player object.
	 * This method is static to make sure that all
	 * states can access the player inventory.
	 * 
	 * @return inv The inventory.
	 */
	public static Inventory getInventory() {
		return inv;
	}
	
	/**
	 * Getter for player health.
	 * 
	 * @return the player health
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Setter for player health.
	 * 
	 * @param health The new player health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Resurrect the player.
	 */
	public void resurrect() {
		alive = true;
	}

	/**
	 * Kill the player.
	 */
	public void kill() {
		alive = false;
	}
	
	/**
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * @return direction The player direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * @param stamina the stamina to set
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	
	/**
	 * @return true if the player has stamina left
	 */
	public boolean hasStamina() {
		return stamina > 0;
	}
	
	/**
	 * @return true if the player has full stamina (or more)
	 */
	public boolean hasFullStamina() {
		return stamina >= maxStamina;
	}
}
