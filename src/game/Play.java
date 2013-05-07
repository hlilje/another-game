package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.newdawn.slick.*;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;
import org.newdawn.slick.state.*;
import org.newdawn.slick.tiled.*;

/**
 * This is the main PLAY class used for all rendering and
 * logic of the actual PLAY state.
 * 
 * @author Hampus Liljekvist
 * @version 2013-05-04
 */
public class Play extends BasicGameState {
	private static int score;
	private TiledMap map;
	private boolean[][] blocked; // For map collision detection
	private int tileSize; // The tile height in pixels
	private Input input;
	private Random rand;
	private Animation avatar;
	private Player player;
	private Item[] items;
	private NPC[] NPCs;
	private int NPCsLeft, itemsLeft;
	private ArrayList<Projectile> projectiles;
	private Sound projectileSound;
	private float playerHeight, playerWidth;
	private float playerXpos, playerYpos, cameraXpos, cameraYpos;
	private long accumulatedTime, oldTime; // Used for projectiles to avoid spam
	private boolean isGameOver; // To decide if it's game over or victory
	private boolean doDamage; // To decide if the player does damage when firing
	private int damageDelay, damageTimer; // Keep track of hurt cooldowns
	private ParticleSystem ps; // Used for generating particle effects
	private FireEmitter fe;

