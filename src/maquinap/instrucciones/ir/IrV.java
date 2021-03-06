package maquinap.instrucciones.ir;

import java.util.Stack;

import maquinap.MaquinaP;
import maquinap.instrucciones.Instruccion;
import maquinap.valor.Bool;
import maquinap.valor.Valor;

public class IrV extends Instruccion {

	private int dir;

	public IrV(int dir) {
		this.dir = dir;
	}

	@Override
	public void ejecutar(MaquinaP maq) {
		Stack<Valor<?>> pila = maq.getPilaEvaluacion();

		if (pila.isEmpty())
			throw new UnsupportedOperationException(getClass().getSimpleName()
					+ " la pila de evaluación está vacía.");

		Valor<?> cima = pila.pop();

		if (cima instanceof Bool)

			if (((Bool) cima).getValor())
				maq.setContadorPrograma(dir);
			else
				maq.incrementaContadorPrograma();

		else
			throw new UnsupportedOperationException(getClass().getSimpleName()
					+ " la cima no es un bool, es " + cima.getClass());
	}
}
