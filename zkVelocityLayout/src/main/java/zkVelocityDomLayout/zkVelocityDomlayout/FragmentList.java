package zkVelocityDomLayout.zkVelocityDomlayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentList {
	static List<String> fragmentList = new ArrayList<String>();
	
	static{
		for(FragmentType fragmentName : FragmentType.values()) {	
			fragmentList.add(fragmentName.toString());
		}
	}
	
	public static List<String> getFragmentList() {
		return fragmentList;
	} 

}
