package compilador.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PanelInformador extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextArea informadorLexico;
	private JTextArea informadorSintactico;
	
	public PanelInformador(){
		setLayout(new GridLayout());
		
		Font tipoLetras = new Font("Times New Romes",Font.PLAIN,20);
		informadorLexico = new JTextArea();
		informadorSintactico = new JTextArea();
		ScrollPane scrlLexico = new ScrollPane();
		ScrollPane scrlSintactico = new ScrollPane();
		
		informadorLexico.setEditable(false);
		informadorLexico.setFont(tipoLetras);
		informadorLexico.setBackground(new Color(220,29,98));
		informadorLexico.setForeground(Color.WHITE);
		
		informadorSintactico.setEditable(false);
		informadorSintactico.setFont(tipoLetras);
		informadorSintactico.setBackground(new Color(220,29,98));
		informadorSintactico.setForeground(Color.WHITE);
				
		scrlLexico.add(informadorLexico);
		scrlLexico.setPreferredSize(new Dimension(1000,200));
	    scrlSintactico.add(informadorSintactico);
	    scrlSintactico.setPreferredSize(new Dimension(1000,200));
		
		add(scrlLexico);
		add(scrlSintactico);	
	}
	
	public void imprimirDatosLexicos(String s){
		informadorLexico.append(s);
	}	
	
	public void imprimirDatosSintacticos(String s){
		informadorSintactico.append(s);
	}
	
	public void limpiarDatos(){
		informadorLexico.setText("");
		informadorSintactico.setText("");
	}
}
