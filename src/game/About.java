package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * This is the information screen.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class About extends BasicGameState {
	/**
	 * Constructor.
	 * 
	 * @param state
	 */
	public About(int state) {

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
		
	}

	/**
	 * Renders the graphics to the screen, this method is
	 * always called after update().
	 * 
	 * @param gc
	 * @param sbg
	 * @param g
	 * @throws SlickException
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.drawString("This is another game made by Hampus Liljekvist,",
				gc.getWidth()/2-250, gc.getHeight()/2-80);
		g.drawString("which happens to be free as in free beer.",
				gc.getWidth()/2-250, gc.getHeight()/2-40);
		g.drawString("WASD to move, J to fire, I for inventory, Esc for menu in-game.",
				gc.getWidth()/2-250, gc.getHeight()/2);
		g.drawString("Grab all items to win, kill enemies for bonus points.",
				gc.getWidth()/2-250, gc.getHeight()/2+40);
		g.drawString("Press Esc to go back", gc.getWidth()/2-250, gc.getHeight()/2+80);
	}

	/**
	 * Updates the game logic, this method is always called
	 * before render().
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
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			input.clearKeyPressedRecord();
			sbg.enterState(Game.MENU);
		}
	}

	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return Game.ABOUT;
	}
}
