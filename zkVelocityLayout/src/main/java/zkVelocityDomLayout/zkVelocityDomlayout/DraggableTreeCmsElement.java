package zkVelocityDomLayout.zkVelocityDomlayout;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentType;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.KeyValue;

public class DraggableTreeCmsElement extends DraggableTreeElement {
	
	private FragmentType fragmentTypeDef;
	private Map<String,String> treeAttributeDataMap = new HashMap<String, String>();
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public FragmentType getFragmentTypeDef() {
		return fragmentTypeDef;
	}

	public Map<String, String> getTreeAttributeDataMap() {
		return treeAttributeDataMap;
	}

	public void setTreeAttributeDataMap(Map<String, String> treeAttributeDataMap) {
		this.treeAttributeDataMap = treeAttributeDataMap;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////CUSTRUCTOR
	public DraggableTreeCmsElement(DraggableTreeElement parent, String description, 
								   FragmentType fragmentTypeDef, Map<String,String> treeAttributeDataMap){
		
		super(parent, description);
		this.fragmentTypeDef=fragmentTypeDef;
		for (String keyValue: treeAttributeDataMap.keySet()) {
			this.treeAttributeDataMap.put(keyValue, treeAttributeDataMap.get(keyValue));
		}
	}

}
