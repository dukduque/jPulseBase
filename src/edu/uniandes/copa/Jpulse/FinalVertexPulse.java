/**
 * This class represents the final node in a network. The pulse procedure is overrided!
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

import java.util.ArrayList;



public class FinalVertexPulse extends VertexPulse {
	
	// Node id
	private int id;
	
	// SP stuff
	private VertexPulse bLeft;
	private VertexPulse bRigth;
	private EdgePulse reverseEdges;
	
	// Bounds
	int minDist;
	int maxTime;
	int minTime;
	int maxDist;
	
	private boolean inserted;
	
	int c=0;
	int d=0;
	public FinalVertexPulse(int i) {
		super(i);
		id = i;
		inserted = false;
		minDist = infinity;
		minTime = infinity;
		maxTime = 0;
		maxDist = 0;
		bLeft = this;
		bRigth = this;
	}

	
	public int  getID()
	{
		return id;
	}
	
	public void addReversedEdge(EdgePulse e)
	{
		if(reverseEdges!=null){
			reverseEdges.addNextCommonTailEdge(e);
		}else
			reverseEdges = e;
	}
	
	
	public EdgePulse findEdgeByTarget(VertexPulse target){
		if(reverseEdges!=null){
			reverseEdges.findEdgebyTarget(target);
		}
		return null;
	}
	public EdgePulse getReversedEdges() {
		if(reverseEdges!= null){
			return reverseEdges;
		}return new EdgePulse(1,1, this,this , -1);
	}
	
	public void setMinDist(int c){
		minDist = c;
	}
	
	public int getMinDist(){
		return minDist;
	}
	
	public void setMaxTime(int mt){
		maxTime = mt;
	}
	
	public int getMaxTime(){
		return maxTime;
	}
	
	public void setMinTime(int t){
		minTime = t;
	}
	
	public int getMinTime(){
		return minTime;
	}
	
	public void setMaxDist(int md){
		maxDist = md;
	}
	
	public int getMaxDist(){
		return maxDist;
	}
	
	
	/**
	 * Unlink a vertex from the bucket
	 * @return true, if the buckets gets empty
	 */
	public boolean unLinkVertexDist(){
		if(bRigth.getID() == id){
			bLeft=this;
			bRigth=this;
			return true;
		}else{
			bLeft.setRigthDist(bRigth);
			bRigth.setLeftDist(bLeft);
			bLeft = this;
			bRigth = this;
			return false;
		}
	}
	/**
	 * Insert a vertex in a bucket. 
	 * New vertex is inserted on the left of the bucket entrance 
	 * @param v vertex in progress to be inserted
	 */
	public void insertVertexDist(VertexPulse v) {
		v.setLeftDist(bLeft);
		v.setRigthDist(this);
		bLeft.setRigthDist(v);
		bLeft = v;
	}
	
	
	public void setLeftDist(VertexPulse v){
		bLeft= v;
	}
	public void setRigthDist(VertexPulse v){
		bRigth= v;
	}
	public VertexPulse getBLeftDist(){
		return bLeft;
	}
	public VertexPulse getBRigthDist(){
		return bRigth;
	}
	public void setInsertedDist(){
		inserted = true;
	}
	public boolean isInserteDist(){
		return inserted;
	}

	public void reset(){
		inserted = false;
	}
	public void setBounds(int MT, int MD){
		maxDist = MD- minDist;
		maxTime = MT - minTime;
	}
		
	public void pulse(int PTime, int PDist,ArrayList<Integer> path) {
		// Add node id to path
		path.add(id);
		// If the path is feasible and updates the primal bound the information on the best solution found is updated
		if(PDist<=PulseGraph.PrimalBound && PTime<=PulseGraph.TimeC){
			
			// Update the best solution known
			PulseGraph.Path.clear();
			PulseGraph.Path.addAll(path);
			PulseGraph.TimeStar=PTime;
			PulseGraph.PrimalBound=PDist;
				
		}
		// Remove the node id to backtrack
		path.remove((path.size()-1));
	}

	
}
