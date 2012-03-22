/* RCNode's will be used in the RCList linked list class */ 

public class RCNode {
	private int index; 
	private RCNode nextRCNode = null; 
	private MatrixElem element; 
	
	public RCNode(int index) {
		this.index = index; 
	}
	
	public RCNode getNextRCNode() {
		return nextRCNode; 
	}
	
	public MatrixElem getElement() {
		return element; 
	}
	
	public int getIndex() { 
		return index; 
	}
	
	public void setConnect(MatrixElem element) {
		this.element = element; 
	}
	
	public void setNextRCNode(RCNode nextRCNode) {
		this.nextRCNode = nextRCNode; 
	}
	
	public String toString() {
		return "" + index; 
	}
}
