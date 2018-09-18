package compilador.visual;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import compilador.util.TipoSimbolo;
import compilador.util.Escribano;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.Toolkit;

public class VentanaPrincipal extends JFrame implements Escribano,TipoSimbolo{
	 private static final long serialVersionUID = 1L;
	 
	 private static final String TITULO = "Seminario de Traductores de Lenguaje 2 \"Compilador\"";
	 private int ancho;
	 
	 private JTextArea areaCompilacion;
	 private PanelInformador panelInformador;
	    
	 public VentanaPrincipal(){
	  	Dimension ventana = Toolkit.getDefaultToolkit().getScreenSize();
	   	ancho = (int)ventana.getWidth();
	    	
	    setTitle(TITULO); 
	    setSize(ancho,700);
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    PanelBotones palBotones = new PanelBotones(this);
	    panelInformador = new PanelInformador();
	    
	    areaCompilacion = new JTextArea();
	    areaCompilacion.setFont(new Font("Times New Romes",Font.PLAIN,20));
	    areaCompilacion.setBackground(new Color(182,7,70));	
	    areaCompilacion.setForeground(Color.WHITE);
	    
	    ScrollPane scrollAreaCompilacion = new ScrollPane();	    
	    scrollAreaCompilacion.add(areaCompilacion);
	    
	    add("North",palBotones);
	    add("Center",scrollAreaCompilacion);
	    add("South",panelInformador);	    
	    
	    setVisible(true);
	 }

	 @Override
	public void limpiarDatos() {		
		panelInformador.limpiarDatos();
	}
	 
	@Override
	public String dameFlujoCaracteres() {
		return areaCompilacion.getText();
	}	
	
	@Override
	public void imprimirDatosLexicos(String s) {
		panelInformador.imprimirDatosLexicos(s);
	}
	
	@Override
	public void imprimirDatosSintacticos(String s) {		
		panelInformador.imprimirDatosSintacticos(s);
	}	
}
