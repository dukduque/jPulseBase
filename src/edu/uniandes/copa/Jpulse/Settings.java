/**
 * This class defines an instance
 * 
 * Ref.: Lozano, L. and Medaglia, A. L. (2013). 
 * On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 * DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 * 
 * 
 * @author L. Lozano
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
import java.util.StringTokenizer;

public class Settings {

	String DataFile;
	int NumArcs;
	int NumNodes;
	int LastNode;
	int Source;
	double TimeC;
	
	public Settings(String ConfigFile) throws IOException{
		
		File file = new File(ConfigFile);
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		
		String [][] readed = new String [6][2];
		
		int row = 0;
		int col = 0;
	 
		//read each line of text file
		while((line = bufRdr.readLine()) != null && row < 6)
		{	
		StringTokenizer st = new StringTokenizer(line,":");
		while (st.hasMoreTokens())
		{
			//get next token and store it in the array
			readed[row][col] = st.nextToken();
			col++;
			
		}
		col = 0;
		row++;
		
		}
				
		DataFile=readed[0][1];
		NumArcs=Integer.parseInt(readed[1][1]);
		NumNodes=Integer.parseInt(readed[2][1]);
		TimeC=Double.parseDouble(readed[3][1]);
		Source=Integer.parseInt(readed[4][1]);
		LastNode=Integer.parseInt(readed[5][1]);		 
		
		
		
	}
	
	
}
