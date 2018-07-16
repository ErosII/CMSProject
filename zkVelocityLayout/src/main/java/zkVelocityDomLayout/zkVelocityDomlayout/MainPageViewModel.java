package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentList;
import zkVelocityLayout.FragmentPackage.FragmentType;

//@ComponentAnnotation("@ZKBIND(ACCESS=both, SAVE_EVENT=onAddedNode)")
public class MainPageViewModel {

	private DraggableTreeCmsElement root;
	private DraggableTreeModel model;
	private DraggableTreeCmsElement selectedElement;
	private List<String> fragmentList;
	private FragmentType selectedFragment;

	public MainPageViewModel(){
	
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("id", "root");
		root= new DraggableTreeCmsElement(null, "Sample Page", null, rootMap);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public FragmentType getSelectedFragment() {
		return selectedFragment;
	}

	public void setSelectedFragment(FragmentType selectedFragment) {
		this.selectedFragment = selectedFragment;
	}
	
	public List<String> getFragmentList() {
		return FragmentList.getFragmentList();
	}
	
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
	public void addNodeGlobal(@BindingParam("nodeTitle") String nodeTitle) {
		
		if (selectedElement==null) {
			Clients.showNotification("No selected node!");
		}
		else {
			new DraggableTreeElement(selectedElement,nodeTitle);
			root.recomputeSpacersRecursive();
		}

	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void showSelectedFragment() {
		if (selectedFragment==null) {
			Clients.showNotification("No selected element!");
		}	
		else
		{	
			Clients.showNotification(selectedFragment.toString() + " selected");
		}
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void reloadTree(@BindingParam("model") DraggableTreeModel componentModel, 
						   @BindingParam("selectedElement") DraggableTreeCmsElement componentSelectedElement)
		{
			model=componentModel;
			//selectedElement=componentSelectedElement;
		
	}
	
	@Command
	@NotifyChange("*")
	public void deleteNode(){
		if (selectedElement==null) {
			Clients.showNotification("No selected element!");
		}
		else
		{	
			DraggableTreeComponent.removeFromParent(selectedElement);
			root.recomputeSpacersRecursive();
		}
		
	}
}
