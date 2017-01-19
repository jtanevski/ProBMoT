package search;

import java.util.*;

import struct.*;
import struct.temp.*;

/**
 * An annotation for a TP tree. I.e., a state for an IP. Keeps the
 *  - initial state
 *  - current state
 *  - operations for next/previous state
 *  - number of states and so one
 *  
 *  Terminology: At each moment, RefState represents one state
 *  - initial state - the state with which it was created, usually non-terminal
 *  - first state   - the first terminal state
 *  - current state - the current terminal state
 *  
 *  - next/previous - changes the state between the first and last
 *  - if in initial state, then next changes to first state
 *  
 *  A Terminal node has only one state, and that is its initial, first, last and everything
 *  A Non-terminal node has at least two states
 *   - initial - which is not fully instantiated
 *   - at least one instantiated 
 *  
 * @author darko
 *
 */
public class RefState {
	/**
	 * An annotation of a single TP. Uses as a building block for RefState
	 * @author darko
	 *
	 */
	static class RefNode {
		private TP tp;
		
		private RefNode parent;
		private List<RefNode> children = new LinkedList<RefNode> ();
		
		private boolean onlyTopLevel;
		
		/**
		 * the child it is currently pointing to.
		 * Null can mean that
		 *  - it is terminal
		 *  - it is not pointing to any child meaning the IP state is not completely resolved
		 */
		private Integer curIndex;
		
		/**
		 * Valid terminals along this branch
		 */
		private int count;
		
		RefNode(TP tp, boolean onlyTopLevel) {
			this.tp = tp;
			this.onlyTopLevel = onlyTopLevel;
			
			for (TP subTP : tp.subs.values()) {
				RefNode childNode = new RefNode(subTP, onlyTopLevel);
				// add only children that contain valid terminals. That way the tree will consist of only valid nodes
				if (childNode.getCount() > 0) {
					this.children.add(childNode);
					childNode.parent = this;
				}
			}
			this.curIndex = null;
			this.count();
		}
		
		public TP getTP() {
			return this.tp;
		}
		
		public RefNode getParent() {
			return this.parent;
		}
		
		public List<RefNode> getChildren() {
			return this.children;
		}
		
		public Integer getCurIndex() {
			return this.curIndex;
		}
		
		public void setCurIndex(Integer curIndex) {
			this.curIndex = curIndex;
		}
		
		public boolean isTerminal() {
			return this.children.isEmpty();
		}
		
		private void count() {
			if (this.onlyTopLevel) {
				if (this.isTerminal()) {
					if (this.getTP().isTopLevel()) {
						this.count = 1;
					} else {
						this.count = 0;
					}
				} else {
					int count = 0;
					for (RefNode childNode : this.getChildren()) {
						count += childNode.getCount();
					}
					this.count = count;
				}
			} else {
				if (this.isTerminal()) {
					this.count = 1;
				} else {
					int count = 0;
					for (RefNode childNode : this.getChildren()) {
						count += childNode.getCount();
					}
					this.count = count;
				}
			}
		}
		
		public int getCount() {
			return this.count;
		}
		
		public String toString() {
			StringBuilder buf = new StringBuilder();
			
			buf.append("[ ");
			buf.append(this.tp.id + (!this.isTerminal()?" : " + this.curIndex + " ":" "));
			for (RefNode child : this.getChildren()) {
				buf.append(child.toString() + " ");
			}
			buf.append("]");
			return buf.toString();
		}
	}

	/**
	 * the complete tree is accessible trough this node
	 */
	private RefNode rootNode;
	private TP rootTP;
	
	private boolean onlyTopLevel;

	/**
	 * Null means there is no current one
	 */
	private Integer current;
	private RefNode curNode;
	
	/**
	 * This node is used by nextState/previousState for reporting the node which was the last result
	 * this is the only way this field should be used!!
	 */
	private static RefNode tempNode;

	RefState(TP tp, boolean onlyTopLevel) {
		this.rootTP = tp;
		this.onlyTopLevel = onlyTopLevel;

		this.rootNode = new RefNode(this.rootTP, this.onlyTopLevel);
		
		this.curNode = this.rootNode;
		this.current = null;
	}
	
	public TP currentState() {
		return this.curNode.getTP();
	}
	
	/**
	 * Move to initial state by moving the root node to initial state
	 * @return
	 */
	public List<RefCommand> initialState() {
		this.curNode = this.rootNode;
		this.current = null;
		
		return initialState(this.rootNode);
	}

	/**
	 * Move to first state by moving the root node to first state
	 * @return
	 */
	public List<RefCommand> firstState() {
		RefNode node = this.rootNode;
		while (!node.isTerminal()) {
			node = node.getChildren().get(0);
		}
		this.curNode = node;
		this.current = 0;
		
		return firstState(this.rootNode);
	}

