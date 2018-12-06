package compilador.sistema.semantico;

public class Token {
	String tipo;
	String ID;
	String dato;
	String ambito;
	String idFuncion;
	String valor;
	String error;
	String parametros;
	
	private void inicializar() {
		idFuncion = "";
		valor = "";
		error = "";
		parametros = "";
	}
	
	public Token(String tipo, String ID, String dato, String ambito) {
		this.tipo = tipo;
		this.ID = ID;
		this.dato = dato;
		this.ambito = ambito;
		inicializar();
	}
	
	public String dameTipo() {
		return tipo;
	}

	public String dameID() {
		return ID;
	}

	public String dameDato() {
		return dato;
	}

	public String dameAmbito() {
		return ambito;
	}

	public String dameIDFuncion() {
		return idFuncion;
	}

	public String dameValor() {
		return valor;
	}

	public void fijaIDFuncion(String idFuncion) {
		this.idFuncion = idFuncion;
	}

	public void fijaTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void fijaValor(String valor) {
		this.valor = valor;
	}
	
	public void fijaError(String err) {
		error += err+ "\n";
	}

	public String dameError() {
		return error;
	}	
	
	public void agregarParametro(String parametro) {
		parametros += parametro;
	}
	
	public String dameParametros(){
		return parametros;
	}
}
