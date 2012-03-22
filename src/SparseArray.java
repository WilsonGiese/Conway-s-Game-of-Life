
public class SparseArray {
	private final Object DEFAULT_VALUE; 
	private RCList x = new RCList(); 
	private RCList y = new RCList(); 
	
	public SparseArray(Object DEFAULT_VALUE) {
		this.DEFAULT_VALUE = DEFAULT_VALUE; 
	}
	
	public Object defaultValue() {
		return DEFAULT_VALUE;
	}

	public Object elementAt(int row, int col) {
		RCNode node = x.checkForNode(row); //Only checks row because if it exists it will be in a row. 
		if(node == null) {
			return DEFAULT_VALUE; 
		} else {
			MatrixElem element = checkRowElements(col, node); 
			if(element != null) {
				return element.value(); 
			}
		}
		return DEFAULT_VALUE; 
	}

	public void setValue(int row, int col, Object value) {
		RCNode rowNode = x.checkForNode(row); 
		RCNode columnNode = y.checkForNode(col); 
		
		if(value.equals(DEFAULT_VALUE)) {
			if(rowNode != null) {
				MatrixElem element = checkForExistingRowElement(row, col, rowNode); 
				if(element != null) {
					deleteRowElement(col, rowNode); 
					deleteColumnElement(row, columnNode); 
				}
			}
		} else {
			MatrixElem newElement = new MatrixElem(row, col, value);
			MatrixElem element; 
			
			if(rowNode != null) {
				element = checkForExistingRowElement(row, col, rowNode); 
				if(element != null) {
					element.setValue(value); 
				} else {
					addElementToRow(newElement, rowNode); 
				}
			} else {
				rowNode = x.addNode(row); 
				addElementToRow(newElement, rowNode); 
			}
			
			if(columnNode != null) {
				element = checkForExistingColumnElement(row, col, columnNode); 
				if(element == null) {
					addElementToColumn(newElement, columnNode); 
				}
			} else {
				columnNode = y.addNode(col); 
				addElementToColumn(newElement, columnNode); 
			}
		}
	}

	//Checks for element, if it exists it returns that element to be edited. 
	private static MatrixElem checkRowElements(int col, RCNode starterPos) {
		MatrixElem tmp = starterPos.getElement(); 
		while(tmp != null) {
			if(tmp.columnIndex() == col) {
				return tmp; 
			} else {
				tmp = tmp.getNextX(); 
			}
		}
		return null; 
	}
	
	//Creates order list. 
	private static void addElementToRow(MatrixElem newElement, RCNode node) {
		MatrixElem element = node.getElement(); 
		
		if(element == null) { 
			node.setConnect(newElement); 
		} else if(element.getNextX() == null) {
			if(element.columnIndex() > newElement.columnIndex()) {
				newElement.setNextX(element); 
				node.setConnect(newElement); 
			} else {
				element.setNextX(newElement); 
			}
		} else if(node.getElement().columnIndex() > newElement.columnIndex()) {
			newElement.setNextX(node.getElement()); 
			node.setConnect(newElement); 
		} else {
			while(element.getNextX() != null) {
				if(element.getNextX().columnIndex() > newElement.columnIndex()) {
					newElement.setNextX(element.getNextX()); 
					element.setNextX(newElement); 
					break; 
				}
				element = element.getNextX(); 
			}
			element.setNextX(newElement); 
		}
	}
	
	private static void addElementToColumn(MatrixElem newElement, RCNode node) {
		MatrixElem element = node.getElement(); 
		if(element == null) {
			node.setConnect(newElement); 
		} else if(element.getNextY() == null) {
			if(element.rowIndex() > newElement.rowIndex()) {
				newElement.setNextY(element); 
				node.setConnect(newElement); 
			} else {
				element.setNextY(newElement); 
			} 
		} else if(node.getElement().rowIndex() > newElement.rowIndex()) {
			newElement.setNextY(node.getElement()); 
			node.setConnect(newElement); 
		} else {
			while(element.getNextY() != null) {
				if(element.getNextY().rowIndex() > newElement.rowIndex()) {
					newElement.setNextY(element.getNextY()); 
					element.setNextY(newElement); 
					break;
				}
				element = element.getNextY(); 
			}
			element.setNextY(newElement); 
		}
	}
	
