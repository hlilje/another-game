package game;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;

/**
 * This is the main MENU class for starting the game.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-08
 */
public class Menu extends BasicGameState {
	private TextField tfEnemies, tfItems;
	private static int numNPCs, numRandItems;
	private boolean fetchFields;
	
	/**
	 * Constructor.
	 * 
	 * @param state
	 */
	public Menu(int state) {
		
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
		tfEnemies = new TextField(gc, gc.getDefaultFont(), 100, 100, 100, 30);
		numNPCs = 100;
		tfEnemies.setText("" + numNPCs);
		tfEnemies.setFocus(false);
		tfItems = new TextField(gc, gc.getDefaultFont(), 300, 100, 100, 30);
		numRandItems = 50;
		tfItems.setText("" + numRandItems);
		tfItems.setFocus(false);
		
		fetchFields = false;
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
		// Menu options
		g.drawString("Press S to start playing", gc.getWidth()/2-110, gc.getHeight()/2-40);
		g.drawString("Press A for information", gc.getWidth()/2-110, gc.getHeight()/2);
		g.drawString("Press Esc to exit", gc.getWidth()/2-110, gc.getHeight()/2+40);
		// Splash screen
		Image splash = new Image("res/img/splash.png");
		g.drawImage(splash, gc.getWidth()/2-splash.getWidth()/2, gc.getHeight()/2-splash.getHeight()-40);
		// Text fields
		tfEnemies.render(gc, g);
		tfItems.render(gc, g);
		g.drawString("Enter the desired amount of", tfEnemies.getX(), tfEnemies.getY()-50);
		g.drawString("enemies:", tfEnemies.getX(), tfEnemies.getY()-30);
		g.drawString("random items:", tfItems.getX(), tfItems.getY()-30);
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
		if(fetchFields) {
			try {
				String enemiesText = tfEnemies.getText();
				numNPCs = Integer.parseInt(enemiesText);
			} catch(Exception e) {
				System.err.println("INVALID VALUE OF ENEMIES");
			}
			try {
				String randItemsText = tfItems.getText();
				numNPCs = Integer.parseInt(randItemsText);
			} catch(Exception e) {
				System.err.println("INVALID VALUE OF ITEMS");
			}
			fetchFields = false;
		}
		
		if(input.isKeyPressed(Input.KEY_S) && !fieldsHasFocus()) {
			input.clearKeyPressedRecord();
			sbg.enterState(Game.PLAY, new FadeOutTransition(), new FadeInTransition());
		}
		if(input.isKeyPressed(Input.KEY_A) && !fieldsHasFocus()) {
			input.clearKeyPressedRecord();
			sbg.enterState(Game.ABOUT);
		}
		if(input.isKeyPressed(Input.KEY_ESCAPE) && !fieldsHasFocus()) {
			input.clearKeyPressedRecord();
			gc.exit();
		}
	}
	
	/**
	 * @return true if enemy field OR item field has focus
	 */
	private boolean fieldsHasFocus() {
		return tfEnemies.hasFocus() || tfItems.hasFocus();
	}
	
	/**
	 * @return the numNPCs
	 */
	public static int getNumNPCs() {
		return numNPCs;
	}

	/**
	 * @return the numRandItems
	 */
	public static int getNumRandItems() {
		return numRandItems;
	}

	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return Game.MENU;
	}
}
