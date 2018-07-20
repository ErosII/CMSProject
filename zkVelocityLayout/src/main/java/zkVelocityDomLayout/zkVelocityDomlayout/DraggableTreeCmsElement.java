package zkVelocityDomLayout.zkVelocityDomlayout;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.KeyValue;

public class DraggableTreeCmsElement extends DraggableTreeElement implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		for (String keyValue: treeAttributeDataMap.keySet()) {
			this.treeAttributeDataMap.put(keyValue, treeAttributeDataMap.get(keyValue));
		}
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
