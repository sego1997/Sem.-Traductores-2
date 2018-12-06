package compilador.sistema.sintactico;

public class Nodo {
	int corX;
	int corY;
	int cor;
	int ancho;
	int idRegla;
	private Nodo padre;
	private Nodo pri;
	private Nodo seg;
	private Nodo ter;
	private Nodo cua;
	private Nodo qui;
	private Nodo sex;		
	private String tipo;
	private int numHijos;
	private String dato;
	
	public Nodo(String dato) {
		this.dato = dato;
		pri = null;
		seg = null;
		ter = null;
		cua = null;
		qui = null;
		sex = null;
		padre = null;
		numHijos = 0;
		cor = 0;
	}
	
	public void fijaPri(Nodo primero) { 
		this.pri = primero; 
	}
	public Nodo damePri() { 
		return pri; 
	}
	
	public void fijaSeg(Nodo segundo) { 
		this.seg = segundo; 
	}
	public Nodo dameSeg() { 
		return seg; 
	}
	
	public void fijaTer(Nodo tercero) { 
		this.ter = tercero; 
	}
	public Nodo dameTer() { 
		return ter; 
	}
	
	public void fijaCua(Nodo cuarto) { 
		this.cua = cuarto; 
	}
	public Nodo dameCua() { 
		return cua; 
	}
	
	public void fijaQui(Nodo quinto) { 
		this.qui = quinto; 
	}
	public Nodo dameQui() { 
		return qui; 
	}
	
	public void fijaSex(Nodo sexto) { 
		this.sex = sexto; 
	}
	public Nodo dameSex() { 
		return sex; 
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
	public String dameDato() {
		return dato;
	}
}
