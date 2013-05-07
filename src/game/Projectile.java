package game;

import org.newdawn.slick.geom.Circle;

/**
 * A class to represent projectiles in the game.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class Projectile extends Circle {
	// Generated value used for serialisation
	private static final long serialVersionUID = -6560910767408869508L;
	String direction;
	private int damage;
	private boolean active; // True if has not hit an NPC

	/**
	 * Constructor.
	 * 
	 * @param centerPointX
	 * @param centerPointY
	 * @param radius
	 * @param direction the direction in which the projectile
	 * is moving
	 */
	public Projectile(float centerPointX, float centerPointY,
			float radius, String direction, int damage) {
		super(centerPointX, centerPointY, radius);
		
		this.direction = direction;
		this.damage = damage;
		
		active = true;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * @return true if the projectile hasn't hit an NPC
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Make the projectile inactive, this should be called
	 * upon impact on an NPC.
	 */
	public void setNotActive() {
		active = false;
	}
}
