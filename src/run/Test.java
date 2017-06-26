package run;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.collections.map.*;
import org.bridj.*;
import org.bridj.Platform;

import util.*;

import com.google.common.collect.*;

import fit.*;
import fit.compile.ModelCompiler;
import fit.jvode.bridj.nvector.*;
import fit.jvode.bridj.sundials.*;
import serialize.CSerializer;
import struct.inst.Model;
import struct.temp.Library;
import temp.IQGraph;
import temp.IQGraph.Type;
import temp.IQNode;
import temp.SimpleWriter;
import traverse.Traverse;

public class Test {

	/**
	 * @param args
	 * @throws RecognitionException 
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, RecognitionException {
//		System.setProperty("java.library.path", CVodeSimulator.JVODE_LIB_PATH);
//	
//
//		Pointer<_generic_N_Vector> nvector = NvectorLibrary.N_VNew_Serial(5);
//		Pointer<_N_VectorContent_Serial> content = nvector.get().content();
//		Pointer<Double> data = content.get().data();
//		
//		Pointer<Double> data2 = NvectorLibrary.N_VGetArrayPointer_Serial(nvector);
//		
//		System.out.println(Pointer.getPeer(nvector));
//		System.out.println(Pointer.getPeer(content));
//		System.out.println(Pointer.getPeer(data));
//		System.out.println(Pointer.getPeer(data2));
//		
		//System.out.println(data.get());
		
		Library library = Traverse.addLibrary("massBalance.pbl");
		Model model = Traverse.addModel("mqsRA.pbm");
		IQGraph graph = new IQGraph(model);
		System.out.println(graph.allAlgebraic.keyList());
		System.out.println(graph.allEquations);
		System.out.println(ModelCompiler.modelToString(model, "asdfa"));
	
	}

}
