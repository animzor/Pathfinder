package javafiler;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.LineNumberInputStream;
import java.util.ArrayList;

import javafiler.graphs.Edge;



import javafiler.graphs.ListGraph;

import javax.swing.*;

public class PathFinder extends JFrame implements ActionListener {
	
	private JButton[] knappar = new JButton[5];
	private JMenuItem[] knapparMenu = new JMenuItem[10];
	private String[] knappText = {"Nytt", "Öppna", "Spara", "Spara Som", "Avsluta", "Hitta Väg", "Visa Förbindelser",
			"Ny Plats", "Ny Förbindelse", "Ändra Förbindelse"};
	private JPanel panel = new JPanel();
	private BildPanel bp = new BildPanel();
	private javafiler.graphs.ListGraph<Marker> listGraph = new javafiler.graphs.ListGraph<Marker>();
	private ArrayList<Marker> valdaMarkers = new ArrayList<Marker>();
	private int counter = 0;

	
	PathFinder(){
		
		super("PathFinder");
		
		setLayout(new BorderLayout());
		JMenuBar mBar = new JMenuBar();
		setJMenuBar(mBar);
		JMenu arkivMenu = new JMenu("Arkiv");
		mBar.add(arkivMenu);
		JMenu operationerMenu = new JMenu("Operationer");
		mBar.add(operationerMenu);
		
		for(int i = 0; i<10; i++){
			knapparMenu[i] = new JMenuItem();
			knapparMenu[i].addActionListener(this);
			knapparMenu[i].setText(knappText[i]);
		}
		for(int i = 0; i<5; i++)
			arkivMenu.add(knapparMenu[i]);
		for(int i = 5; i<10; i++)
			operationerMenu.add(knapparMenu[i]);
		for(int i = 0; i<5; i++){
			knappar[i] = new JButton();
			knappar[i].setText(knappText[i+5]);
			knappar[i].addActionListener(this);
			panel.add(knappar[i]);
			add(panel, BorderLayout.NORTH);
		}
		
		setSize(900,900);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	private class FormulärNyPlats extends JPanel {

		private JTextField fältNamn = new JTextField(12);
		private JPanel panelTop = new JPanel();
		private JPanel panelNamn = new JPanel();


		public FormulärNyPlats() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			panelTop.add(new JLabel("Platsens namn:"));
			add(panelTop);
			panelNamn.add(fältNamn);
			add(panelNamn);
		}
		public String getFältNamn() {
			return fältNamn.getText();

		}
	}
	private class FormulärNyFörbindelse extends JPanel {

		private JTextField fältNamn = new JTextField(12);
		private JTextField fältTid = new JTextField(12);
		private JPanel panelTop = new JPanel();
		private JPanel panelNamn = new JPanel();
		private JPanel panelTid = new JPanel();

