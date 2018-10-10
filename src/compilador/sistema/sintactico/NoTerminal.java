package compilador.sistema.sintactico;

public class NoTerminal extends Simbolo{
	private int idRegla;
	private int numSimbolos;
	
	public NoTerminal(String dato, int idRegla, int numSimbolos) {
		super(dato);
		this.idRegla = idRegla;
		this.numSimbolos = numSimbolos;
	}
	
	public int dameIdRegla() {
		return idRegla;
	}	
	
	public int numSimbolos() {
		return numSimbolos;		
	}
}
