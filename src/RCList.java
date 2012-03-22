
public class RCList {
	RCNode head = null; 
	
	public RCNode getHead() {
		return head; 
	}
	
	public void setHead(RCNode newHead) {
		head = newHead;
	}
	
	public void deleteNode(int index) {
		if(head.getIndex() == index) {
			head = head.getNextRCNode(); 
		} else {
			RCNode tmp = head; 
			while(tmp.getNextRCNode().getIndex() != index) {	
				tmp = tmp.getNextRCNode(); 
			} 
			tmp.setNextRCNode(tmp.getNextRCNode().getNextRCNode()); 
		}
	}
	
	//If it finds the node, it return it, if not, null. 
	public RCNode checkForNode(int index) {
		if(head == null) {
			return null; 
		} else {
			RCNode tmp = head; 
			while(tmp != null) {
				if(tmp.getIndex() == index) {
					return tmp; 
				}
				tmp = tmp.getNextRCNode(); 
			}
		}
		return null; 
	}
	
	//Returns newly added node. 
	public RCNode addNode(int index) {
		if(head == null) { //No nodes yet
			head = new RCNode(index); 
			return head; 
		} else if(head.getNextRCNode() == null) {
			if(head.getIndex() > index) { //Index Smaller
				RCNode newHead = new RCNode(index); 
				newHead.setNextRCNode(head); 
				head = newHead; 
				return newHead; 
			} else { //Index Larger
			head.setNextRCNode(new RCNode(index)); 
				return head.getNextRCNode(); 
			}
		} else if(head.getIndex() > index) {
			RCNode newNode = new RCNode(index); 
			newNode.setNextRCNode(head); 
			head = newNode; 
			return head; 
		} else {
			RCNode tmp = head; 
			while(tmp.getNextRCNode() != null) {
				if(tmp.getNextRCNode().getIndex() > index) {
					RCNode newNode = new RCNode(index); 
					newNode.setNextRCNode(tmp.getNextRCNode()); 
					tmp.setNextRCNode(newNode); 
					return newNode; 
				}
				tmp = tmp.getNextRCNode(); 
			}
			//If index is the largest in the list -- stick it at the end. 
			tmp.setNextRCNode(new RCNode(index)); 
			return tmp.getNextRCNode(); 
		}
	}
}
