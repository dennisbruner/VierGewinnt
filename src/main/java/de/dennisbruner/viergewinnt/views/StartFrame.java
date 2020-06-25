package de.dennisbruner.viergewinnt.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import de.dennisbruner.viergewinnt.controller.BotGameController;
import de.dennisbruner.viergewinnt.controller.GameController;
import de.dennisbruner.viergewinnt.controller.LocalMultiplayerGameController;
import de.dennisbruner.viergewinnt.models.Difficulty;
import de.dennisbruner.viergewinnt.models.GameField;
import de.dennisbruner.viergewinnt.models.HumanPlayer;
import de.dennisbruner.viergewinnt.models.LevelOneBotPlayer;
import de.dennisbruner.viergewinnt.models.LevelThreeBotPlayer;
import de.dennisbruner.viergewinnt.models.LevelTwoBotPlayer;
import de.dennisbruner.viergewinnt.models.Player;
import de.dennisbruner.viergewinnt.models.PlayerColor;

@SuppressWarnings("serial")
public class StartFrame extends JFrame implements ActionListener
{
    private Container contentPane;
    
    private JTabbedPane tabbedPane;
    
    // Multiplayer
    private JTextField mpPlayer1TextBox, mpPlayer2TextBox;
    private JButton mpStartButton;
    
    // Singleplayer
    private JTextField spPlayerTextBox;
    private JComboBox<Difficulty> spDifficultyComboBox;
    private JButton spStartButton;
    
    // bot vs bot
    private JComboBox<Difficulty> bbPlayer1Difficulty, bbPlayer2Difficulty;
    private JButton bbStartButton;
    
    
    public StartFrame()
    {
        super("Vier Gewinnt - Auswahl des Spielmodus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Content pane
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        // Multiplayer
        initMultiPlayerTab();
        
        // Singleplayer
        initSinglePlayerTab();
        
        // Bot vs Bot
        initBotVsBotTab();
         
        // Network
        initNetworkTab();
    }
    
    private void initMultiPlayerTab()
    {
    	// Multiplayer tab panel
    	JPanel panel = new JPanel();
    	tabbedPane.addTab("Multiplayer", panel);
    	
    	// Border layout
    	panel.setLayout(new BorderLayout());
    	
    	// Subpanels
    	JPanel centerPanel = new JPanel(new GridBagLayout());
    	JPanel southPanel = new JPanel(new FlowLayout());
    	
    	// GBC
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.insets = new Insets(10, 10, 10, 10);
    	
    	// Center
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 0;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Spieler 1:"), gbc);
    		
    		mpPlayer1TextBox = new JTextField(32);
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 0;
    		gbc.ipadx = 150;
    		
    		centerPanel.add(mpPlayer1TextBox, gbc);
    	}
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 1;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Spieler 2:"), gbc);
    		
    		mpPlayer2TextBox = new JTextField(32);
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 1;
    		gbc.ipadx = 150;
    		centerPanel.add(mpPlayer2TextBox, gbc);
    	}
    	panel.add(centerPanel, BorderLayout.CENTER);
    	
    	// South
    	mpStartButton = new JButton("Spiel starten");
    	mpStartButton.addActionListener(this);
    	southPanel.add(mpStartButton);
    	panel.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void initSinglePlayerTab()
    {
    	// Singleplayer tab panel
    	JPanel panel = new JPanel();
    	tabbedPane.addTab("Singleplayer", panel);
    	
    	// Border layout
    	panel.setLayout(new BorderLayout());
    	
    	// Subpanels
    	JPanel centerPanel = new JPanel(new GridBagLayout());
    	JPanel southPanel = new JPanel(new FlowLayout());
    	
    	// GBC
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.insets = new Insets(10, 10, 10, 10);
    	
    	// Center
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 0;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Spieler:"), gbc);
    		
    		spPlayerTextBox = new JTextField(32);
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 0;
    		gbc.ipadx = 150;
    		
    		centerPanel.add(spPlayerTextBox, gbc);
    	}
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 1;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Schwierigkeit:"), gbc);
    		
