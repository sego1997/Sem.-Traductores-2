package compilador.visual;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import compilador.sistema.Compilador;
import compilador.util.Escribano;

public class PanelBotones extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final String ETIQUETA_BTN_COMPILAR = "Compilar";
	private static final String RUTA_IMG_COM = "/compilador/imagenes/compilar.png";	
	private Escribano escribano;	
	private JLabel btnCompilar;
	private Image imgCompilar;

	public PanelBotones(Escribano escribano) {		
		this.escribano = escribano;
		setLayout(new GridLayout());
		setBackground(new Color(105,5,41));		
		EventosClick eventosClick = new EventosClick();                
        btnCompilar = new JLabel(ETIQUETA_BTN_COMPILAR);
        btnCompilar.setIcon(cargarImagen(ETIQUETA_BTN_COMPILAR,imgCompilar));
        btnCompilar.setForeground(Color.WHITE);
        btnCompilar.setName(ETIQUETA_BTN_COMPILAR);        
        btnCompilar.addMouseListener(eventosClick);                
        add(btnCompilar);
	}
	
	private ImageIcon cargarImagen(String etiqueta, Image img) {
		Image nuevaImg = null;
		switch(etiqueta) {
			case ETIQUETA_BTN_COMPILAR: 
				nuevaImg = imgCompilar = new ImageIcon(VentanaPrincipal.class.getResource(RUTA_IMG_COM)).getImage();
			break;
		}		
        return new ImageIcon(nuevaImg);
	}
	
	private class EventosClick extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent raton) {
			String compClickeado = raton.getComponent().getName();
			switch(compClickeado) {
			case ETIQUETA_BTN_COMPILAR:
				Compilador compilador = new Compilador(escribano);
				compilador.inicializar();								
			break;
			}
		}					
	}
}
