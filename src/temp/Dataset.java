package temp;

import java.io.*;
import java.util.*;

import javax.management.*;

import org.apache.commons.lang3.*;
import org.slf4j.*;

import task.*;
import xml.*;
import xml.TaskSpec.*;

import com.Ostermiller.util.*;

public class Dataset {
	public static final Logger logger = LoggerFactory.getLogger(Dataset.class);

	private String filepath;
	private String separator;
	private int id;

	private CSVParser parser;

	private String[] colNames;
	private Map<String, Integer> colIndex = new LinkedHashMap<String, Integer>();


	/**
	 * dataset stored as strings by rows (for printing)
	 */
	private String[][] stringData;

	/**
	 * dataset stored as doubles by columns (for computation)
	 */
	private Double[][] doubleData;

	private int nRows;
	private int nCols;

	// public Map<String, Integer> varIndex = new LinkedHashMap<String, Integer>();
	private void readDataset(InputStream input, String separator)
			throws IOException {

		this.separator = separator;
		this.parser = new CSVParser(input, separator.charAt(0));

		String[][] allData = parser.getAllValues();

		this.colNames = allData[0];
		for (int i = 0; i < colNames.length; i++) {
			colIndex.put(colNames[i], i);
		}

		this.nRows = allData.length - 1;
		this.nCols = this.colNames.length;

		this.stringData = new String[this.getNRows()][this.getNCols()];
		this.doubleData = new Double[this.getNCols()][this.getNRows()];

		for (int col = 0; col < this.getNCols(); col++) {
			for (int row = 0; row < this.getNRows(); row++) {
				String s;
				try {
					s = allData[row + 1][col];
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new IllegalArgumentException("Dataset row " + (row + 1) + " contains " + allData[row + 1].length + " elements. Should contain " + this.getNCols());
				}
				this.stringData[row][col] = s;
				try {
					this.doubleData[col][row] = Double.valueOf(s);
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Dataset contains invalid character '" + s + "' in row=" + (row +1) + ", column=" + col);
				}
			}
		}

		logger.info("Dataset '" + getFilepath() + "' read successfully -");
	}

	public void writeDataset(OutputStream output, String separator, String[] header)
			throws IOException {
		CSVPrinter printer = new CSVPrinter(output);
		printer.changeDelimiter(separator.charAt(0));

		printer.writeln(header);
		printer.writeln(stringData);

		printer.close();
	}

	public void writeDataset(OutputStream output, String separator)
			throws IOException {
		writeDataset(output, separator, this.colNames);
	}

	public void writeDataset(OutputStream output)
			throws IOException {
		this.writeDataset(output, this.separator);
	}

	public String toString() {
		OutputStream output = new ByteArrayOutputStream();
		try {
			this.writeDataset(output);
		} catch (IOException ex) {
			logger.error("Error with ByteArrayOutputStream", ex);
			throw new RuntimeException(ex);
		}
		return output.toString();
	}

	/**
	 *
	 * @param data
	 *           - data by columns
	 * @param colNames
	 */
	public Dataset(Double[][] data, String[] colNames) {
		this.separator = " ";

		this.colNames = colNames;
		for (int i = 0; i < colNames.length; i++) {
			colIndex.put(colNames[i], i);
		}

		this.doubleData = matrixCopy(data);

		this.nRows = this.doubleData[0].length;
		this.nCols = this.colNames.length;

		this.stringData = new String[this.getNRows()][this.getNCols()];
		for (int row = 0; row < this.getNRows(); row++) {
			for (int col = 0; col < this.getNCols(); col++) {
				this.stringData[row][col] = Double.toString(this.doubleData[col][row]);
			}
		}
	}

	public Dataset(InputStream input, String separator)
			throws IOException {
		readDataset(input, separator);
	}

	public Dataset(String filepath, String separator ,int id)
			throws IOException {
		this.setFilepath(filepath);
		this.id=id;

		readDataset(new FileInputStream(filepath), separator);
	}
	
	public Dataset(String filepath, String separator )
			throws IOException {
		this.setFilepath(filepath);


		readDataset(new FileInputStream(filepath), separator);
	}

	public Dataset(String filepath)
			throws IOException {
		this(filepath, ",");
	}

	public Dataset(DatasetSpec ds)
			throws IOException {
		this(ds.datasetFilepath, ds.separator, ds.id);
	}


	public String[] getNames() {
		return this.colNames;
	}

	public int getNCols() {
		return this.nCols;
	}

	public int getNRows() {
		return this.nRows;
	}

	public Integer getColIndex(String colName) {
//		if (!this.colIndex.containsKey(colName)) {
//			throw new IllegalArgumentException("Dataset '" + this.filepath + "' does not contain column '" + colName + "'");
//		}
		return this.colIndex.get(colName);
	}

	public Double[] getCol(int col) {
		return this.doubleData[col];
	}

	public Double getElem(int row, int col) {
		return this.doubleData[col][row];
	}

	public Double getElem(int row, String col) {
		return this.getElem(row,this.getColIndex(col));
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilepath() {
		return filepath;
	}

	public String getSeparator() {
		return this.separator;
	}
	
	public int getId ()
	{
		return this.id;
	}

	public void setId (int id)
	{
		this.id=id;
	}
	private Double[][] matrixCopy(Double[][] original) {
		Double[][] newDouble = new Double[original.length][];
		for (int i = 0; i < original.length; i++) {
			Double[] aRow = original[i];
			newDouble[i] = new Double[aRow.length];
			System.arraycopy(aRow, 0, newDouble[i], 0, aRow.length);
		}
		return newDouble;
	}

}
