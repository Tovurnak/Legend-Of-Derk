/****************************************
 * Name: Game.java					    *
 * Date: Apr 1, 2013					*
 * Author: Derek Jew					*
 * Purpose: Legend of Derk Main Program *
 ****************************************/

// fix collisions involving enemies and walls
// fix monster spawning
// flashing invincible
// Jean spells

package pack;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;	
import java.io.File;
import java.util.ArrayList;

public class Game extends Canvas {

	private int pressCount = 1;
	private long counter = 0;

	public boolean isColliding = false;		   // whether or not the player is colliding

	protected Sprite background;			   // background image

	protected boolean isMad = false;
	protected boolean isInvincible = false;	   // if the player is invincible
	protected long InvincibleTimer = 0;		   // the timer for invincible
	private long InvincibleFrames = 3000;      // time the player is invincible for
	protected long stopKnockBackTimer = 0;    // Keeps track of the time from when you got hit
	protected long knockBackLength = 100;     // How long until player stops flying backwards

	private BufferStrategy strategy;   	       // take advantage of accelerated graphics
	private boolean waitingForKeyPress = true; // true if game is waiting for a key press
	// a key is pressed
	private boolean waitingForGameStart = true;
	private boolean leftPressed = false;  	   // true if left arrow key currently pressed
	private boolean rightPressed = false;      // true if right arrow key currently pressed
	private boolean upPressed = false;  	   // true if up arrow key currently pressed
	private boolean downPressed = false;  	   // true if down arrow key currently pressed
	private boolean stabPressed = false;	   // true if stabbing
	private boolean firePressed = false;       // true if firing
	private boolean playerSwordActive = false; // true of the player's sword is active
	protected String lastDirection = "";		   // last direction that the player was moving

	private byte levelOn = 4;

	private boolean gameRunning = true;
	protected ArrayList<Entity> entities = new ArrayList<Entity>(); 	  // list of entities
	// in game
	protected ArrayList<Entity> removeEntities = new ArrayList<Entity>(); // list of entities
	// to remove this loop
	protected Entity player;  				   // the player
	private Entity wall; 					   // a wall
	protected Entity enemySword;			   // an enemy sword
	private Entity enemyArrow;				   // an enemy arrow
	protected Entity playerSword;			   // a player sword
	private Entity playerArrow;				   // a player arrow
	protected Entity healthBar;	 	           // player's health bar
	private double moveSpeed = 150; 		   // move speed of player (px/s)
	private long enemyLastFire = 0;			   // time last attack enemy used
	private long playerLastFire = 0; 		   // time last attack player used
	private long enemyFiringInterval = 2000;   // interval between enemy shots
	protected long jeanFiringInterval = 500;	   // interval between Jean attacks
	private long playerFiringInterval = 500;   // interval between player shots
	protected int monsterCount = 0; 		   // number of monsters left on screen

	private String message = ""; 			   // message to display while waiting
	// for a key press

	protected boolean gotHit = true;
	private boolean swordRight = false;		  // Which direction the enemy is attacking from
	private boolean swordLeft = false;		  // Which direction the enemy is attacking from 
	private boolean swordUp = false;		  // Which direction the enemy is attacking from
	private boolean swordDown = false;		  // Which direction the enemy is attacking from

	private long animationInterval = 100; // animation stuff
	private long lastAnimationSwitch = 0; // time before last animation swap
	private int enemyFrame = 0;			  // frame enemy is in
	private int frame = 0;				  // frame to draw for animation
	private int initialFrame = 0;		  // something to do with frames
	private Entity wallDestroyable;		  // Destroyable wall
	private Entity wallDestroyable1;	  // Destroyable Wall

	private boolean showMenu = true;
	private boolean showInstructions = false;
	private boolean showCredits = false;
	protected boolean jeanFiring = false;
	private boolean waitingForResumeGame = false;
	private boolean firstFire = true;
	private boolean death = false;
	private boolean victory = false;

