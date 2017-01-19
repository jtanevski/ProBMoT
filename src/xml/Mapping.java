package xml;

import javax.xml.bind.annotation.*;

public class Mapping {
		@XmlAttribute
		public String name;

		@XmlAttribute
		public String col;

		@XmlAttribute
		public String weight;

		private Mapping() {}

		public Mapping(String name, String col) {
			this(name, col, null);
		}

		public Mapping(String name, String col, String weight) {
			this.name = name;
			this.col = col;
			this.weight = weight;
		}

		public String toString() {
			return "(" + this.name + ", " + this.col + (this.weight!=null?("±"+this.weight):"") + ")";
		}
}