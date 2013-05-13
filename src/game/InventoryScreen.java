package game;

import java.util.HashMap;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * This is the inventory screen used to show the items
 * currently in the player's inventory. 
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-13
 */
public class InventoryScreen extends BasicGameState {
	private Inventory inv;
	private int imgX, imgY, defaultImgX, defaultImgY;
	
	public InventoryScreen(int state) {

	}

	/**
	 * Initialises.
	 * 
	 * @param gc
	 * @param sbg
	 * @throws SlickException
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		inv = Player.getInventory();
		imgX = gc.getWidth()/4;
		imgY = gc.getHeight()/4;
		defaultImgX = imgX;
		defaultImgY = imgY;
	}
	
	/**
	 * Renders.
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 * @throws SlickException
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.drawString("INVENTORY SCREEN", gc.getWidth()/2-75, gc.getHeight()/3-160);
		g.drawString("Press I to resume", gc.getWidth()/2-80, gc.getHeight()/3-120);
		
		HashMap<String, Item> invMap = inv.getAllItems();
		for(String key : invMap.keySet()) {
			Item item = invMap.get(key);
			Image image = item.getImage();
			g.drawImage(image, imgX, imgY);
			//image.draw(imgX + 10, imgY + 10);
			int incrementX = image.getWidth() + 10;
			int incrementY = image.getHeight() + 10;
			
			// Should be in update method?
			// Check for x overflow
			if(imgX + incrementX > gc.getWidth() - defaultImgX) {
				imgX = defaultImgX;
				// Check for y overflow
				if(imgY + incrementY > gc.getHeight() - defaultImgY) {
					// Do nothing if y overflows, let them stack up
					g.drawString("Inventory Image Overflow", 50f, 50f);
				} else {
					imgY += incrementY;
				}
			} else {
				imgX += incrementX;
			}
		}
		// Reset the default values (counter) after each loop through
		imgX = defaultImgX;
		imgY = defaultImgY;
	}

	/**
	 * Updates.
	 * 
	 * @param gc
	 * @param sbg
	 * @param delta
	 * @throws SlickException
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_I)) {
			input.clearKeyPressedRecord();
			sbg.enterState(Game.PLAY);
		}
	}

	/**
	 * Called upon entering.
	 * 
	 * @param gc
	 * @param sbg
	 * @throws SlickException
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// Since a new Player is created upon resetting the game,
		// make sure you always fetch the static inv upon entering.
		// Otherwise this class will only reference the inv created
		// first.
		inv = Player.getInventory();
	}

	/**
	 * @return the ID
	 */
	@Override
	public int getID() {
		return Game.INVENTORYSCREEN;
	}
}
