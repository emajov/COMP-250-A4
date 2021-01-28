package assignment4;
import java.util.ArrayList;

public class FastSortTester {
	
	public static void main(String args[]) {
		MyHashTable<String,Integer> hash;
		int numBuckets = 3;
		
		hash = new MyHashTable<String, Integer>(numBuckets);
		
		hash.put("monkey", 1);
		hash.put("lion", 1);
		hash.put("giraffe", 25);
		hash.put("cat", 25);
		hash.put("sealion", 15);
		hash.put("tiger", 2);
		hash.put("cheetah", 2);
		hash.put("elephant", 200);
		hash.put("gorilla", 10);
		hash.put("antelope", 10);
		hash.put("grasshopper", 6);
		
		ArrayList<String> keys = MyHashTable.fastSort(hash);
        System.out.println(keys);
        /*for (String key : keys) {
        	System.out.println(hash.get(key));
        }*/

        ArrayList<Integer> values = hash.values();
        for (Integer value : values) {
        	System.out.println(value);
        }
        
        for (HashPair<String,Integer> pair : hash) {
        	System.out.println(pair.getKey());
        }
        
        }	
	}

