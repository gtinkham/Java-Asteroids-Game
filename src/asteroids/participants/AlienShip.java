package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;

public class AlienShip extends Participant implements AsteroidDestroyer, ShipDestroyer
{

	private Shape outline;
	private int size;
	private Controller controller;
	private int level;
	/*
	 * Creates an AlienShip
	 */
	public AlienShip(Double x, Double y, int level, Controller controller)
	{
		
		
		super();
		setPosition(x, y);
		
		this.controller = controller;
		this.level = level;
		Path2D.Double poly = new Path2D.Double();
		if(level == 2)
		{
			size = 0;
			poly.moveTo(-20, 0);
		    poly.lineTo(20, 0);
			poly.lineTo(40, -20);
			poly.lineTo(20, -40);
			poly.lineTo(10, -50);
	        poly.lineTo(-10,-50);  
	        poly.lineTo(-40, -20);
	        poly.lineTo(-20,0);
			poly.closePath();
			setSpeed(0.5);
			
	        
		}
		else if(level >2)
		{
			size = 1;
			poly.moveTo(-10, 0);
	        poly.lineTo(10, 0);
	        poly.lineTo(20, -10);
	        poly.lineTo(10, -20);
	        poly.lineTo(5, -25);
	        poly.lineTo(-5,-25);  
	        poly.lineTo(-20, -10);
	        poly.lineTo(-10,0);
	       setSpeed(2);
		}
		
		outline = poly;
		
		new ParticipantCountdownTimer(this, "move", Constants.BULLET_DURATION);
		//rotate(Math.PI / 8);
		
		super.accelerate(Constants.SHIP_ACCELERATION);

		
		
	}
	/*
	 * Changes the direction of the alienShip path
	 */
	private double direction = 0;
	
	private boolean isPlaced = false;
	
	@Override
	public void countdownComplete (Object payload)
	{
		
		if (((String)payload).equals("move")) {
			double temp = Constants.RANDOM.nextDouble();
			if(temp>= 0 && temp <= 0.5)
			{
				super.accelerate(5);
			}
			if(temp > 0.5 && temp <= 1)
			{
				super.accelerate(-5);
			}
			if(temp > 0.5 && temp <= 0.75)
			{
				direction = -1.0*Math.PI/6;
			}
			if(temp > 0.75 && temp <= 1.0)
			{
				direction = Math.PI/6;
			}
			rotate(direction);
			
			
			if (isPlaced) {
				controller.alienFire();
			}
			new ParticipantCountdownTimer(this, "move", Constants.BULLET_DURATION);
			
		}
		
		if (((String)payload).equals("appear")) {
			controller.addParticipant(this);
			isPlaced = true;
		}
		
    }
	
	

	@Override
	protected Shape getOutline() 
	{
		return outline;
	}

	@Override
	public void collidedWith(Participant p) {
		if(p instanceof AlienDestroyer)
		{
        	controller.addParticipant(new Debris(getX(), getY()));
			
			Participant.expire(this);
			controller.alienKill(size);
		}
	}
	


}
