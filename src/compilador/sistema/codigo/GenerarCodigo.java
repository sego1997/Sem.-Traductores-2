package compilador.sistema.codigo;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import compilador.sistema.semantico.Token;

public class GenerarCodigo {
	private static final String LIBRERIAS="#include <iostream>\n"+
										   "#include <cstdlib>\n"+
										   "#include <cstdio>\n"+
										   "using namespace std;";										
	private static final String ENCABEZADO = "\nint main(){\n";	
	private static final String PIE = "\nreturn(0); \n}";	
	public GenerarCodigo(ArrayList<Token> tokens) {
		try {			
			try {
				String variables = "",sal="";				
				for(Token t: tokens) {
					if(t.dameDato().equals("Variable") || t.dameDato().equals("Parametro")) {
						variables += "\n"+t.dameTipo()+" "+t.dameID()+"="+t.dameValor()+";";						
					}
				}
				for(Token t: tokens) {
					if(t.dameDato().equals("Variable") || t.dameDato().equals("Parametro")) {
						sal += "\ncout<<endl<<\"\tValor de "+t.dameID()+"=\"<<"+t.dameID()+";";						
					}
				}
				FileWriter fw = new FileWriter("ejecutable.cpp");
				PrintWriter pw =  new PrintWriter(fw);
		        pw.print(LIBRERIAS+ENCABEZADO+variables+sal+PIE);
		        pw.close();		        				
		        fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}           
			PrintWriter salida = new PrintWriter("ejecutable.bat");
			salida.println("@echo off");
			salida.println("title Ejecutable");
			salida.println("cd C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2");
			salida.println("g++ -c ejecutable.cpp");
			salida.println("g++ -o ejecutable ejecutable.cpp");
			salida.println("ejecutable.exe");
			salida.println("@ECHO.");
			salida.println("@pause");
			salida.println("exit");
			salida.close();
            Runtime rt=Runtime.getRuntime();
            try {
				rt.exec("cmd /c start \"\" ejecutable.bat");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
