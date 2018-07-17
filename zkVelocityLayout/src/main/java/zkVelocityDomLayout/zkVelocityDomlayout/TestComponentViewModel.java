package zkVelocityDomLayout.zkVelocityDomlayout;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
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
	
	private FileWriter out;
	private FragmentType fragmentTypeDef;
	private Map<String,String> attributeDataMap;
	
	private DraggableTreeCmsElement componentRoot;
	private DraggableTreeModel componentModel;
	private DraggableTreeCmsElement componentSelectedElement;
	
	private String fragmentId;
	private String contentString;
	private String colorAttribute;
	private List<FragmentType> fragmentList;
	private FragmentType selectedFragment;
	
	private boolean addPopupVisibility = false;
	private boolean modifyPopupVisibility = false;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS MASK
	public List<FragmentType> getFragmentList() {
		return FragmentList.getFragmentList();
	}
	
	public FragmentType getSelectedFragment() {
		return selectedFragment;
	}

	public void setSelectedFragment(FragmentType selectedFragment) {
		this.selectedFragment = selectedFragment;
	}
	
	public boolean isModifyPopupVisibility() {
		return modifyPopupVisibility;
	}

	public void setModifyPopupVisibility(boolean modifyPopupVisibility) {
		this.modifyPopupVisibility = modifyPopupVisibility;
	}
	
	public boolean isAddPopupVisibility() {
		return addPopupVisibility;
	}

	public void setAddPopupVisibility(boolean addPopupVisibility) {
		this.addPopupVisibility = addPopupVisibility;
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
	
	public DraggableTreeCmsElement getComponentTreeElement() {
		return componentRoot;
	}

	public void setComponentTreeElement(DraggableTreeCmsElement componentTreeElement) {
		this.componentRoot = componentTreeElement;
	}

	public DraggableTreeModel getComponentModel() {
		return componentModel;
	}

	public void setComponentModel(DraggableTreeModel componentModel) {
		this.componentModel = componentModel;
	}
	
	public DraggableTreeCmsElement getComponentSelectedElement() {
		return componentSelectedElement;
	}

	public void setComponentSelectedElement(DraggableTreeCmsElement componentSelectedElement) {
		this.componentSelectedElement = componentSelectedElement;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS
	@Command
	@NotifyChange("*")
	public void addComponent(@BindingParam("selectedElement") DraggableTreeCmsElement mainPageselectedElement,
							 @BindingParam("root") DraggableTreeCmsElement mainPageRoot,
							 @BindingParam("fragmentType") FragmentType componentType) throws Exception{
		
		attributeDataMap=generateFragment();
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// INITIALIZING COMPONENT ELEMENTS WITH MAIN PAGE ELEMENTS
		componentRoot = mainPageRoot;
		componentSelectedElement = mainPageselectedElement;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// NODE ADDING
		new DraggableTreeCmsElement(componentSelectedElement, fragmentId, componentType, attributeDataMap);
		componentRoot.recomputeSpacersRecursive();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("selectedElement", componentSelectedElement);
		BindUtils.postGlobalCommand(null, null, "reloadMainPageTree", args);
		// RESET WINDOW SELECTIONS OR CONTENT
		resetPopUpSelectionAndBack();

	}
    
	@Command
	@NotifyChange("*")
	public void updateComponent(@BindingParam("selectedElement") DraggableTreeCmsElement mainPageselectedElement) throws Exception{
		
		attributeDataMap=generateFragment();
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// INITIALIZING COMPONENT ELEMENTS WITH MAIN PAGE ELEMENTS
		componentSelectedElement = mainPageselectedElement;
		componentSelectedElement.setAttributeDataMap(attributeDataMap);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("selectedElement", componentSelectedElement);
		BindUtils.postGlobalCommand(null, null, "reloadMainPageTree", args);
		// RESET WINDOW SELECTIONS OR CONTENT
		resetPopUpSelectionAndBack();
	}
	
	@Command
	@NotifyChange ("colorAttribute")
	public void saveColor(@BindingParam ("colorAttribute") String color) {
		this.colorAttribute=color;
	}
	
	@Command
	@NotifyChange ("*")
	public void resetPopUpSelectionAndBack() {
		addPopupVisibility=false;
        fragmentId=null;
    	contentString=null;
    	colorAttribute=null;
       	selectedFragment=null;
	}
	
	@Command
	public void showSelectedFragment() {
		if (selectedFragment==null) {
			Clients.showNotification("No selected element!");
		}	
		else
		{	
			Clients.showNotification(selectedFragment.toString() + " selected");
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////// PRIVATE UTILITIES
	public Map<String, String> generateFragment() throws Exception {
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// ADDING ATTRIBUTES TO HASHMAP
		Map<String, String> attributeDataMap = new HashMap<String, String>();
		attributeDataMap.put("id", fragmentId);
		attributeDataMap.put("contentString", contentString);
		attributeDataMap.put("colorAttribute", colorAttribute);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// SETTING UP VELOCITY
		VelocityEngine engine = new VelocityEngine();
		ServletContext sc = WebApps.getCurrent().getServletContext();
		engine.setApplicationAttribute("javax.servlet.ServletContext", sc);
		engine.setProperty("resource.loader", "webapp");
		engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
		engine.setProperty("webapp.resource.loader.path", "/templateFolder/");
		engine.init();
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// GETTING THE TEMPLATE AND USE IT
		Template template = engine.getTemplate("template.vm");
		VelocityContext vc = new VelocityContext();
		vc.put("attributeDataMap", attributeDataMap);
		StringWriter writer = new StringWriter();
		template.merge(vc, writer);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// SAVING OUTPUT
		String path = System.getProperty("user.home");
		out = new FileWriter(path + "/git/CMSProject/zkVelocityLayout/src/main/webapp/templateFolder/mod.html");
		out.write(writer.toString());
		out.close();
		System.out.println(writer);
		return attributeDataMap;
	}
}
