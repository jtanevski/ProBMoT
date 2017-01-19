package fit.jvode.bridj.sundials;
import java.nio.*;

import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;

import fit.jvode.bridj.nvector.*;

import util.*;



/**
 * <i>native declaration : include\sundials\sundials_nvector.h:102</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */

public class _generic_N_Vector extends StructObject {
	public _generic_N_Vector() {
		super();
	}
	public _generic_N_Vector(Pointer pointer) {
		super(pointer);
	}
	/// C type : void*
	@Field(0) 
	public Pointer<_N_VectorContent_Serial > content() {
		return this.io.getPointerField(this, 0);
	}
	/// C type : void*
	@Field(0) 
	public _generic_N_Vector content(Pointer<_N_VectorContent_Serial > content) {
		this.io.setPointerField(this, 0, content);
		return this;
	}
	/// C type : _generic_N_Vector_Ops*
	@Field(1) 
	public Pointer<_generic_N_Vector_Ops > ops() {
		return this.io.getPointerField(this, 1);
	}
	/// C type : _generic_N_Vector_Ops*
	@Field(1) 
	public _generic_N_Vector ops(Pointer<_generic_N_Vector_Ops > ops) {
		this.io.setPointerField(this, 1, ops);
		return this;
	}
	
	
	public static void mapToNVector(ListMap<String, Double> listmap, Pointer<_generic_N_Vector> nVector) {
		if (listmap.size() != nVector.get().content().get().length()) {
			throw new IllegalArgumentException("Cannot copy ListMap to NVector - ListMap size: " + listmap.size() +  ", NVector size: " + nVector.get().content().get().length());
		}
		Pointer<Double> buffer = nVector.get().content().get().data();
	
		for (int i = 0; i < nVector.get().content().get().length(); i++) {
			buffer.set(i, listmap.get(i));
		}
	}
	
	public static void nVectorToMap(Pointer<_generic_N_Vector> nVector, ListMap<String, Double> listmap) {
		if (listmap.size() != nVector.get().content().get().length()) {
			throw new IllegalArgumentException("Cannot copy NVector to ListMap - ListMap size: " + listmap.size() +  ", NVector size: " + nVector.get().content().get().length());
		}
		Pointer<Double> buffer = nVector.get().content().get().data();
	
		for (int i = 0; i < nVector.get().content().get().length(); i++) {
			listmap.setValue(i, buffer.get(i));
		}
	}
	
	public static void arrayToNVector(double[] array, Pointer<_generic_N_Vector> nVector) {
//		if (array.length != nVector.get().content().get().length()) {
//			throw new IllegalArgumentException("Cannot copy double[] to NVector - array size: " + array.length + ", NVector size: " + nVector.get().content().get().length());
//		}
		Pointer<Double> buffer = NvectorLibrary.N_VGetArrayPointer_Serial(nVector);
		
//		for (int i = 0; i < nVector.get().content().get().length(); i++) {
			buffer.setDoubles(array);
//		}
	}
	
	public static void nVectorToArray(Pointer<_generic_N_Vector> nVector, double[] array) {
//		if (array.length != nVector.get().content().get().length()) {
//			throw new IllegalArgumentException("Cannot copy NVector to double[] - array size: " + array.length +  ", NVector size: " + nVector.get().content().get().length());
//		}
		Pointer<Double> buffer = NvectorLibrary.N_VGetArrayPointer_Serial(nVector);
		
		double[] temp = buffer.getDoubles(array.length);
		System.arraycopy(temp, 0, array, 0, array.length);
		
//		for (int i = 0; i < nVector.get().content().get().length(); i++) {
//			array[i] = buffer.get(i);
//		}
	}
	
	
	public static void pointerToArray(Pointer pointer, double[] array) {
		double[] temp = pointer.getDoubles(array.length);
		System.arraycopy(temp, 0, array, 0, array.length);
	}
	
	public static void arrayToPointer(double[] array, Pointer pointer) {
		pointer.setDoubles(array);
	}

}
