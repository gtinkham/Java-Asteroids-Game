package asteroids;

import javax.swing.*;

import java.awt.*;

import static asteroids.Constants.*;

/**
 * Defines the top-level appearance of an Asteroids game.
 */
@SuppressWarnings("serial")
public class Display extends JFrame
{
    // The area where the action takes place
    private Screen screen;
    
    private static JLabel amtLive = new JLabel();
    private static JLabel Level = new JLabel();
    private static JLabel Score = new JLabel();

    /**
     * Lays out the game and creates the controller
     */
    public Display (Controller controller)
    {
        // Title at the top
        setTitle(TITLE);

        // Default behavior on closing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main playing area and the controller
        screen = new Screen(controller);
        
        // This panel contains the screen to prevent the screen from being
        // resized
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(screen);

        // This panel contains buttons and labels
        JPanel controls = new JPanel();

        // The button that starts the game
        JButton startGame = new JButton(START_LABEL);
        controls.add(startGame);
        
        //setLives(3);
        Level.setText("Level: 1");
        Score.setText("Score: 0");
        
        controls.add(amtLive);
        controls.add(Level);
        controls.add(Score);
        
        // Organize everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(screenPanel, "Center");
        mainPanel.add(controls, "North");
        setContentPane(mainPanel);
        
        pack();

        // Connect the controller to the start button
        startGame.addActionListener(controller);
    }
    
    
    public static void setLives(int lives) {
    	
    	amtLive.setText("Lives: " + lives);
    }
    
    public static void Level(int level) {
    	Level.setText("Level: " + level);
    }
    public static void Score(int score)
    {
    	Score.setText("Score: "+ score);
    }
    
    /**
     * Called when it is time to update the screen display. This is what drives
     * the animation.
     */
    public void refresh ()
    {
        screen.repaint();
    }
    
    /**
     * Sets the large legend
     */
    public void setLegend (String s)
    {
        screen.setLegend(s);
    }
}
