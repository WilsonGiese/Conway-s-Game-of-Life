
public class MatrixElem {
	private int row; 
	private int column; 
	private Object value; 
	private MatrixElem nextX = null;  
	private MatrixElem nextY = null; 
	
	public MatrixElem(int row, int column, Object value) {
		this.row = row; 
		this.column = column; 
		this.value = value; 
	}
	
	public int rowIndex() {
		return row;
	}

	public int columnIndex() {
		return column;
	}

	public Object value() {
		return value;
	}
	
	public void setValue(Object newValue) {
		this.value = newValue; 
	}

	public MatrixElem getNextX() {
		return nextX;
	}

	public void setNextX(MatrixElem nextX) {
		this.nextX = nextX;
	}

	public MatrixElem getNextY() {
		return nextY;
	}

	public void setNextY(MatrixElem nextY) {
		this.nextY = nextY;
	}
	
	public String toString() {
		return "(" + row + "," + column + ")" + " - " + value; 
	}
}
