package search.heuristic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.antlr.runtime.RecognitionException;

import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.util.PseudoRandom;
import struct.inst.Model;
import struct.temp.Library;
import temp.ExtendedModel;
import traverse.Traverse;

public class Test {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, RecognitionException {
		Library library = Traverse.addLibrary("WholeAquaticEcosystem.pbl");
		Model model = Traverse.addIncompleteModel("BledIncomplete.pbm");
		
		GeneticCodec ec = new GeneticCodec();
		ec.enumerate = false;
		System.out.println(ec.encode(new ExtendedModel(model), null));
		
		Variable[] initialState = new Variable[10];
		
		
		for(int i=0; i<10; i++) {
			initialState[i] = new Int(1,1,1);
		}
		
		System.out.println(ec.decode(initialState));

	}

}
