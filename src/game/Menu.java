package game;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;

/**
 * This is the main MENU class for starting the game.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-09
 */
public class Menu extends BasicGameState {
	private TextField tfNPCs, tfItems;
	private static int numNPCs, numRandItems;
	private boolean startGame, invalidInput, initPlayState;
	
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
		tfNPCs = new TextField(gc, gc.getDefaultFont(), 100, 100, 100, 30);
		numNPCs = 100;
		tfNPCs.setText("" + numNPCs);
		tfNPCs.setFocus(false);
		tfItems = new TextField(gc, gc.getDefaultFont(), 300, 100, 100, 30);
		numRandItems = 50;
		tfItems.setText("" + numRandItems);
		tfItems.setFocus(false);
		
		startGame = false;
		invalidInput = false;
		// Force the Play state to fetch the init values the user supplied
		initPlayState = true;
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
		tfNPCs.render(gc, g);
		tfItems.render(gc, g);
		g.drawString("Enter the desired amount of", tfNPCs.getX(), tfNPCs.getY()-50);
		g.drawString("NPCs:", tfNPCs.getX(), tfNPCs.getY()-30);
		g.drawString("random items:", tfItems.getX(), tfItems.getY()-30);
		if(invalidInput) {
			g.drawString("<<< INVALID VALUE ENTERED >>>", gc.getWidth()/2-135, gc.getHeight()/2-80);
		}
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
		if(startGame) { // Resets startGame to false if the input is invalid
			try {
				String NPCsText = tfNPCs.getText();
				numNPCs = Integer.parseInt(NPCsText);
			} catch(Exception e) {
				System.err.println("INVALID VALUE OF ENEMIES");
				startGame = false;
				invalidInput = true;
			}
			try {
				String randItemsText = tfItems.getText();
				numRandItems = Integer.parseInt(randItemsText);
			} catch(Exception e) {
				System.err.println("INVALID VALUE OF RANDOM ITEMS");
				startGame = false;
				invalidInput = true;
			}
			if(initPlayState) {
				// Init the Play state again to make it fetch the new values
				// TODO Doesn't work after victory or game over since init is called
				// by that corresponding screen, and thus the values fetched by
				// Play will be the old ones.
				sbg.getState(Game.PLAY).init(gc, sbg);
				initPlayState = false;
			}
		}
		if(startGame) {
			startGame = false;
			sbg.enterState(Game.PLAY, new FadeOutTransition(), new FadeInTransition());
		}
		if(input.isKeyPressed(Input.KEY_S) && !fieldsHasFocus()) {
			input.clearKeyPressedRecord();
			startGame = true; // Start the game in the next frame
			invalidInput = false; // Remove the error message
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
	 * Called when this state is entered.
	 * 
	 * @param gc
	 * @param sbg
	 * @throws SlickException
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		System.out.println("ENTER");
	}

	/**
	 * @return true if enemy field OR item field has focus
	 */
	private boolean fieldsHasFocus() {
		return tfNPCs.hasFocus() || tfItems.hasFocus();
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
