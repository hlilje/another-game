package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;

/**
 * This is the victory screen shown when the player has
 * won the game.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-11
 */
public class Victory extends BasicGameState {
	/**
	 * Constructor.
	 * 
	 * @param state
	 */
	public Victory(int state) {

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
		g.drawString("SCORE: " + Play.getScore(), gc.getWidth()/2-60, gc.getHeight()/2-100);
		g.drawString("Victory!", gc.getWidth()/2-60, gc.getHeight()/2-40);
		g.drawString("Press R to play again", gc.getWidth()/2-110, gc.getHeight()/2);
		g.drawString("Press Esc for menu", gc.getWidth()/2-100, gc.getHeight()/2+40);
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
		if(input.isKeyPressed(Input.KEY_R)) {
			input.clearKeyPressedRecord();
			// Force the update before the transition animation to hide the
			// init if you left Play from a victory or game over.
			sbg.getState(Game.PLAY).enter(gc, sbg);
			sbg.enterState(Game.PLAY, new FadeOutTransition(), new FadeInTransition());
		}
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
		return Game.VICTORY;
	}
}
