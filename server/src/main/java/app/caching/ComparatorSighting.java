package app.caching;

import java.util.Comparator;

import app.domain.Sighting;

public class ComparatorSighting implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		Sighting s1 = (Sighting)arg0;
		Sighting s2 = (Sighting)arg1;
		
		 int flag=s1.getBirdname().compareTo(s2.getBirdname());
		 
		 if(flag==0){
			 return s1.getDate().compareTo(s2.getDate());
		 }else{
		   return flag;
		 }
	}
	

}
