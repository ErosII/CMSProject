package zkVelocityDomLayout.zkVelocityDomlayout;

import biz.opengate.zkComponents.draggableTree.*;
import java.util.HashMap;
import java.util.Map;

public class DraggableTreeCmsElement extends DraggableTreeElement{
		
	private Map<String,String> treeAttributeDataMap = new HashMap<String, String>();

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public Map<String, String> getTreeAttributeDataMap() {
		return treeAttributeDataMap;
	}

	public void setTreeAttributeDataMap(Map<String, String> treeAttributeDataMap) {
		for (String keyValue: treeAttributeDataMap.keySet()) {
			this.treeAttributeDataMap.put(keyValue, treeAttributeDataMap.get(keyValue));
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////CUSTRUCTOR
	public DraggableTreeCmsElement(DraggableTreeElement parent, String description, Map<String,String> treeAttributeDataMap){
		
		super(parent, description);
		for (String keyValue: treeAttributeDataMap.keySet()) {
			this.treeAttributeDataMap.put(keyValue, treeAttributeDataMap.get(keyValue));
		}
	}
	
	public DraggableTreeCmsElement() {
		super(null, null);
		
	}
	
}
