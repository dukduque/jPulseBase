/**
 * This class holds the Dijkstra's algorithm logic for the distance attribute 
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



public class DukqstraDist {
	public int C;
	private int Delta;
	private PulseGraph G;
	private Bucket[] lowLevelBuckets;
	private AproxBucket[] highLevelBuckets;
	
	private AproxBucket baseAproxBucket;
	private AproxBucket topAproxBucket;
	
	/**
	 * Index where the Low level starts
	 */
	private int indexL;
	private int dL;
	/**
	 * Index where the high level starts
	 */
	private int indexK;
	private int dK;

	private int Snum;
	private int numHigh;
	
	private int numNodes;
	private int iH;
	private int jH;
	public DukqstraDist(PulseGraph g, int source) {
		
		G=g;
		// C es el max arco
		C= G.getCd();
		double x = (int)(Math.log(Math.sqrt(C))/Math.log(2))+0.0;
		Delta = (int)(Math.pow(2,x));
		numHigh = (int)(((C+1)/Delta))+1;
		//System.out.println(numHigh);
		lowLevelBuckets = new Bucket[Delta];
		highLevelBuckets = new AproxBucket[numHigh];
		
		G.getVertexByID(source).setMinDist(0);
		Snum = 0;
		numNodes = G.getNumNodes();
		indexL = 0;
		dL = 0;
		indexK = 1;
		dK = Delta;
		iH = -1;
		jH = -1;
		
		Bucket st =new Bucket(G.getVertexByID(source), 0);
		lowLevelBuckets[0] = st;
		//AproxBucket st2 =new AproxBucket(G.getVertexByID(source), 0);
		initializeBuckets();
		baseAproxBucket =  null;
		topAproxBucket = null;
	}

	
	/**
	 * Run Dijkstra's algorithm for distance
	 */
	public void runAlgDist() {

		int numN = numNodes;
		VertexPulse vi;
		int di;
		int ti;
		int lowIterator = 0;
		boolean empty;
		while (Snum < numN) {
			while (lowIterator < Delta) {
				
				vi = lowLevelBuckets[lowIterator].getEntrance();
				di = vi.getMinDist();
				ti = vi.getMaxTime();
				empty = deleteToLabel(lowIterator);
				Snum++;

				EdgePulse e = vi.getReversedEdges();
				int dj;
				VertexPulse vj;
				while (e != null) {
					
					vj = e.getTarget();
					dj = vj.getMinDist();
					if (dj > di + e.getWeightDist()) {
						int ndj = di + e.getWeightDist();
						// dMap.put(vj.getID(), ndj);
						vj.setMinDist(ndj);
						vj.setMaxTime(ti + e.getWeightTime());
						if (ndj < dK) {
							if (vj.isInserteDist()) {
								moveToLow(dj, ndj, vj);
							} else {
								vj.setInsertedDist();
								insertLow(ndj, vj);
							}
						} else {
							if (vj.isInserteDist()) {
								moveBucketToBucket(dj, ndj, vj);
							} else {
								vj.setInsertedDist();
								firstInsertion(ndj, vj);
							}
						}
					}
					e = e.getNext();
				}
				if(empty){
					lowIterator = moveInLow(lowIterator);
				}
			}
			stepForwardInDist();
			lowIterator = whereToStart();
		}
	}
	
	
	
	private int whereToStart() {
		for (int i = 0; i < Delta; i++) {
			if(!lowLevelBuckets[i].empty()){
				return i;
			}
		}
		return Delta;
	}

	/*	VertexPulse entrance = baseAproxBucket.getEntrance();
	boolean emp=false;
	int dv;
	while (!emp) { 
		emp = baseAproxBucket.deleteToMove(entrance);
		dv= entrance.getMinDist();
		lowLevelBuckets[dv-dL].insertVertex(entrance);
		entrance = baseAproxBucket.getEntrance();
	}*/
	private void stepForwardInDist() {
		if (baseAproxBucket!=null) {
			indexL= baseAproxBucket.getKey();
			dL = indexL*Delta;
	
			VertexPulse entrance = baseAproxBucket.getEntrance(); 
			entrance.getBLeftDist().unlinkRighBoundDist();
			int dv;
			while(entrance!=null){
				baseAproxBucket.deleteToPassDist(entrance);
				dv= entrance.getMinDist();
				lowLevelBuckets[dv-dL].insertVertexDist(entrance);
				entrance = baseAproxBucket.getEntrance();
			}
			highLevelBuckets[iH]=null;
			int oldBaseKEy= baseAproxBucket.getKey();
			baseAproxBucket = baseAproxBucket.deleteBucketToEmpty();
			if(baseAproxBucket!=null){
				iH = posCalculator(baseAproxBucket.getKey(), oldBaseKEy);
			}else{
				iH=-1;
				jH=-1;
			}
		}
		indexK= indexL + 1;
		dK = indexK*Delta;
	}
	/*VertexPulse entrance = baseAproxBucket.getEntrance();
	boolean emp=false;
	int dv;
	while (!emp) { 
		emp = baseAproxBucket.deleteToMove(entrance);
		dv= entrance.getMinTime();
		lowLevelBuckets[dv-dL].insertVertex(entrance);
		entrance = baseAproxBucket.getEntrance();
	}*/
	
		
	
	/**
	 * Insert a vertex to the key-L low bucket
	 * @param ndj d(v) , tentative shortest path
	 * @param v vertex being inserted
	 */
	private void insertLow(int ndj, VertexPulse v){
		lowLevelBuckets[ndj - dL].insertVertexDist(v);
	}
	
	private void moveToLow(int dj, int ndj, VertexPulse v){
		if(dj<dK){
			lowLevelBuckets[dj - dL].deleteToMoveDist(v);
			insertLow(ndj, v);
		}else{
			int key = keyCalculator(dj);
			boolean empty = deleteToMove(key, v);
			insertLow(ndj, v);
			if(empty){
				deleteBucket(key);
			}
		}
	}
	
	
	private void deleteBucket(int key) {
		int pos = posCalculator(key, baseAproxBucket.getKey());
		AproxBucket del = highLevelBuckets[pos];
		if(del.getDown()==null){
			baseAproxBucket = del.getUP();
			if(del.getUP()==null){
				topAproxBucket = null;
				iH = -1;
				jH = -1;
			}else{
				iH = posCalculator(del.getUP().getKey(), key);
				baseAproxBucket.setDown(null);
			}
		}else{
			if(del.getUP()!=null){
				del.getDown().setUP(del.getUP());
				del.getUP().setDown(del.getDown());
			}else{
				jH = posCalculator(del.getDown().getKey(), baseAproxBucket.getKey());
				topAproxBucket = del.getDown();
				topAproxBucket.setUP(null);
			}			
		}
		highLevelBuckets[pos]= null;
	}

	/**
	 * Insert
	 * @param key
	 * @param clueKey
	 * @param v
	 */
	private void insert(int key, int clueKey, int ndj,  VertexPulse v )
	{
		int pos = posCalculator(key, baseAproxBucket.getKey());
		if(highLevelBuckets[pos] != null){
			highLevelBuckets[pos].insertVertexDist(v);
		}else{
			AproxBucket theNewBucket = new AproxBucket(v, key,Delta );
			highLevelBuckets[pos] = theNewBucket;
			if(ndj < baseAproxBucket.getLowerBound()){
				int deltaPos =key - baseAproxBucket.getKey() ;
				baseAproxBucket.setDown(theNewBucket);
				theNewBucket.setUP(baseAproxBucket);
				baseAproxBucket = theNewBucket;
				iH += deltaPos;
				if(iH<0){
					iH+= numHigh;
				}
			}
			else if(ndj> topAproxBucket.getUpperBound()){
				int deltaPos = key - topAproxBucket.getKey();
				topAproxBucket.setUP(theNewBucket);
				theNewBucket.setDown(topAproxBucket);
				topAproxBucket = theNewBucket;
				jH += deltaPos;
				if(jH>=numHigh){
					jH-=numHigh;
				}
			}else{
				int cluePos = posCalculator(clueKey, baseAproxBucket.getKey());
				AproxBucket aux = highLevelBuckets[cluePos];
				boolean inserted = false;
				while(aux!= null && !inserted){
					if(ndj > aux.getUpperBound()){
						theNewBucket.setUP(aux.getUP());
						theNewBucket.setDown(aux);
						aux.getUP().setDown(theNewBucket);
						aux.setUP(theNewBucket);
						inserted =true;
					}
					aux =  aux.getDown();
				}
			}
		}
	}
	
	/**
	 * Insertion of node to a bucket (only first time)
	 * @param ndj key of the bucket
	 * @param v vertex being inserted
	 */
	private void firstInsertion(int ndj,VertexPulse v){
		int key = keyCalculator(ndj);
		if(baseAproxBucket!=null){
			int pos = posCalculator(key, baseAproxBucket.getKey());
			if(highLevelBuckets[pos] != null){
				highLevelBuckets[pos].insertVertexDist(v);
			}
			else{
				AproxBucket theNewBucket = new AproxBucket(v, key, Delta);
				highLevelBuckets[pos] = theNewBucket;
				if(ndj < baseAproxBucket.getLowerBound()){
					int deltaPos = key- baseAproxBucket.getKey();
					baseAproxBucket.setDown(theNewBucket);
					theNewBucket.setUP(baseAproxBucket);
					baseAproxBucket = theNewBucket;
					iH += deltaPos;
					if(iH<0){
						iH+= numHigh;
					}
				}
				else if(ndj> topAproxBucket.getUpperBound()){
					int deltaPos = key -topAproxBucket.getKey();
					topAproxBucket.setUP(theNewBucket);
					theNewBucket.setDown(topAproxBucket);
					topAproxBucket = theNewBucket;
					jH += deltaPos;
					if(jH>=numHigh){
						jH-=numHigh;
					}
				}
				else{
					AproxBucket aux = baseAproxBucket.getUP();
					boolean inserted = false;
					while(aux!= null && !inserted){
						if(ndj < aux.getLowerBound()){
							theNewBucket.setUP(aux);
							theNewBucket.setDown(aux.getDown());
							aux.getDown().setUP(theNewBucket);
							aux.setDown(theNewBucket);
							inserted =true;
						}
						aux =  aux.getUP();//Upward iteration
					}
				}
				}
		}else{
			AproxBucket theNewBucket = new AproxBucket(v, key, Delta);
			int pos = key-indexK;
			iH = pos;
			jH = pos;
			highLevelBuckets[pos] = theNewBucket;
			baseAproxBucket= theNewBucket;
			topAproxBucket = theNewBucket;
		}
	}
	
	/**
	 * Delete the entrance vertex of the baseBucket
	 */
	private boolean deleteToLabel(int lowIter){
		return lowLevelBuckets[lowIter].deleteLabeledVertexDist();
	}
	
	
	private int moveInLow(int lowIter){
		while(lowIter<Delta){
			if(!lowLevelBuckets[lowIter].empty()){
				return lowIter;
			}
			lowIter++;
		}
		return lowIter;
	}
	
	/**
	 * Delete temporaly a node to move from an aproximate bucket to another
	 * @param key Key of the bucke where v is
	 * @param v Vertex being moved
	 * @return true if the bucket got empty
	 */
	private boolean deleteToMove(int key , VertexPulse v){
		int pos = posCalculator(key, baseAproxBucket.getKey());
		AproxBucket b = highLevelBuckets[pos];
		return b.deleteToMoveDist(v);
	}
	
	/**
	 * Move a vertex from one bucket to another
	 * @param oldKey Key where the vertex is
	 * @param newKey Key where the vertex have to be
	 * @param v Vetex to be moved
	 */
	private void moveBucketToBucket(int dj, int ndj, VertexPulse v){
		int oldKey = keyCalculator(dj);
		int newKey = keyCalculator(ndj);
		if(oldKey!=newKey){
			boolean empty = deleteToMove(oldKey, v);
			insert(newKey, oldKey, ndj, v);
			if(empty){
				deleteBucket(oldKey);
			}
		}
	}


		
	public void resetBucketMap(int source){
		C= G.getCt();
		double x = (int)(Math.log(Math.sqrt(C))/Math.log(2))+0.0;
		Delta = (int)(Math.pow(2,x));
		Snum = 0;
		indexL = 0;
		dL = 0;
		indexK = 1;
		dK = Delta;
		lowLevelBuckets = new Bucket[Delta];
		numHigh = (int)(((C+1)/Delta))+1;
		highLevelBuckets = new AproxBucket[numHigh];
		Bucket st =new Bucket(G.getVertexByID(source), 0);
		lowLevelBuckets[0] = st;
		initializeBuckets();
		iH = -1;
		jH = -1;
		baseAproxBucket =  null;
		topAproxBucket = null;
	}
	private void initializeBuckets() {
		if(Delta>1){
			for(int i = 1 ; i<Delta; i++){
				lowLevelBuckets[i]= new Bucket(i);
			}
		}
	}
	private int keyCalculator(int dj){
		return (int)(dj/Delta);
	}
	
	private int posCalculator(int key , int baseKey ) {
		int pos = iH + key - baseKey;
		if(pos>=numHigh){
			return pos-numHigh;
		}else if(pos<0){
			return pos+numHigh;
		}else{
			return pos;
		}
	}
	
	
	
}
