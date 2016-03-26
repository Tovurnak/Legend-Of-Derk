/*****************************************
 * Name: JeanEntity.java				 *
 * Date: March 28, 2013					 *
 * Author: Derek Jew					 *
 * Purpose: represents one of the aliens *
 *****************************************/

package pack;
import java.awt.Graphics;

public class JeanEntity extends Entity {
	private Game game; 			   // the game in which alien exists

	/****************************************************
	 * Construct a new monster							*
	 * Input: game - the game in which enemy is created *
	 * 		 r - the image representing the enemy		*
	 * 		 x , y - initial locale						*
	 ****************************************************/
	public JeanEntity(Game g, String r, int newX, int newY){
		// call the constructor of superclass
		super(r, newX, newY);
		game = g;
		dx = 0;
		dy = 0;
	} // constructor

	public void draw(Graphics g, int frame) {
		String r = "";
		if(game.isMad) {
			switch (frame) {
			//when player isn't moving
			case 0: r = "sprite/jeanMad1.gif";
			break;
			case 1: r = "sprite/jeanMad2.gif";
			break;
			case 2: r = "sprite/jean1FireMad.gif";
			break;
			case 3: r = "sprite/jean2FireMad.gif";
			break;
			default: r = "sprite/jeanMad1.gif";
			frame = 0; 
			break; 
			} // Switch
		} else {
			switch (frame) {
			//when player isn't moving
			case 0: r = "sprite/jean1.gif";
			break;
			case 1: r = "sprite/jean2.gif";
			break;
			case 2: r = "sprite/jean1fire.gif";
			break;
			case 3: r = "sprite/jean2fire.gif";
			break;
			default: r = "sprite/jean1.gif";
			frame = 0; 
			break; 
			} // Switch
		} // else 
		sprite = (SpriteStore.get().getSprite(r));
		sprite.draw(g, (int)x, (int)y);
	} // draw

	public void animate(String r) {}

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms)	*
	 * Purpose: to move enemy							*
	 ****************************************************/
	public void move(long delta, Graphics g) {
		dx = 0;
		dy = 0;
		super.move(delta, g);
	} // move

	public void aiAttack(Entity other, Graphics g) {
		if(other instanceof PlayerEntity) {
			game.enemyTryToFire(this);
			game.jeanFiring = true;
		} // if
	} // aiAttack

	/******************************************************
	 * Name: collidedWith								  *
	 * Input: the entity with which the Jean has collided *
	 * Purpose: notification that the Jean has collided   *
	 ******************************************************/
	public void collidedWith(Entity other) {}
} // JeanEntity