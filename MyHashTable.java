package assignment4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
    	if (initialCapacity < 0)
    		throw new IllegalArgumentException("Must have initial buckets > 0");
    	if (initialCapacity == 0) {
    		initialCapacity+=2;
    	}
        this.numEntries = 0;
        this.numBuckets = initialCapacity;
        this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(initialCapacity);
        for(int i = 0; i < this.numBuckets; i++) {
        	LinkedList<HashPair<K,V>> newList = new LinkedList<HashPair<K,V>>();
        	this.buckets.add(newList);
        }
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList<HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
    	
    	int bucketIndex = hashFunction(key);
    	LinkedList<HashPair<K,V>> newList = buckets.get(bucketIndex);
    	
    	for(HashPair<K,V> pair : newList) {
    		if (pair.getKey().equals(key)) {
    			V temp = pair.getValue();
    			pair.setValue(value);
    			return temp; 
    		}
    		
    	}
    	
    	// insert key in chain 	
    	// getting linked list and adding new hash pair  	
    	buckets.get(bucketIndex).add(new HashPair<K,V>(key,value));
    	numEntries++;
    	
    	// check if load factor goes beyond threshold 
    	if ((1.0*numEntries)/numBuckets >= MAX_LOAD_FACTOR) {
    		rehash(); // double size if method goes beyond max
    	}    	 	
    	return null;     	
    }
        
    
    // Get the value corresponding to key. Expected average runtime O(1)
    
    public V get(K key) {
    	
    	if (numBuckets != 0) {
    		int bucketIndex = hashFunction(key);
    		LinkedList<HashPair<K,V>> newList = buckets.get(bucketIndex);
    	
    		for (HashPair<K,V> pair : newList) {
    				if (pair.getKey().equals(key))
    					return pair.getValue();
    		} 
    	}
    	return null;   	
    }
    
  
    // Remove the HashPair corresponding to key . Expected average runtime O(1) 
     
    public V remove(K key) {

    	if (numBuckets != 0) {
    		int bucketIndex = hashFunction(key);
    		LinkedList<HashPair<K,V>> newList = buckets.get(bucketIndex);
    	
    		for (HashPair<K,V> pair : newList) {
    			//while (pair != null) {
    			if (pair.getKey().equals(key)) {
    				numEntries--;
    				newList.remove(pair);
    				return pair.getValue();
    			}
    		}
    	}
    	return null;
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
    	  	
    	ArrayList<LinkedList<HashPair<K,V>>> temp = buckets;
    	
    	numEntries = 0;  
		numBuckets*=2;	
    	
		buckets = new ArrayList<LinkedList<HashPair<K,V>>>(numBuckets);
		
		for(int i = 0; i < numBuckets; i++) {
        	LinkedList<HashPair<K,V>> newList = new LinkedList<HashPair<K,V>>();
        	this.buckets.add(newList);
        }			
		
		for (LinkedList<HashPair<K,V>> newList : temp) {
			for (HashPair<K,V> pair : newList) {
				buckets.get(hashFunction(pair.getKey())).add(pair);
				numEntries++;
			}
		}
    }
      
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */   
    public ArrayList<K> keys() {
    	
    	ArrayList<K> keyList = new ArrayList<K>();
    	
    	for (LinkedList<HashPair<K,V>> newList : buckets) {
    		for (HashPair<K,V> pair : newList) {
    			K key = pair.getKey();
    			keyList.add(key);
    		}
    	}    	
    	return keyList;
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
    	
    	MyHashTable<V,V> hashVal = new MyHashTable<>(1);
    	
    	for (LinkedList<HashPair<K,V>> newList : buckets) {
    		for (HashPair<K,V> pair : newList) {
    			V value = pair.getValue();
    			if (hashVal.get(value) == null) {
    				hashVal.put(value, value);
    			}
    		}
    	}  	
    	ArrayList<V> valList = new ArrayList<V>(hashVal.keys());  	
    	return valList;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
    	
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K,V> entry : results) {
        	V element = entry.getValue();
    		K toAdd = entry.getKey();
    		int i = sortedResults.size() - 1;
    		V toCompare = null;
           	while (i >= 0) {
           		toCompare = results.get(sortedResults.get(i));
           		if (element.compareTo(toCompare) <= 0 )
           			break;
           		i--;
           	}
           	sortedResults.add(i+1, toAdd);
       	}
        return sortedResults;
    }
     
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */  
    
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {   	
    	
    	ArrayList<HashPair<K,V>> sortedList = new ArrayList<>();
    	for (LinkedList<HashPair<K, V>> linked : results.buckets) {
    		for (HashPair<K, V> pair : linked) {
    			sortedList.add(pair);
    		}
    	}
    	
    	ArrayList<HashPair<K,V>> list = mergeSort(sortedList);
    	ArrayList<K> finalList = new ArrayList<K>();
    	
    	for (HashPair<K,V> pair : list) {
    		finalList.add(pair.getKey());
    	}
    	return finalList;
    }
    
    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> mergeSort(ArrayList<HashPair<K,V>> results) {
    	if (results.size() == 1 || results.size() == 0) {
    		return results;
    	}
    	else {
    		int mid = ((results.size()-1)/2);
    		ArrayList<HashPair<K,V>> list1 = new ArrayList<HashPair<K,V>>(results.subList(0,mid+1));
    		ArrayList<HashPair<K,V>> list2 = new ArrayList<HashPair<K,V>>(results.subList(mid+1,results.size()));
    		list1 = mergeSort(list1);
    		list2 = mergeSort(list2);
    		return merge(list1,list2);
    	}   	
    }
    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> merge(ArrayList<HashPair<K,V>> list1, ArrayList<HashPair<K,V>> list2) {
    	ArrayList<HashPair<K,V>> newList = new ArrayList<HashPair<K,V>>();
    	while (!list1.isEmpty() && !list2.isEmpty()) {
    		if (list1.get(0).getValue().compareTo(list2.get(0).getValue()) > 0) {
    			newList.add(list1.remove(0));
    		}
    		else
    			newList.add(list2.remove(0));
    	}
    	while (!list1.isEmpty())
    		newList.add(list1.remove(0));
    	while (!list2.isEmpty())
    		newList.add(list2.remove(0));
    	return newList;
    }
      
   
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        // fields
    	private ArrayList<HashPair<K,V>> arr;
    	private int curInd;
    	    	
    	// helper
    	public ArrayList<HashPair<K,V>> hashList(){
    		ArrayList<HashPair<K,V>> newList = new ArrayList<HashPair<K,V>>();
    		for (LinkedList<HashPair<K,V>> list : buckets) {
    			for (HashPair<K,V> pair : list) {
    				newList.add(pair);
    			}
    		}
    		return newList;
    	}
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
        	arr = hashList();
        	curInd = 0;
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            return (curInd != arr.size());
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
        	if (hasNext()) {
        		return arr.get(curInd++);
        	}
        	return null;       	
        }
        
    }
}
