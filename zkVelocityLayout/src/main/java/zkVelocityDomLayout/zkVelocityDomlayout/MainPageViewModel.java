package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentType;

public class MainPageViewModel {

	private DraggableTreeCmsElement root;
	private DraggableTreeModel model;
	private DraggableTreeCmsElement selectedElement;
	
	private ArrayList<String> idList = new ArrayList<String>();

	public MainPageViewModel(){
	
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("id", "root");
		root= new DraggableTreeCmsElement(null, "root", null, rootMap);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public DraggableTreeModel getModel() {
		
		if (model == null) {
			model = new DraggableTreeModel(root);
		}
		return model;
	}

	public void setSelectedElement(DraggableTreeCmsElement selectedElement) {
		this.selectedElement = selectedElement;
	}
	
	public DraggableTreeCmsElement getSelectedElement() {
		return selectedElement;
	}
	
	public DraggableTreeElement getRoot() {
		return root;
	}

	public void setRoot(DraggableTreeCmsElement root) {
		this.root = root;
	}
	
	public ArrayList<String> getIdList() {
		return idList;
	}

	public void setIdList(ArrayList<String> idList) {
		this.idList = idList;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS
	@GlobalCommand
	@NotifyChange("*")
	public void reloadMainPageTree(@BindingParam("selectedElement") DraggableTreeCmsElement componentSelectedElement,
								   @BindingParam("idList") ArrayList<String> componentIdList){
		selectedElement = componentSelectedElement;
		root.recomputeSpacersRecursive();
		idList=componentIdList;
	}
		
	@Command
	@NotifyChange("*")
	public void deleteNode(){
		DraggableTreeComponent.removeFromParent(selectedElement);
		root.recomputeSpacersRecursive();
		idList.remove(selectedElement.getAttributeDataMap().get("id"));
	}
}
