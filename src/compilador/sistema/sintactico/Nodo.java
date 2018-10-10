package compilador.sistema.sintactico;

public class Nodo {
	int corX;
	int corY;
	int cor;
	int ancho;
	int idRegla;
	private Nodo padre;
	private Nodo primero;
	private Nodo segundo;
	private Nodo tercero;
	private Nodo cuarto;
	private Nodo quinto;
	private Nodo sexto;		
	private String tipo;
	private int numHijos;	
	private Simbolo simbolo;	
	
	public Nodo(Simbolo simbolo) {
		this.simbolo = simbolo;
		primero = null;
		segundo = null;
		tercero = null;
		cuarto = null;
		quinto = null;
		sexto = null;
		padre = null;
		numHijos = 0;
		cor = 0;
	}
	
	public void fijaPrimero(Nodo primero) { 
		this.primero = primero; 
	}
	public Nodo damePrimero() { 
		return primero; 
	}
	
	public void fijaSegundo(Nodo segundo) { 
		this.segundo = segundo; 
	}
	public Nodo dameSegundo() { 
		return segundo; 
	}
	
	public void fijaTercero(Nodo tercero) { 
		this.tercero = tercero; 
	}
	public Nodo dameTercero() { 
		return tercero; 
	}
	
	public void fijaCuarto(Nodo cuarto) { 
		this.cuarto = cuarto; 
	}
	public Nodo dameCuarto() { 
		return cuarto; 
	}
	
	public void fijaQuinto(Nodo quinto) { 
		this.quinto = quinto; 
	}
	public Nodo dameQuinto() { 
		return quinto; 
	}
	
	public void fijaSexto(Nodo sexto) { 
		this.sexto = sexto; 
	}
	public Nodo dameSexto() { 
		return sexto; 
	}
	
	public void fijaPadre(Nodo padre) { 
		this.padre = padre; 
	}
	public Nodo damePadre() { 
		return padre; 
	}
	
	public void fijaTipo(String tipo) { 
		this.tipo = tipo; 
	}
	public String dameTipo() {
		return tipo;
	}
	
	public void fijaSimbolo(Simbolo simbolo) {
		this.simbolo = simbolo;
	}
	public Simbolo dameSimbolo() {
		return simbolo;
	}
	
	public void numHijos(int nh) {
		numHijos += nh;
	}	
	public int numHijos() {
		return numHijos;
	}
	public void fijaCorXCorY(int corX, int corY) {
		this.corX = corX;
		this.corY = corY;
		if(cor==0) cor = corX;
	}
	public int corX() {
		return corX;
	}
	public int corY() {
		return corY;
	}
	public void anchoNodo(int ancho) {
		this.ancho = ancho;
	}
	public int anchoNodo() {
		return ancho;
	}
	public int cor() {
		return cor;
	}
}
