===========================================================================================================================================================================
 Readme for the Pulse algorithm Java code for CSP from 
 On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 Version: 1.0
===========================================================================================================================================================================

 Author:       Leonardo Lozano (leo-loza@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes
 URL:          http://www.leo-loza.com


 Author:       Daniel Duque (d.duque25@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes

 Author:       Andres L. Medaglia (amedagli@uniandes.edu.co)
               Industrial Engineering Department
               Universidad de los Andes
 URL:          http://wwwprof.uniandes.edu.co/~amedagli


===========================================================================================================================================================================

This file contains important information about the Java code for the CSP.

===========================================================================================================================================================================

The file Jpulse-CSP.zip contains all the source code for executing the pulse algorithm for the Constrained Shortest Path Problem (CSP). 

The main class is called "PulseMain" and requires a configuration file with the following information:

- Name of the data file
- Number of arcs in the network
- Number of nodes in the network
- Resource constraint upper limit T
- Start node
- End node
 
We include a sample configuration file (config.txt) and a sample data file (USA-road-NY.txt). The first line presents the number of nodes and arcs in the network.
From the second line to the end, the arcs information is presented in the form: (tail, head, cost, weight).


===========================================================================================================================================================================
Sample Network
===========================================================================================================================================================================

The sample network is New York road network taken from the 9th DIMACS Implementation Challenge. The optimal value for this sample network is:

Distance: 678151
Time: 750312


===========================================================================================================================================================================
References
===========================================================================================================================================================================

- Demetrescu, C., Goldberg, A., & Johnson, D. (2006). 9th DIMACS Implementation Challenge - Shortest Paths.
	 http://www.dis.uniroma1.it/~challenge9/

	 
===========================================================================================================================================================================
Usage & License
===========================================================================================================================================================================

This is the Java implementation of the pulse algorithm as published in: "Lozano, L. and Medaglia, A. L. (2013). On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384." This paper is available at: http://dx.doi.org/10.1016/j.cor.2012.07.008 . If you use (or modified) this code, please cite the paper by Lozano and Medaglia (2013) and this item found in DSpace. The authors would really enjoy to know the (good) use of the pulse algorithm in different fields, so please send a line to amedagli@uniandes.edu.co or copa@uniandes.edu.co describing us your application (as brief as you want). 
