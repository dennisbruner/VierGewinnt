package de.dennisbruner.viergewinnt.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.dennisbruner.viergewinnt.controller.GameController;
import de.dennisbruner.viergewinnt.controller.PropertyChangedListener;
import de.dennisbruner.viergewinnt.models.GameState;
import de.dennisbruner.viergewinnt.models.HumanPlayer;
import de.dennisbruner.viergewinnt.models.Player;
import de.dennisbruner.viergewinnt.models.PlayerColor;

@SuppressWarnings("serial")
public class GameFrame extends JDialog implements ActionListener, PropertyChangedListener
{
	private GameController game;
	
    private Container contentPane;
    private JButton[] buttonList;
    private JLabel[][] labelList;
    
    private JLabel currentPlayerLabel;
    
    public GameFrame(JFrame parent, GameController game)
    {
        super(parent, "Vier Gewinnt");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setModal(true);
        
        this.game = game;
        game.addPropertyChangedListener(this);
        
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        // Subpanels
        JPanel northPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new GridLayout(getFieldWidth(), getFieldHeight() + 1));
        contentPane.add(northPanel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        // Buttons erzeugen
        buttonList = new JButton[getFieldWidth()];
        for (int i = 0; i < getFieldWidth(); i++)
        {
        	JButton btn = new JButton("V");
        	btn.addActionListener(this);
        	buttonList[i] = btn;
        	
        	centerPanel.add(btn);
        }
        
        // Labels erzeugen
        labelList = new JLabel[getFieldWidth()][getFieldHeight()];
        for (int i = 0; i < getFieldHeight(); i++)
        {
        	for (int j = 0; j < getFieldWidth(); j++)
        	{
        		JLabel lbl = new JLabel();
        		lbl.setOpaque(true);
        		labelList[j][i] = lbl;
        		
        		centerPanel.add(lbl);
        	}
        }
        
        // Current player
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setText(
        		String.format("Spieler %s ist am Zug", game.getCurrentPlayer().getName()));
        northPanel.add(currentPlayerLabel);
        
        // Intial update
        updateField();
    }
    
    public int getFieldWidth()
    {
    	return game.getGameField().getWidth();
    }
    
    public int getFieldHeight()
    {
    	return game.getGameField().getHeight();
    }
    
    /**
	 * Synchronisiert alle Labels mit dem Zustand
	 * des Spielfelds
	 */
	private void updateField()
	{
		for (int i = 0; i < getFieldWidth(); i++)
		{
			for (int j = 0; j < getFieldHeight(); j++)
			{
				PlayerColor pc = game.getGameField().getColorAt(i, j);
				if (pc == PlayerColor.RED)
				{
					labelList[i][j].setBackground(Color.RED);
				}
				else if (pc == PlayerColor.YELLOW)
				{
					labelList[i][j].setBackground(Color.YELLOW);
				}
				else
				{
					labelList[i][j].setBackground(null);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JButton)
		{
			JButton button = (JButton)e.getSource();
			for (int i = 0; i < buttonList.length; i++)
			{
				if (buttonList[i] == button)
				{
					game.turn(i);
					break;
				}
			}
		}
	}

	@Override
	public void propertyChanged(String propertyName, Object value) {
		// Aktueller Spieler hat sich geändert
		// => Spielernamen oben im Frame ändern
		if (propertyName == "currentPlayer")
		{
			Player player = (Player)value;
			currentPlayerLabel.setText(
					String.format("Spieler %s ist am Zug", player.getName()));
			
			// Buttons ausgrauen wenn der lokale Spieler nicht am Zug ist
			if (player instanceof HumanPlayer)
			{
				for (int i = 0; i < buttonList.length; i++)
				{
					buttonList[i].setEnabled(true);
				}
			}
			else
			{
				for (int i = 0; i < buttonList.length; i++)
				{
					buttonList[i].setEnabled(false);
				}
			}
		}
		// Spielfeld hat sich geändert
		// => Spielfeld aktualisieren
		else if (propertyName == "gameField")
		{
			updateField();
		}
		// Gamestate ändert sich
		else if (propertyName == "gameState")
		{
			GameState state = (GameState)value;
			// Buttons ausgrauen wenn spiel vorbei
			if (state == GameState.WON || state == GameState.DRAW)
			{
				for (int i = 0; i < buttonList.length; i++)
				{
					buttonList[i].setEnabled(false);
				}
			}
			
			Player winningPlayer = game.getWinningPlayer();
			
			// Nachricht oben aktualisieren
			if (state == GameState.WON)
			{
				currentPlayerLabel.setText(
						String.format("Spieler %s hat gewonnen!!!", winningPlayer.getName()));
			}
			else if (state == GameState.DRAW)
			{
				currentPlayerLabel.setText("Das Spiel endet unentschieden!!!");
			}
		}
	}
}
