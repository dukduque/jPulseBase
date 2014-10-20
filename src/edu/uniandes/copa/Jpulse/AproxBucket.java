/**
 * Data structure for an aproximate bucket. Used for the SP implementation
 * 
 * Ref.: Lozano, L. and Medaglia, A. L. (2013). 
 * On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 * DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 * 
 * 
 * @author D. Duque
 * @affiliation Universidad de los Andes - Centro para la Optimización y Probabilidad Aplicada (COPA)
 * @url http://copa.uniandes.edu.co/
 * 
 */


package edu.uniandes.copa.Jpulse;



public class AproxBucket {
	
	private AproxBucket down;
	private AproxBucket up;
	private VertexPulse entrance;
	
	/**
	 * Key of the bucket, also is the lower bound
	 * of the bucket
	 */
	private int key;
	/**
	 * upper is the upper bound of the bucket
	 */
	private int upper;
	
	/**
	 * lower is the upper bound of the bucket
	 */
	private int lower;
	
	/**
	 * Create an instance of an aproximate bucket. If a bucket
	 * is opened, a new vertex is being added
	 * @param v
	 */
	public AproxBucket(VertexPulse v, int nKey, int delta){
		down = null;
		up = null;
		entrance = v;
		key = nKey;
		lower = key*delta;
		upper = (key+1)*delta-1;
	}
	
	
	/**
	 * Insert a vertex in the bucket.
	 * @param v Vertex being inserted
	 */
	public void insertVertexDist(VertexPulse v){
		//System.out.println("Entrando "+ v.getID() + " FO : " + v.getMinCost() );
		entrance.insertVertexDist(v);
	}
	public void insertVertexTime(VertexPulse v){
		//System.out.println("Entrando "+ v.getID() + " FO : " + v.getMinCost() );
		entrance.insertVertexTime(v);
	}
	
	/**
	 * 
	 * @return
	 */
	public AproxBucket deleteLabeledBucket() {
		if(up!=null){
			up.down = null;
			return up;
		}
		return null;
	}
	 
	public void deleteToPassDist(VertexPulse v){
		entrance = entrance.getBRigthDist();
		v.fastUnlinkDist();
	}
	
	public void deleteToPassTime(VertexPulse v){
		entrance = entrance.getBRigthTime();
		v.fastUnlinkTime();
	}

	
	public boolean deleteToMoveDist(VertexPulse v){
		if(entrance.getID() == v.getID()){
			entrance = entrance.getBRigthDist();
		}
		return v.unLinkVertexDist();
	}
	
	public boolean deleteToMoveTime(VertexPulse v){
		if(entrance.getID() == v.getID()){
			entrance = entrance.getBRigthTime();
		}
		return v.unLinkVertexTime();
	}

	
	public AproxBucket deleteBucketToEmpty(){
		if(up!=null){
			up.down = null;
			return up;
		}
		return null;
	}
	
	public AproxBucket deleteBucket(){
		if(up!=null){
			up.down = down;
			if(down != null){
				down.up = up;
			}else{
				return null;
			}
		}
		else{
			if(down != null){
				down.up = null;
				return down;
			}else{
				return null;
			}
		}
		return null;
	}
	
	public int getKey(){
		return key;
	}
	
	public int getLowerBound()
	{
		return lower;
	}
	
	public int getUpperBound()
	{
		return upper;//No me gusta llamar a delta asÃ­
	}
	
	public VertexPulse getEntrance(){
		return  entrance;
	}
	public AproxBucket getUP(){
		return up;
	}
	public AproxBucket getDown(){
		return down;
	}
	public void setUP(AproxBucket v){
		up = v;
	}
	public void setDown(AproxBucket v){
		down = v;
	}
	public void turnTheBucket(){
		entrance= entrance.getBRigthDist();
	}
	public void turnTheBucketTime(){
		entrance= entrance.getBRigthTime();
	}
}
