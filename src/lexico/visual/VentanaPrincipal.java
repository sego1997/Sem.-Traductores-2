package lexico.visual;

import javax.swing.JFrame;
//import javax.swing.JTable;
import javax.swing.JTextArea;
//import javax.swing.table.DefaultTableModel;

import lexico.util.TipoSimbolo;
import lexico.util.Escribano;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.Toolkit;

public class VentanaPrincipal extends JFrame implements Escribano,TipoSimbolo{
	 private static final long serialVersionUID = 1L;
	 
	 private static final String TITULO = "Seminario de Traductores de Lenguaje 2 \"Compilador\"";
//	 private int alto;
	 private int ancho;
	 
//	 private JTable tablaSimbolos;
	 private JTextArea areaCompilacion;
	 private JTextArea areaInformador;
	    
	 public VentanaPrincipal(){
	  	Dimension ventana = Toolkit.getDefaultToolkit().getScreenSize();
//	   	alto = (int)ventana.getHeight();
	   	ancho = (int)ventana.getWidth();
	    	
	    setTitle(TITULO); 
	    setSize(ancho,700);
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    PanelBotones palBotones = new PanelBotones(this);
	    Font tipoLetras = new Font("Times New Romes",Font.PLAIN,30);
	    areaCompilacion = new JTextArea();
	    areaCompilacion.setFont(tipoLetras);
	    areaCompilacion.setBackground(new Color(182,7,70));	
	    areaCompilacion.setForeground(Color.WHITE);
	    areaInformador = new JTextArea();
	    areaInformador.setEditable(false);
	    areaInformador.setFont(tipoLetras);
	    areaInformador.setBackground(new Color(220,29,98));
	    areaInformador.setForeground(Color.WHITE);
	    ScrollPane scrollAreaInformador = new ScrollPane();
	    scrollAreaInformador.add(areaInformador);
	    scrollAreaInformador.setPreferredSize(new Dimension(1000,200));
	    ScrollPane scrollAreaCompilacion = new ScrollPane();	    
	    scrollAreaCompilacion.add(areaCompilacion);
//	    crearTablaSimbolos();
	    
	    add("North",palBotones);
//	    add("West",tablaSimbolos);
	    add("South",scrollAreaInformador);
	    add("Center",scrollAreaCompilacion);
	    
	    setVisible(true);
	 }
 /*
	 private void crearTablaSimbolos() {
		 tablaSimbolos = new JTable();
		 Object[] TITULOS = new Object[] {"Descripcion","Simbolo","Valor"};
		 Object[][] DATOS = new Object[][] {
			 {"Identificador","",ES_IDENTIFICADOR},
			 {"Entero","",ES_DATO_ENTERO},
			 {"Real","",ES_DATO_REAL},
			 {"Cadena","",ES_DATO_CADENA},
			 {"Tipo","int, float, void",ES_TIPO},
			 {"Op. Suma - Resta","+, -",ES_SUM_O_RES},
			 {"Op. Mul. - Div.","*, /",ES_MUL_O_DIV},
			 {"Op. Relacion","<, <=, >, >=",ES_OP_RELACION},
			 {"OP. OR","||",ES_OR},
			 {"OP. AND","&&",ES_AND},{"OP. NOT","!",ES_NOT}, 
			 {"OP. Igualdad","==, !=",ES_OP_IGUALDAD}, {",","",ES_PUNTO_Y_COMA},
			 {",","",ES_COMA},
			 {"(","",ES_ABRIR_PARENTESIS},
			 {"","",ES_CERRAR_PARENTESIS},
			 {"{","",ES_ABRIR_CORCHETE},
			 {"}","",ES_CERRAR_CORCHETE},
			 {"=","",ES_IGUAL},
			 {"if","",ES_IF},
			 {"while","",ES_WHILE},
			 {"return","",ES_RETURN},
			 {"else","",ES_ELSE},
			 {"$","",ES_PESO}
		 };
		 DefaultTableModel modelo = new DefaultTableModel(DATOS,TITULOS);		 
		 tablaSimbolos.setModel(modelo);
		 tablaSimbolos.setFont(new Font("Arial",Font.PLAIN,15));
	 }
*/	 
	 @Override
	 public void imprimirAreaInformador(String s) {
		 areaInformador.setText(s);
	 }

	@Override
	public JTextArea dameAreaCompilacion() {
		return areaCompilacion;
	}
}