	private static MatrixElem checkForExistingRowElement(int x, int y, RCNode rowNode) {
		if(rowNode.getElement().columnIndex() == y) {
			return rowNode.getElement(); 
		} else {
			MatrixElem tmp = rowNode.getElement(); 
			while(tmp != null) {
				if(tmp.columnIndex() == y) {
					return tmp; 
				}
				tmp = tmp.getNextX(); 
			}
		}
		return null; 
	}
	
	private static MatrixElem checkForExistingColumnElement(int x, int y, RCNode columnNode) {
		if(columnNode.getElement().rowIndex() == x) {
			return columnNode.getElement(); 
		} else {
			MatrixElem tmp = columnNode.getElement(); 
			while(tmp != null) {
				if(tmp.rowIndex() == x) {
					return tmp; 
				}
				tmp = tmp.getNextY(); 
			}
		}
		return null; 
	}
	
	private void deleteRowElement(int col, RCNode node) {
		MatrixElem current = node.getElement(); 
		if(current.columnIndex() == col) {
			if(current.getNextX() == null) {
				x.deleteNode(node.getIndex()); 
			} else {
				node.setConnect(current.getNextX()); 
			}
		} else {
			while(current.getNextX().columnIndex() != col) {
				current = current.getNextX(); 
			}
			current.setNextX(current.getNextX().getNextX()); 
		}
	}
	
	private void deleteColumnElement(int row, RCNode node) {
		MatrixElem current = node.getElement(); 
		if(current.rowIndex() == row) {
			if(current.getNextY() == null) {
				y.deleteNode(node.getIndex()); 
			} else {
				node.setConnect(current.getNextY()); 
			}
		} else {
			while(current.getNextY().rowIndex() != row) {
				current = current.getNextY(); 
			}
			current.setNextY(current.getNextY().getNextY()); 
		}
	}
	
	public RowIterator iterateRows() {
		return new RowIterator(); 
	}

	public ColumnIterator iterateColumns() {
		return new ColumnIterator(); 
	}
	
	//Internal
	public class RowIterator extends RIterator{
		RCNode row = x.getHead(); 
		ElementIterator elemItr = new ElementIterator(true); //True, this is a rowIterator. 
			
		public ElementIterator next() {
			MatrixElem dummy = new MatrixElem(Integer.MIN_VALUE, Integer.MIN_VALUE, "DUMMY_ELEMENT"); 
			dummy.setNextX(row.getElement()); 
			elemItr = new ElementIterator(true); 
			elemItr.setElement(dummy); 
			elemItr.setNonIteratingIndex(row.getIndex()); 
			row = row.getNextRCNode(); 
			return elemItr; 
		}
			public boolean hasNext() {
			if(row != null) {
				return true; 
			}
			return false; 
		}
	}
		
	public class ColumnIterator extends CIterator{
		RCNode column = y.getHead(); 
		ElementIterator elemItr = new ElementIterator(false); //True, this is a rowIterator. 
		
		public ElementIterator next() {
			MatrixElem dummy = new MatrixElem(Integer.MIN_VALUE, Integer.MIN_VALUE, "DUMMY_ELEMENT"); 
			dummy.setNextY(column.getElement()); 
			elemItr = new ElementIterator(false); 
			elemItr.setElement(dummy); 
			elemItr.setNonIteratingIndex(column.getIndex()); 
			column = column.getNextRCNode(); 
			return elemItr; 
		}

		public boolean hasNext() {
			if(column != null) {
				return true; 
			}
			return false; 
		}
	}
	
	public class ElementIterator extends EIterator{
		private MatrixElem element = null;
		boolean isRowIterator;
		boolean firstElementThrown = false;
		int atIndex;

		public ElementIterator(boolean isRowIterator) {
			this.isRowIterator = isRowIterator;
		}

		public boolean iteratingRow() {
			return isRowIterator;
		}

		public boolean iteratingCol() {
			return !isRowIterator;
		}

		public int nonIteratingIndex() {
			return atIndex;
		}

		public MatrixElem next() {
			if (isRowIterator) {
				element = element.getNextX();
			} else {
				element = element.getNextY();
			}
			return element;
		}

		public boolean hasNext() {
			if (isRowIterator) {
				if (element.getNextX() != null) {
					return true;
				}
				return false;
			} else {
				if (element.getNextY() != null) {
					return true;
				}
				return false;
			}
		}

		protected void setElement(MatrixElem element) {
			this.element = element;
		}

		protected void setNonIteratingIndex(int index) {
			atIndex = index;
		}
	}
}