/********************************************************
 * Name: ArrowEntity.java								*
 * Date: Apr 9, 2013									*
 * Author: Derek Jew									*
 * Purpose: Represents the arrow that the player shoots *
 ********************************************************/
package pack;

import java.awt.Graphics;

public class ArrowEntity extends Entity {

	private double moveSpeed = -300; // speed shot moves
	private boolean used = false; // true if arrow hit something
	private Game game; // the game in which the arrow exists

	/*******************************************************
	 * Construct a new arrow							   *
	 * Input: game - the game in which the shot is created *
	 * 		 r - the image representing the shot		   *
	 * 		 x , y - initial locale						   *
	 *******************************************************/
	public ArrowEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g; 
		dy = moveSpeed;
	} // ArrowEntity

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms)	*
	 * Purpose: to move monster							*
	 ****************************************************/
	public void move(long delta, Graphics g) {
		super.move(delta, g);

		// if shot moves off screen, remove it from entity list
		if(y < -50) {
			game.removeEntity(this);
		} // if
		if(y > 650) {
			game.removeEntity(this);
		} // if
		if(x < -50) {
			game.removeEntity(this);
		} // if
		if(x > 850) {
			game.removeEntity(this);
		} // if
	} // move

	public void aiAttack(Entity other, Graphics g) {}

	public void collidedWith(Entity other) {
		// prevent double kills
		if(used) {
			return;
		} // if 

		// if it hits an alien, kill the alien and the shot
		if(other instanceof MonsterEntity || other instanceof Monster2Entity
				|| other instanceof Monster3Entity  || other instanceof JeanEntity) {
			// remove affected entities from the Entity list
			game.removeEntity(this);
			other.hp--;
			if(other.hp <= 10 && other instanceof JeanEntity){
				game.jeanFiringInterval = 50;
				game.isMad = true;
			} // if
			if(other.hp == 0) {
				game.removeEntity(other);
				game.notifyMonsterKilled();
			} // if

			// if the enemy was wielding a sword, kill it
			other.swordActive = false;
			game.removeEntities.add(game.enemySword);
			
			used = true;
		} // if

		if(other instanceof WallEntity) {
			game.removeEntity(this);
		} // if

	} // collidedWith
} // ArrowEntity