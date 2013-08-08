package app.caching;

import java.util.Comparator;

import app.domain.Bird;

public class ComparatorBird implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		Bird b1 = (Bird)o1;
		Bird b2 = (Bird)o2;
		
		return b1.getName().compareTo(b2.getName());

	}
	

}
