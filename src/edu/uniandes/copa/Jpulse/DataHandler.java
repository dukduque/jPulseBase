/**
 * This is a class that holds all the relevant data for an instance.
 * 
 * Ref.: Lozano, L. and Medaglia, A. L. (2013). 
 * On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 * DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 * 
 * 
 * @author L. Lozano & D. Duque
 * @affiliation Universidad de los Andes - Centro para la Optimización y Probabilidad Aplicada (COPA)
 * @url http://copa.uniandes.edu.co/
 * 
 */
package edu.uniandes.copa.Jpulse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class DataHandler {
	// Name of the instance
	String CvsInput;
	// Number of arcs
	int NumArcs;
	// Number of nodes
	static int NumNodes;
	// Destinantion node
	int LastNode;
	// Source node
	int Source;
	// All the arcs in the network stored in a vector where Arcs[i][0]= Tail for arc i and Arcs[i][1]= Head for arc i 
	static int[][] Arcs;
	// The distance attribute for any arc i
	static int[] Distance;
	// The time attribute for any arc i
	static int[] Time;
	// Data structure for storing the graph
	private PulseGraph Gd;
	
	// Read data from an instance
	public DataHandler(Settings Instance) {
		CvsInput = Instance.DataFile;
		NumArcs = Instance.NumArcs;
		NumNodes = Instance.NumNodes;
		LastNode = Instance.LastNode;
		Source = Instance.Source;
		Arcs = new int[Instance.NumArcs][2];
		Distance = new int[Instance.NumArcs];
		Time = new int[Instance.NumArcs];
		Gd = new PulseGraph(NumNodes);
	}

		
// This procedure creates the nodes for the graph
	public void upLoadNodes(){
		// All nodes are VertexPulse except the final node
		for (int i = 0; i < NumNodes; i++) {
			if(i!=(LastNode-1)){
				Gd.addVertex(new VertexPulse(i) );
			}
		}
		// The final node is a FinalVertexPulse 
		FinalVertexPulse vv = new FinalVertexPulse(LastNode-1);
		Gd.addFinalVertex(vv);
	}
	
	// This procedure returns a graph
	public PulseGraph getGd()
	{
		return Gd;
	}

	// This procedure reads data from a data file in DIMACS format
	public void ReadDimacs() throws NumberFormatException, IOException {
		File file = new File(CvsInput);

		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;

		String[] readed = new String[5];

		int row = 0;
		int col = 0;
		
		upLoadNodes();
		
		while ((line = bufRdr.readLine()) != null && row < NumArcs + 1) {
			StringTokenizer st = new StringTokenizer(line, " ");
			while (st.hasMoreTokens()) {
				// get next token and store it in the array
				readed[col] = st.nextToken();
				col++;
			}

			if (row >= 1) {
				Arcs[row - 1][0] = (Integer.parseInt(readed[1]) - 1);
				Arcs[row - 1][1] = (Integer.parseInt(readed[2]) - 1);
				Distance[row - 1] = Integer.parseInt(readed[3]);
				Time[row - 1] = Integer.parseInt(readed[4]);
				// Add edges to the network
				Gd.addWeightedEdge( Gd.getVertexByID(Arcs[row - 1][0]), Gd.getVertexByID(Arcs[row - 1][1]),Distance[row - 1], Time[row - 1] , row-1);
			}

			col = 0;
			row++;

		}
		
	}
	
}

