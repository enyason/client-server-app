package app.caching;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import app.domain.Sighting;

public class SightingCache {
	
	/**
	 * cache all sightings.
	 */
	public static final  List<Sighting> signtings = new CopyOnWriteArrayList<Sighting>();
	
	/**
	 * cache new sightings.
	 */
	public static final  List<Sighting> addSigntings = new CopyOnWriteArrayList<Sighting>();
	
	private SightingCache() {
		
	}


	
}
