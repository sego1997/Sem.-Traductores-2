package compilador.visual;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.GridLayout;
import java.awt.ScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PanelInformador extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextArea infoLexico;
	private JTextArea infoSintactico;
	
	public PanelInformador(){
		setLayout(new BorderLayout());
		
		Font tipoLetras = new Font("Times New Romes",Font.PLAIN,20);
		infoLexico = new JTextArea();
		infoSintactico = new JTextArea();
		ScrollPane scrlLexico = new ScrollPane();
		ScrollPane scrlSintactico = new ScrollPane();
		
		infoLexico.setEditable(false);
		infoLexico.setFont(tipoLetras);
		infoLexico.setBackground(new Color(220,29,98));
		infoLexico.setForeground(Color.WHITE);
		
		infoSintactico.setEditable(false);
		infoSintactico.setFont(tipoLetras);
		infoSintactico.setBackground(new Color(220,29,98));
		infoSintactico.setForeground(Color.WHITE);
				
		scrlLexico.add(infoLexico);
		scrlLexico.setPreferredSize(new Dimension(350,350));
	    scrlSintactico.add(infoSintactico);
	    scrlSintactico.setPreferredSize(new Dimension(1000,350));
		
		add("West",scrlLexico);
		add("East",scrlSintactico);	
	}
	
	public void imprimirDatosLexicos(String s){
		infoLexico.append(s);
	}		
	public void imprimirDatosSintacticos(String s){
		infoSintactico.append(s);
	}	
	public void limpiarDatos(){
		infoLexico.setText("");
		infoSintactico.setText("");
	}
}