    		spDifficultyComboBox = new JComboBox<Difficulty>(Difficulty.values());
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 1;
    		gbc.ipadx = 75;
    		centerPanel.add(spDifficultyComboBox, gbc);
    	}
    	panel.add(centerPanel, BorderLayout.CENTER);
    	
    	// South
    	spStartButton = new JButton("Spiel starten");
    	spStartButton.addActionListener(this);
    	southPanel.add(spStartButton);
    	panel.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void initBotVsBotTab()
    {
    	// Singleplayer tab panel
    	JPanel panel = new JPanel();
    	tabbedPane.addTab("Bot vs. Bot", panel);
    	
    	// Border layout
    	panel.setLayout(new BorderLayout());
    	
    	// Subpanels
    	JPanel centerPanel = new JPanel(new GridBagLayout());
    	JPanel southPanel = new JPanel(new FlowLayout());
    	
    	// GBC
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.insets = new Insets(10, 10, 10, 10);
    	
    	// Center
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 0;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Schwierigkeit 1:"), gbc);
    		
    		bbPlayer1Difficulty = new JComboBox<Difficulty>(Difficulty.values());
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 0;
    		gbc.ipadx = 75;
    		centerPanel.add(bbPlayer1Difficulty, gbc);
    	}
    	{
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 0;
    		gbc.gridy = 1;
    		gbc.ipadx = 0;
    		centerPanel.add(new JLabel("Schwierigkeit 2:"), gbc);
    		
    		bbPlayer2Difficulty = new JComboBox<Difficulty>(Difficulty.values());
    		gbc.fill = GridBagConstraints.NONE;
    		gbc.gridx = 1;
    		gbc.gridy = 1;
    		gbc.ipadx = 75;
    		centerPanel.add(bbPlayer2Difficulty, gbc);
    	}
    	panel.add(centerPanel, BorderLayout.CENTER);
    	
    	// South
    	bbStartButton = new JButton("Spiel starten");
    	bbStartButton.addActionListener(this);
    	southPanel.add(bbStartButton);
    	panel.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void initNetworkTab()
    {
    	// Network tab panel
    	tabbedPane.addTab("Netzwerk", null, null, "TODO");
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
    	if (e.getSource() == mpStartButton)
    	{
    		// Spielfeld erstellen
    		GameField field = new GameField(7, 6);
    		
    		// Zwei menschliche Spieler erstellen
    		Player player1 = new HumanPlayer(mpPlayer1TextBox.getText(), PlayerColor.RED);
    		Player player2 = new HumanPlayer(mpPlayer2TextBox.getText(), PlayerColor.YELLOW);
    		
    		// Game controller
    		GameController controller = new LocalMultiplayerGameController(field, player1, player2);
    		
    		// Launch game
    		GameFrame gf = new GameFrame(this, controller);
    		gf.setVisible(true);
    	}
    	else if (e.getSource() == spStartButton)
    	{
    		// Spielfeld erstellen
    		GameField field = new GameField(7, 6);
    		
    		// Menschlichen Spieler erstellen
    		Player player1 = new HumanPlayer(spPlayerTextBox.getText(), PlayerColor.RED);
    		
    		// Bot erstellen
    		Player player2;
    		switch ((Difficulty)spDifficultyComboBox.getSelectedItem())
    		{
    		case HARD:
    			player2 = new LevelThreeBotPlayer(PlayerColor.YELLOW);
    			break;
    		case NORMAL:
    			player2 = new LevelTwoBotPlayer(PlayerColor.YELLOW);
    			break;
    		case EASY:
    		default:
    			player2 = new LevelOneBotPlayer(PlayerColor.YELLOW);
    		}
    		
    		// Game controller
    		GameController controller = new BotGameController(field, player1, player2);
    		
    		// Launch game
    		GameFrame gf = new GameFrame(this, controller);
    		gf.setVisible(true);
    	}
    	else if (e.getSource() == bbStartButton)
    	{
    		// Spielfeld erstellen
    		GameField field = new GameField(7, 6);
    		
    		// Bot erstellen
    		Player player1;
    		switch ((Difficulty)bbPlayer1Difficulty.getSelectedItem())
    		{
    		case HARD:
    			player1 = new LevelThreeBotPlayer(PlayerColor.RED);
    			break;
    		case NORMAL:
    			player1 = new LevelTwoBotPlayer(PlayerColor.RED);
    			break;
    		case EASY:
    		default:
    			player1 = new LevelOneBotPlayer(PlayerColor.RED);
    		}
    		
    		Player player2;
    		switch ((Difficulty)bbPlayer2Difficulty.getSelectedItem())
    		{
    		case HARD:
    			player2 = new LevelThreeBotPlayer(PlayerColor.YELLOW);
    			break;
    		case NORMAL:
    			player2 = new LevelTwoBotPlayer(PlayerColor.YELLOW);
    			break;
    		case EASY:
    		default:
    			player2 = new LevelOneBotPlayer(PlayerColor.YELLOW);
    		}
    		
    		// Game controller
    		GameController controller = new BotGameController(field, player1, player2);
    		
    		// Launch game
    		GameFrame gf = new GameFrame(this, controller);
    		gf.setVisible(true);
    	}
    }
}
