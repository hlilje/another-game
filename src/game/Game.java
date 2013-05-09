package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * This is the master game class which manages everything.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-09
 */
public class Game extends StateBasedGame {
	public static final String GAMENAME = "Another Game";
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int GAMEOVER = 2;
	public static final int VICTORY = 3;
	public static final int INVENTORYSCREEN = 4;
	public static final int ABOUT = 5;
	
	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer appGC;
		try {
			appGC = new AppGameContainer(new Game(GAMENAME));
			// Hardcoded display resolution due to the game being
			// dependent on it for relative positioning.
			appGC.setDisplayMode(1024, 768, false);
			appGC.setVSync(true);
			appGC.setSmoothDeltas(true);
			appGC.start();
		} catch(SlickException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param gameName
	 */
	public Game(String gameName) {
		super(gameName);
		// Should be added (init) first since Play uses values from it
		this.addState(new Menu(MENU));
		this.addState(new Play(PLAY));
		this.addState(new GameOver(GAMEOVER));
		this.addState(new Victory(VICTORY));
		this.addState(new InventoryScreen(INVENTORYSCREEN));
		this.addState(new About(ABOUT));
		this.enterState(MENU);
	}

	/**
	 * This method is unnecessary since init() is automatically called
	 * by addState(), and thus it has no functionality.
	 * 
	 * @param container
	 * @throws SlickException
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// init() is automatically called by addState()
	}
}
