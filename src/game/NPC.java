package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

/**
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class NPC extends Polygon {
	// Generated value used for serialisation
	private static final long serialVersionUID = -9137064999281407541L;
	private Animation anim;
	private int health, damage;
	private boolean hostile;
	private boolean alive;
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 * @throws SlickException 
	 */
	public NPC(float x, float y, int health, int damage,
			boolean hostile) throws SlickException {
		addPoints();
		
		super.setX(x);
		super.setY(y);
		
		initAnimations();
		
		this.health = health;
		this.damage = damage;
		this.hostile = hostile;
		
		alive = true;
	}

	/**
	 * Add points to create a square polygon. 
	 */
	private void addPoints() {
		// Assuming the NPC is drawn as a box
		int size = 25;

		addPoint(0, 0);
		addPoint(0, size);
		addPoint(size, size);
		addPoint(size, 0);
	}
	
	/**
	 * Initialise the images and resources.
	 * 
	 * @throws SlickException
	 */
	private void initAnimations() throws SlickException {
		int[] moveDur = {200, 200}; // ms
		
		Image[] animImgs = {new Image("res/img/avatarUp1.png"),
				new Image("res/img/avatarUp2.png")};
		
		// False to only update when the user presses a key
		anim = new Animation(animImgs, moveDur, false);
	}
	
	/**
	 * Get the NPC animation.
	 * 
	 * @return anim The animation to get
	 */
	public Animation getAnimation() {
		return anim;
	}
	
	/**
	 * Getter for health.
	 * 
	 * @return health The NPC health
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Set new health;
	 * 
	 * @param health The health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Getter for damage.
	 * 
	 * @return damage The NPC attack damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Resurrect the NPC.
	 */
	public void resurrect() {
		alive = true;
	}

	/**
	 * Kill the NPC.
	 */
	public void kill() {
		alive = false;
	}
	
	/**
	 * @return true if the NPC is alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * @return true if the NPC is hostile
	 */
	public boolean isHostile() {
		return hostile;
	}
}
