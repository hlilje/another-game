package game;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;

/**
 * This is the main MENU class for starting the game.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-06
 */
public class Menu extends BasicGameState {
	private TextField tfEnemies, tfItems;
	
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
		tfEnemies.setFocus(false);
		tfItems = new TextField(gc, gc.getDefaultFont(), 300, 100, 100, 30);
		tfItems.setFocus(false);
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
		g.drawString("items:", tfItems.getX(), tfItems.getY()-30);
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
		String enemiesText = tfEnemies.getText();
		String itemsText = tfItems.getText();
		
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
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return Game.MENU;
	}
}
