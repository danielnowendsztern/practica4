package traductor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.expresiones.Expresion;
import modelo.expresiones.ExpresionBinaria;
import modelo.expresiones.ExpresionBoolean;
import modelo.expresiones.ExpresionDesignador;
import modelo.expresiones.ExpresionDouble;
import modelo.expresiones.ExpresionInteger;
import modelo.expresiones.ExpresionUnaria;
import modelo.expresiones.TipoExpresion;
import modelo.instrucciones.Asignacion;
import modelo.instrucciones.Bloque;
import modelo.instrucciones.Bucle;
import modelo.instrucciones.Caso;
import modelo.instrucciones.Condicional;
import modelo.instrucciones.DecSubprograma;
import modelo.instrucciones.DecTipo;
import modelo.instrucciones.DecVariable;
import modelo.instrucciones.Delete;
import modelo.instrucciones.Designador;
import modelo.instrucciones.Instruccion;
import modelo.instrucciones.Llamada;
import modelo.instrucciones.New;
import modelo.instrucciones.Parametro;
import modelo.instrucciones.Programa;
import modelo.instrucciones.Read;
import modelo.instrucciones.TiposInstruccion;
import modelo.instrucciones.Write;
import modelo.tipos.Tipo;
import modelo.tipos.Tipos;

public class Chequeo {

	private Map<Object, Object> vinculos;
	private Map<Object, Tipos> tiposSimples;

	public Chequeo(Map<Object, Object> vinculos) {
		this.vinculos = vinculos;		
		this.tiposSimples = new HashMap<Object, Tipos>();
	}
	
	public Tipos getTipoSimple(Object object){
		return tiposSimples.get(object);
	}
	
	public void insertaTipo(Object obj, Tipos tipo){
		this.tiposSimples.put(obj, tipo);
	}
	
	private boolean compatibles(Tipos tipoA, Tipos tipoB) {
		return tipoA == tipoB;
	}
	
	private boolean esTipoPresentable(Tipos tipoA) {
		return tipoA != Tipos.ARRAY || tipoA != Tipos.POINTER || tipoA != Tipos.STRUCT;
	}

	private boolean esTipoLegible(Tipos tipoA) {
		return tipoA == Tipos.IDENT;
	}

	public void chequea(Programa p) {
		chequeaTipos(p.getDecTipos());
		chequeaVars(p.getDecVariables());
		chequeaSubs(p.getDecSubprogramas());
		
		for (DecTipo d : p.getDecTipos()){
			simplificaDefTipos(d);
		}
		
		for (DecVariable d : p.getDecVariables()){
			simplificaDefTipos(d);					
		}
		
		for (DecSubprograma d : p.getDecSubprogramas()){
			simplificaDefTipos(d);			
		}
		
		chequea(p.getBloque());		
	}

	private void simplificaDefTipos(DecSubprograma d) {
		List<Parametro> params = d.getParametros();
		for (Parametro p : params){
			simplificaDefTipo(p);
		}
		d.getPrograma();
	}

	private void simplificaDefTipo(Parametro p) {
		
		
	}

	private void simplificaDefTipos(DecVariable d) {
		
	}

	private void simplificaDefTipos(DecTipo d) {
		
	}

	private void chequeaTipos(List<DecTipo> decTipos) {
		for (DecTipo d : decTipos){
			chequea(d);
		}		
	}

	private void chequea(DecTipo d) {
		chequea(d.getTipo());
	}

	private void chequeaVars(List<DecVariable> decVariables) {
		for (DecVariable d : decVariables){
			chequea(d);
		}		
	}

	private void chequea(DecVariable d) {
		chequea(d.getTipo());
		
	}	
	

	private void chequea(Tipo tipo) {
		switch(tipo.getTipoConcreto()){
		case ARRAY:
			break;
		case BOOL:
			break;
		case DOUBLE:
			break;
		case IDENT:			
			break;
		case INT:
			break;
		case POINTER:
			break;
		case STRUCT:
			break;
		default:
			break;
		}
		
	}

	private void chequeaSubs(List<DecSubprograma> decSubprogramas) {
		for (DecSubprograma d : decSubprogramas){
			chequea(d);
		}		
	}

	private void chequea(DecSubprograma d) {
		d.getParametros();
		d.getPrograma();
	}

	private void chequea(Bloque bloque) {
		for (Instruccion i : bloque.getInstrucciones()){
			chequea(i);
		}		
	}

