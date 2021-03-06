package fit.jvode.bridj.sundials;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : include\sundials\sundials_spgmr.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */

public class SpgmrMemRec extends StructObject {
	public SpgmrMemRec() {
		super();
	}
	public SpgmrMemRec(Pointer pointer) {
		super(pointer);
	}
	@Field(0) 
	public int l_max() {
		return this.io.getIntField(this, 0);
	}
	@Field(0) 
	public SpgmrMemRec l_max(int l_max) {
		this.io.setIntField(this, 0, l_max);
		return this;
	}
	/// C type : N_Vector*
	@Field(1) 
	public Pointer<Pointer<_generic_N_Vector > > V() {
		return this.io.getPointerField(this, 1);
	}
	/// C type : N_Vector*
	@Field(1) 
	public SpgmrMemRec V(Pointer<Pointer<_generic_N_Vector > > V) {
		this.io.setPointerField(this, 1, V);
		return this;
	}
	/// C type : realtype**
	@Field(2) 
	public Pointer<Pointer<Double > > Hes() {
		return this.io.getPointerField(this, 2);
	}
	/// C type : realtype**
	@Field(2) 
	public SpgmrMemRec Hes(Pointer<Pointer<Double > > Hes) {
		this.io.setPointerField(this, 2, Hes);
		return this;
	}
	/// C type : realtype*
	@Field(3) 
	public Pointer<Double > givens() {
		return this.io.getPointerField(this, 3);
	}
	/// C type : realtype*
	@Field(3) 
	public SpgmrMemRec givens(Pointer<Double > givens) {
		this.io.setPointerField(this, 3, givens);
		return this;
	}
	/// C type : N_Vector
	@Field(4) 
	public Pointer<_generic_N_Vector > xcor() {
		return this.io.getPointerField(this, 4);
	}
	/// C type : N_Vector
	@Field(4) 
	public SpgmrMemRec xcor(Pointer<_generic_N_Vector > xcor) {
		this.io.setPointerField(this, 4, xcor);
		return this;
	}
	/// C type : realtype*
	@Field(5) 
	public Pointer<Double > yg() {
		return this.io.getPointerField(this, 5);
	}
	/// C type : realtype*
	@Field(5) 
	public SpgmrMemRec yg(Pointer<Double > yg) {
		this.io.setPointerField(this, 5, yg);
		return this;
	}
	/// C type : N_Vector
	@Field(6) 
	public Pointer<_generic_N_Vector > vtemp() {
		return this.io.getPointerField(this, 6);
	}
	/// C type : N_Vector
	@Field(6) 
	public SpgmrMemRec vtemp(Pointer<_generic_N_Vector > vtemp) {
		this.io.setPointerField(this, 6, vtemp);
		return this;
	}
}
