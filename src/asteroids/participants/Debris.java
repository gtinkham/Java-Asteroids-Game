package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;

public class Debris extends Participant {

	private Shape outline;
	
	public Debris(Double x, Double y)
	{
		super();
		setPosition(x,y);
		
		Path2D.Double poly = new Path2D.Double();
		
//			poly.moveTo(20, 0);
//	        poly.lineTo(-20, 12);
//	        poly.lineTo(-13, 10);
//	        poly.moveTo(-13, 10);
//	        
//	        poly.lineTo(10, 15);
			
			double rando1 = Constants.RANDOM.nextDouble() * 40;
			double rando2 = Constants.RANDOM.nextDouble() * 40;
			double rando3 = Constants.RANDOM.nextDouble() * 40;
			
			poly.moveTo(0, 0);
	        poly.lineTo(rando1, 12);
	        
	        poly.moveTo(-20, 40);
	        poly.lineTo(-10, rando2);

	        poly.moveTo(-13, 10);
	        poly.lineTo(10, rando3);
	        
	        double randoSpeed = Constants.RANDOM.nextDouble() * 5;
	        double randoNeg = Constants.RANDOM.nextDouble();
	        
	        if (randoNeg < .5) {
	        	randoSpeed *= -1;
	        }
	        
	        setSpeed(randoSpeed);
			
			
//	        poly.lineTo(-13, -10);
//	        poly.lineTo(-20, -12);
		
		outline = poly;
		
		new ParticipantCountdownTimer(this, Constants.BULLET_DURATION);
		
	}
	@Override
	protected Shape getOutline() {
		
		return outline;
	}

	@Override
	public void collidedWith(Participant p) {
		return;
		
	}
	@Override
	public void countdownComplete (Object payload)
    {
		expire(this);
		
    }

}
