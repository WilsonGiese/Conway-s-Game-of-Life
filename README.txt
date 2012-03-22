Author: Wilson Giese - giese.wilson@gmail.com
Program: Conway's Game of Life

About:  
	This program implements a sparse array to run cellular automation 
	with the rulesfrom John Conway's game of life. 


Rules: 
 	If a live cell has less than 2 neighbors, the cell dies. 
	If a live cell has between 2 - 3 neighbors, the cell lives. 
	If a live cell has more than 3 neighbors, the cell dies. 
	If a dead cell has exactly three live neighbors, the becomes alive. 
	

Usage Example: 
	java -cp Life.jar Life path/To/InputFile path/To/OutputFile 
	numberOfGenerations
		


Example inputFile - Contains initial live cells. 
.......................................................................................
100, 100
100, 101
100, 102
100, 103
100, 104
.......................................................................................



Example outputFile after 4 generations. 
.......................................................................................
98, 101
98, 102
98, 103
99, 100
99, 104
100, 100
100, 104
101, 100
101, 104
102, 101
102, 102
102, 103
.......................................................................................