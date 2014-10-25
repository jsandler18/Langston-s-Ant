import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.io.File;

public class LangstonsAnt extends JPanel implements ActionListener,Runnable{

	int[][] grid;
	int xpos;
	int ypos;
	BufferedImage image;
	Color[] dirsColors;
	char[] dirs;
	int nextColor;
	String directions;
	String iterations;
	JFrame frame;
	JLabel directionsLabel;
	JTextField directionsField;
	JLabel iterationsLabel;
	JTextField iterationsField;
	JLabel drawSpeedLabel;
	JTextField drawSpeedField;
	JButton change;
	JTextField saveAs;
	JButton save;
	int drawspeed;
	
	public LangstonsAnt(){
		frame = new JFrame("Langston's Ant");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);
		
		BorderLayout layout = new BorderLayout();
		JPanel inputPanel = new JPanel();
		GridLayout innerLayout = new GridLayout(9, 1);
		frame.setLayout(layout);
		inputPanel.setLayout(innerLayout);
		
		directionsLabel = new JLabel("Directions:");
		directionsField = new JTextField("LRRRLLL");
		iterationsLabel = new JLabel("Iterations:");
		iterationsField = new JTextField("10000");
		drawSpeedLabel = new JLabel("Draw speed:");
		drawSpeedField = new JTextField("1");
		change = new JButton("Change!");
		saveAs = new JTextField("Ant.png");
		save = new JButton("Save!");
		change.addActionListener(this);
		save.addActionListener(this);
		
		frame.add(inputPanel, BorderLayout.EAST);
		frame.add(this);
		inputPanel.add(directionsLabel);
		inputPanel.add(directionsField);
		inputPanel.add(iterationsLabel);
		inputPanel.add(iterationsField);
		inputPanel.add(drawSpeedLabel);
		inputPanel.add(drawSpeedField);
		inputPanel.add(change);
		inputPanel.add(saveAs);
		inputPanel.add(save);
		
		directions="LRRRLLL";
		iterations="10000";
		drawspeed=1;

		frame.revalidate();

	}
	
	public void run(){
		//int array representing a grid, stores the color beneath it, starts completely black, x is inner, y is outer
		xpos = 125;
	    ypos = 125;
		grid = new int[250][250];
		for(int x = 0;x<250; x++){
			for(int y = 0; y<250; y++){
				grid[x][y]=0;
			}
		}
		//int for determining direction. 0=N,1=E,2=S,3=W
		int compass = 0;
		//large base of colors to choose from
		Color[] colors = {Color.black, Color.blue,Color.cyan, Color.green, Color.yellow, Color.orange, Color.red, Color.magenta, Color.pink, Color.white, Color.lightGray, Color.gray, Color.darkGray};
		//each char in this array is a direction
		dirs = directions.toCharArray();
		//pulls from base colors and matches directions one to one
		dirsColors = new Color[dirs.length];
		int currentColor=0;
		for(int x=0; x<dirs.length;x++){
			dirsColors[x]=colors[x];
		}	
		
		image = new BufferedImage(1000,1000,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.black);
		g2d.fill(new Rectangle2D.Double(0,0,1000,1000));
		
		//main loop
		for(int i = 0; i<Integer.parseInt(iterations); i++){
			if(xpos>249){
				xpos=0;
			}
			if(xpos<0){
				xpos=249;
			}
			if(ypos>249){
				ypos=0;
			}
			if(ypos<0){
				ypos=249;
			}
			
			//get color from grid
			if(grid[xpos][ypos]==dirsColors.length){
				currentColor=0;
			}
			else{
				currentColor=grid[xpos][ypos];
			}
			//turn
			if(dirs[currentColor]=='L'){
				compass--;
			}
			else if(dirs[currentColor]=='R'){
				compass++;
			}
			if(compass>3){
				compass=0;
			}
			if(compass<0){
				compass=3;
			}
			//change color of square
			nextColor=currentColor+1;
			if(nextColor==dirsColors.length){
				nextColor=0;
			}
			g2d.setColor(dirsColors[nextColor]);
			grid[xpos][ypos]=nextColor;
			g2d.fill(new Rectangle2D.Double(xpos*4, ypos*4, 4,4));
			//move
			if(compass==0){
				ypos--;
			}
			if(compass==1){
				xpos++;
			}
			if(compass==2){
				ypos++;
			}
			if(compass==3){
				xpos--;
			}
			this.paintImmediately(new Rectangle(0, 0, 1000, 1000));
			frame.revalidate();
			try {
				Thread.sleep(drawspeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2dLive = (Graphics2D)g;
		g2dLive.drawImage(image,0,0,1000,1000,null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Change!")){
			directions =directionsField.getText();
			iterations=iterationsField.getText();
			drawspeed=(int)Double.parseDouble(drawSpeedField.getText());
			Thread go = new Thread(this);
			go.start();
		}
		else if(e.getActionCommand().equals("Save!")){
			try{
				ImageIO.write(image,"png",new File(saveAs.getText()));
			} catch (java.io.IOException f){
				System.out.println("error writing image");
			}
		}
		
	}
	
	public static void main(String[] args){
		LangstonsAnt go = new LangstonsAnt();
	}


}