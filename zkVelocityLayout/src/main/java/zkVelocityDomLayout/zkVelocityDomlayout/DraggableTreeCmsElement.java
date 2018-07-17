package zkVelocityDomLayout.zkVelocityDomlayout;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentType;
import java.util.Map;

public class DraggableTreeCmsElement extends DraggableTreeElement {
	
	private FragmentType fragmentTypeDef;
	private Map<String,String> attributeDataMap;
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public FragmentType getFragmentTypeDef() {
		return fragmentTypeDef;
	}

	public void setFragmentTypeDef(FragmentType fragmentTypeDef) {
		this.fragmentTypeDef = fragmentTypeDef;
	}

	public Map<String, String> getAttributeDataMap() {
		return attributeDataMap;
	}

	public void setAttributeDataMap(Map<String, String> attributeDataMap) {
		this.attributeDataMap = attributeDataMap;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////CUSTRUCTOR
	public DraggableTreeCmsElement(DraggableTreeElement parent, String description, 
								   FragmentType fragmentTypeDef, Map<String,String> attributeDataMap){
		
		super(parent, description);
		this.fragmentTypeDef=fragmentTypeDef;
		this.attributeDataMap=attributeDataMap;
	}

}
