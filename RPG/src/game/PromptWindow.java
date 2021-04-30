package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import game.Graph.Difficulty;

public class PromptWindow extends JFrame implements ActionListener{
	
	JButton fight;
	JButton button;
	JButton ult;
	JTextArea textArea;
	JLabel playerHp;
	JLabel monsterHp;
	MapTile tile;
	Monster mp;
	Map map;
	Player player;
	Difficulty gameDiff;
	Graph reference;
	public PromptWindow(String title, Map mp, Player p, Difficulty diff, Graph graph) {
		super(title);
		map = mp;
		player = p;
		gameDiff = diff;
		reference = graph;
		setSize(100, 200);
	}
	
	public void createWindow() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	   tile = map.returnCurrentMapTile();
       JPanel panel = new JPanel();
       panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
       panel.setOpaque(true);
       textArea = new JTextArea(15, 50);
       textArea.setWrapStyleWord(true);
       textArea.setEditable(false);

       JScrollPane scroller = new JScrollPane(textArea);
       scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
       scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       JPanel inputpanel = new JPanel();
       inputpanel.setLayout(new FlowLayout());
       fight = new JButton("Attack!");
       ult = new JButton("Ultimate");
       button = new JButton("Run");
       String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
       playerHp = new JLabel(currentHp);
       monsterHp = new JLabel();
       fight.addActionListener(this);
       fight.setVisible(false);
       monsterHp.setVisible(false);
       ult.addActionListener(this);
       ult.setVisible(false);
       button.addActionListener(this);
       if (gameDiff == Difficulty.HARD && tile.isTreasureHere()) {
       		textArea.append("Congrats! You found treasure! Health fully restored \n");
       		player.restoreHealth();
       }
       if (tile.hasEnemyHere()) {
           	mp = tile.getEnemy();
           	textArea.append("Found a level " + Integer.toString(mp.getLevel()) + " enemy here! Prepare to fight!\n");
           	fight.setVisible(true);
           	ult.setVisible(true);
           	monsterHp.setVisible(true);
           	String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
           	monsterHp.setText(text);
       } else {
       		textArea.setText("There's nothing here. HP restored by 1.\n");
       		player.increaseHealth();
       	
       }
       DefaultCaret caret = (DefaultCaret) textArea.getCaret();
       caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
       panel.add(scroller);
       inputpanel.add(playerHp);
       inputpanel.add(fight);
       inputpanel.add(ult);
       inputpanel.add(button);
       inputpanel.add(monsterHp);
       panel.add(inputpanel);
       getContentPane().add(BorderLayout.CENTER, panel);
       pack();
       setLocationByPlatform(true);
       setVisible(true);
       setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fight) {
			textArea.append("Fight!\n");
			player.attack(mp);
			textArea.append("Monster has taken " + Integer.toString(mp.getPreviousAttack()) + " damage!\n");
			String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
			monsterHp.setText(text);
			mp.attack(player);
			textArea.append("Player has taken " + Integer.toString(player.getPreviousAttack()) + " damage!\n");
			String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
			playerHp.setText(currentHp);
			if(mp.getHealth() <= 0 && mp.isAlive()) {
				textArea.append("Congrats! You won the fight!\n");
				textArea.append(player.incExp(mp) + "\n");
				if(mp.getMonsterType() == "Necromancer") {
					textArea.append("You killed a necromancer!\n");
				}
				System.out.println(player.getExp());
				mp.isDead();
				reference.numMonstersLeft--;
				reference.updateCanvas();
			}
			if(player.getHealth() <= 0) {
				textArea.append("Game Over! You're dead!\n");
				setBackground(Color.RED);
			}
       	
		}
		else if (e.getSource() == button) {
			if (tile.hasEnemyHere() && mp.isAlive() && player.getHealth() > 0) {
				textArea.append("Cannot run from a fight!\n");
			}
			else {
				dispose();
				if(gameDiff == Difficulty.HARD) {
					ArrayList<MapTile> tiles = map.getNecromancers();
					if(tiles.size() != 0) {
						System.out.println("RES");
						System.out.println("Necromancers Left: " + Integer.toString(tiles.size()));
						Necromancer n = (Necromancer) tiles.get(0).getEnemy();
						boolean decide = n.resurrectMonster(map, reference.numMonstersLeft, reference.controlNum);
						if(decide) {
							reference.numMonstersLeft++;
						}
					}
//					map.moveEnemies();
				}
				reference.update();
			}
		} else if(e.getSource() == ult) {
			int max = player.getMaxHealth();
			textArea.append("Use ultimate move!\n");
			if(player.getHealth() != max) {
				textArea.append("...but the player wasn't strong enough to handle it.\n");
				player.setHealth(0);
				String currentHp = Integer.toString(0) + "/" + Integer.toString(player.getMaxHealth());
				playerHp.setText(currentHp);
				textArea.append("Game Over! You're dead!\n");
				setBackground(Color.RED);
			} else {
				int enemyHp = mp.getMaxHealth();
				player.attack(mp, enemyHp);
				textArea.append("Monster has taken " + Integer.toString(enemyHp) + " damage!\n");
				String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
				monsterHp.setText(text);
				mp.attack(player, player.getMaxHealth()-1);
				textArea.append("Player has taken " + Integer.toString(player.getMaxHealth()-1) + " damage!\n");
				String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
				playerHp.setText(currentHp);
				if(mp.getHealth() <= 0 && mp.isAlive()) {
					textArea.append("Congrats! You won the fight!\n");
					textArea.append(player.incExp(mp) + "\n");
					System.out.println(player.getExp());
					mp.isDead();
					reference.numMonstersLeft--;
					reference.update();
				}
			}
			reference.update();
		}
		
	}
}   
