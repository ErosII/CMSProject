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
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;

import biz.opengate.zkComponents.draggableTree.*;

public class DOMViewModel {
		
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

	private DraggableTreeElement root;
	private DraggableTreeModel model;
	
	@Init
	@NotifyChange("*")
	public void init() {

		root = new DraggableTreeElement(null,"Pagina Esempio");
		
		model = new DraggableTreeModel(root);

	}
	
}
