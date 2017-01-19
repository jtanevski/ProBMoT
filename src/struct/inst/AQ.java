package struct.inst;

import java.util.*;

import struct.*;

import equation.*;
import equation.Number;

public class AQ extends IQ {
	Map<String, IQ> iqs = new LinkedHashMap<String, IQ>();
	
	int iqCount;
	
	public AQ(IQ iq) {
		this.cont = null;
		this.template = null;
		
		this.type = iq.type;
		this.setLHS(iq.lhs);
		
		this.rhs = iq.rhs;
		
		this.ieVars.putAll(iq.ieVars);
		this.ieConsts.putAll(iq.ieConsts);
		this.ipConsts.putAll(iq.ipConsts);
		
		this.iqs.put(iq.cont.id, iq);
		
		this.iqCount = 1;
		
	}
	
	public AQ(IQ...iqs ) {
		this(iqs[0]);
		this.iqCount = iqs.length;
		for (int i = 1; i < iqs.length; i++) {
			this.add(iqs[i]);
		}
		if (iqCount > 1 && lhs.agg == Aggregation.AVERAGE) {
			rhs = new Slash(rhs,new Number(iqCount));
		}

	}
	
	public AQ(Collection<IQ> iqs) {
		this(iqs.iterator().next());
		this.iqCount = iqs.size();
		boolean first = true;
		for (IQ iq : iqs) {
			if (first) {
				first=false;
				continue;
			}
			this.add(iq);
		}
		if (iqCount > 1 && lhs.agg == Aggregation.AVERAGE) {
			rhs = new Slash(rhs,new Number(iqCount));
		}

	}

	public void add(IQ iq) {
		if (this.lhs != iq.lhs) {
			throw new RuntimeException("LHS must be the same in order to aggregate equations");
		}
		if (this.type != iq.type) {
			throw new RuntimeException("LHS must be the same in order to aggregate equaitons");
		}
		
		switch (this.lhs.agg) {
			case SUM:
			case AVERAGE:
				this.rhs = new Plus(this.rhs,iq.rhs);
				break;
			case PRODUCT:
				this.rhs = new Times(this.rhs,iq.rhs);
				break;
			case MAXIMUM:
				this.rhs = new Maximum(this.rhs, iq.rhs);
				break;
			case MININUM:
				this.rhs = new Minimum(this.rhs, iq.rhs);
				break;
			default:
				throw new IllegalStateException("invalid state");
		}
		
		this.ieVars.putAll(iq.ieVars);
		this.ieConsts.putAll(iq.ieConsts);
		this.ipConsts.putAll(iq.ipConsts);
		
		this.iqs.put(iq.cont.id, iq);

	}
}
