package compilador.util;

public interface Escribano {
	public void limpiarDatos();
	public String dameFlujoCaracteres();
	public void imprimirDatosLexicos(String s);
	public void imprimirDatosSintacticos(String s);		
}
