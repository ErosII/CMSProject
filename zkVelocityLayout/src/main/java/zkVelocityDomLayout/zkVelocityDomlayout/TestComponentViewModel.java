package zkVelocityDomLayout.zkVelocityDomlayout;

import java.io.FileWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.util.Clients;
import biz.opengate.zkComponents.draggableTree.DraggableTreeComponent;
import biz.opengate.zkComponents.draggableTree.DraggableTreeElement;
import biz.opengate.zkComponents.draggableTree.DraggableTreeModel;
import zkVelocityLayout.FragmentPackage.FragmentType;
import zkVelocityLayout.FragmentPackage.FragmentList;

public class TestComponentViewModel {
	

	
	private String fragmentId;
	private String contentString;
	private String colorAttribute;

	
	private FragmentType selectedFragment;
	

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS MASK

	public FragmentType getSelectedFragment() {
		return selectedFragment;
	}

	public void setSelectedFragment(FragmentType selectedFragment) {
		this.selectedFragment = selectedFragment;
	}
	

	
	public String getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(String fragmentId) {
		this.fragmentId = fragmentId;
	}

	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}

	public String getColorAttribute() {
		return colorAttribute;
	}

	public void setColorAttribute(String colorAttribute) {
		this.colorAttribute = colorAttribute;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS DRAGGABLETREE EXTENSION


	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS
	
	@Command
	@NotifyChange("*")
	public void updateComponent(@BindingParam("selectedElement") DraggableTreeCmsElement mainPageselectedElement,
								@BindingParam("idList") ArrayList<String> mainPageIdList) throws Exception{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////CHECK DATA
		String errString=checkFields(mainPageselectedElement, mainPageIdList, fragmentId, contentString, colorAttribute);
		if ((errString.equals(""))==true){
			attributeDataMap=generateFragment();
			///////////////////////////////////////////////////////////////////////////////////////////////////////////
			////// INITIALIZING COMPONENT ELEMENTS WITH MAIN PAGE ELEMENTS
			componentSelectedElement = mainPageselectedElement;
			componentIdList=mainPageIdList;
			componentIdList.remove(componentSelectedElement.getAttributeDataMap().get("id"));
			componentSelectedElement.setAttributeDataMap(attributeDataMap);
			componentSelectedElement.setDescription(fragmentId);
			componentIdList.add(fragmentId);
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("selectedElement", componentSelectedElement);
			args.put("idList", componentIdList);
			BindUtils.postGlobalCommand(null, null, "reloadMainPageTree", args);
			// RESET WINDOW SELECTIONS OR CONTENT
			resetPopUpSelectionAndBack();
		}else {
			addPopupVisibility=true;
			Clients.showNotification(errString);
		}
	}

	private String checkFields(DraggableTreeCmsElement selectedElement, ArrayList<String> idList, String fragmentId, String contentString, String colorAttribute) {
		String errMsgFun = "";
		
		if(fragmentId==null || (fragmentId.equals(""))==true) {
			
			errMsgFun += "Node Id \n";
		}
		if(contentString==null || (contentString.equals(""))==true) {
			errMsgFun += "Title \n";
		}
		if(colorAttribute==null || (colorAttribute.equals(""))==true) {
			errMsgFun += "Color Attribute \n";
		}
		if((errMsgFun.equals(""))==false) {
			errMsgFun += " empty. Please insert all the data. \n";
		}
		if(fragmentId!= selectedElement.getAttributeDataMap().get("id") && idList.contains(fragmentId)==true) {
			errMsgFun += "Node Id already exists. Change it please. \n";
		}
		return errMsgFun;
	}

}
