package zkVelocityDomLayout.zkVelocityDomlayout;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import java.io.FileWriter;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;

import biz.opengate.zkComponents.draggableTree.*;

public class DOMViewModel {
	
	private DraggableTreeElement root;
	private DraggableTreeModel model;
	private DraggableTreeElement selectedElement;
	
	public DraggableTreeElement getRoot() {
		return root;
	}

	public void setRoot(DraggableTreeElement root) {
		this.root = root;
	}

	public DraggableTreeModel getModel() {
		return model;
	}

	public void setModel(DraggableTreeModel model) {
		this.model = model;
	}

	public DraggableTreeElement getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(DraggableTreeElement selectedElement) {
		System.out.println("setSelectedElement - main");
		this.selectedElement = selectedElement;
	}

	@Init
	@NotifyChange("*")
	public void init() {

		root = new DraggableTreeElement(null,"Sample Page");
		new DraggableTreeElement(root,"First Element");
		new DraggableTreeElement(root,"Second Element");
		model = new DraggableTreeModel(root);

	}
	
	@GlobalCommand
	@NotifyChange("model")
	public void reloadTree(@BindingParam("componentModel") DraggableTreeModel componentModel){
		System.out.println(componentModel);
		model=componentModel;
	    System.out.println("Ricarico l'albero");
	}
	
}
