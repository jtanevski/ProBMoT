package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CombinationGenerator<E> implements Iterator<List<E>>, Iterable<List<E>> {

	private final List<E> set;
	private int[] currentIdxs;
	private final int[] lastIdxs;

	public CombinationGenerator(List<E> set, int r) {
		if (r < 1 || r > set.size()) {
			throw new IllegalArgumentException("r < 1 || r > set.size()");
		}
		this.set = new ArrayList<E>(set);
		this.currentIdxs = new int[r];
		this.lastIdxs = new int[r];
		for (int i = 0; i < r; i++) {
			this.currentIdxs[i] = i;
			this.lastIdxs[i] = set.size() - r + i;
		}
	}

	public boolean hasNext() {
		return currentIdxs != null;
	}

	public Iterator<List<E>> iterator() {
		return this;
	}

	public List<E> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		List<E> currentCombination = new ArrayList<E>();
		for (int i : currentIdxs) {
			currentCombination.add(set.get(i));
		}
		setNextIndexes();
		return currentCombination;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void setNextIndexes() {
		for (int i = currentIdxs.length - 1, j = set.size() - 1; i >= 0; i--, j--) {
			if (currentIdxs[i] != j) {
				currentIdxs[i]++;
				for (int k = i + 1; k < currentIdxs.length; k++) {
					currentIdxs[k] = currentIdxs[k - 1] + 1;
				}
				return;
			}
		}
		currentIdxs = null;
	}

	public static void main(String[] args) {
		List<Character> set = Arrays.asList('A', 'B', 'C', 'D', 'E');

		for (int i = 1; i <= set.size(); i++) {
			CombinationGenerator<Character> cg = new CombinationGenerator<Character>(set, i);
			for (List<Character> combination : cg) {
				System.out.println(combination);
			}
		}
	}
}