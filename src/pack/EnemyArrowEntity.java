/*******************************************************
 * Name: EnemyArrowEntity.java						   *
 * Date: Apr 9, 2013								   *
 * Author: Derek Jew								   *
 * Purpose: Represents the arrow that the enemy shoots *
 *******************************************************/
package pack;

import java.awt.Graphics;

public class EnemyArrowEntity extends Entity{

	private double moveSpeed = -300; // speed shot moves
	private boolean used = false; // true if arrow hit something
	private Game game; // the game in which the arrow exists

	/*******************************************************
	 * Construct a new arrow							   *
	 * Input: game - the game in which the shot is created *
	 * 		 r - the image representing the shot		   *
	 * 		 x , y - initial locale						   *
	 *******************************************************/
	public EnemyArrowEntity(Game g, String r, int newX, int newY) {
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

		// if it hits the player, kill the player and the shot
		if(other instanceof PlayerEntity) {
			// notify the game that the player is dead
			if(game.isInvincible == false){
				game.isInvincible = true;
				game.player.hp--;
				game.knockBack("arrow");
				game.InvincibleTimer = System.currentTimeMillis();
				game.stopKnockBackTimer = System.currentTimeMillis();
				game.playOther();
			} // if

			// remove affected entities from the Entity list
			other.swordActive = false;
			game.removeEntities.add(game.playerSword);
			game.removeEntity(this);

			used = true;
		} // if

		if(other instanceof WallEntity) {
			game.removeEntity(this);
		} // if
	} // collidedWith
} // EnemyArrowEntity
