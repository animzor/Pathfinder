package javafiler;

import java.awt.*;

import javax.swing.*;

public class Marker extends JComponent {
	
	String namn = "";
	boolean vald = false;
	
	public Marker(int x, int y, String namn){
		this.namn = namn;
		setBounds(x, y, 100, 50);
		setMaximumSize(new Dimension(100, 50));
		setMinimumSize(new Dimension(100, 50));
		setPreferredSize(new Dimension(100, 50));
		
	}
	public void setNamn(String namn){
		this.namn = namn;
	}
	public String getNamn(){
		return namn;
	}
	
	 public void setVald(boolean b){
			vald = b;
			repaint();
		    }

		    public boolean isVald(){
			return vald;
		    }
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(vald){
			g.setColor(Color.RED);
			g.fillOval(0, 0, 25, 25);
		} else {
			g.setColor(Color.BLUE);
			g.fillOval(0, 0, 25, 25);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times", Font.BOLD, 10));
		g.drawString(namn, 0, 40);
	}
	
	public String toString(){
		return namn;
		
	}
}
