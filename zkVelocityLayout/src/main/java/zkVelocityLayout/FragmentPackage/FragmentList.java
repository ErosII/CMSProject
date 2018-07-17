package zkVelocityLayout.FragmentPackage;

import java.util.ArrayList;
import java.util.List;

public class FragmentList {
	static List<FragmentType> fragmentList = new ArrayList<FragmentType>();
	
	static{
		for(FragmentType fragmentName : FragmentType.values()) {	
			fragmentList.add(fragmentName);
		}
	}
	
	public static List<FragmentType> getFragmentList() {
		return fragmentList;
	} 

}
