import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Life {
	private static Object DEFAULT_VALUE = new Integer(0); 
	private SparseArray currentGen; 
	private SparseArray numNeighbors;
	private SparseArray nextGen; 

	private String inputPath; 
	private String outputPath; 
	
	//Generates initial nodes as soon as an instance of the class is made
	public Life(String inputPath, String outputPath) {
		currentGen = new SparseArray(DEFAULT_VALUE);
		numNeighbors = new SparseArray(DEFAULT_VALUE); 
		nextGen = new SparseArray(DEFAULT_VALUE);
		this.inputPath = inputPath; 
		this.outputPath = outputPath;
		addNodesFromFile(); 
	}
	
	/* This method reads in the specified input file and adds the initial
	 * nodes to the currentGen SparseArray. */
	private void addNodesFromFile() {
		try {
			File file = new File(inputPath); 
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String sourceline; 
			
			while((sourceline = reader.readLine()) != null) {
				sourceline = sourceline.replaceAll("\\s","");
				String[] coordinates = sourceline.split("\\D");
				if(coordinates.length != 2) {
					System.out.println("The file you've provided was poorly formatted. \nFormat Example: x, y");
					System.exit(1); 
				} else {
					try {
						int x = Integer.parseInt(coordinates[0]); 
						int y = Integer.parseInt(coordinates[1]); 
						currentGen.setValue(x, y, new Integer(1)); 
					} catch(NumberFormatException nfe) {
						System.out.println("Error: The input file contained non-integer values. ");
						System.exit(1); 
					}
				}
			}
		} catch (FileNotFoundException fne) {
			System.out.println("Error: Could not find specified input file. ");
		} catch (IOException ioe) {
			System.out.println("Error: An error occured while reading the input file. ");
		}
	}
	
	/* This method writes the nodes that are currently in the currentGen sparse array. 
	 * This will be called after all generations have been run. */
	private void writeResults() {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputPath))); //Write invertedIndex.txt in root. 
			RIterator r = currentGen.iterateRows();
			while (r.hasNext()) {
			   EIterator elmItr = r.next();
			   while (elmItr.hasNext()) {
			      MatrixElem me = elmItr.next();
			      writer.print(me.rowIndex() + ", " + me.columnIndex() + "\n");
			   }
			} 
			writer.close(); 
		} catch (IOException e) {
			System.out.println("An error occuring while writing the file. Do not modifiy the outputFile while the program is running. "); 
		}
	}
	
	public void run(int genNumber) {
		for(int i = 0; i < genNumber; i++) {
			buildNumNeighborArray(); 
			buildNextGen(); 
		}
		//When finished, write results to the file. 
		writeResults(); 
	}
	
	/* This method builds an array of all 8 neighbor cell locations to be used in buildNeighborArray */ 
	private coords[] buildNeighborCells(int x, int y) {
		coords[] coordinates = new coords[8];  
		int yIndex = y - 1; 
		int xIndex = x; 
		
		//Top 3 
		coordinates[0] = new coords(xIndex - 1, yIndex); 
		coordinates[1] = new coords(xIndex, yIndex); 
		coordinates[2] = new coords(xIndex + 1, yIndex);  
		
		//Middle 2
		yIndex++; 
		coordinates[3] = new coords(xIndex - 1, yIndex); 
		coordinates[4] = new coords(xIndex + 1, yIndex); 
		
		//Bottom 3
		yIndex++; 
		coordinates[5] = new coords(xIndex - 1, yIndex); 
		coordinates[6] = new coords(xIndex, yIndex); 
		coordinates[7] = new coords(xIndex + 1, yIndex); 
		
		return coordinates; 
	}
	
	/* This method counts the number of neighbors for any currently living cell and neighbors. */ 
	private void buildNumNeighborArray() { 
		RIterator r = currentGen.iterateRows(); 
		while (r.hasNext()) {
		   EIterator elmItr = r.next();
		   while (elmItr.hasNext()) {
		      MatrixElem me = elmItr.next();
		      coords[] neighbors = buildNeighborCells(me.rowIndex(), me.columnIndex());
		      //Setting live cell to its number of neighbors. 
		      numNeighbors.setValue(me.rowIndex(), me.columnIndex(), new Integer(countNeighbors(neighbors))); 
		      
		      //Checking live cells neighbors to see if they should be revived. 
		      for(int i = 0; i < neighbors.length; i++) {
		    	  coords[] nn = buildNeighborCells(neighbors[i].x, neighbors[i].y); 
	 	  
		    	  //Only add the cell if it doesn't exist yet, and the neighbors is 3. 
		    	  //If it checks already live cells it may ignore a cell with two neighbors
		    	  //even though that cell should live on. Also, do not add if it has a negative index(out of range)
		    	  if(currentGen.elementAt(neighbors[i].x, neighbors[i].y).equals(DEFAULT_VALUE) && (neighbors[i].x >= 0 && neighbors[i].y >= 0)) {
		    		  int nCount = countNeighbors(nn); 
		    		  if(nCount == 3) {
		    			  numNeighbors.setValue(neighbors[i].x, neighbors[i].y, new Integer(nCount));
		    		  }
		    	  }
		      }
		   }
		} 
	}

	/* Once the numNeighbors SparseArray is built this method will go through the array and 
	 * revives any cell with 2-3 neighbors, and kills any cell that has more or less than that.*/ 	 
	private void buildNextGen() { 
		RIterator r = numNeighbors.iterateRows();
		while (r.hasNext())
		{
		   EIterator elmItr = r.next();
		   while (elmItr.hasNext())
		   {
		      MatrixElem me = elmItr.next();
		      //System.out.println("(" + me.rowIndex() + ", " + me.columnIndex() + ") - " + me.value());
		      if(me.value().equals(new Integer(2)) || me.value().equals(new Integer(3))) {
		    	  //System.out.println("Adding");
		    	  nextGen.setValue(me.rowIndex(), me.columnIndex(), new Integer(1)); 
		      } else {
		    	  //System.out.println("Deleting");
		    	  nextGen.setValue(me.rowIndex(), me.columnIndex(), DEFAULT_VALUE); 
		      }
		   }
		} 
		//Setting currentGen to nextGen so the nextGen can be calculated again. 
		currentGen = nextGen; 
		
		//Resetting SparseArrays so new cells for the next generation can be added without overlapping cells. 
		nextGen = new SparseArray(DEFAULT_VALUE); 
		numNeighbors = new SparseArray(DEFAULT_VALUE); 
	}
	
	/* A simple method to count the neighbors using the neighbor array. */  
	private int countNeighbors(coords[] c) {
		int count = 0; 
		for(int i = 0; i < c.length; i++) {
			if(!(currentGen.elementAt(c[i].x, c[i].y).equals(DEFAULT_VALUE))) {
				count++; 
			}
		}
		return count; 
	}
	
	/*MAIN: Takes 3 command line arguments: Input file path, output file path, and the number of generations.*/ 
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Arguments were invalid - Usage Example: /path/to/inputFile  /path/to/outputFile  4(some Integer)");
		} else {
			Life life = new Life(args[0], args[1]);
			try {
				int genNum = Integer.parseInt(args[2]); 
				life.run(genNum);
			} catch(NumberFormatException nfe) {
				System.out.println("Number of generations must be an integer - Input was: " + args[2]);
			}
			
		}
	}
	
	
	/* Holds coordinates */
	private class coords {
		int x; 
		int y; 
		public coords(int x, int y) {
			this.x = x; 
			this.y = y; 
		}
	}
}