	/**
	 * Constructor.
	 * 
	 * @param state
	 */
	public Play(int state) {

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
		map = new TiledMap("res/map/map2.tmx");
		rand = new Random();
		// Player position initially 0 for object creation
		playerXpos = 0f;
		playerYpos = 0f;
		 
		/*
		 * Default 'camera' position is 0.
		 * The camera is used together with the player coordinates
		 * and the map coordinates to give the illusion of the
		 * player moving around the world while being centered on
		 * the screen.
		 */
		cameraXpos = 0f;
		cameraYpos = 0f;
		// Create a new Player object
		player = new Player(playerXpos, playerYpos);
		playerHeight = player.getHeight();
		playerWidth = player.getWidth();
		
		/*
		 * Set starting position as the center of the screen, this
		 * is needed together with the avatar animation always being
		 * rendered at this screen location to make sure that
		 * everything else works.
		 * It 'works' thanks to the relation in coordinates between
		 * the actual player, the position of the camera and the
		 * map coordinates.
		 * Unfortunately this means that the starting location is
		 * decided by the camera (screen) size, so playing in a
		 * different resolution will give you a different initial
		 * spawning location.
		 */
		playerXpos = gc.getWidth()/2 - playerWidth;
		playerYpos = gc.getHeight()/2 - playerHeight;
		player.setX(playerXpos);
		player.setY(playerYpos);

		// Get the player animation
		avatar = player.getAnimation();
		// Build a collision map based on the tile properties of the map.
		buildCollisionMap();
		// Initialise and add all the items on the map
		addItems();

		/**
		 * Initialise and add all the NPCs on the map, they
		 * will be given random starting positions in this
		 * helper method.
		 */
		addNPCs();
		// Create array to hold projectiles
		projectiles = new ArrayList<Projectile>();
		// Get the input for every method to use
		input = gc.getInput();
		// Keep track of damage cooldown
		damageDelay = 500;
		damageTimer = damageDelay;
		doDamage = true;
		// Start the game as not over
		isGameOver = false;
		
		try {
			projectileSound = new Sound("res/sound/SpeechMisrecognition.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		score = 0; // Start with 0 score
		ps = new ParticleSystem(new Image("res/img/door.png"));
		fe = new FireEmitter(100, 100);
		fe.setEnabled(true);
		ps.addEmitter(fe);
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
		// Get actual player coordinates as floats
		playerXpos = player.getX();
		playerYpos = player.getY();

		/*
		 * Used to render the map at the correct screen position.
		 * This works since the 'camera' is basically the inverted
		 * (negated) coordinates of where the map is being rendered.
		 * When we the player moves right, the camera and player X
		 * value is increased.
		 * The coordinates of where to draw the map will thus be
		 * decreased (probably negative), and the map will move to the
		 * left, giving the illusion of the player moving to the right.
		 * The actual player object is moving around the game screen,
		 * while the animation is always being drawn at the center.
		 */
		float mapXpos = - cameraXpos;
		float mapYpos = - cameraYpos;
		// Render the map at (int) location (0,0) initially
		map.render((int) mapXpos, (int) mapYpos);

		// TODO Optimisation of map rendering?
		//		int startTileX = ((int) - mapXpos + tileSize*2)/tileSize;
		//		int startTileY = ((int) - mapYpos + tileSize*2)/tileSize;
		//		int sectionTileWidth = (gc.getWidth() + tileSize*2)/tileSize;
		//		int sectionTileHeight = (gc.getHeight() + tileSize*2)/tileSize;
		//		
		//		map.render((int) mapXpos, (int) mapYpos, startTileX,
		//				startTileY, sectionTileWidth, sectionTileHeight);

		// Draw the player health to the screen after the map
		g.drawString("HP: " + player.getHealth(), 100f, 10f);
		g.drawString("NPCs left: " + NPCsLeft, 200f, 10f); // Draw the NPCs left
		g.drawString("Items left: " + itemsLeft, 350f, 10f); // Draw the items left
		g.drawString("Score: " + score, 500f, 10f); // Draw the current score

//		g.draw(player); // DEBUG Draw the actual 'ghost player'
		// Always render the player animation centered on the screen
		avatar.draw(gc.getWidth() / 2 - playerWidth,
				gc.getHeight() / 2 - playerHeight);
		// Render all the items
		renderItems(g);
		// Render all the NPCs
		renderNPCs(g);
		// Render all the projectiles
		renderProjectiles(g);
		// Render the ParticleSystem
		ps.render(mapXpos + (map.getWidth()*tileSize)/2,
				mapYpos + (map.getHeight()*tileSize)/2);
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
		// Start by checking if it's game over
		if(!isGameOver) {
			if(victoryCondition()) { // Check if the game is won
				sbg.enterState(Game.VICTORY);
			}
			if(player.isAlive()) {
				float deltaMod = 0.35f; // Set speed
				// Get player coordinates as floats
				playerXpos = player.getX();
				playerYpos = player.getY();

				if(input.isKeyDown(Input.KEY_W)) {
					player.setUpAnimation();
					avatar = player.getAnimation();
					float newPlayerYpos = playerYpos - delta * deltaMod;

					// Check the NEXT position is blocked before updating it
					if(!tileIsBlocked(playerXpos, newPlayerYpos)
							&& checkDoorAtTile(playerXpos, newPlayerYpos, 0)) {
						avatar.update(delta);
						playerYpos -= delta * deltaMod;
						cameraYpos -= delta * deltaMod;
						player.setY(playerYpos);
					}
				}
				if(input.isKeyDown(Input.KEY_S)) {
					player.setDownAnimation();
					avatar = player.getAnimation();
					float newPlayerYpos = playerYpos + playerHeight + delta * deltaMod;

					// Compensate for coordinate index in top right corner
					if(!tileIsBlocked(playerXpos, newPlayerYpos)
							&& checkDoorAtTile(playerXpos, newPlayerYpos, 0)) {
						avatar.update(delta);
						playerYpos += delta * deltaMod;
						cameraYpos += delta * deltaMod;
						player.setY(playerYpos);
					}
				}
				if(input.isKeyDown(Input.KEY_A)) {
					player.setLeftAnimation();
					avatar = player.getAnimation();
					float newPlayerXpos = playerXpos - delta*deltaMod;

					if(!tileIsBlocked(newPlayerXpos, playerYpos)
							&& checkDoorAtTile(newPlayerXpos, playerYpos, 0)) {
						avatar.update(delta);
						playerXpos -= delta * deltaMod;
						cameraXpos -= delta * deltaMod;
						player.setX(playerXpos);
					}
				}
				if(input.isKeyDown(Input.KEY_D)) {
					player.setRightAnimation();
					avatar = player.getAnimation();
					float newPlayerXpos = playerXpos + playerHeight + delta*deltaMod; 

					// Compensate for coordinate index in top right corner
					if(!tileIsBlocked(newPlayerXpos, playerYpos)
							&& checkDoorAtTile(newPlayerXpos, playerYpos, 0)) {
						avatar.update(delta);
						playerXpos += delta * deltaMod;
						cameraXpos += delta * deltaMod;
						player.setX(playerXpos);
					}
				}
				if(input.isKeyDown(Input.KEY_J)) {
					// To avoid projectile spamming
					long currentTime = System.currentTimeMillis();
					// Will be set to currentTime initially
					long passedTime = currentTime - oldTime;
					// Will be set to currentTime initially and make if-statement true
					accumulatedTime += passedTime;

					if(accumulatedTime >= 150L) {
						String playerDirection = player.getDirection();
						projectiles.add(new Projectile(playerXpos, playerYpos,
								3, playerDirection, 100));
						accumulatedTime = 0; // Reset the accumulated time
					}
					oldTime = currentTime;
					projectileSound.play(0.5f, 0.4f); // Pitch, volume
				}
				if(input.isKeyPressed(Input.KEY_ESCAPE)) { // Single click
					input.clearKeyPressedRecord();
					sbg.enterState(Game.MENU);
				}
				if(input.isKeyPressed(Input.KEY_I)) { // Single click
					input.clearKeyPressedRecord();
					sbg.enterState(Game.INVENTORYSCREEN);
				}
				updateNPCs(delta, deltaMod); // Update all the NPCs
				updateProjectiles(delta, deltaMod); // Update all the projectiles
				ps.update(delta); // Update the particle system
			} else {
				// Since the player is dead, the game is over
				isGameOver = true;
			}
		} else {
			sbg.enterState(Game.GAMEOVER); // Since the game was over
		}
	}

	/**
	 * Helper method to decide if the NEXT tile is blocked.
	 * Since this method is called with the next player
	 * position before updating them the player image will
	 * look like it stopped updating a delta distance from
	 * the actual rendered tile.
	 * 
	 * @param x
	 * @param y
	 * @return true if the position is blocked 
	 */
	private boolean tileIsBlocked(float x, float y) {
		/**
		 * Cast to int and divide by the tile size in pixels.
		 * This will cause double rounding errors, when the
		 * coordinates are casted and after the int division.
		 * The dropped decimals will make sure the checked
		 * position can be used as index in the blocked array.
		 * 
		 * To my surprise this works exactly the same after
		 * changing the way coordinates are tracked and drawn
		 * to make the player centered on screen while the
		 * camera is moving. The difference is that the actual
		 * player position or item position will move around
		 * as ghosts on the screen if they are used with g.draw().
		 */
		int xBlock = (int) x / tileSize;
		int yBlock = (int) y / tileSize;
		// DEBUG
		//		System.out.println("Next player pos: (" + x + ", " + y + ")");
		//		System.out.println("Gives tile: (" + xBlock + ", " + yBlock + ")");

		// To avoid index out of bounds if player ventures out of map
		if(((xBlock < 0) || (xBlock > blocked[0].length - 1)) ||
				((yBlock < 0) || (yBlock > blocked.length - 1))) {
			System.err.println("OUT OF BOUNDS");
			return true;
		}
		// DEBUG
		//		System.out.println("Which is blocked: " + blocked[xBlock][yBlock]);
		return blocked[xBlock][yBlock];
	}

	/**
	 * Helper method to add all the items on the map.
	 * 
	 * @throws SlickException
	 */
	private void addItems() throws SlickException {
		int notRand = 3; // Number of items without randomised positions
		int isRand = 50; // Number of items with randomised positions
		items = new Item[notRand + isRand];

		// Add static items with static sizes
		Item item = new Item(170f, 100f, 25f, 25f, "key1");
		item.setImage("res/img/item1.png");
		item.setSound("res/sound/battle002.wav");
		items[0] = item;

		Item item2 = new Item(1000f, 800f, 50f, 50f, "random2");
		item2.setImage("res/img/item1.png");
		item2.setSound("res/sound/battle002.wav");
		items[1] = item2;
		
		Item item3 = new Item(450f, 500f, 50f, 50f, "tag");
		item3.setImage("res/img/item1.png");
		item3.setSound("res/sound/battle002.wav");
		items[2] = item3;
		
		// Generate randomised items with randomised sizes
		for(int i=notRand;i<(notRand+isRand);i++) {
			int randX = tileSize + rand.nextInt(map.getWidth()*tileSize
					- tileSize*2);
			int randY = tileSize + rand.nextInt(map.getHeight()*tileSize
					- tileSize*2);
			// Randomise again if the positions are blocked, give up after 10 tries
			int i2 = 0;
			while(tileIsBlocked(randX, randY) && i2<10) {
//				System.out.println("REDO RAND ITEM" + randX + ", " + randY); // DEBUG
				randX = tileSize + rand.nextInt(map.getWidth()*tileSize
						- tileSize*2);
				randY = tileSize + rand.nextInt(map.getHeight()*tileSize
						- tileSize*2);
			}
			
			int randW = 1 + rand.nextInt(100);
			int randH = 1 + rand.nextInt(100);
			Item randItem = new Item(randX, randY, randW, randH, "generic"+(i-notRand+1));
			items[i] = randItem;
			randItem.setImage("res/img/item1.png");
			randItem.setSound("res/sound/battle002.wav");
		}
		itemsLeft = items.length;
	}

	/**
	 * Helper method to add all the NPCs on the map.
	 * 
	 * @throws SlickException
	 */
	private void addNPCs() throws SlickException {
		NPCs = new NPC[100]; // Choose how many NPCs to generate
		/*
		 * Randomise their starting positions, add some padding
		 * with tileSize to hinder them from spawing out of
		 * bounds. Currently ignores if their spawning positions
		 * are valid or not (may spawn in blocked tiles, etc.).
		 */
		for(int i=0; i<NPCs.length; i++) {
			int randX = tileSize + rand.nextInt(map.getWidth()*tileSize
					- tileSize*2);
			int randY = tileSize + rand.nextInt(map.getHeight()*tileSize
					- tileSize*2);
			// Randomise again if the positions are blocked, give up after 10 tries
			int i2 = 0;
			while(tileIsBlocked(randX, randY) && i2<10) {
//				System.out.println("REDO RAND NPC " + randX + ", " + randY); // DEBUG
				randX = tileSize + rand.nextInt(map.getWidth()*tileSize
						- tileSize*2);
				randY = tileSize + rand.nextInt(map.getHeight()*tileSize
						- tileSize*2);
			}
			
			int randHP = 1 + rand.nextInt(2000);
			int randDMG = 1 + rand.nextInt(100);
			boolean randHostile = true;
			if(rand.nextInt(2) == 1)
				randHostile = false;
			// Create new NPC object, x, y, hp, dmg, hostile
			NPCs[i] = new NPC(randX, randY, randHP, randDMG, randHostile);
		}
		NPCsLeft = NPCs.length;
	}

	/**
	 * Helper method to get the tileId of the current player position.
	 * This method is used by checkDoorAtTile().
	 * 
	 * @param xPos
	 * @param yPos
	 * @param layerIndex
	 * @return the tileId at the current player position
	 */
	private int getTileId(float xPos, float yPos, int layerIndex) {
		int xTile = (int) xPos / tileSize;
		int yTile = (int) yPos / tileSize;
		return map.getTileId(xTile, yTile, layerIndex);
	}

	/**
	 * Method used to check if the tile the player is about to enter is
	 * positioned at is a locked door. If the door is locked and the player
	 * has the right key in the inventory, true is returned. If the door is
	 * locked and the player don't have the key, false is returned.
	 * If the door isn't locked (or if the current tile isn't a door at all),
	 * true is returned. 
	 * 
	 * @param xPos
	 * @param yPos
	 * @param layerIndex The Layer of the map to check the tile at
	 * @return true if the door is unlocked, if the player has the right key
	 * to the locked door, or if the tile isn't even a door at all
	 */
	private boolean checkDoorAtTile(float xPos, float yPos, int layerIndex) {
		int tileID = getTileId(xPos, yPos, layerIndex);
		// False is default value
		String lockedProperty = map.getTileProperty(tileID, "locked", "false");
		String keyProperty = map.getTileProperty(tileID, "key", "null");
		Inventory inv = Player.getInventory();
		
		if(lockedProperty.equals("true")) {
			if(inv.hasItem(keyProperty))
				return true;
		} else if(lockedProperty.equals("false")) {
			return true;
		}
		return false;
	}

	/**
	 * Helper method to build a collision map based on the 'blocked'
	 * value of the background tiles. This collision map is represented
	 * as a boolean matrix with an element for each tile on the map,
	 * where a value of true means that the tile at position [x][y] is
	 * indeed blocked.
	 */
	private void buildCollisionMap() {
		tileSize = map.getTileHeight();
		blocked = new boolean[map.getWidth()][map.getHeight()];

		for(int xAxis = 0; xAxis < map.getHeight(); xAxis++) {
			for(int yAxis = 0; yAxis < map.getWidth(); yAxis++) {
				// 0 is the index of the layer to get the tile from
				int tileID = map.getTileId(xAxis, yAxis, 0);
				// "false" is the default value to return
				String blockedValue = map.getTileProperty(tileID, "blocked", "false");
				if("true".equals(blockedValue)) {
					blocked[xAxis][yAxis] = true;
				}
			}
		}
	}

	/**
	 * Helper method used in update() to update all the NPCs of
	 * the game. Also checks if the NPC attacks the player.
	 * 
	 * @param delta
	 * @param deltaMod
	 */
	private void updateNPCs(int delta, float deltaMod) {
		for(NPC NPC: NPCs) {
			Animation anim = NPC.getAnimation();
			// We set new random positions in render
			anim.update(delta);

			// Only update if the NPC is actually alive
			if(NPC.isAlive()) {
				float xPos = NPC.getX();
				float yPos = NPC.getY();
				// Need two different randoms to avoid a linear relation
				int randNum1 = rand.nextInt(2);
				int randNum2 = rand.nextInt(2);

				/*
				 * Randomly decide if x and y positions should be
				 * increased or decreased for the NPC.
				 */
				if(randNum1 == 0)
					xPos += delta * deltaMod/2;
				else if(randNum1 == 1)
					xPos -= delta * deltaMod/2;
				if(randNum2 == 0)
					yPos += delta * deltaMod/2;
				else if(randNum2 == 1)
					yPos -= delta * deltaMod/2;

				// Pixel index 0!
				if(!(xPos >= map.getWidth()*tileSize || xPos < 0))
					/*
					 * May in some cases not update x due to y being
					 * blocked, but let's ignore this for now.
					 */
					if(!tileIsBlocked(xPos, yPos))
						NPC.setX(xPos);
				if(!(yPos >= map.getHeight()*tileSize || yPos < 0))
					if(!tileIsBlocked(xPos, yPos))
						NPC.setY(yPos);
				
				// Check if the NPC intersects (attacks) the player
				if(player.intersects(NPC) && NPC.isHostile()) {
					damageTimer -= delta;

					// To handle the damage cooldown
					if(doDamage) {
						int NPCDamage = NPC.getDamage();
						int playerHP = player.getHealth();
						int newPlayerHP = playerHP - NPCDamage;
						if(newPlayerHP <= 0)
							player.kill();
						player.setHealth(newPlayerHP);
						doDamage = false;
					}
					if(damageTimer <= 0) {
						damageTimer = damageDelay;
						doDamage = true;
					}
				}
			}
		}
	}

	/**
	 * Helper method used by render() to loop through the array of
	 * items to perform all necessary logic checks to decide which
	 * ones should be rendered.
	 * Unfortunately we have to update the inventory in the render()
	 * method due to the way this is currently implemented.
	 * At least we won't have to loop in update() as well.
	 * 
	 * @param g
	 * @throws SlickException
	 */
	private void renderItems(Graphics g) throws SlickException {
		for(Item item : items) {
			Image itemImage = item.getImage();
			Sound itemSound = item.getSound();
			Inventory inv = Player.getInventory();
			/* 
			 * Item coordinates, we want to shift these so that the item
			 * won't be drawn at it's actual static position, but rather
			 * to move when the map is 'moving'. This is achieved by
			 * subtracting the camera position from the item position.
			 */
			float itemRelativeXpos = item.getX() - cameraXpos;
			float itemRelativeYpos = item.getY() - cameraYpos;
			// Only draw if the item hasn't been picked up (not in inventory)
			if(!inv.hasItem(item.getID())) {
//				g.draw(item); // DEBUG Draw the actual item at its location on the screen
				// Draw the item image
				g.drawImage(itemImage, itemRelativeXpos, itemRelativeYpos);
			}

			/*
			 * This works as normal thanks to the relation between the actual
			 * coordinates of the player/items and their visual location on
			 * the screen.
			 */
			if (player.intersects(item) && !inv.hasItem(item.getID())) {
				inv.addItem(item); // Add the item to inventory
				itemSound.play(0.9f, 0.8f); // Play the sound effect
				itemsLeft -= 1;
				// TODO Remove taken items?
			}
		}
	}

	/**
	 * Helper method used by render() to loop through the array of
	 * NPCs and render them all.
	 */
	private void renderNPCs(Graphics g) {
		for(NPC NPC: NPCs) {
			Animation anim = NPC.getAnimation();
			/**
			 * The animations will be drawn relative to the camera, so
			 * that they will appear as expected on the relative map
			 * coordinates.
			 */
			float relativeXpos = NPC.getX() - cameraXpos;
			float relativeYpos = NPC.getY() - cameraYpos;
			anim.draw(relativeXpos, relativeYpos); // Animation
//			g.draw(NPC); // DEBUG Shape
		}
	}

	/**
	 * Helper method to render all the projectiles.
	 * 
	 * @param g
	 */
	private void renderProjectiles(Graphics g) {
		for(Projectile projectile : projectiles) {
			/*
			 * This needs to be done thanks to the relative camera
			 * position.
			 */
			float currentXpos = projectile.getCenterX();
			float currentYpos = projectile.getCenterY();
			float relativeXpos = currentXpos - cameraXpos;
			float relativeYpos = currentYpos - cameraYpos;
			// Render at the relative positions
			projectile.setCenterX(relativeXpos);
			projectile.setCenterY(relativeYpos);
			g.draw(projectile);
			// Change back to the actual coordinates after rendering
			projectile.setCenterX(currentXpos);
			projectile.setCenterY(currentYpos);
		}
	}

	/**
	 * Helper method to update all the projectiles, also checks if
	 * the projectile has hit an NPC.
	 * 
	 * @param delta
	 * @param deltaMod
	 */
	private void updateProjectiles(int delta, float deltaMod) {
		Iterator<Projectile> it = projectiles.iterator();

		while(it.hasNext()) {
			Projectile projectile = it.next();
			float centerXpos = projectile.getCenterX();
			float centerYpos = projectile.getCenterY();
			String projectileDirection = projectile.getDirection();
			
			if(projectileDirection.equals("up"))
				centerYpos -= delta * deltaMod*2;
			else if(projectileDirection.equals("down"))
				centerYpos += delta * deltaMod*2;
			else if(projectileDirection.equals("left"))
				centerXpos -= delta * deltaMod*2;
			else if(projectileDirection.equals("right"))
				centerXpos += delta * deltaMod*2;
			else
				System.err.println("INVALID DIRECTION");
			// Set new coordinates beforehand
			projectile.setCenterX(centerXpos);
			projectile.setCenterY(centerYpos);
			/*
			 * Loop through the array of NPC to check for hits. This could
			 * be done in some of the NPC update() or render() methods as
			 * well, but since the array of projectiles probably is larger
			 * than the array of NPCs it is done this way instead.
			 * 
			 * The isActive() check is unnecessary since inactive projectiles
			 * will be immediately removed later in this method.
			 */
			for(NPC NPC: NPCs) {
				if(projectile.intersects(NPC) && projectile.isActive()) {
					int NPCHealth = NPC.getHealth();
					int projectileDamage = projectile.getDamage();
					int newNPCHealth = NPCHealth - projectileDamage;

					if(newNPCHealth <= 0 && NPC.isAlive()) {
						NPC.kill();
						Animation NPCAnimation = NPC.getAnimation();
						NPCAnimation.stop();
						NPCsLeft -= 1;
						if(NPC.isHostile()) // Only give points for killing enemies
							score += NPC.getHealth() + NPC.getDamage();
						else // Punish evil people
							score -= NPC.getHealth() + NPC.getDamage();
					}
					NPC.setHealth(newNPCHealth);
					// To make sure the same projectile won't stack its damage
					projectile.setNotActive();
				}
			}
			/*
			 *  Destroy the projectiles if they go out of bounds or collide
			 *  with a blocked tile.
			 */
			if((centerXpos >= map.getWidth()*tileSize || centerXpos < 0)) {
				it.remove();
			} else if((centerYpos >= map.getWidth()*tileSize || centerYpos < 0)) {
				it.remove();
			} else if(tileIsBlocked(centerXpos, centerYpos)) {
				it.remove();
			} else if(!projectile.isActive()) // Also remove inactive projectiles
				it.remove();
		}
	}
	
	/**
	 * This method is used to define the victory condition of
	 * the game. It should be checked on every update to
	 * determine when the game is won.
	 * 
	 * @return true if the victory condition is fulfilled
	 */
	private boolean victoryCondition() {
		return itemsLeft == 0;
	}

	/**
	 * Returns the ID.
	 * 
	 * @return The ID
	 */
	@Override
	public int getID() {
		return Game.PLAY;
	}
	
	/**
	 * @return the score
	 */
	public static int getScore() {
		return score;
	}
}
