package run;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.collections.map.*;
import org.apache.commons.configuration.ConfigurationException;
import org.bridj.*;
import org.bridj.Platform;

import util.*;
import xml.OutputSpec;

import com.google.common.collect.*;

import fit.*;
import fit.compile.ModelCompiler;
import fit.jvode.bridj.nvector.*;
import fit.jvode.bridj.sundials.*;
import search.ModelEnumerator;
import serialize.CSerializer;
import struct.inst.IncompleteModel;
import struct.inst.Model;
import struct.temp.Library;
import temp.ExtendedModel;
import temp.IQGraph;
import temp.IQGraph.Type;
import temp.IQNode;
import temp.SimpleWriter;
import traverse.Traverse;

//This class is used to test new features


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
	 * @throws InterruptedException 
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, RecognitionException, ConfigurationException, InterruptedException {
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
		
		Library library = Traverse.addLibrary("ssystem/SSystem.pbl");
		Model model = Traverse.addIncompleteModel("ssystem/IncompleteSSBranch2.pbm");
		
		
		
		ModelEnumerator search = new ModelEnumerator(new ExtendedModel(model.copy()));
		
		int pmin, pavg, pmax;
		pmin = Integer.MAX_VALUE; pavg = 0; pmax = 0;
		
		int count = 0;
		
		//Find reach parameters by loading the task and the proper output
		
		while(search.hasNextModel()) {
			ExtendedModel currModel = search.nextModel();
			IQGraph graph = new IQGraph(currModel.getModel());
			graph.sort(Type.DIFFERENTIAL);
			int cp = graph.unknownParameters.size();			
			if(cp < pmin) pmin=cp;
			if(cp > pmax) pmax=cp;
			pavg += cp;
			count++;
		}
		pavg /= count;
		
		System.out.println(pmin +  " " + pavg + " " + pmax + " " + count);
		//System.out.println(search.count());
//		try {
//			while(search.hasNextModel()) {
//				System.out.println(search.getCounter());
//				System.out.println(search.nextModel().getModel().allConsts);
//			}
//		} catch (ConfigurationException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		GeneticCodec ec = new GeneticCodec();
//		//ec.enumerate = false;
//		ec.encode(new ExtendedModel(model), null);
		
		//ec.enumerate = false;
		//System.out.println(ec.encode(new ExtendedModel(model), null));
		
		//System.out.println(ec.encode(new ExtendedModel(model.copy()), new OutputSpec()));
	
		
		//consistency
		
//		search = new ModelEnumerator(new ExtendedModel(model.copy()));
//		
//		try {
//			while(search.hasNextModel()) {
//				System.out.println(search.getCounter());
//				System.out.println(search.nextModel().getModel().allConsts);
//			}
//		} catch (ConfigurationException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		Number n = 5;
		
		System.out.println(n.getClass() == Integer.class);
		
	//Testing for MassBalance experiments 	
//		Library library = Traverse.addLibrary("massBalance.pbl");
//		Model model = Traverse.addModel("mqsRA.pbm");
//		IQGraph graph = new IQGraph(model);
//		System.out.println(graph.allAlgebraic.keyList());
//		System.out.println(graph.allEquations);
//		System.out.println(ModelCompiler.modelToString(model, "asdfa"));
	
	}

}
