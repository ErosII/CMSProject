package zkVelocityDomLayout.zkVelocityDomlayout;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import java.io.FileWriter;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.*;

public class DOMViewModel {
	
	private DraggableTreeElement root = new DraggableTreeElement(null, "Sample Page");
	private DraggableTreeModel model;
	private DraggableTreeElement selectedElement;

	public DOMViewModel(){
		new DraggableTreeElement(root,"First Element");
		new DraggableTreeElement(root,"Second Element");
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
		
	@GlobalCommand
	@NotifyChange("*")
	public void addNode(@BindingParam("nodeTitle") String nodeTitle) {
		new DraggableTreeElement(selectedElement,nodeTitle);
		root.recomputeSpacersRecursive();
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
