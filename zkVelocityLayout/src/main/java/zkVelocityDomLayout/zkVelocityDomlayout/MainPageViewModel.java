package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;

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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS
	@GlobalCommand
	@NotifyChange("*")
	public void reloadMainPageTree(@BindingParam("selectedElement") DraggableTreeCmsElement componentSelectedElement){
		selectedElement = componentSelectedElement;
	}
		
	@Command
	@NotifyChange("*")
	public void deleteNode(){
		DraggableTreeComponent.removeFromParent(selectedElement);
		root.recomputeSpacersRecursive();

	}
}
