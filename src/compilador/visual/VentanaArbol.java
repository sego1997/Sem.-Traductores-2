package compilador.visual;
import java.awt.ScrollPane;
import javax.swing.JFrame;
import compilador.sistema.sintactico.Sintactico.Arbol;
import compilador.visual.PanelArbol;

public class VentanaArbol extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final String TITULO = "Arbol sintactico";
	
	public VentanaArbol(Arbol arbol) {
		setTitle(TITULO);
		setSize(VentanaPrincipal.ancho,VentanaPrincipal.alto);		
		ScrollPane scrlPanelArbol = new ScrollPane();
		scrlPanelArbol.add(new PanelArbol(arbol));		
		add(scrlPanelArbol);		
		setVisible(true);
	}
}
