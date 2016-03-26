/********************************
 * Name: PlayerEntity.java		*
 * Date: March 28, 2013	 	    *
 * Author: Derek Jew	        *
 * Purpose: represents a player *
 ********************************/

package pack;
import java.awt.Graphics;

public class PlayerEntity extends Entity {

	private Game game; // the game in which ship exists

	/*************************************************************
	 * Constructor												 *
	 * Input: game - the game in which ship is created			 *
	 * 		  r - a string with the name of the image associated *
	 *		  x, y - initial location of shot					 *
	 *************************************************************/
	public PlayerEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g;
	} // ShipEntity

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms)	*
	 * Purpose: to move ship 							*
	 ****************************************************/
	public void move(long delta, Graphics g) {
		// moves can't happen if at edge of screen
		if(dx < 0 && x < 0) {
			return;
		} // if
		if(dx > 0 && x > 781) {
			return;
		} // if
		if(dy < 0 && y < 0) {
			return;
		} // if
		if(dy > 0 && y > 568) {
			return;
		} // if
		super.move(delta, g);
	} // move

	public void aiAttack(Entity other, Graphics g) {}

	/******************************************************
	 * Name: collidedWith							  	  *
	 * Input: the entity with which the ship has collided *
	 * Purpose: notification that the ship has collided   *
	 ******************************************************/
	public void collidedWith(Entity other) {
		if(other instanceof MonsterEntity || other instanceof Monster2Entity
				|| other instanceof Monster3Entity || other instanceof JeanEntity) {
			if(game.isInvincible == false) {
				game.isInvincible = true;
				game.player.hp--;
				game.knockBack("enemy");
				game.InvincibleTimer = System.currentTimeMillis();	
				game.stopKnockBackTimer = System.currentTimeMillis();
				game.playOther();
			} // if
		} // if 

		if(other instanceof WallEntity || other instanceof WaterEntity) {
			// figure out from which x direction the player is coming from

			// if the ship is moving in the x direction
			if(this.dx != 0) {
				if((this.getX()) > (other.getX())) {
					this.setX(this.getX() + 3);
				} // if
				if((this.getX()) < (other.getX())) {
					this.setX(this.getX() - 2);
				} // if
			} // if
			if(this.dy != 0) {
				if((this.getY()) < (other.getY())) {  // Legend of derk, more like legend of derp.
					this.setY(this.getY() - 2);
				} // if		
				if((this.getY()) > (other.getY())) {
					this.setY(this.getY() + 3);
				} // if
			} // else if
			game.isColliding = true;
		} // if
		
		if(other instanceof LavaEntity) {
			game.notifyDeath();
		} // if
		game.isColliding = false;
	} // collidedWith

	public void animate(String r) {}

	// determines which file to draw the player
	 public void draw(Graphics g, int frame){
		  String r = "";
		  String facing = "sprite/Hero/down2.png";
		  if (game.lastDirection.equals("right")){
		   facing = "sprite/Hero/right2.png";
		  }
		  if (game.lastDirection.equals("up")){
		   facing = "sprite/Hero/up2.png";
		  }
		  if (game.lastDirection.equals("left")){
		   facing = "sprite/Hero/left2.png";
		  }
		  
		  if (game.lastDirection.equals("right")){
		   facing = "sprite/Hero/right2.png";
		  }
		  switch (frame) {
		  //when player isn't moving
		  case 0: r = facing;
		  break;
		  //the right animation is drawn
		  case 1: r = "sprite/Hero/right1.png";
		  break;
		  case 2: r = "sprite/Hero/right3.png";
		  break;

		  //the left animation is drawn
		  case 3: r = "sprite/Hero/left1.png";
		  break;
		  case 4: r = "sprite/Hero/left3.png";
		  break;

		  //the up animation are drawn
		  case 5: r = "sprite/Hero/up1.png";
		  break;
		  case 6: r = "sprite/Hero/up3.png";
		  break;

		  case 7: r = "sprite/Hero/down1.png";
		  break;
		  case 8: r = "sprite/Hero/down3.png";
		  break;

		  case 9: r = "sprite/Hero/down2.png";
		  frame = 0;
		  break;
		  default: r = "sprite/Hero/down2.png";
		  frame = 0; 
		  break; 
		  } // Switch
		  sprite = (SpriteStore.get().getSprite(r));
		  sprite.draw(g, (int)x, (int)y);
		 } // draw
} // class