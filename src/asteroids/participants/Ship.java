package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.*;

import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.*;
import static asteroids.Constants.*;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, AlienDestroyer
{
	
    // The outline of the ship
    private Shape outline;

    // Game controller
    private Controller controller;
    


    // Constructs a ship at the specified coordinates
    // that is pointed in the given direction.
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);
        
        
        Path2D.Double poly = new Path2D.Double();
        
        
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-13, 10);
        
//        grant mods
//        poly.lineTo(-13, 8);
//        poly.lineTo(-25,0);
//        poly.lineTo(-13,-8);
//        poly.lineTo(-13,8);
//        poly.lineTo(-25, 0);
//        poly.lineTo(-13,8);
        
        
        poly.lineTo(-13, -10);
        poly.lineTo(-20, -12);
        poly.closePath();
        
        
        outline = poly;
        
        // Schedule an acceleration in two seconds
        //new ParticipantCountdownTimer(this, "move", 2000);
    }
    /*
     * Changes the outline for accelerating
     */
    public Shape drawFlame()
    {
    	
    	
    	Path2D.Double poly = new Path2D.Double();
    	
        
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-13, 10);
        //grant mods
        
        poly.lineTo(-13, 8);
        poly.lineTo(-30,0);
        poly.lineTo(-13,-8);
        poly.lineTo(-13,8);
        poly.lineTo(-30, 0);
        poly.lineTo(-13,8);
        
        
        poly.lineTo(-13, -10);
        poly.lineTo(-20, -12);
        poly.closePath();
        
       	outline = poly;
       	return outline;

    }
    /*
     * changes outline back to normal
     */
    public Shape drawShip()
    {
    	
    	Path2D.Double poly = new Path2D.Double();
    	
        
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-13, 10);
        poly.lineTo(-13, -10);
        poly.lineTo(-20, -12);
        poly.closePath();
        
       	outline = poly;
       	return outline;
    }
    
    
    

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }
    
    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipKiller, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }
    
    /**
     * This method is invoked when a ParticipantCountdownTimer completes
     * its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
  
    }




}