	/**
	 * Move to last state by moving the root node to last state
	 * @return
	 */
	public List<RefCommand> lastState() {
		RefNode node = this.rootNode;
		while (!node.isTerminal()) {
			node = node.getChildren().get(node.getChildren().size() - 1);
		}
		this.curNode = node;
		this.current = this.getCount() - 1;
	
		return lastState(this.rootNode);
	}

	/**
	 * The whole tree has a next state if the root node has a next state
	 * @return
	 */
	public boolean hasNextState() {
		return hasNextState(this.rootNode);
	}
	
	/**
	 * Move to next state by moving the root node to next state
	 * @return
	 */
	public List<RefCommand> nextState() {
		List<RefCommand> comms = nextState(this.rootNode);
		this.curNode = tempNode;
		tempNode = null;
	
		if (this.current != null)
			this.current++;
		else
			this.current = 0;
		
		return comms;
	}

	/**
	 * The whole tree has a previous state if the root node has a previous state
	 * @return
	 */
	public boolean hasPreviousState() {
		return hasPreviousState(this.rootNode);
	}

	/**
	 * Move to previous state by moving the root node to previous state
	 * @return
	 */
	public List<RefCommand> previousState() {
		List<RefCommand> comms = previousState(this.rootNode);
		this.curNode = tempNode;
		tempNode = null;
		
		if (this.current != null)
			this.current--;
		else
			this.current = this.getCount() - 1;
		
		return comms;
	}

	/**
	 * Move the node to initial state
	 *  - If it is a terminal it is already in initial state
	 *  - If current child is null, that means it is already in initial state, do nothing
	 *  - If current child is non-null  move the current child to initial state and append an UP command. Set current child to 'null'
	 * @param node
	 * @return
	 */
	private static List<RefCommand> initialState(RefNode node) {
		tempNode = node;
		
		if (node.isTerminal()) {
			return new LinkedList<RefCommand> ();
		} else if (node.getCurIndex() == null){
			return new LinkedList<RefCommand> ();
		} else {
			RefNode child = node.getChildren().get(node.getCurIndex());
			List<RefCommand> comms = initialState(child);
			comms.add(RefCommand.newUP());
			node.setCurIndex(null);
			return comms;
		}
	}

	/**
	 * Move to first state:
	 *  - if it is a terminal, than this is the only state, do nothing
	 *  - if current index is 0, this node is in local first state, and only set the first child to first state
	 *  - if it is in non-initial state, move it to initial first
	 *  - if it has children, than set the current child to the first child
	 *   and call recursively firstState for the first child. Create a DOWN command and append the child's commands
	 * @param node
	 * @return
	 */
	private static List<RefCommand> firstState(RefNode node) {
		tempNode = node;
		
		if (node.isTerminal()) {
			return new LinkedList<RefCommand>();
		} else if (node.getCurIndex()!=null && node.getCurIndex() == 0){
			return firstState(node.getChildren().get(node.getCurIndex()));
		} else {
			List<RefCommand> toInitial;
			if (node.getCurIndex() != null) {
				toInitial = initialState(node);
			} else {
				toInitial = new LinkedList<RefCommand>();
			}
			node.setCurIndex(0);
			RefNode child = node.getChildren().get(node.getCurIndex());
			// concatenate toInital + DOWN(child) + firstState(child)
			List<RefCommand> comms = firstState(child);
			toInitial.add(RefCommand.newDown(child.getTP()));
			toInitial.addAll(comms);
			return toInitial;
		}
	}

	/**
	 * Move to last state:
	 *  - if it is a terminal, than this is the only state, do nothing
	 *  - if current index is the maximum, this node is in local last state, and only set the last child to last state
	 *  - if it is in non-initial state, move it to initial first
	 *  - if it has children, than set the current child to the the last  child
	 *   and call recursively lastState for the last child. Create a DOWN command and append the child's commands
	 * @param node
	 * @return
	 */
	private static List<RefCommand> lastState(RefNode node) {
		tempNode = node;
		
		if (node.isTerminal()) {
			return new LinkedList<RefCommand>();
		} else if (node.getCurIndex()!=null && node.getCurIndex() == node.getChildren().size() - 1){
			return lastState(node.getChildren().get(node.getCurIndex()));
		} else {
			List<RefCommand> toInitial;
			if (node.getCurIndex() != null) {
				toInitial = initialState(node);
			} else {
				toInitial = new LinkedList<RefCommand>();
			}
			node.setCurIndex(node.getChildren().size() - 1);
			RefNode child = node.getChildren().get(node.getCurIndex());
			// concatenate toInital + DOWN(child) + lastState(child)
			List<RefCommand> comms = lastState(child);
			toInitial.add(RefCommand.newDown(child.getTP()));
			toInitial.addAll(comms);
			return toInitial;
		}
	}

