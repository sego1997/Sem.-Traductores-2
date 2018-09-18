package compilador.util;

public interface TipoSimbolo {
	static final int ES_IDENTIFICADOR = 0;
	static final int ES_DATO_ENTERO = 1;
	static final int ES_DATO_REAL = 2;
	static final int ES_DATO_CADENA = 3;
	static final int ES_TIPO = 4;
	static final int ES_SUM_O_RES = 5;
	static final int ES_MUL_O_DIV = 6;
	static final int ES_OP_RELACION = 7;
	static final int ES_OR = 8;
	static final int ES_AND = 9;
	static final int ES_NOT = 10;
	static final int ES_OP_IGUALDAD = 11;
	static final int ES_PUNTO_Y_COMA = 12;
	static final int ES_COMA = 13;
	static final int ES_ABRIR_PARENTESIS = 14;
	static final int ES_CERRAR_PARENTESIS = 15;
	static final int ES_ABRIR_CORCHETE = 16;
	static final int ES_CERRAR_CORCHETE = 17;
	static final int ES_IGUAL = 18;
	static final int ES_IF = 19;
	static final int ES_WHILE = 20;
	static final int ES_RETURN = 21;
	static final int ES_ELSE = 22;
	static final int ES_PESO = 23;
}