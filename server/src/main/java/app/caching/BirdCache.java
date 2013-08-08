package app.caching;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import app.domain.Bird;

public class BirdCache {
	
	/**
	 * cache for all birds.
	 */
	public static final Set<Bird> birds = new CopyOnWriteArraySet<Bird>();
	
	/**
	 * cache for new birds.
	 */
	public static final Set<Bird> addbirds = new CopyOnWriteArraySet<Bird>();
	
	/**
	 * cache for removed birds.
	 */
	public static final Set<String> removebirds = new CopyOnWriteArraySet<String>();
	
	
	private BirdCache() {
		
	}
	
}
