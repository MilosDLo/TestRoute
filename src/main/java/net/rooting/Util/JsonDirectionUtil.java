package net.rooting.Util;

import java.sql.ResultSet;
import java.util.List;

import javax.enterprise.context.Dependent;

import net.rooting.domain.Org;

@Dependent
public class JsonDirectionUtil {

	public String getJsonFromResultSet(List<Org> orgs ) {
		
		
		//testing purpose
		String a = "";
		for (Org org : orgs) {
			a += ""+org.getName()+","+org.getLongitude()+","+org.getLatitude()+"\n";
		}
		return a;
	}
	
}