		public FormulärNyFörbindelse(Marker marker1, Marker marker2) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			panelTop.add(new JLabel("Förbindelse från: " + marker1.getNamn() + " till " + marker2.getNamn()));
			add(panelTop);
			panelNamn.add(new JLabel("Namn: "));
			panelNamn.add(fältNamn);
			add(panelNamn);
			panelTid.add(new JLabel("Tid: "));
			panelTid.add(fältTid);
			add(panelTid);
		}
		public String getFältNamn() {
			return fältNamn.getText();

		}
		public void setFältNamn(String str) {
			fältNamn.setText(str);

		}
		public int getFältTid() {
			return Integer.parseInt(fältTid.getText());

		}
		public void setFältTid(int i) {
			fältTid.setText("" + i);

		}
		public void setEnabled(boolean b1, boolean b2){
			fältNamn.setEnabled(b1);
			fältTid.setEnabled(b2);
		}
	}
	private class FormulärHittaVäg extends JPanel{
		private JTextArea lista = new JTextArea();
		private JScrollPane scrollPane = new JScrollPane(lista);
		
		public FormulärHittaVäg(String str){
			setLayout(new BorderLayout());
			add(scrollPane, BorderLayout.CENTER);
			lista.setText(str);
		}
	}
	
	private class MusLyss extends MouseAdapter{
		
		FormulärNyPlats fNP = new FormulärNyPlats();
		@Override
		public void mouseClicked(MouseEvent mev){
			boolean okInput = false;
			while(!okInput){
				try{
			int svar = JOptionPane.showConfirmDialog(PathFinder.this, fNP, "Ny plats", JOptionPane.CANCEL_OPTION);
			if (svar == JOptionPane.CANCEL_OPTION) {
				JOptionPane.getRootFrame().dispose();
				 Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
				   setCursor(c);
				   bp.removeMouseListener(this);
				break;
			}
			if(fNP.getFältNamn().isEmpty()){
				throw new IllegalArgumentException();
			}
			
			   Marker nyMarker = new Marker(mev.getX(), mev.getY(),fNP.getFältNamn());
			   MarkerLyss mL = new MarkerLyss();
			   nyMarker.addMouseListener(mL);
			   bp.add(nyMarker);
			   listGraph.add(nyMarker);
			   validate();
			   repaint();
			   Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			   setCursor(c);
			   bp.removeMouseListener(this);
			   okInput = true;
			}catch(IllegalArgumentException e){
				okInput = false;
				JOptionPane.showMessageDialog(PathFinder.this,
					    "Ett fält är tomt.",
					    "Felmeddelande",
					    JOptionPane.ERROR_MESSAGE);
			}
			}
		}
	}
	
	private class MarkerLyss extends MouseAdapter{
			@Override
			public void mouseClicked(MouseEvent mev){
			    Marker m = (Marker)mev.getSource();
	
			    if(!m.vald && counter < 2){
			    	m.setVald(true);
			    	valdaMarkers.add(m);
			    	counter++;
			    } 
			    else if(m.vald){
			    	m.setVald(false);
			    	valdaMarkers.remove(m);
			    	counter--;
			    }
			    }
			    }
			
		
	public void actionPerformed(ActionEvent e) {
			
			
			if(e.getSource() == knapparMenu[0]){
				final JFileChooser fc = new JFileChooser(".");
				int returnVal = fc.showOpenDialog(this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					bp.setBild(file);
					add(bp, BorderLayout.SOUTH);
					listGraph.getNodes().clear();
					valdaMarkers.clear();
					counter = 0;
					bp.removeAll();
					bp.validate();
					bp.repaint();
					pack();
					
				}
			} else if(e.getSource() == knapparMenu[1]) {
			} else if(e.getSource() == knapparMenu[2]) {
			} else if(e.getSource() == knapparMenu[3]) {
			} else if(e.getSource() == knapparMenu[4]) {
				System.exit(0);
			} else if(e.getSource() == knapparMenu[5] || e.getSource() == knappar[0]) {
				
				int count = 0;
				for(Marker m : valdaMarkers)
					if(m.vald)
						count++;
				if(count != 2){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Två platser måste vara valda.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				} else {
					
					Marker marker1 = valdaMarkers.get(0);
					Marker marker2 = valdaMarkers.get(1);
					if(!listGraph.pathExists(marker1, marker2)){
						JOptionPane.showMessageDialog(PathFinder.this,
							    "Det finns ingen reseväg mellan dessa två platser.",
							    "Felmeddelande",
							    JOptionPane.ERROR_MESSAGE);
					} else {
						System.out.println(listGraph.Dijkstra(marker1, marker2));
						int totalVikt = 0;
						for(Edge<Marker> edge : listGraph.anyPath(marker1, marker2))
							totalVikt += edge.getVikt();
						String str = "från: " + marker1.getNamn() + " till " + marker2.getNamn() + "\n" + listGraph.anyPath(marker1, marker2) + "Totalt " +totalVikt;	
						String formattedString = str.replace("[", "").replace("]", "").replace(",", "");
						FormulärHittaVäg fHV = new FormulärHittaVäg(formattedString);
						JOptionPane.showMessageDialog(PathFinder.this, fHV);
					
					}
					
				}
				
				
				
			} else if(e.getSource() == knapparMenu[6] || e.getSource() == knappar[1]) {
				int count = 0;
				for(Marker m : valdaMarkers)
					if(m.vald)
						count++;
				if(count != 2){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Två platser måste vara valda.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				}else{
					Marker marker1 = valdaMarkers.get(0);
					Marker marker2 = valdaMarkers.get(1);
					Edge<Marker> edge = listGraph.getEdgeBetween(marker1, marker2);
					if(edge == null){
						JOptionPane.showMessageDialog(PathFinder.this,
							    "Det finns ingen förbindelse mellan dessa två platser.",
							    "Felmeddelande",
							    JOptionPane.ERROR_MESSAGE);
					} else {
					FormulärNyFörbindelse fNF = new FormulärNyFörbindelse(marker1, marker2);
					fNF.setFältNamn(edge.getNamn());
					fNF.setFältTid(edge.getVikt());
					fNF.setEnabled(false, false);
					int svar = JOptionPane.showConfirmDialog(PathFinder.this, fNF, "Visa förbindelse", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} else if(e.getSource() == knapparMenu[7] || e.getSource() == knappar[2]) {
				if(bp.getBild() == null){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Öppna en karta först.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				} else {
				Cursor c = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
				setCursor(c);
				MusLyss m1 = new MusLyss();
				bp.addMouseListener(m1);
				}
			} else if(e.getSource() == knapparMenu[8] || e.getSource() == knappar[3]) {
				int count = 0;
				for(Marker m : valdaMarkers)
					if(m.vald)
						count++;
				if(count != 2){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Två platser måste vara valda.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				} else {
				Marker marker1 = valdaMarkers.get(0);
				Marker marker2 = valdaMarkers.get(1);
				Edge<Marker> edge = listGraph.getEdgeBetween(marker1, marker2);
				if(edge != null){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Det finns redan en förbindelse mellan dessa två platser.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				} 
				
				
				else{
					
					
				FormulärNyFörbindelse fNF = new FormulärNyFörbindelse(marker1, marker2);
				boolean okInput = false;
				while(!okInput){
				try{
					okInput = true;
				int svar = JOptionPane.showConfirmDialog(PathFinder.this, fNF, "Ny plats", JOptionPane.CANCEL_OPTION);
				if(svar == JOptionPane.CANCEL_OPTION){
					JOptionPane.getRootFrame().dispose();
				} else {
				if (fNF.getFältNamn().isEmpty()) {
					throw new IllegalArgumentException();
				}
				if (fNF.getFältTid() < 1) {
					throw new NumberFormatException();
				}
				
				listGraph.connect(marker1, marker2, fNF.getFältNamn(), fNF.getFältTid());
				}
				} catch  (NumberFormatException ex){
					okInput = false;
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Skriv in ett positivt tal.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				} catch (IllegalArgumentException ex){
					okInput = false;
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Skriv in ett namn.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				}
				
				
				}
				}
				}
			} else if(e.getSource() == knapparMenu[9] || e.getSource() == knappar[4]) {
				int count = 0;
				for(Marker m : valdaMarkers)
					if(m.vald)
						count++;
				if(count != 2){
					JOptionPane.showMessageDialog(PathFinder.this,
						    "Två platser måste vara valda.",
						    "Felmeddelande",
						    JOptionPane.ERROR_MESSAGE);
				}else{
					Marker marker1 = valdaMarkers.get(0);
					Marker marker2 = valdaMarkers.get(1);
					Edge<Marker> edge = listGraph.getEdgeBetween(marker1, marker2);
					if(edge == null){
						JOptionPane.showMessageDialog(PathFinder.this,
							    "Det finns ingen förbindelse mellan dessa två platser.",
							    "Felmeddelande",
							    JOptionPane.ERROR_MESSAGE);
					} else {
					FormulärNyFörbindelse fNF = new FormulärNyFörbindelse(marker1, marker2);
					boolean okInput = false;
					while(!okInput){
					try{
						okInput = true;
					fNF.setFältNamn(edge.getNamn());
					fNF.setFältTid(edge.getVikt());
					fNF.setEnabled(false, true);
					int svar = JOptionPane.showConfirmDialog(PathFinder.this, fNF, "Ändra förbindelse", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if(svar == JOptionPane.CANCEL_OPTION){
						JOptionPane.getRootFrame().dispose();
					} else {
						if (fNF.getFältTid() < 1) {
							throw new NumberFormatException();
						} else {
					listGraph.setConnectionWeight(marker1, marker2, fNF.getFältTid());
					}
					}
					} catch  (NumberFormatException ex){
						okInput = false;
						JOptionPane.showMessageDialog(PathFinder.this,
							    "Skriv in ett positivt tal.",
							    "Felmeddelande",
							    JOptionPane.ERROR_MESSAGE);
					} catch (IllegalArgumentException ex){
						okInput = false;
						JOptionPane.showMessageDialog(PathFinder.this,
							    "Skriv in ett namn.",
							    "Felmeddelande",
							    JOptionPane.ERROR_MESSAGE);
					}
					}
				
				}
			}
			}
			
		}
	
	public static void main(String[] args) {
		
		new PathFinder();
	}

}
