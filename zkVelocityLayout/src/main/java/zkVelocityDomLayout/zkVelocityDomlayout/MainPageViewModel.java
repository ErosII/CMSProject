package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.*;

public class MainPageViewModel {
	
	private DraggableTreeElement root = new DraggableTreeElement(null, "Sample Page");
	private DraggableTreeModel model;
	private DraggableTreeElement selectedElement;
	private List<String> fragmentList;
	private FragmentType selectedFragment;

	public MainPageViewModel(){
		new DraggableTreeElement(root,"First Element");
		new DraggableTreeElement(root,"Second Element");
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

	public void setSelectedElement(DraggableTreeElement selectedElement) {
		this.selectedElement = selectedElement;
	}
	
	public DraggableTreeElement getSelectedElement() {
		return selectedElement;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS
	@GlobalCommand
	@NotifyChange("*")
	public void addNode(@BindingParam("nodeTitle") String nodeTitle) {
		new DraggableTreeElement(selectedElement,nodeTitle);
		root.recomputeSpacersRecursive();
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
