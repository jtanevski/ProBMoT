package fit.jvode.bridj.nvector;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPRuntime;

import fit.jvode.bridj.sundials.*;
/**
 * Wrapper for library <b>nvector</b><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("sundials_nvecserial") 
@Runtime(CPPRuntime.class) 
public class NvectorLibrary {
	static {
		BridJ.register();
	}
// Parallel methods commented
//	public static native Pointer<_generic_N_Vector > N_VNew_Parallel(NvectorLibrary.MPI_Comm comm, int local_length, int global_length);
//	public static native Pointer<_generic_N_Vector > N_VNewEmpty_Parallel(NvectorLibrary.MPI_Comm comm, int local_length, int global_length);
//	public static native Pointer<_generic_N_Vector > N_VMake_Parallel(NvectorLibrary.MPI_Comm comm, int local_length, int global_length, Pointer<Double > v_data);
//	public static native Pointer<Pointer<_generic_N_Vector > > N_VCloneVectorArray_Parallel(int count, Pointer<_generic_N_Vector > w);
//	public static native Pointer<Pointer<_generic_N_Vector > > N_VCloneVectorArrayEmpty_Parallel(int count, Pointer<_generic_N_Vector > w);
//	public static native void N_VDestroyVectorArray_Parallel(Pointer<Pointer<_generic_N_Vector > > vs, int count);
//	public static native void N_VPrint_Parallel(Pointer<_generic_N_Vector > v);
//	public static native Pointer<_generic_N_Vector > N_VCloneEmpty_Parallel(Pointer<_generic_N_Vector > w);
//	public static native Pointer<_generic_N_Vector > N_VClone_Parallel(Pointer<_generic_N_Vector > w);
//	public static native void N_VDestroy_Parallel(Pointer<_generic_N_Vector > v);
//	public static native void N_VSpace_Parallel(Pointer<_generic_N_Vector > v, Pointer<Integer > lrw, Pointer<Integer > liw);
//	public static native Pointer<Double > N_VGetArrayPointer_Parallel(Pointer<_generic_N_Vector > v);
//	public static native void N_VSetArrayPointer_Parallel(Pointer<Double > v_data, Pointer<_generic_N_Vector > v);
//	public static native void N_VLinearSum_Parallel(double a, Pointer<_generic_N_Vector > x, double b, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
//	public static native void N_VConst_Parallel(double c, Pointer<_generic_N_Vector > z);
//	public static native void N_VProd_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
//	public static native void N_VDiv_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
//	public static native void N_VScale_Parallel(double c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
//	public static native void N_VAbs_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
//	public static native void N_VInv_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
//	public static native void N_VAddConst_Parallel(Pointer<_generic_N_Vector > x, double b, Pointer<_generic_N_Vector > z);
//	public static native double N_VDotProd_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y);
//	public static native double N_VMaxNorm_Parallel(Pointer<_generic_N_Vector > x);
//	public static native double N_VWrmsNorm_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w);
//	public static native double N_VWrmsNormMask_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w, Pointer<_generic_N_Vector > id);
//	public static native double N_VMin_Parallel(Pointer<_generic_N_Vector > x);
//	public static native double N_VWL2Norm_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w);
//	public static native double N_VL1Norm_Parallel(Pointer<_generic_N_Vector > x);
//	public static native void N_VCompare_Parallel(double c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
//	public static native int N_VInvTest_Parallel(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
//	public static native int N_VConstrMask_Parallel(Pointer<_generic_N_Vector > c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > m);
//	public static native double N_VMinQuotient_Parallel(Pointer<_generic_N_Vector > num, Pointer<_generic_N_Vector > denom);

	public static native Pointer<_generic_N_Vector > N_VNew_Serial(int vec_length);
	public static native Pointer<_generic_N_Vector > N_VNewEmpty_Serial(int vec_length);
	public static native Pointer<_generic_N_Vector > N_VMake_Serial(int vec_length, Pointer<Double > v_data);
	public static native Pointer<Pointer<_generic_N_Vector > > N_VCloneVectorArray_Serial(int count, Pointer<_generic_N_Vector > w);
	public static native Pointer<Pointer<_generic_N_Vector > > N_VCloneVectorArrayEmpty_Serial(int count, Pointer<_generic_N_Vector > w);
	public static native void N_VDestroyVectorArray_Serial(Pointer<Pointer<_generic_N_Vector > > vs, int count);
	public static native void N_VPrint_Serial(Pointer<_generic_N_Vector > v);
	public static native Pointer<_generic_N_Vector > N_VCloneEmpty_Serial(Pointer<_generic_N_Vector > w);
	public static native Pointer<_generic_N_Vector > N_VClone_Serial(Pointer<_generic_N_Vector > w);
	public static native void N_VDestroy_Serial(Pointer<_generic_N_Vector > v);
	public static native void N_VSpace_Serial(Pointer<_generic_N_Vector > v, Pointer<Integer > lrw, Pointer<Integer > liw);
	public static native Pointer<Double > N_VGetArrayPointer_Serial(Pointer<_generic_N_Vector > v);
	public static native void N_VSetArrayPointer_Serial(Pointer<Double > v_data, Pointer<_generic_N_Vector > v);
	public static native void N_VLinearSum_Serial(double a, Pointer<_generic_N_Vector > x, double b, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
	public static native void N_VConst_Serial(double c, Pointer<_generic_N_Vector > z);
	public static native void N_VProd_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
	public static native void N_VDiv_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y, Pointer<_generic_N_Vector > z);
	public static native void N_VScale_Serial(double c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
	public static native void N_VAbs_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
	public static native void N_VInv_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
	public static native void N_VAddConst_Serial(Pointer<_generic_N_Vector > x, double b, Pointer<_generic_N_Vector > z);
	public static native double N_VDotProd_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > y);
	public static native double N_VMaxNorm_Serial(Pointer<_generic_N_Vector > x);
	public static native double N_VWrmsNorm_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w);
	public static native double N_VWrmsNormMask_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w, Pointer<_generic_N_Vector > id);
	public static native double N_VMin_Serial(Pointer<_generic_N_Vector > x);
	public static native double N_VWL2Norm_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > w);
	public static native double N_VL1Norm_Serial(Pointer<_generic_N_Vector > x);
	public static native void N_VCompare_Serial(double c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
	public static native int N_VInvTest_Serial(Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > z);
	public static native int N_VConstrMask_Serial(Pointer<_generic_N_Vector > c, Pointer<_generic_N_Vector > x, Pointer<_generic_N_Vector > m);
	public static native double N_VMinQuotient_Serial(Pointer<_generic_N_Vector > num, Pointer<_generic_N_Vector > denom);

	/// Undefined type
	public static interface MPI_Comm {
		
	};
}
