package asteroids;

import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import asteroids.participants.AlienBullet;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Debris;
import asteroids.participants.Ship;
import static asteroids.Constants.*;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{
    // The state of all the Participants
    private ParticipantState pstate;
    
    // The ship (if one is active) or null (otherwise)
    private Ship ship;
    private AlienShip Aship;
    
    // When this timer goes off, it is time to refresh the animation
    private Timer refreshTimer;

    // The time at which a transition to a new stage of the game should be made.
    // A transition is scheduled a few seconds in the future to give the user
    // time to see what has happened before doing something like going to a new
    // level or resetting the current level.
    private long transitionTime;
    
    // Number of lives left
    private int lives;
    private int level;
    
    private int speed;

    // The game display
    private Display display;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Record the game and screen objects
        display = new Display(this);
        
        
        
        display.setVisible(true);        
        
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        refreshTimer.start();
       
       
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship
     * first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
    }
    
    /*
     * places an alien ship and removes any old ship
     */
    public void placeAlien(int level)
    {
    	
    	
    	Participant.expire(Aship);
    	Aship = new AlienShip(100.0, 100.0, level  ,this);
    	
    	new ParticipantCountdownTimer(Aship, "appear", Constants.ALIEN_DELAY);
    }

    /**
     * Places four asteroids near the corners of the screen. Gives them random
     * velocities and rotations.
     */
    private void placeAsteroids ()
    {
//    	if(level>1 && speed < SPEED_LIMIT)
//    	{
//    		speed++;
//    	}
//    	
    		
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
        addParticipant(new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET, 3, this));
        addParticipant(new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));
        addParticipant(new Asteroid(3, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place four asteroids
        
        placeAsteroids();

        // Place the ship
        placeShip();
        
        

        lives = 3;
        level = 1;
        score = 0;
        placeAlien(level);
        // Start listening to events
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
        
        
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
    	isRight = false;
    	isLeft = false;
    	isShoot = false;
    	isAcc = false;
    	
    	addParticipant(new Debris(ship.getX(),ship.getY()));
    	
        // Null out the ship
        ship = null;
        
        
        
        bullets.clear(); 
        // Display a legend
        display.setLegend("");
        // Decrement lives
        lives--;
  
        Display.setLives(lives);
        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * An asteroid of the given size has been destroyed, calculates score based on size
     */
    public void asteroidDestroyed (int size)
    {
    	
    	
    	if(size == 2)
    	{
    		score += 20;
    	}
    	if(size == 1)
    	{
    		score += 50;
    	}
    	if(size == 0)
    	{
    		score +=100;
    	}
    	Display.Score(score);
    	
        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0)
        {
        	
            scheduleTransition(END_DELAY);
            
            nextLevel();
        }
    }

   
    /*
     * transitions the game to the next level
     */
	private void nextLevel() {

		placeAsteroids();
		placeShip();
		level++;
		Display.Level(level);
		placeAlien(level);
		
	}


	/**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }
    
    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
    	
    	
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
            Display.setLives(3);
            bullets.clear();
            Display.Score(0);
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
        	
//        	if (level>1)
        	{
        		
        	}
//        	alien.moveZiggy();
        	
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
            
            if (ship == null) {
            	return;
            }
            
            if(isLeft)
        	{
        		ship.turnLeft();
        	}
        	if(isRight)
        	{
        		ship.turnRight();
        	}
        	if(isAcc)
        	{
        		ship.accelerate();
        	}
        	if(isShoot)
        	{
        		fire();
        	}

            
        }
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }

            // If the ship was destroyed, place a new one and continue
            else if (ship == null)
            {
                placeShip();
            }
        }
    }
    
    private boolean isLeft = false ;
    private boolean isRight = false;
    private boolean isAcc = false;
    private boolean isShoot = false;
    
    public boolean isAccelerate()
    {
    	return isAcc;
    }
    

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
    	
        if ((e.getKeyCode() == KeyEvent.VK_LEFT ||e.getKeyCode() == KeyEvent.VK_A) && ship != null)
        {
        	//turn left	
        	isLeft = true;
        }
         if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D )&& ship != null)
        {
        	 
        	 //turn right
        	 isRight = true;
        }
        //modded by grant 
         if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W )&&  ship != null)
        {
        	 //accelerate
        	 isAcc = true;
        	 ship.drawFlame();
        	
        }
         if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_S) && ship != null)
        {
        	 //shoot
        	 isShoot = true;
        }
    }
    
	 /*
	  * Fire a bullet from the nose of the ship
	  */
	private ArrayList<Bullet> bullets = new ArrayList<>();
    private void fire() {
    
    	if (bullets.size() <= 8)
    	{
    		bullets.add(new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation(), this));
    		addParticipant(bullets.get(bullets.size() - 1));
    		
    	}
    }
    /*
     * Shoots a bullet from the alien ship
     */
    public void alienFire()
    {
    	if (Aship == null || ship == null) {
    		return;
    	}
    	
    	//System.out.println("BALLS");
    	
    	if (level == 2) {
    		addParticipant(new AlienBullet(Aship.getX(),Aship.getY(), Aship.getDirection(), this));
    	}
    	if (level > 2) {
    		
    		double deltaY = Aship.getX() - ship.getX();
    		double deltaX = Aship.getY() - ship.getY();
    		
    		double angleInDegrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
    		
    		addParticipant(new AlienBullet(Aship.getX(), Aship.getY(), angleInDegrees, this));
    	}
    }
    /*
     * ensures more bullets are available after they expire
     */
    
    public void bulletKill(Bullet b) {
    	bullets.remove(b);  
	}

    private int score;
    
    public int scoreCount()
    {
    	return score;
    }

    /**
     * Ignore these events.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * Ignore these events.
     */
    @Override
    public void keyReleased (KeyEvent e)
    {     
    	if ((e.getKeyCode() == KeyEvent.VK_LEFT ||e.getKeyCode() == KeyEvent.VK_A) && ship != null)
        {
        	//turn left
    	
        	isLeft = false;
        }
         if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D )&& ship != null)
        {
        	 
        	 //turn right
        	 isRight = false;
        }

        //modded by grant 
         if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W )&&  ship != null)
        {
        	 //accelerate
        	 isAcc = false;
        	 ship.drawShip();
        	
        }
         if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_S) && ship != null)
        {
        	 //shoot
        	 isShoot = false;
        	 
        }
    }

	public void alienKill(int size)
	{
		Aship = null;
		
		if(size == 0)
		{
			score += 200;
		}
		else if(size == 1)
		{
			score += 400;
		}
		Display.Score(score);
		
		placeAlien(level);

//		placeAlien(2);
	}
	public int Level() {
		
		return level;
	}

	public int getAsteroidSpeed() {
		
		return speed;
	}
    
}
