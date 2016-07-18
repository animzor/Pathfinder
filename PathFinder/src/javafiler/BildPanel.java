package javafiler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

class BildPanel extends JPanel{
    private ImageIcon bild = null;

    public BildPanel(){
    	setLayout(null);
    	
    }
    
    public void setBild(File file){
    	this.bild = new ImageIcon(file.getPath());
    	int h = bild.getIconHeight();
    	int w = bild.getIconWidth();
    	setPreferredSize(new Dimension(w,h));
    	setMaximumSize(new Dimension(w,h));
    	setMinimumSize(new Dimension(w,h));
    }
    public ImageIcon getBild(){
    	return this.bild;
    }

    protected void paintComponent(Graphics g){
    	super.paintComponent(g);
	g.drawImage(bild.getImage(), 0,0,getWidth(),getHeight(),this);
    }
}
