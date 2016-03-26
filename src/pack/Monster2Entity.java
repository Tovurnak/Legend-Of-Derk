/**************************************
 * Name: Monster2Entity.java		  *
 * Date: March 28, 2013			      *
 * Author: Derek Jew				  *
 * Purpose: represents the sword dude *
 **************************************/

package pack;
import java.awt.Graphics;

public class Monster2Entity extends Entity {
	private int randy = 0;		   // random number
	private int anotherRandy = 0;  // another random number
	private long moveDuration = 0; // time between directional movement
	private Game game; 			   // the game in which alien exists
	private int frame;			   // which sprite to use

	/****************************************************
	 * Construct a new monster							*
	 * Input: game - the game in which enemy is created *
	 * 		 r - the image representing the enemy		*
	 * 		 x , y - initial locale						*
	 ****************************************************/
	public Monster2Entity(Game g, String r, int newX, int newY){
		// call the constructor of superclass
		super(r, newX, newY);
		game = g;
		randy = (int)Math.round(Math.random()) * 2 - 1;
		anotherRandy = (int)(Math.random() * 50 + 25);
		dy = anotherRandy * randy;
		anotherRandy = (int)(Math.random() * 50 + 25);
		dx = anotherRandy * randy;
	} // constructor

	public void animate(String r) {}

	/***************************************
	 * Name: draw						   *
	 * Input: g - the graphics of the game *
	 * 		  frame - which sprite to draw *
	 * Purpose: to draw the correct sprite *
	 ***************************************/
	public void draw(Graphics g, int frame) {
		String r = "";
		switch (frame) {
		case 0: r = "sprite/Sword Man/sworddude1.gif"; // up
		break;
		case 1: r = "sprite/Sword Man/sworddude2.gif"; // right
		break;		
		case 2: r = "sprite/Sword Man/sworddude3.gif"; // down
		break;	
		case 3: r = "sprite/Sword Man/sworddude4.gif"; // left
		break;	
		} // Switch
		sprite = (SpriteStore.get().getSprite(r));
		sprite.draw(g, (int)x, (int)y);
	} // draw

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms)	*
	 * 		  g - the graphics of the game				*
	 * Purpose: to move monster							*
	 ****************************************************/
	public void move(long delta, Graphics g) {
		double mega = Math.random() * 1000 + 1000; 	   // time to stop
		double superMega = Math.random() * 1000 + 500; // time to move
		// if the enemy is allowed to switch movement
		if(System.currentTimeMillis() - mega >= moveDuration) {
			// if the enemy is moving, stop him
			if(this.dx != 0 && this.dy != 0) {
				dx = 0;
				dy = 0;
			} // if
			moveDuration = System.currentTimeMillis();
		} else if(System.currentTimeMillis() - superMega >= moveDuration) {
			// if the enemy wasn't moving, move him 
			if(this.dx == 0 && this.dy == 0) {
				// generate a random dx and dy, seperate of each other
				moveDuration = System.currentTimeMillis();
				randy = (int)Math.round(Math.random()) * 2 - 1; 
				anotherRandy = (int)(Math.random() * 50 + 25);
				dy = anotherRandy * randy;
				anotherRandy = (int)(Math.random() * 50 + 25);
				dx = anotherRandy * randy;
				
				// if the enemy is closer to the player in the x direction
				if((Math.abs(this.x - game.player.x)) > (Math.abs(this.y - game.player.y))) {
					// if the enemy is left of the player
					if(this.x < game.player.x) {
						// if the enemy is moving left, turn him around
						if(this.dx < 0) {
							dx *= -1;
						} // if
					// if the enemy is right of the player
					} else if(this.x > game.player.x) {
						// if the enemy is moving right, turn him around
						if(this.dx > 0) {
							dx *= -1;
						} // if
					} // else if
				// if the enemy is closer to the player in the y direction
				} else if((Math.abs(this.x - game.player.x)) < (Math.abs(this.y - game.player.y))) {
					// if the enemy is above the player
					if(this.y < game.player.y){
						// if the enemy is moving up, turn him around
						if(this.dy < 0) {
							dy *= -1;
						} // if
					// if the enemy is below the player
					} else if(this.y > game.player.y){
						// is the enemy is moving down, turn him around
						if(this.dy > 0) {
							dy *= -1;
						} // if
					} // else if
				} // else if
			} // if

			// pick correct sprite
			// still frame
			if(dx == 0 && dy == 0) {
				frame = 2;
			} // if
			
			// if the enemy is moving right
			if(dx > 0) {
				// if the enemy is moving more right than up or down
				if(dx > Math.abs(dy)) {
					frame = 1;
				// if the enemy is moving more up or down than right
				} else if (dx < Math.abs(dy)) {
					// if the enemy is moving down
					if(dy > 0) {
						frame = 2;
					// if the enemy is moving up
					} else if (dy < 0) {
						frame = 0;
					} // else if
				} // else if
			// if the enemy is moving left
			} else if(dx < 0) {
				// if the enemy is moving more left than up or down
				if(Math.abs(dx) > Math.abs(dy)) {
					frame = 3;
				// if the enemy is moving more up or down than left
				} else if(Math.abs(dx) < Math.abs(dy)) {
					if(dy > 0) {
						frame = 2;
					} else if (dy < 0) {
						frame = 0;
					} // else if
				} // else if
			} // else if
			
			// draw the frame
			this.draw(g, frame);
		} // if
		
		// rebound enemy if the edge of the screen is struck
		if(dx < 0 && x < 10) {
			dx *= -1;
		} // if
		if(dx > 0 && x > 750) {
			dx *= -1;
		} // if
		if(dy > 0 && y > 550) {
			dy *= -1;
		} // if
		if(dy < 0 && y < 10) {
			dy *= -1;
		} // if
		super.move(delta, g);
	} // move

	/*************************************************************************
	 * Name: aiAttack														 *
	 * Input: other - the other entity										 *
	 * 		  x - the element in the ArrayList which the entity attacking is *
	 * Purpose: to attempt to attack the player								 *
	 *************************************************************************/
	public void aiAttack(Entity other, Graphics g) {
		if(other instanceof PlayerEntity) {
			game.enemyTryToStab(this, g);
		} // if
	} // aiAttack

	/***************************************************************
	 * Name: collidedWith								 		   *
	 * Input: other - the entity with which the alien has collided *
	 * Purpose: notification that the alien has collided   		   *
	 ***************************************************************/
	public void collidedWith(Entity other) {
		if(other instanceof WallEntity) {
			// figure out which side the enemy is hitting the wall
			if(this.getX() + 28 <= other.getX()) {
				dx *= -1;
			} // if
			if(this.getX() >= other.getX() + 14) {
				dx *= -1;
			} // if
			if(this.getY() + 30 <= other.getY()) {
				dy *= -1;
			} // if
			if(this.getY() >= other.getY() + 14) {
				dy *= -1;
			} // if
		} // if

		if(other instanceof Monster2Entity) {
			// figure out which side the enemy is hitting from
			if(this.getX() + 28 <= other.getX()) {
				dx *= -1;
			} // if
			if(this.getX() >= other.getX() + 28) {
				dx *= -1;
			} // if
			if(this.getY() + 30 <= other.getY()) {
				dy *= -1;
			} // if
			if(this.getY() >= other.getY() + 30) {
				dy *= -1;
			} // if
		} // if
	} // collidedWith
} // Monster2Entity