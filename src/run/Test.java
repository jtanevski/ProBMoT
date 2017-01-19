package run;

import java.util.*;

import org.apache.commons.collections.map.*;
import org.bridj.*;
import org.bridj.Platform;

import util.*;

import com.google.common.collect.*;

import fit.*;
import fit.jvode.bridj.nvector.*;
import fit.jvode.bridj.sundials.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("java.library.path", CVodeSimulator.JVODE_LIB_PATH);
	

		Pointer<_generic_N_Vector> nvector = NvectorLibrary.N_VNew_Serial(5);
		Pointer<_N_VectorContent_Serial> content = nvector.get().content();
		Pointer<Double> data = content.get().data();
		
		Pointer<Double> data2 = NvectorLibrary.N_VGetArrayPointer_Serial(nvector);
		
		System.out.println(Pointer.getPeer(nvector));
		System.out.println(Pointer.getPeer(content));
		System.out.println(Pointer.getPeer(data));
		System.out.println(Pointer.getPeer(data2));
		
		//System.out.println(data.get());
		
	}

}
