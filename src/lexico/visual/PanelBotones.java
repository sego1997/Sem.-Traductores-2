package lexico.visual;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lexico.sistema.Compilador;
import lexico.util.Escribano;

public class PanelBotones extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private static final int LADO_IMG = 50;
	private static final int REDUCCION_IMG = 10;
	
	private static final String ETIQUETA_BTN_COMPILAR = "Compilar";
	private static final String RUTA_IMG_COM = "/lexico/imagenes/compilar.png";
	
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
		
		@Override
		public void mouseEntered(MouseEvent raton) {		 
			String compSelec = raton.getComponent().getName();
			switch(compSelec) {
				case ETIQUETA_BTN_COMPILAR: 
					efectoImagen(btnCompilar,imgCompilar,LADO_IMG+REDUCCION_IMG,Image.SCALE_SMOOTH); 
				break;
			}
		}
		
		@Override
		public void mouseExited(MouseEvent raton) {
			String compDeselec = raton.getComponent().getName();			
			switch(compDeselec) {
				case ETIQUETA_BTN_COMPILAR: 
					efectoImagen(btnCompilar,imgCompilar,LADO_IMG,Image.SCALE_SMOOTH);
				break;
			}
		}
		
		private void efectoImagen(JLabel btn, Image img, int ladoImg, int tipo){
			ImageIcon imgBtn = new ImageIcon(img.getScaledInstance(ladoImg, ladoImg, tipo));
			btn.setIcon(imgBtn);
			btn.repaint();
		}
	}
}