	@SuppressWarnings("unchecked")
	private void chequea(Instruccion i) {
		if (i == null) return;
		TiposInstruccion tipo = i.getTipoInstruccion();
		switch(tipo){
			case ASIG: {				
				chequea((Asignacion) i);
			};  break;
			case BLOQUE: {			
				chequea((Bloque) i);
			}; break;
			case BUCLE: {
				chequea((Bucle) i);
			}; break;
			case CASOS: {
				chequeaCasos((List<Caso>) i);
			}; break;
			case DELETE: {
				chequea((Delete) i);
			}; break;
			case IF: {
				chequea((Condicional) i);
			}; break;
			case LLAMADA: {
				chequea((Llamada) i);
			}; break;
			case NEW: {
				chequea((New) i);
			}; break;
			case READ: {
				chequea((Read) i);
			}; break;
			case WRITE: {
				chequea((Write) i);
			}; break;
			default: break;
		}		
	}

	private void chequea(Write i) {
		chequea(i.getExpresion());
		Tipos tipoA = getTipoSimple(i.getExpresion());
		if (tipoA == null || !esTipoPresentable(tipoA)){
			throw new UnsupportedOperationException("No es posible escribir valores de ese tipo.");			
		}
	}

	private void chequea(Read i) {
		chequea(i.getDesignador());
		Tipos tipoA = getTipoSimple(i.getDesignador());
		if (tipoA == null || !esTipoLegible(tipoA)){
			throw new UnsupportedOperationException("No es posible escribir valores de ese tipo.");			
		}
	}

	private void chequea(New i) {
		chequea(i.getDesignador());
		Tipos tipoA = getTipoSimple(i.getDesignador());
		if (tipoA == null || Tipos.POINTER != tipoA){
			throw new UnsupportedOperationException("No es de tipo pointer.");			
		}
	}

	private void chequea(Delete i) {
		chequea(i.getDesignador());
		Tipos tipoA = getTipoSimple(i.getDesignador());
		if (tipoA == null || Tipos.POINTER != tipoA){
			throw new UnsupportedOperationException("No es de tipo pointer.");			
		}
	}

	private void chequea(Llamada i) {
		
	}

	private void chequea(Condicional i) {
		
	}

	private void chequea(Bucle i) {
		
	}

	private void chequea(Asignacion i) {
		chequea(i.getExpresion());
		chequea(i.getDesignador());
		Tipos tipoA = getTipoSimple(i.getExpresion());
		Tipos tipoB = getTipoSimple(i.getDesignador());
		if (tipoA == null || tipoB == null || !compatibles(tipoA, tipoB)){
			throw new UnsupportedOperationException("Incompatibilidad de tipos en asignaci�n.");
		}
	}

	private void chequeaCasos(List<Caso> i) {
		for (Caso c : i){
			chequea(c);
		}		
	}

	private void chequea(Designador designador) {
		if (designador == null) return;
		
		Expresion e = designador.getExpresion();
		Designador d = designador.getDesignador();
		String id = designador.getIdentificador();
		
		switch(designador.getTipo()){
			case ARRAY: {
				chequea(d);
				chequea(e);
				break;
			}
			case ID: {
				if (id.equalsIgnoreCase("null")){ break; }
				
//				Object vinculo = declaracionDe(id);
//				if (vinculo == null){
//					throw new UnsupportedOperationException("Identificador no declarado. " + id);			
//				}		
//				insertaVinculo(designador, vinculo);
				
				break;
			}
			case STRUCT: {	
				chequea(d);
				break;				
			}
			case PUNTERO: {
				chequea(d);
				break;
			}
		default: break;
		}				
	}

	private void chequea(Expresion expresion) {
		if (expresion == null) return;
		TipoExpresion te = expresion.getTipoExpresion();
		switch(te){
			case BINARIA: {
				chequea((ExpresionBinaria)expresion);
			} break;
			case BOOLEAN: {
				chequea((ExpresionBoolean)expresion);				
			} break;
			case DESIGNADOR: {
				chequea((ExpresionDesignador)expresion);					
			} break;
			case DOUBLE: {
				chequea((ExpresionDouble)expresion);
			} break;
			case INTEGER: {
				chequea((ExpresionInteger)expresion);
			} break;
			case UNARIA: {
				chequea((ExpresionUnaria)expresion);
			} break;
			default: break;		
		}				
	}
	
	
	private void chequea(ExpresionInteger expresion) { }

	private void chequea(ExpresionDouble expresion) { }

	private void chequea(ExpresionBoolean expresion) { }
	
	private void chequea(ExpresionDesignador expresion) {
		chequea(expresion.getValor());
	}

	private void chequea(ExpresionUnaria expresion) {
		chequea(expresion.getExp());
	}

	private void chequea(ExpresionBinaria expresion) {
		chequea(expresion.getExp0());
		chequea(expresion.getExp1());	
	}

}
