package game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.*;

/**
 * This is the game over screen shown when the game is lost.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class GameOver extends BasicGameState {
	/**
	 * Constructor.
	 * 
	 * @param state
	 */
	public GameOver(int state) {
		
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
		// Nothing to initialise
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
		g.drawString("SCORE: " + Play.getScore(), gc.getWidth()/2-50, gc.getHeight()/2-100);
		g.drawString("GAME OVER", gc.getWidth()/2-50, gc.getHeight()/2-40);
		g.drawString("Press R to retry", gc.getWidth()/2-80, gc.getHeight()/2);
		g.drawString("Press Esc for menu", gc.getWidth()/2-90, gc.getHeight()/2+40);
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
			sbg.init(gc);
			sbg.enterState(Game.PLAY, new FadeOutTransition(), new FadeInTransition());
		}
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			input.clearKeyPressedRecord();
			sbg.init(gc);
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
		return Game.GAMEOVER;
	}
}
