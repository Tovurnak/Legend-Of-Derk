/*****************************************
 * Name: Monster3Entity.java			 *
 * Date: March 28, 2013					 *
 * Author: Derek Jew					 *
 * Purpose: represents one of the aliens *
 *****************************************/

package pack;
import java.awt.Graphics;

public class Monster3Entity extends Entity {
	private int randy = 0;		   // random number
	private int anotherRandy = 0;  // another random number
	private long moveDuration = 0;  // number between directional movement
	private Game game; 			   // the game in which alien exists

	/****************************************************
	 * Construct a new monster							*
	 * Input: game - the game in which enemy is created *
	 * 		 r - the image representing the enemy		*
	 * 		 x , y - initial locale						*
	 ****************************************************/
	public Monster3Entity(Game g, String r, int newX, int newY){
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
	
	public void draw(Graphics g, int frame) {
		String r = "";
		switch (frame) {
		//when player isn't moving
		case 0: r = "sprite/goku.gif";
		break;
		case 1: r = "sprite/gokuleft.gif";
		break;		
		default: r = "sprite/goku.gif";
		frame = 0; 
		break; 
		} // Switch
		sprite = (SpriteStore.get().getSprite(r));
		sprite.draw(g, (int)x, (int)y);
	} // draw

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms)	*
	 * Purpose: to move monster							*
	 ****************************************************/
	public void move(long delta, Graphics g) {
		double mega = Math.random() * 1000 + 1000; // time to stop
		double superMega = Math.random() * 1000 + 500; // time to move
		if(System.currentTimeMillis() - mega >= moveDuration) {
			if(this.dx != 0 && this.dy != 0) {
				dx = 0;
				dy = 0;
			} // if
			moveDuration = System.currentTimeMillis();
		} else if(System.currentTimeMillis() - superMega >= moveDuration) {
			if(this.dx == 0 && this.dy == 0) {
				moveDuration = System.currentTimeMillis();
				randy = (int)Math.round(Math.random()) * 2 - 1;
				anotherRandy = (int)(Math.random() * 50 + 25);
				dy = anotherRandy * randy;
				anotherRandy = (int)(Math.random() * 50 + 25);
				dx = anotherRandy * randy;
				if((Math.abs(this.x - game.player.x)) > (Math.abs(this.y - game.player.y))) {
					if(this.x < game.player.x) {
						if(this.dx < 0) {
							dx *= -1;
						} // if
					} else if(this.x > game.player.x) {
						if(this.dx > 0) {
							dx *= -1;
						} // if
					} // else if
				} else if((Math.abs(this.x - game.player.x)) < (Math.abs(this.y - game.player.y))) {
					if(this.y < game.player.y){
						if(this.dy < 0) {
							dy *= -1;
						} // if
					} else if(this.y > game.player.y){
						if(this.dy > 0) {
							dy *= -1;
						} // if
					} // else if
				} // else if
			} // if
		} // if
		// if we reach left side of screen and are moving left
		// rebound them
		if(dx < 0 && x < 10) {
			dx *= -1;
		} // if

		// same thing happens if we hit the right side of the screen
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

	public void aiAttack(Entity other, Graphics g) {
		if(other instanceof PlayerEntity) {
			game.enemyTryToFire(this);
		} // if
	} // aiAttack

	/*******************************************************
	 * Name: collidedWith								   *
	 * Input: the entity with which the alien has collided *
	 * Purpose: notification that the alien has collided   *
	 *******************************************************/
	public void collidedWith(Entity other) {
		if(other instanceof WallEntity || other instanceof WaterEntity) {
			// figure out which side the enemy is hitting from
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

		if(other instanceof Monster3Entity) {
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
} // Monster3Entity