package game;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import game.Graph.Difficulty;

public class SaveFileWriter {
		private int initialPlayerLevel;
		private int baseExpForLevel;
		private String fileName;
	
		public SaveFileWriter(String name) {
			fileName = name;
		}
		
		public void writeFile(Player p, int monsterLevel, int numMonsters, int numSquares, Map map, int initialLevel, int baseExpForLevel, int resetsLeft) {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
	        try {
	            dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.newDocument();
	            //add elements to Document
	            Element rootElement = doc.createElement("Game");
	            //append root element to document
	            doc.appendChild(rootElement);
	            Element save = doc.createElement("Save");
	            rootElement.appendChild(save);
	            save.setAttribute("playerLevel", Integer.toString(p.getLevel()));
	            save.setAttribute("playerHealth", Integer.toString(p.getHealth()));
	            save.setAttribute("monsterLevel", Integer.toString(monsterLevel));
	            save.setAttribute("numMonsters", Integer.toString(numMonsters));
	            save.setAttribute("numSquares", Integer.toString(numSquares));
	            save.setAttribute("playerExp", Integer.toString(p.getExp()));
	            save.setAttribute("initialLevel", Integer.toString(initialLevel));
	            save.setAttribute("baseExpForLevel", Integer.toString(baseExpForLevel));
	            save.setAttribute("resetsLeft", Integer.toString(resetsLeft));
	            save.setAttribute("numHeals", Integer.toString(p.getHeals()));
	            for (int i = 0; i < 13; i++) {
	            	Element column = doc.createElement("Column");
	            	column.setAttribute("num", Integer.toString(i));
	            	
	            	for (int j = 0; j < 13; j++) {
	            		Element row = doc.createElement("Row");
	            		row.setAttribute("num", Integer.toString(j));
	            		row.setAttribute("hasBeenVisited", Boolean.toString(map.getMapTileAt(i, j).hasBeenVisited()));
	            		row.setAttribute("hasTreasure", Boolean.toString(map.getMapTileAt(i, j).isTreasureHere()));
	            		row.setAttribute("hasMonster", Boolean.toString(map.getMapTileAt(i, j).hasEnemyHere()));
	            		if (map.getMapTileAt(i, j).hasEnemyHere()) {
	            			row.setAttribute("level", Integer.toString(map.getMapTileAt(i, j).getEnemy().getLevel()));
	            			row.setAttribute("isAlive", Boolean.toString(map.getMapTileAt(i, j).getEnemy().isAlive()));
	            			row.setAttribute("type", map.getMapTileAt(i, j).getEnemy().getMonsterType());
	            			
	            		}
	            		column.appendChild(row);
	            	}
	            	save.appendChild(column);
	            }
	            
	            Element currentPosition = doc.createElement("PlayerPosition");
	            currentPosition.setAttribute("X", Integer.toString(map.getPlayerPosition()[0]));
	            currentPosition.setAttribute("Y", Integer.toString(map.getPlayerPosition()[1]));
	            save.appendChild(currentPosition);
	            //append first child element to root element

	            //for output to file, console
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            //for pretty print
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            DOMSource source = new DOMSource(doc);

	            //write to console or file
	            StreamResult file = new StreamResult(new File(fileName));

	            //write data
	            transformer.transform(source, file);
	            System.out.println("DONE");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		
		public Map readFromFile() {
			try {
				File xml = new File(fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xml);
				doc.getDocumentElement().normalize();
				Player player;
				Map map;
				NodeList list = doc.getElementsByTagName("Save");
				Node save = list.item(0);
				System.out.println(save.getNodeName());
				NamedNodeMap saveAttr = save.getAttributes();
				Node playerLev = saveAttr.getNamedItem("playerLevel");
				int playerLevel = Integer.parseInt(playerLev.getNodeValue());
				int playerHealth = Integer.parseInt(saveAttr.getNamedItem("playerHealth").getNodeValue());
				int baseMonsterLevel = Integer.parseInt(saveAttr.getNamedItem("monsterLevel").getNodeValue());
				int numMonsters = Integer.parseInt(saveAttr.getNamedItem("numMonsters").getNodeValue());
				int numSquares = Integer.parseInt(saveAttr.getNamedItem("numSquares").getNodeValue());
				int exp = Integer.parseInt(saveAttr.getNamedItem("playerExp").getNodeValue());
				int resetsLeft = Integer.parseInt(saveAttr.getNamedItem("resetsLeft").getNodeValue());
				initialPlayerLevel = Integer.parseInt(saveAttr.getNamedItem("initialLevel").getNodeValue());
				baseExpForLevel = Integer.parseInt(saveAttr.getNamedItem("baseExpForLevel").getNodeValue());
				int heals = Integer.parseInt(saveAttr.getNamedItem("numHeals").getNodeValue());
				player = new Player(20, 5, playerLevel);
				player.setHealth(playerHealth);
				player.setExp(exp);
				player.setNumHeals(heals);
				map = new Map(player, numMonsters, baseMonsterLevel, numSquares);
				
				NodeList columnList = doc.getElementsByTagName("Column");
				for (int i = 0; i < 13; i++) {
					Node column = columnList.item(i);
					NodeList rowList = column.getChildNodes();
					for (int j = 1; j < rowList.getLength(); j+=2) {
						Node cell = rowList.item(j);
						NamedNodeMap cellAttr = cell.getAttributes();
						boolean hasBeenVisited = (cellAttr.getNamedItem("hasBeenVisited").getNodeValue().equals("true")) ? true: false;
						boolean hasTreasure = (cellAttr.getNamedItem("hasTreasure").getNodeValue().equals("true")) ? true: false;
						boolean hasMonster = (cellAttr.getNamedItem("hasMonster").getNodeValue().equals("true")) ? true: false;
						if (hasMonster) {
							int monsterLevel = Integer.parseInt(cellAttr.getNamedItem("level").getNodeValue());
							String type = cellAttr.getNamedItem("type").getNodeValue();
							if (type.equals("Necromancer")) {
								System.out.println("Found");
								map.getMapTileAt(i, j/2).setEnemy(new Necromancer(6, 1, monsterLevel));
							} else {
								map.getMapTileAt(i, j/2).setEnemy(new Monster(6, 1, monsterLevel));
							}
							boolean isAlive = (cellAttr.getNamedItem("isAlive").getNodeValue().equals("true")) ? true: false;
							if (!isAlive) {
								map.getMapTileAt(i, j/2).getEnemy().isDead();
							}
						}
						if (hasTreasure) {
							map.getMapTileAt(i, j/2).setNumTreasure(1);
						}
						if (hasBeenVisited) {
							map.getMapTileAt(i, j/2).toggleVisited();
						}
					}
				}
				
				Node playerPosition = doc.getElementsByTagName("PlayerPosition").item(0);
				NamedNodeMap positionAttr = playerPosition.getAttributes();
				int x = Integer.parseInt(positionAttr.getNamedItem("X").getNodeValue());
				int y = Integer.parseInt(positionAttr.getNamedItem("Y").getNodeValue());
				int [] position = {x, y};
				map.setPlayerPosition(position);
				map.setResetsLeft(resetsLeft);
				return map;
				
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public int getInitialPlayerLevel() {
			return initialPlayerLevel;
		}
		
		public int getBaseExpForLevel() {
			return baseExpForLevel;
		}
		
		public static void main (String [] args) {
			Player player = new Player(20, 5, 1);
			Map map = new Map(player);
			map.populateMap(30, 0, Difficulty.HARD);
			SaveFileWriter filer = new SaveFileWriter("game.xml");
			filer.readFromFile();
		}
}
