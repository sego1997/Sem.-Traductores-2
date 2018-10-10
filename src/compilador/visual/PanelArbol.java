package compilador.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
import compilador.sistema.sintactico.Nodo;
import compilador.sistema.sintactico.Sintactico.Arbol;

public class PanelArbol extends JPanel{
	private static final long serialVersionUID = 1L;
		
	private static final int MARGEN_X = 7;
	private static final int MARGEN_Y = 30;	
	private static final int ALTO_NODO = 50;
	private static final int TAMA_LETRA = 19;
	private int corX;
	private int corY;
	private int anchoNodo;
	private int nodoActual;
	private Nodo raiz;
	private static ArrayList<Nodo> nodos;
	
	public PanelArbol(Arbol arbol) {
		raiz = arbol.dameRaiz();
		nodos = new ArrayList<>();
		nodos.add(arbol.dameRaiz());		;
		setBackground(new Color(105,5,41));
		setPreferredSize(new Dimension(5000, 2000));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.BOLD,20));
		int defFunc=-1, bloqFunc=0, defLocales=-1, definiciones=0, otro=0;
		corX = 1000;
		corY = 0;
		nodoActual = 0;
		nodos.clear();
		nodos.add(raiz);
		Nodo nr = nodos.get(nodoActual);
		encolarHijos(nr);	
		anchoNodo = nr.dameSimbolo().dameDato().length()*TAMA_LETRA;
		g.drawOval(corX, corY, anchoNodo-45, ALTO_NODO);	
		g.drawString(nr.dameSimbolo().dameDato(), corX+MARGEN_X, corY+MARGEN_Y);
		nr.fijaCorXCorY(corX, corY);
		nr.anchoNodo(anchoNodo);
		while(nodos.size()>(nodoActual+1)){			
			nodoActual++;
			Nodo n = nodos.get(nodoActual);
			encolarHijos(n);			
			corX = n.damePadre().corX();
			corY = n.damePadre().corY()+70;
			anchoNodo = n.dameSimbolo().dameDato().length()*TAMA_LETRA;				
			
			if(anchoNodo>160) anchoNodo -= 60;
			else if(anchoNodo==20) anchoNodo += 30;							
			
			if(n.damePadre().numHijos()!=1) {
				if(n.damePadre().damePrimero()==n) {					
					if(!n.dameSimbolo().dameDato().equals("Otro")){
						corX -= (n.damePadre().numHijos()*50);
					}else corX -= (n.damePadre().numHijos()*20);
				}else {
///*					
					if(n.dameSimbolo().dameDato().equals("BloqFunc")){
						System.out.println("BloqFunc >> "+bloqFunc);
						bloqFunc++;
						corX += bloqFunc*200-(bloqFunc*100);
					}else
					if(n.dameSimbolo().dameDato().equals("Otro")){
						otro++;
						corX += otro*250;
					}else
					if(n.dameSimbolo().dameDato().equals("Definiciones")){
						definiciones++;
						System.out.println("Definiciones >> "+definiciones);
						corX += definiciones*1000-((definiciones-1)*750);
						defLocales=-1;
						defFunc=-1;
						otro=0;															
					}else
					if(n.dameSimbolo().dameDato().equals("DefLocales")){
						defLocales++;
						if(n.damePrimero()!=null) {
							if(n.damePrimero().numHijos()!=0) {
								corX += defLocales*100;
							}else defLocales++;
						}
					}
					if(n.dameSimbolo().dameDato().equals("DefFunc")){
						defFunc++;
						corX += defFunc*100;
					}				
//*/				
				}
			}else {
				if(n.damePadre().anchoNodo()<anchoNodo) {
					corX -= ((anchoNodo-n.damePadre().anchoNodo())/2);
				}				
			}			
			n.fijaCorXCorY(corX, corY);	
			n.anchoNodo(anchoNodo);
			g.drawLine(n.damePadre().cor()+(n.damePadre().anchoNodo()/2), n.damePadre().corY()+ALTO_NODO, corX+(anchoNodo/2), corY);
			n.damePadre().fijaCorXCorY(corX+anchoNodo, corY-70);
			g.drawOval(corX, corY, anchoNodo, ALTO_NODO);
			g.drawString(n.dameSimbolo().dameDato(),corX+MARGEN_X, corY+MARGEN_Y);
			cor_X = corX;
		};
		
	}
	
	private void encolarHijos(Nodo n) {
		for(int i=0,j=n.numHijos(); i<j; i++) {
			switch(i) {
				case 0: nodos.add(n.damePrimero()); break;
				case 1: nodos.add(n.dameSegundo()); break; 
				case 2: nodos.add(n.dameTercero()); break; 
				case 3: nodos.add(n.dameCuarto()); break; 
				case 4: nodos.add(n.dameQuinto()); break; 
				case 5: nodos.add(n.dameSexto()); break; 
			}
		}
	}
}
