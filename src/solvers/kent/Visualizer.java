package solvers.kent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import graph.Graph;
import graph.Node;

public class Visualizer{

	static long SPEED = 1000;

	static Graph graph;

	public static void startVisualizer(Graph g){
		graph = g;
		texts=new ArrayList<String>(graph.n);
		for (int i=0; i<g.n*2; i++)
			texts.add(null);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI(){
		JFrame f = new JFrame("Visualizer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel());
		f.pack();
		f.setVisible(true);
	}

	static List<String> texts;
	static List<Point2D.Float> nodes = new LinkedList<Point2D.Float>();
	static List<Point2D.Float> nodeDests = new LinkedList<Point2D.Float>();
	static List<Point2D.Float> nodeFroms = new LinkedList<Point2D.Float>();
	static List<Float> nodeColors = new LinkedList<Float>();
	static int numNodes = 0;
	static long startTime = System.currentTimeMillis();

	public static void setNumNodes(int n){
		for (; numNodes < n; numNodes++){
			nodes.add(new Point2D.Float(300, 400));
			nodeDests.add(new Point2D.Float());
			nodeFroms.add(new Point2D.Float(300, 400));
			nodeColors.add(0f);
		}
	}

	public static void setNodePos(Node node, int x, int y){
		startTime = System.currentTimeMillis();
		setNumNodes(node.nodeId() + 1);
		nodeFroms.get(node.nodeId()).setLocation(nodeDests.get(node.nodeId()));
		nodeDests.get(node.nodeId()).setLocation(x, y);
	}

	public static void setNodeColor(Node node, float color){
		setNumNodes(node.nodeId() + 1);
		nodeColors.set(node.nodeId(), color);
	}
	
	public static void resetText(){
		for (int i=0; i<graph.n*2; i++)
			texts.set(i, null);
	}
	
	public static void setText(int level, String text){
		texts.set(level, text);
	}

	private static class MyPanel extends JPanel implements ActionListener{

		private static final long serialVersionUID = 7052943016172629560L;

		Timer timer = new Timer(16, this);

		public MyPanel(){
			this.setBackground(Color.WHITE);
			timer.start();
		}

		public Dimension getPreferredSize(){
			return new Dimension(800, 600);
		}

		public void actionPerformed(ActionEvent ev){
			if (ev.getSource() == timer) {
				float difference = Math.min(1, (System.currentTimeMillis() - startTime) / (SPEED/2f));
				difference = difference * difference * (3 - 2 * difference);
				for (int i = 0; i < numNodes; i++){
					nodes.get(i).x = nodeDests.get(i).x * difference + nodeFroms.get(i).x * (1 - difference);
					nodes.get(i).y = nodeDests.get(i).y * difference + nodeFroms.get(i).y * (1 - difference);
				}
				repaint();
			}
		}

		public void paintComponent(Graphics gg){
			super.paintComponent(gg);
			Graphics2D g = (Graphics2D) gg;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("default", Font.BOLD, 14));
			
			for (int i = 0; i < numNodes; i++){
				Point2D.Float from = nodes.get(i);
				for (int j = 0; j < i; j++)
					if (graph.isEdge(i, j)) {
						Point2D.Float to = nodes.get(j);
						g.drawLine(Math.round(from.x), Math.round(from.y), Math.round(to.x), Math.round(to.y));
					}
			}
			
			int ctr = 0;
			for (Point2D.Float p : nodes){
				g.setColor(Color.getHSBColor(nodeColors.get(ctr) + 0.6f, 0.5f, 1));
				g.fillArc(Math.round(p.x) - 10, Math.round(p.y) - 10, 20, 20, 0, 360);
				g.setColor(Color.BLACK);
				g.drawArc(Math.round(p.x) - 10, Math.round(p.y) - 10, 20, 20, 0, 360);
				g.drawString("" + (char) ('A' + ctr), p.x - 4, p.y + 5);
				ctr++;
			}
			
			for (int i=0; i<graph.n*2; i++)
				if (texts.get(i)!=null)
				g.drawString(""+texts.get(i), 500, i*50+50);
		}

	}

}