	/**
	 * Whether the node has a next state
	 * - if it is terminal, then no
	 * - if it is is initial then yes, the next state is the first state
	 * - if it is in some state, then if the current child has next state that is the next one
	 * - if the current child doesn't have new states, then if there are other children
	 * @param node
	 * @return
	 */
	private static boolean hasNextState(RefNode node) {
		if (node.isTerminal()) {
			return false;
		} else if (node.getCurIndex() == null) {
			return true;
		} else {
			if (hasNextState(node.getChildren().get(node.getCurIndex()))) {
				return true;
			} else {
				if (node.getCurIndex() < node.getChildren().size() - 1) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	
	/**
	 * Move the node to next state
	 *  - if it is a terminal, raise exception
	 *  - if it in initial state, move to first state
	 *  - if it is in some state, see if the current child has next state, if so, move it to next state
	 *  - if the node has a next child move to that child
	 *    * call initial state for this node
	 *    * and call first state on the next child
	 *  
	 *  -in all cases report this node in tempNode
	 * @param node
	 * @return
	 */
	private static List<RefCommand> nextState(RefNode node) {
		tempNode = node;
		
		if (node.isTerminal()) {
			throw new RuntimeException("This is the last state");
		} else if (node.getCurIndex() == null) {
			return firstState(node);
		} else {
			if (hasNextState(node.getChildren().get(node.getCurIndex()))) {
				return nextState(node.getChildren().get(node.getCurIndex()));
			} else {
				if (node.getCurIndex() < node.getChildren().size() - 1) {
					int cur = node.getCurIndex();
					List<RefCommand> toInitial = initialState(node);
					node.setCurIndex(cur + 1);
					RefNode child = node.getChildren().get(node.getCurIndex());
					// concatenate toInital + DOWN(child) + firstState(child)
					List<RefCommand> comms = firstState(child);
					toInitial.add(RefCommand.newDown(child.getTP()));
					toInitial.addAll(comms);
					return toInitial;
				} else {
					throw new RuntimeException("This is the last state");
				}
			}
		}
	}
	

	/**
	 * Whether the node has a previous state
	 * - if it is terminal, then no
	 * - if it is is initial then yes, the last state is the previous state
	 * - if it is in some state, then if the current child has previous state that is the previous one
	 * - if the current child doesn't have previous states, then if there are other previous children
	 * @param node
	 * @return
	 */
	private static boolean hasPreviousState(RefNode node) {
		if (node.isTerminal()) {
			return false;
		} else if (node.getCurIndex() == null) {
			return true;
		} else {
			if (hasPreviousState(node.getChildren().get(node.getCurIndex()))) {
				return true;
			} else {
				if (node.getCurIndex() > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	
	/**
	 * Move the node to previous state
	 *  - if it is a terminal, raise exception
	 *  - if it in initial state, move to last state
	 *  - if it is in some state, see if the current child has previous state, if so, move it to previous state
	 *  - if the node has a previous child move to that child
	 *    * call initial state for this node
	 *    * and call last state on the previous child
	 *  
	 *  -in all cases report this node in tempNode
	 * @param node
	 * @return
	 */
	private static List<RefCommand> previousState(RefNode node) {
		tempNode = node;
		
		if (node.isTerminal()) {
			throw new RuntimeException("This is the first state");
		} else if (node.getCurIndex() == null) {
			return lastState(node);
		} else {
			if (hasPreviousState(node.getChildren().get(node.getCurIndex()))) {
				return previousState(node.getChildren().get(node.getCurIndex()));
			} else {
				if (node.getCurIndex() > 0) {
					int cur = node.getCurIndex();
					List<RefCommand> toInitial = initialState(node);
					node.setCurIndex(cur - 1);
					RefNode child = node.getChildren().get(node.getCurIndex());
					// concatenate toInital + DOWN(child) + lastState(child)
					List<RefCommand> comms = lastState(child);
					toInitial.add(RefCommand.newDown(child.getTP()));
					toInitial.addAll(comms);
					return toInitial;
				} else {
					throw new RuntimeException("This is the last state");
				}
			}
		}
	}
	
	public int getCount() {
		return this.rootNode.getCount();
	}
	
	
	public String toString() {
		return this.current + " - " + this.rootNode;
	}
}

enum Direction {
	UP,
	DOWN;
}

/**
 * Represent a refinement command
 *  - UP
 *  - DOWN(TP)
 * @author darko
 *
 */
class RefCommand {
	public Direction dir;
	public TP tp;
	
	private RefCommand(Direction dir) {
		this.dir = dir;
	}
	private RefCommand(Direction dir, TP tp) {
		this.dir = dir;
		this.tp = tp;
	}

	public static RefCommand newUP() {
		return new RefCommand(Direction.UP);
	}
	public static RefCommand newDown(TP tp) {
		return new RefCommand(Direction.DOWN, tp);
	}
	
	public String toString() {
		return dir.toString() + ((dir==Direction.DOWN)?"("+tp.id+")":"");
	}
}
