package zkVelocityLayout.FragmentPackage;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FragmentMap {
	static Map<String, HashMap<String, Boolean>> fragmentMap = new HashMap<String, HashMap<String,Boolean>>();
	static List<String> fragmentList = new ArrayList<String>();
	
	static{
		HashMap<String, Boolean> titleMap=new HashMap<String, Boolean>();
		titleMap.put("id", true);
		titleMap.put("content", true);
		titleMap.put("colorAttribute", false);
		
		HashMap<String, Boolean> subtitleMap=new HashMap<String, Boolean>();
		subtitleMap.put("id", true);
		subtitleMap.put("content", true);
		subtitleMap.put("fontSize", false);
		
		HashMap<String, Boolean> paragraphMap=new HashMap<String, Boolean>();
		paragraphMap.put("id", true);
		paragraphMap.put("content", true);
		
		for(FragmentType fragmentName : FragmentType.values()) {
			
			fragmentList.add(fragmentName.toString());
			
			if(fragmentName.equals(FragmentType.Title)) {
				fragmentMap.put(fragmentName.toString(), titleMap);
			}
		
			if(fragmentName.equals(FragmentType.Subtitle)) {
				fragmentMap.put(fragmentName.toString(), subtitleMap);
			}
				
			if(fragmentName.equals(FragmentType.Paragraph)) {
				fragmentMap.put(fragmentName.toString(), paragraphMap);	
			}
			
		}
	}
	
	public static Map<String, HashMap<String, Boolean>> getFragmentMap() {
		return fragmentMap;
	} 
	
	public static List<String> getFragmentList() {
		return fragmentList;
	} 

}