	/*****************************************
	 * Construct our game and set it running *
	 *****************************************/
	public Entity getPlayerEntity() {
		return player;
	} // getPlayerEntity

	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("The Legend of Derk");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 800, 600);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// start the game
		gameLoop(); 
	} // constructor

	/*******************************
	 * Start the music of the game *
	 *******************************/
	public static void playMusic() {
		try {
			File file = new File("src/smallGroove.wav");
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch(Exception e) {
			System.out.print(e);
		} // catch
	} // playMusic

	public static void playOther() {
		try {
			File file = new File("src/hurt.wav");
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch(Exception e) {
			System.out.print(e);
		} // catch
	} // playMusic

	/*************
	 * Show Menu *
	 *************/
	private void showMenu() {
		if(waitingForKeyPress && ! waitingForResumeGame) {
			Graphics g = (Graphics2D) strategy.getDrawGraphics();
			if(showMenu) {
				background = (SpriteStore.get()).getSprite("sprite/menu.png");
			} // if
			if(showInstructions) {
				background = (SpriteStore.get()).getSprite("sprite/instructions.png");
			} // if
			if(showCredits) {
				background = (SpriteStore.get()).getSprite("sprite/credits.png");
			} // if
			background.draw(g, 0, 0);
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
		} // if
	} // showMenu

	/***************************************************************************
	 * Name: initEntities													   *
	 * Input: none															   *
	 * Output: none															   *
	 * Purpose: Initialize the starting state of the ship and enemy entities   *
	 *          Each entity will be added to the array of entities in the game *
	 ***************************************************************************/
	private void initEntities() {
		if(levelOn == 1) {
			// create some enemies
			monsterCount = 0;
			for(int i = 0 ; i < 10 ; i++) {
				Entity monster = new MonsterEntity (this, "sprite/blooper.gif",
						(int)(Math.random() * 750),
						(int)(Math.random() * 450));
				entities.add(monster);
				monster.hp = 1;
				monsterCount++;
			} // for
			// create the player and put in center of screen
			player = new PlayerEntity(this, "sprite/hero/down2.png", 370, 500);
			entities.add(player);
			player.hp = 3;

			// put water at the top of the window
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WaterEntity(this, "sprite/water.gif", (i * 32), 0);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 19 ; i++) {
				wall = new WaterEntity(this, "sprite/water.gif", 0, (i * 32));
				entities.add(wall);
			} // for

			// add rocks
			for(int i = 0 ; i < 22 ; i++) {
				if(i > 10 && i < 13){
					wallDestroyable = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560)); // 406 - 490
					entities.add(wallDestroyable);
					i++;
					wallDestroyable1 = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wallDestroyable1);
				} else {
					wall = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wall);
				}
			} // for
			knockBack("");
			for(int i = 0 ; i < 16 ; i++) {
				wall = new WallEntity(this, "sprite/rock.gif", 760, 32 + (i * 33));
				entities.add(wall);
			} // for 
		} else if(levelOn == 2) {			
			// create some enemies
			monsterCount = 0;
			for(int i = 0 ; i < 4 ; i++) {
				Entity monster = new Monster2Entity (this, "sprite/Sword Man/sworddude3.gif",
						(int)(Math.random() * 730),
						(int)(Math.random() * 230));
				monster.hp = 2;
				entities.add(monster);
				monsterCount++;
			} // for
			for(int i = 0 ; i < 4 ; i++) {
				Entity monster = new Monster2Entity (this, "sprite/Sword Man/sworddude3.gif",
						(int)(Math.random() * 750),
						(int)(Math.random() * 130 + 448));
				monster.hp = 2;
				entities.add(monster);
				monsterCount++;
			} // for
			// create the player and put in center of screen
			player = new PlayerEntity(this, "sprite/hero/down2.png", 370, 500);
			entities.add(player);
			player.hp = 3;

			// put water
			for(int i = 0 ; i < 3 ; i++) {
				for(int j = 0 ; j < 11 ; j++) {
					wall = new WaterEntity(this, "sprite/blankk.gif", (j * 32), (i * 32));
					entities.add(wall);
				} // for
			} // for
			for(int i = 0 ; i < 3 ; i++) {
				for(int j = 0 ; j < 17 ; j++) {
					wall = new WaterEntity(this, "sprite/blankk.gif", (j * 32), 96 + (i * 32));
					entities.add(wall);
				} // for
				for(int j = 0 ; j < 5 ; j++) {
					wall = new WaterEntity(this, "sprite/blankk.gif", 640 + (j * 32), 96 + (i * 32));
					entities.add(wall);
				} // for
			} // for
			for(int i = 0 ; i < 4 ; i++) {
				wall = new WaterEntity(this, "sprite/blankk.gif", (i * 32), 192);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 3 ; i++) {
				wall = new WaterEntity(this, "sprite/blankk.gif", (i * 32), 224);
				entities.add(wall);
			} // for 
			wall = new WaterEntity(this, "sprite/blankk.gif", 0, 256);
			entities.add(wall);

			// add rocks
			wall = new WallEntity(this, "sprite/rock.gif", 0, (560));
			entities.add(wall);
			for(int i = 0 ; i < 22 ; i++) {
				if(i > 10 && i < 13){
					wallDestroyable = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560)); // 406 - 490
					entities.add(wallDestroyable);
					i++;
					wallDestroyable1 = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wallDestroyable1);
				} else {
					wall = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wall);
				}
			} // for
			knockBack("");
			for(int i = 0 ; i < 16 ; i++) {
				wall = new WallEntity(this, "sprite/rock.gif", 760, 32 + (i * 33));
				entities.add(wall);
			} // for 
		} else if(levelOn == 3) {
			// create some enemies
			monsterCount = 0;
			for(int i = 0 ; i < 3 ; i++) {
				Entity monster = new Monster3Entity (this, "sprite/goku.gif",
						(int)(Math.random() * 750),
						(int)(Math.random() * 450));
				monster.hp = 3;
				entities.add(monster);
				monsterCount++;
			} // for
			// create the player and put in center of screen
			player = new PlayerEntity(this, "sprite/player/.png", 370, 500);
			entities.add(player);
			player.hp = 3;

			// put water at the top of the window
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/water.gif", (i * 32), 0);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 19 ; i++) {
				wall = new WallEntity(this, "sprite/water.gif", 0, (i * 32));
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/water.gif", (i * 32), 0);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 19 ; i++) {
				wall = new WallEntity(this, "sprite/water.gif", 0, (i * 32));
				entities.add(wall);
			} // for 

			// add rocks
			for(int i = 0 ; i < 22 ; i++) {
				if(i > 10 && i < 13){
					wallDestroyable = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560)); // 406 - 490
					entities.add(wallDestroyable);
					i++;
					wallDestroyable1 = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wallDestroyable1);
				} else {
					wall = new WallEntity(this, "sprite/rock.gif", 32 + (i * 34), (560));
					entities.add(wall);
				}
			} // for
			knockBack("");
			for(int i = 0 ; i < 16 ; i++) {
				wall = new WallEntity(this, "sprite/rock.gif", 760, 32 + (i * 33));
				entities.add(wall);
			} // for 
		} else if(levelOn == 4) {
			// create some enemies
			monsterCount = 0;
			Entity monster = new JeanEntity (this, "sprite/jean1.gif", 310, 170);
			monster.hp = 20;
			entities.add(monster);
			monsterCount++;
			// create the player and put in center of screen
			player = new PlayerEntity(this, "sprite/hero/down2.png", 370, 500);
			entities.add(player);
			player.hp = 3;

			// put water at the top of the window
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", (i * 32), 0);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", (i * 32), 32);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 15 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", 0, (i * 32) + 64);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 15 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", 32, (i * 32) + 64);
				entities.add(wall);
			} // for

			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", (i * 32), 544);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 25 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", (i * 32), 576);
				entities.add(wall);
			} // for
			for(int i = 0 ; i < 15 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", 736, 64 + (i * 32));
				entities.add(wall);
			} // for 
			for(int i = 0 ; i < 15 ; i++) {
				wall = new WallEntity(this, "sprite/blankk.gif", 768, 64 + (i * 32));
				entities.add(wall);
			} // for 
		} // else if
	} // initEntities

	/********************************************************
	 * Remove an entity from the game. It will no longer be *
	 *     moved or drawn.									*
	 ********************************************************/
	public void removeEntity(Entity entity) {
		removeEntities.add(entity);
	} // removeEntity

	/*****************************************
	 * Notification that the player has died *
	 *****************************************/
	public void notifyDeath() {
		Graphics g = (Graphics2D) strategy.getDrawGraphics();
		String temp = "Good job. Honry died. Press the spacebar to return to the menu or press 1 to continue";
		g.setColor(Color.white);
		g.drawString(temp, (800 - (g.getFontMetrics().stringWidth(temp))) / 2, 250);
		// clear graphics and flip buffer
		g.dispose();
		strategy.show();
		death = true;
		waitingForResumeGame = true;
		waitingForKeyPress = true;
		isMad = false;
		jeanFiringInterval = 500;
		counter = 0;
	} // notifyDeath


	/*******************************************************
	 * Notification that the player has killed all enemies *
	 *******************************************************/
	public void notifyWin() {
		removeEntities.add(wallDestroyable);
		removeEntities.add(wallDestroyable1);
		Entity entity = new BeatLevelEntity(this, "sprite/blank.gif", 406, 595);
		entities.add(entity);
		if(levelOn == 4) {
			entities.clear();
			Graphics g = (Graphics2D) strategy.getDrawGraphics();
			String temp = "You beat the final boss! Press any key to continue";
			g.setColor(Color.white);
			g.drawString(temp, (800 - (g.getFontMetrics().stringWidth(temp))) / 2, 250);
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			leftPressed = false;
			upPressed = false;
			downPressed = false;
			rightPressed = false;
			stabPressed = false;
			firePressed = false;
			waitingForKeyPress = true;
			waitingForResumeGame = true;
			victory = true;
			pressCount = 1;
			isMad = false;
			jeanFiringInterval = 500;
			counter = 0;
		} // if
	} // notifyWin

	/***************************************************
	 * Notification that the player has beat the level *
	 ***************************************************/
	public void notifyVictory() {
		Graphics g = (Graphics2D) strategy.getDrawGraphics();
		String temp = "Press 1 to continue to the next level";
		g.setColor(Color.white);
		g.drawString(temp, (800 - (g.getFontMetrics().stringWidth(temp))) / 2, 250);
		// clear graphics and flip buffer
		g.dispose();
		strategy.show();
		waitingForResumeGame = true;
		waitingForKeyPress = true;
		entities.clear();
		levelOn++;
	} // notifyVictory

	/**********************************************
	 * Notification than an enemy has been killed *
	 **********************************************/
	public void notifyMonsterKilled() {
		monsterCount--;
		if(monsterCount == 0) {
			notifyWin();
		} // if
	} // notifyMonsterKilled

	/************************
	 * Invincibility Frames *
	 ************************/
	public void resetInvincible() {
		removeEntities.add(healthBar);
		switch(player.hp) {
		case 3:
			healthBar = new WallEntity(this, "sprite/3health.gif", 0, 0);
			entities.add(healthBar);
			break;
		case 2: 
			healthBar = new WallEntity(this, "sprite/2health.gif", 0, 0);
			entities.add(healthBar);
			break;
		case 1: 
			healthBar = new WallEntity(this, "sprite/1health.gif", 0, 0);
			entities.add(healthBar);
			break;
		case 0:
			removeEntities.add(healthBar);	
			break;
		default: 
			removeEntities.add(healthBar);
			break;
		} // switch
		if (player.hp == 0){
			notifyDeath();
		} // if
		if ((System.currentTimeMillis() - InvincibleTimer) >= InvincibleFrames){
			isInvincible = false;
		} // if
		return;
	} // resetInvincible

	/*************
	 * Knockback *
	 *************/
	public void knockBack(String s) {
		//	player.setHorizontalMovement(0);
		//	player.setVerticalMovement(0);
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		if(s == "sword") {
			if(swordRight) {
				gotHit = true;
				moveSpeed = 500;
				player.setHorizontalMovement(moveSpeed);
				rightPressed = true;
			} // if
			if(swordLeft) {
				gotHit = true;
				moveSpeed = 500;
				player.setHorizontalMovement(-moveSpeed);
				leftPressed = true;
			} // if
			if(swordUp) {
				gotHit = true;
				moveSpeed = 500;
				player.setVerticalMovement(-moveSpeed);
				upPressed = true;
			} // if
			if(swordDown) {
				gotHit = true;
				moveSpeed = 500;
				player.setVerticalMovement(moveSpeed);
				downPressed = true;
			} // if
		} else if(s == "enemy") {
			if(lastDirection == "right") {
				gotHit = true;
				moveSpeed = 500;
				player.setHorizontalMovement(-moveSpeed);
				leftPressed = true;
			} else if(lastDirection == "left") {
				gotHit = true;
				moveSpeed = 500;
				player.setHorizontalMovement(moveSpeed);
				rightPressed = true;
			} else if(lastDirection == "down") {
				gotHit = true;
				moveSpeed = 500;
				player.setVerticalMovement(-moveSpeed);
				upPressed = true;
			} else if(lastDirection == "up") {
				gotHit = true;
				moveSpeed = 500;
				player.setVerticalMovement(moveSpeed);
				downPressed = true;
			} // else if
		} else if(s == "arrow") {
			if(Math.abs(enemyArrow.dx) > Math.abs(enemyArrow.dy)) {
				if(enemyArrow.dx > 0) {
					gotHit = true;
					moveSpeed = 500;
					player.setHorizontalMovement(moveSpeed);
					rightPressed = true;
				} else if(enemyArrow.dx < 0) {
					gotHit = true;
					moveSpeed = 500;
					player.setHorizontalMovement(-moveSpeed);
					leftPressed = true;
				} // else if
			} else if(Math.abs(enemyArrow.dx) < Math.abs(enemyArrow.dy)) {
				if(enemyArrow.dy > 0) {
					gotHit = true;
					moveSpeed = 500;
					player.setVerticalMovement(moveSpeed);
					downPressed = true;
				} else if(enemyArrow.dy < 0) {
					gotHit = true;
					moveSpeed = 500;
					player.setVerticalMovement(-moveSpeed);
					upPressed = true;
				} // else if
			} // else if
		} // else if
		swordRight = false;
		swordDown = false;
		swordUp = false;
		swordLeft = false;
	} // knockBack

	/*******************
	 * Attempt to stab *
	 *******************/
	public void enemyTryToStab(Monster2Entity me, Graphics g) { // i is the index of the enemy that is attempting the stab
		// check that we've waiting long enough to attack
		if ((System.currentTimeMillis() - enemyLastFire) < enemyFiringInterval) {
			return;
		} // if

		// otherwise, add a shot to entities
		enemyLastFire = System.currentTimeMillis();
		if((Math.abs(me.x - player.x)) > (Math.abs(me.y - player.y))) {
			if(me.x < player.x) {
				swordRight = true; swordUp = false; swordDown = false; swordLeft = false;
				EnemySwordEntity sword = new EnemySwordEntity(this, "sprite/Sword Man/enemysword2.gif", 
						(int)me.getX() + 13, (int)me.getY() + 13);
				enemySword = sword;
				me.swordActive = true;
				entities.add(sword);
				me.draw(g, 1);
			} else if(me.x > player.x) {
				swordRight = false; swordUp = false; swordDown = false; swordLeft = true;
				EnemySwordEntity sword = new EnemySwordEntity(this, "sprite/Sword Man/enemysword4.gif", 
						(int)me.getX() - 28, (int)me.getY() + 15);
				enemySword = sword;
				me.swordActive = true;
				entities.add(sword);
				me.draw(g, 3);
			} // else if
		} else if((Math.abs(me.x - player.x)) < (Math.abs(me.y - player.y))) {
			if(me.y < player.y) {
				swordRight = false; swordUp = false; swordDown = true; swordLeft = false;
				EnemySwordEntity sword = new EnemySwordEntity(this, "sprite/Sword Man/enemysword3.gif", 
						(int)me.getX() + 2, (int)me.getY() + 11);
				enemySword = sword;
				me.swordActive = true;	
				entities.add(sword);
				me.draw(g, 2);
			} else if(me.y > player.y) {
				swordRight = false; swordUp = true; swordDown = false; swordLeft = false;
				EnemySwordEntity sword = new EnemySwordEntity(this, "sprite/Sword Man/enemysword1.gif", 
						(int)me.getX() + 15, (int)me.getY() - 27);
				enemySword = sword;
				me.swordActive = true;
				entities.add(sword);
				me.draw(g, 0);
			} // else if
		} // else if
	} // enemyTryToStab

	public void playerTryToStab() {
		// check that we've waiting long enough to attack
		if ((System.currentTimeMillis() - playerLastFire) < playerFiringInterval) {
			return;
		} // if

		// otherwise, add a shot to entities
		playerLastFire = System.currentTimeMillis();
		if(lastDirection == "right") {
			SwordEntity sword = new SwordEntity(this, "sprite/sword2.png", 
					(int)player.getX() + 20, (int)player.getY() + 10);
			playerSword = sword;
			playerSwordActive = true;
			entities.add(sword);
		} else if(lastDirection == "left") {
			SwordEntity sword = new SwordEntity(this, "sprite/sword4.png", 
					(int)player.getX() - 28, (int)player.getY() + 10);
			playerSword = sword;
			playerSwordActive = true; 
			entities.add(sword);
		} else if(lastDirection == "down") {
			SwordEntity sword = new SwordEntity(this, "sprite/sword3.png", 
					(int)player.getX() + -4, (int)player.getY() + 40);
			playerSword = sword;
			playerSwordActive = true;
			entities.add(sword);
		} else if(lastDirection == "up") {
			SwordEntity sword = new SwordEntity(this, "sprite/sword.png", 
					(int)player.getX() + 3, (int)player.getY() - 27);
			playerSword = sword;
			playerSwordActive = true;
			entities.add(sword);
		} // else if
	} // playerTryToStab

	/*************************
	 * Attempt to fire arrow *
	 *************************/
	public void enemyTryToFire(Monster3Entity me) {
		// check that we've waiting long enough to attack
		if ((System.currentTimeMillis() - enemyLastFire) < enemyFiringInterval) {
			return;
		} // if

		// otherwise, add a shot to entities
		enemyLastFire = System.currentTimeMillis();
		if((Math.abs(me.x - player.x)) > (Math.abs(me.y - player.y))) { 
			if(me.x < player.x) { // player to the right
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 20, (int)me.getY() + 10);
				double hypot = Math.hypot((me.x - player.x), (me.y - player.y));
				double ratio = -75 / hypot;
				Arrow.dx = (me.x - player.x) * ratio;
				Arrow.dy = (me.y - player.y) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} else if(me.x > player.x) { // player to the left
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() - 14, (int)me.getY() + 10);
				double hypot = Math.hypot((me.x - player.x), (me.y - player.y));
				double ratio = -75 / hypot;
				Arrow.dx = (me.x - player.x) * ratio;
				Arrow.dy = (me.y - player.y) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} // else if
		} else if((Math.abs(me.x - player.x)) < (Math.abs(me.y - player.y))) {
			if(me.y < player.y) { // player below
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 2, (int)me.getY() + 24);
				double hypot = Math.hypot((me.x - player.x), (me.y - player.y));
				double ratio = -75 / hypot;
				Arrow.dx = (me.x - player.x) * ratio;
				Arrow.dy = (me.y - player.y) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} else if(me.y > player.y) { // player above
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 3, (int)me.getY() - 10);
				double hypot = Math.hypot((me.x - player.x), (me.y - player.y));
				double ratio = -75 / hypot;
				Arrow.dx = (me.x - player.x) * ratio;
				Arrow.dy = (me.y - player.y) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} // else if
		} // else if
	} // enemyTryToFire

	public void enemyTryToFire(JeanEntity me) { 
		// check that we've waiting long enough to attack
		if ((System.currentTimeMillis() - enemyLastFire) < jeanFiringInterval) {
			return;
		} // if

		// otherwise, add a shot to entities
		enemyLastFire = System.currentTimeMillis();
		if(player.y > 170 && player.y < 472) { // left or right
			if(me.x < player.x) { // player to the right
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 150, (int)me.getY() + 250);
				double hypot = Math.hypot((player.x - (me.x + 150)), ((me.y + 250) - player.y));
				double ratio = 100 / hypot;
				Arrow.dx = (player.x - (me.x + 150)) * ratio;
				Arrow.dy = (player.y - (me.y + 250)) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} else if(me.x > player.x) { // player to the left
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 150, (int)me.getY() + 250);
				double hypot = Math.hypot((player.x - (me.x + 150)), ((me.y + 250) - player.y));
				double ratio = 100 / hypot;
				Arrow.dx = (player.x - (me.x + 150)) * ratio;
				Arrow.dy = (player.y - (me.y + 250)) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} // else if
		} else {
			if(me.y < player.y) { // player below
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 150, (int)me.getY() + 250);
				double hypot = Math.hypot((player.x - (me.x + 150)), ((me.y + 250) - player.y));
				double ratio = 100 / hypot;
				Arrow.dx = (player.x - (me.x + 150)) * ratio;
				Arrow.dy = (player.y - (me.y + 250)) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} else if(me.y > player.y) { // player above
				EnemyArrowEntity Arrow = new EnemyArrowEntity(this, "sprite/enemyArrow.gif", 
						(int)me.getX() + 150, (int)me.getY() + 250);
				double hypot = Math.hypot((player.x - (me.x + 150)), ((me.y + 250) - player.y));
				double ratio = 100 / hypot;
				Arrow.dx = (player.x - (me.x + 150)) * ratio;
				Arrow.dy = (player.y - (me.y + 250)) * ratio;
				enemyArrow = Arrow;
				entities.add(Arrow);
			} // else if
		} // else if
	} // enemyTryToFire

	public void playerTryToFire() {
		// check that we've waiting long enough to attack
		if ((System.currentTimeMillis() - playerLastFire) < playerFiringInterval) {
			return;
		} // if

		// otherwise, add a shot to entities
		playerLastFire = System.currentTimeMillis();
		if(lastDirection == "right") {
			ArrowEntity Arrow = new ArrowEntity(this, "sprite/arrow2.gif", 
					(int)player.getX() + 15, (int)player.getY() + 10);
			Arrow.dx = 300;
			Arrow.dy = 0;
			playerArrow = Arrow;
			entities.add(Arrow);
		} else if(lastDirection == "left") {
			ArrowEntity Arrow = new ArrowEntity(this, "sprite/arrow4.gif", 
					(int)player.getX() - 15, (int)player.getY() + 10);
			Arrow.dx = -300;
			Arrow.dy = 0;
			playerArrow = Arrow;
			entities.add(Arrow);
		} else if(lastDirection == "down") {
			ArrowEntity Arrow = new ArrowEntity(this, "sprite/arrow3.gif", 
					(int)player.getX() + 4, (int)player.getY() + 32);
			Arrow.dx = 0;
			Arrow.dy = 300;
			playerArrow = Arrow;
			entities.add(Arrow);
		} else if(lastDirection == "up") {
			ArrowEntity Arrow = new ArrowEntity(this, "sprite/arrow.gif", 
					(int)player.getX() + 3, (int)player.getY() - 21);
			Arrow.dx = 0;
			Arrow.dy = -300;
			playerArrow = Arrow;
			entities.add(Arrow);
		} // else if
	} // playerTryToFire

	/*****************************************************************
	 * Name: gameLoop												 *													
	 * Input: none													 *
	 * Output: none													 *
	 * Purpose: Main game loop. Runs throughout game play			 *
	 *          Responsible for the following activities:			 *
	 *           - calculates speed of the game loop to update moves *
	 *           - moves the game entities							 *
	 *           - draws the screen contents (entities, text)		 *
	 *           - updates game events								 *
	 *           - checks input										 *
	 *****************************************************************/
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();

		// keep loop running until game ends
		while (gameRunning) {
			// calculate time since last update, will be used to calculate
			// entities movement
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// get graphics context for the accelerated surface and make it black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			if(waitingForGameStart) {
				background = (SpriteStore.get()).getSprite("sprite/menu.png");
			} else if((levelOn == 1 || levelOn == 3)) {
				background = (SpriteStore.get()).getSprite("sprite/background.png");
			} else if(levelOn == 2) {
				background = (SpriteStore.get()).getSprite("sprite/background2.png");
			} else if (levelOn == 4){
				if(isMad) {
					counter++;
					if(counter < 2) {
						for(int i = 0 ; i < entities.size() ; ) {
							Entity entity = entities.get(i);
							if(entity instanceof WallEntity) {
								entities.remove(entity);
							} else {
								i++;
							} // else
						} // for
						for(int i = 0 ; i < 25 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", (i * 32), 0);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 25 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", (i * 32), 32);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 15 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", 0, (i * 32) + 64);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 15 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", 32, (i * 32) + 64);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 25 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", (i * 32), 544);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 25 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", (i * 32), 576);
							entities.add(wall);
						} // for
						for(int i = 0 ; i < 15 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", 736, 64 + (i * 32));
							entities.add(wall);
						} // for 
						for(int i = 0 ; i < 15 ; i++) {
							wall = new LavaEntity(this, "sprite/blankk.gif", 768, 64 + (i * 32));
							entities.add(wall);
						} // for 
						background = (SpriteStore.get()).getSprite("sprite/madJeanMap.png");
					} // if
				} else {
					background = (SpriteStore.get()).getSprite("sprite/jeanMap.png");
				} // else
			} // else

			background.draw(g, 0, 0);

			if(showMenu || showInstructions || showCredits) {
				showMenu();
			} // if

			if(entities.size() != 0) {

				// move each entity
				if(! waitingForKeyPress) {
					for(int i = 0 ; i < entities.size() ; i++) {
						Entity entity = (Entity)entities.get(i);
						if(i < monsterCount) {
							if(entity.swordActive == true) {
								if(System.currentTimeMillis() - enemyLastFire >= 250) {
									removeEntities.add(enemySword);
									entity.swordActive = false;
								} // if
								continue;
							} // if
						} // if
						entity.move(delta, g);
					} // for
				} // if

				if(! waitingForGameStart) {
					// draw all entities
					for(int i = 0 ; i < entities.size() ; i++) {
						Entity entity = (Entity)entities.get(i);
						entity.draw(g);
					} // for
				} // if

				// find enemies close to the player
				for(int i = 0 ; i < monsterCount ; i++) {
					for(int j = i + 1 ; j < entities.size() ; j++) {
						Entity me = entities.get(i);
						Entity him = entities.get(j);	
						if(me instanceof JeanEntity) {
							me.aiAttack(him, g);
						} else {
							if(Math.hypot(me.x - him.x, me.y - him.y) < 100) {
								me.aiAttack(him, g);
							} // if
						} // else
					} // for
				} // for

				/*************************************************
				 * Brute force collisions, compare every entity  *
				 * against every other entity. If any collisions *
				 * are detected notify both entities that it has *
				 * occurred										 *
				 *************************************************/
				for(int i = 0 ; i < entities.size(); i++) {
					for(int j = i + 1 ; j < entities.size() ; j++) {
						Entity me = entities.get(i);
						Entity him = entities.get(j);
						if(me.collidesWith(him)) {
							me.collidedWith(him);
							him.collidedWith(me);
						} // if
					} // for
				} // for

				if(System.currentTimeMillis() - playerLastFire >= 250) {
					removeEntities.add(playerSword);
					playerSwordActive = false;
				} // if

				// remove dead entities
				entities.removeAll(removeEntities);
				removeEntities.clear();

				if(! waitingForResumeGame) {
					// clear graphics and flip buffer
					g.dispose();
					strategy.show();
				} // if

				// player should not move without user input
				player.setHorizontalMovement(0);
				player.setVerticalMovement(0);

				// respond to user moving player
				if(isColliding == false && playerSwordActive == false) {
					if((leftPressed)&&(!rightPressed) && (!upPressed) && (!downPressed)){
						player.setHorizontalMovement(-moveSpeed);
						lastDirection = "left";
					} else if ((rightPressed) && (!leftPressed) && (!downPressed) && (!upPressed)){
						player.setHorizontalMovement(moveSpeed);
						lastDirection = "right";
					} else if ((upPressed) && (!downPressed) && (!leftPressed) && (!rightPressed)){
						player.setVerticalMovement(-moveSpeed);
						lastDirection = "up";
					} else if ((downPressed) && (!upPressed) && (!leftPressed) && (!rightPressed)){
						player.setVerticalMovement(moveSpeed);
						lastDirection = "down";
					} // else if
				} // if

				/****************
				 * Animate goku *
				 ****************/
				for(int i = 0 ; i < monsterCount ; i++) {
					Entity entity = entities.get(i);
					if(entity instanceof Monster3Entity) {
						if(entity.dx > 0) {
							int ganja = 0;
							entity.draw(g, ganja);
						} else if(entity.dx < 0) {
							int ganja = 1;
							entity.draw(g, ganja);
						} // else if
					} // if
				} // for

				/************************
				 * Animate the bloopers *
				 ************************/
				for(int i = 0 ; i < monsterCount ; i++) {
					Entity blooper = entities.get(i);
					if(blooper instanceof MonsterEntity) {
						if((System.currentTimeMillis() - blooper.lastAnimation) >= 400) {
							blooper.draw(g, blooper.frame);
							blooper.lastAnimation = System.currentTimeMillis();
							blooper.frame++;
							if(blooper.frame == 2) {
								blooper.frame = 0;
							} // if
						} // if
					} // if
				} // for
				resetInvincible();

				if ((System.currentTimeMillis() - stopKnockBackTimer) >= knockBackLength){
					if (gotHit){
						gotHit = false;
						rightPressed = false;
						upPressed = false;
						downPressed = false;
						leftPressed = false;
						moveSpeed = 150;
					} // if
				} // if

				/*********************
				 * Animates the Jean *
				 *********************/
				for(int i = 0 ; i < monsterCount ; i++) {
					Entity jean = entities.get(i);
					if(jean instanceof JeanEntity) {
						if((System.currentTimeMillis() - jean.lastAnimation) >= 100) {
							if(jeanFiring) {
								if(firstFire){
									jean.frame = 2;
									firstFire = false;
								} // if
								jean.draw(g, jean.frame);
								jean.lastAnimation = System.currentTimeMillis();
								jean.frame++;
								if(jean.frame == 4 ) {
									jean.frame = 2;
								} // if
							} else {
								jean.draw(g, jean.frame);
								jean.lastAnimation = System.currentTimeMillis();
								jean.frame++;
								if(jean.frame == 2) {
									jean.frame = 0;
								} // if
							} // else
						} // if
					} // if
				} // for

				/***********************
				 * Animates the Honry  *
				 ***********************/
				if(player.dx !=0) { // moving horizontally

					if(player.dx > 0) { // moving right
						if((System.currentTimeMillis() - lastAnimationSwitch) >= animationInterval) {
							initialFrame = 1;
							if(frame == 0) {
								frame = initialFrame;
							} // if
							player.draw(g, frame);
							frame++;
							if(frame >= 3){
								frame = initialFrame;
							} // if
							lastAnimationSwitch = System.currentTimeMillis(); 
						} // if
					} // if

					if(player.dx < 0) { // moving left

						if((System.currentTimeMillis() - lastAnimationSwitch) >= animationInterval) {
							initialFrame = 3;
							if(frame == 0){
								frame = initialFrame;
							}
							player.draw(g, frame);
							frame++;
							if(frame >= 5){
								frame = initialFrame;
							} // if
							lastAnimationSwitch = System.currentTimeMillis(); 
						} // if
					} // if 


				} else if(player.dy !=0){ // moving vertically
					if(player.dy < 0){ // moving up
						if((System.currentTimeMillis() - lastAnimationSwitch) >= animationInterval) {
							initialFrame = 5;
							if(frame == 0){
								frame = initialFrame;
							} // if
							player.draw(g, frame);
							frame++;
							if(frame >= 7){
								frame = initialFrame;
							} // if
							lastAnimationSwitch = System.currentTimeMillis(); 
						} // if

					} // if
					if(player.dy > 0) { // down
						if((System.currentTimeMillis() - lastAnimationSwitch) >= animationInterval) {
							initialFrame = 7;
							if(frame == 0){
								frame = initialFrame;
							} // if
							player.draw(g, frame);
							frame++;
							if(frame >= 9){
								frame = initialFrame;
							} // if
							lastAnimationSwitch = System.currentTimeMillis(); 
						} // if
					} // if

				} else if(player.dy == 0 && player.dx == 0) {
					frame = 0;
					player.draw(g, frame);
				} // else if
			} // if

			// if space bar pressed, try to stab
			if(stabPressed) {
				playerTryToStab();
			} // if

			// if c is pressed, try to fire arrow
			if(firePressed) {
				playerTryToFire();
			} // if

			// this is the refresh rate of gameLoop
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		} // while
	} // gameLoop


	/***********************************************
	 * Name: startGame							   *
	 * input: none								   *
	 * output: none								   *
	 * purpose: start a fresh game, clear old data *
	 ***********************************************/
	private void startGame() {
		// clear out any existing entities and initialize a new set
		entities.clear();
		initEntities();

		// blank out any keyboard settings that might exist
		leftPressed = false;
		upPressed = false;
		downPressed = false;
		rightPressed = false;
		stabPressed = false;
		firePressed = false;
	} // startGame

	/****************************************
	 * Inner class KeyInputHandler			*
	 * handles keyboard input from the user *
	 ****************************************/
	private class KeyInputHandler extends KeyAdapter {

		/*********************************************
		 * The following methods are required		 *
		 * for any class that extends the abstract	 *
		 * class KeyAdapter. They handle keyPressed, *
		 * keyReleased and keyTyped events			 *
		 *********************************************/
		public void keyPressed(KeyEvent e) {

			// if waiting for key press to start game, do nothing
			if(waitingForKeyPress) {
				return;
			} // if
			// respond to move left, right or fire
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(!gotHit){
					leftPressed = true;
				} // if
			} // if
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(!gotHit){
					rightPressed = true;
				} // if
			} // if
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(!gotHit){
					upPressed = true;
				} // if
			} // if
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(!gotHit){
					downPressed = true;
				} // if
			} // if
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				stabPressed = true;
			} // if
			if(e.getKeyCode() == KeyEvent.VK_C) {
				firePressed = true;
			} // if
		} // keyPressed

		public void keyReleased(KeyEvent e) {
			// if waiting for keypress to start game, do nothing
			if(waitingForKeyPress) {
				return;
			} // if
			// respond to move left, right or attacks
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			} // if
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(!gotHit){
					downPressed = false;
				} else {
					downPressed = true;
				} // else
			} // if
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(!gotHit){
					upPressed = false;
				} else {
					upPressed = true;
				} // else
			} // if
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(!gotHit){
					rightPressed = false;
				} else {
					rightPressed = true;
				} // else
			} // if
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				stabPressed = false;
			} // if
			if(e.getKeyCode() == KeyEvent.VK_C) {
				firePressed = false;
			} // if
		} // keyReleased

		public void keyTyped(KeyEvent e) {
			if(waitingForKeyPress) {
				if (pressCount >= 1 && e.getKeyChar() == KeyEvent.VK_1 && showMenu) {
					waitingForKeyPress = false;
					waitingForGameStart = false;
					waitingForResumeGame = false;
					showMenu = false;
					startGame();
					pressCount = 0;
				} else if(pressCount >= 1 && e.getKeyChar() == KeyEvent.VK_2 && showMenu) {
					showMenu = false;
					showInstructions = true;
					showMenu();
				} else if(pressCount >= 1 && e.getKeyChar() == KeyEvent.VK_3 && showMenu) {
					showMenu = false;
					showCredits = true;
					showMenu();
				} else if(pressCount >= 1 && ((e.getKeyChar() == KeyEvent.VK_BACK_SPACE && showInstructions == true) || (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && showCredits == true))) {
					showMenu = true;
					showInstructions = false;
					showCredits = false;
					showMenu();
				} else if(pressCount >= 1 && waitingForResumeGame && victory) {
					waitingForResumeGame = false;
					showMenu = true;
					showMenu();
				} else if(waitingForResumeGame) {
					waitingForResumeGame = false;
					waitingForKeyPress = false;
					startGame();
				} else {
					//	pressCount++;
					startGame();
				} // else
			} // if
			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed
		} // keyTyped
	} // KeyInputHandler

	/****************
	 * Main Program *
	 ****************/
	public static void main(String [] args) {
		// instantiate this object
		//playMusic();
		new Game();
	} // main
} // Game