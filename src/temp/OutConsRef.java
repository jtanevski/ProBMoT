/**
 * 
 */
package temp;


import equation.*;


public class OutConsRef
		extends Ref {
	transient OutputCons cons;

	public OutConsRef(OutputCons cons) {
		this.cons = cons;
		this.id = cons.name;
	}
}