package assignment4;

import java.util.ArrayList;

public class Twitter {

	ArrayList<String> stopWords;
	ArrayList<Tweet> tweets;
	int numBuckets;
	MyHashTable<String,Tweet> hashByAuthor;
	MyHashTable<String,ArrayList<Tweet>> hashByDate;
	MyHashTable<String,String> hashStop;
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		this.stopWords = stopWords;
		this.tweets = tweets;
		numBuckets = 10;
		this.hashStop = new MyHashTable<String,String>(numBuckets);
		for (String stopWord : stopWords) {
			stopWord = stopWord.toLowerCase();
	       	hashStop.put(stopWord, stopWord);	
	    }
		this.hashByAuthor = new MyHashTable<String, Tweet>(numBuckets);
		this.hashByDate = new MyHashTable<String, ArrayList<Tweet>>(numBuckets);
		for (Tweet t : tweets) {
			addTweet(t);
		} 
	}
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		// for latest tweet by author
		if (hashByAuthor.get(t.getAuthor()) != null) {
			String temp = hashByAuthor.get(t.getAuthor()).getDateAndTime();
			if (t.getDateAndTime().compareTo(temp) > 0) {
				hashByAuthor.put(t.getAuthor(), t);
			}
		}
		else {
			hashByAuthor.put(t.getAuthor(), t);
		}
		
		// for tweets by date
		String[] dates = t.getDateAndTime().split(" ");
		String date = dates[0];
		if (hashByDate.get(date) != null) {			
				hashByDate.get(date).add(t);
		}
		else {	
			ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
			tweetList.add(t);
			hashByDate.put(date,tweetList);
		}
	}
	
    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {  	
        return hashByAuthor.get(author);
    }

    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
    	return hashByDate.get(date);
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
        
        Integer numOccur = 0;
        MyHashTable<String, Integer> hashWords = new MyHashTable<String,Integer>(numBuckets);
    	for (Tweet tweet : tweets) {
    		MyHashTable<String,String> hashTweet = new MyHashTable<String,String>(numBuckets);
    		for (String word : getWords(tweet.getMessage())) {
    			word = word.toLowerCase();
    			if (hashStop.get(word) == null && hashTweet.get(word) == null) {
    				hashTweet.put(word, word);
    			}
    		}
    		for (String key : hashTweet.keys()) {
    			if (hashWords.get(key) == null) {
    				hashWords.put(key, 1);
   				}
    			else {
    				numOccur = hashWords.get(key) + 1;
    				hashWords.put(key, numOccur);
    			}
    		}
    	}
    	ArrayList<String> sorted = MyHashTable.fastSort(hashWords);  
    	return sorted;  
    }
    
    
	/**
     * A helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}
