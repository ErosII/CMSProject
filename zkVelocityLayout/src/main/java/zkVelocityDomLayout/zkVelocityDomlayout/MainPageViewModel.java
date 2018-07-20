package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;

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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.*;
import zkVelocityLayout.FragmentPackage.FragmentMap;
import zkVelocityLayout.FragmentPackage.FragmentType;

public class MainPageViewModel {

	private DraggableTreeCmsElement root;
	private DraggableTreeModel model;
	private DraggableTreeCmsElement selectedElement;
	
	private FileWriter out;
	private FragmentType selectedFragment;
	private Map<String,String> attributeDataMap;
	
	private ArrayList<String> idList = new ArrayList<String>();

	private Map<FragmentType, HashMap<String, Boolean>> fragmentMap;
	
	private List<FragmentType> fragmentList;
	
	private boolean addPopupVisibility= false;
	private boolean modifyPopupVisibility= false;
	private boolean renamePopupVisibility=false;

	@Init
	public void init(){
		attributeDataMap = new HashMap<String,String>();
		attributeDataMap.put("id", "root");
		fragmentMap = FragmentMap.getFragmentMap();
		root= new DraggableTreeCmsElement(null, "root", null, attributeDataMap);
		idList.add("root");
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public List<FragmentType> getFragmentList() {
		return FragmentMap.getFragmentList();
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
	
	public ArrayList<String> getIdList() {
		return idList;
	}

	public void setIdList(ArrayList<String> idList) {
		this.idList = idList;
	}
	
	public FragmentType getSelectedFragment() {
		return selectedFragment;
	}

	public void setSelectedFragment(FragmentType selectedFragment) {
		this.selectedFragment = selectedFragment;
	}

	public Map<String, String> getAttributeDataMap() {
		return attributeDataMap;
	}

	public void setAttributeDataMap(Map<String, String> attributeDataMap) {
		this.attributeDataMap = attributeDataMap;
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
	
	public boolean isRenamePopupVisibility() {
		return renamePopupVisibility;
	}

	public void setRenamePopupVisibility(boolean renamePopupVisibility) {
		this.renamePopupVisibility = renamePopupVisibility;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS		
	@Command
	@NotifyChange("*")
	public void deleteNode(){
		removeChildrenId(selectedElement);
		DraggableTreeComponent.removeFromParent(selectedElement);
		root.recomputeSpacersRecursive();	
	}
	
	@Command
	@NotifyChange ("colorAttribute")
	public void saveColor(@BindingParam ("colorAttribute") String color) {
		attributeDataMap.put("colorAttribute", color);
	}
	
	@Command
	@NotifyChange("*")
	public void addComponent() throws Exception{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// CHECK DATA
		String errString=null;
		errString=checkFields(idList, selectedFragment, attributeDataMap, fragmentMap.get(selectedFragment));
		
		if ((errString.equals(""))==true) {
			
			generateFragment(attributeDataMap);
			///////////////////////////////////////////////////////////////////////////////////////////////////////////
			////// ADDING A NODE
			System.out.println("Stampo map nodo selezionato");
			System.out.println(selectedElement.getTreeAttributeDataMap());
			System.out.println("Stampo map dell'utente");
			System.out.println(attributeDataMap);
			new DraggableTreeCmsElement(selectedElement, attributeDataMap.get("id") , selectedFragment, attributeDataMap);
			idList.add(attributeDataMap.get("id"));
			System.out.println("Stampo map del nodo selezionato dopo l'inserimento");
			System.out.println(selectedElement.getTreeAttributeDataMap());
			// RESET WINDOW SELECTIONS OR CONTENT
			resetPopUpSelectionAndBack();
			System.out.println("Stampo la mappa del nodo selezionato dopo il reset and bask");
			System.out.println(selectedElement.getTreeAttributeDataMap());
		}else {
			addPopupVisibility=true;
			Clients.showNotification(errString);	
		}
	}
	
	@Command
	@NotifyChange("*")
	public void updateComponent() throws Exception{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// CHECK DATA
		System.out.println(selectedElement.getTreeAttributeDataMap());
		String errString=null;
		errString=checkFields(idList, selectedElement.getFragmentTypeDef(), attributeDataMap, fragmentMap.get(selectedElement.getFragmentTypeDef()));
		
		if ((errString.equals(""))==true) {
			
			generateFragment(attributeDataMap);
			///////////////////////////////////////////////////////////////////////////////////////////////////////////
			////// NODE REMOVAL
			idList.remove(selectedElement.getTreeAttributeDataMap().get("id"));
			selectedElement.setTreeAttributeDataMap(attributeDataMap);
			selectedElement.setDescription(attributeDataMap.get("id"));
			idList.add(attributeDataMap.get("id"));
			// RESET WINDOW SELECTIONS OR CONTENT
			resetModifyPopUpAndBack();
		}else {
			modifyPopupVisibility=true;
			Clients.showNotification(errString);	
		}
	}
	
    @Command
    @NotifyChange("*")
    public void resetPopUpSelectionAndBack() {
    	resetHashMap();
    	addPopupVisibility=false;
    	selectedFragment=null;
    }
    
    @Command
    @NotifyChange("*")
    public void resetModifyPopUpAndBack() {
    	resetHashMapModify(selectedElement.getFragmentTypeDef());
    	modifyPopupVisibility=false;
    }
    
    @Command
    @NotifyChange("*")
    public void openModifyPopUp() {
    	
    	if(selectedElement.getDescription().equals(root.getDescription())) {
    		System.out.println(selectedElement);
        	System.out.println(selectedElement.getTreeAttributeDataMap());
        	renamePopupVisibility=true;
    	}
    	else {
        	System.out.println(selectedElement);
        	System.out.println(selectedElement.getTreeAttributeDataMap());
        	resetHashMapModify(selectedElement.getFragmentTypeDef());
        	System.out.println(selectedElement.getTreeAttributeDataMap());
        	modifyPopupVisibility=true;
    	}
    }
    
    @Command
    @NotifyChange("attributeDataMap")
    public void resetHashMap() {
    	System.out.println(selectedElement.getTreeAttributeDataMap());
    	//TAKING THE CONTROL HASHMAP AS REFERERENCE TO POPULATE THE DEFAULT EMPTY HASHMAP
    	for(String currentKey: fragmentMap.get(selectedFragment).keySet()) {
    		attributeDataMap.put(currentKey, "");
    	}
    	System.out.println(selectedElement.getTreeAttributeDataMap());
    	
    }
    
    @Command
    @NotifyChange("*")
    public void resetHashMapModify(@BindingParam("selectedFragment") FragmentType selectedFragment) {
    	
    	//TAKING THE CONTROL HASHMAP AS REFERERENCE TO POPULATE THE DEFAULT EMPTY HASHMAP
    	for(String currentKey: fragmentMap.get(selectedFragment).keySet()) {
    		attributeDataMap.put(currentKey, "");
    	}
    }
    
    @Command
    @NotifyChange("*")
    public void renameProject() {
    	if(attributeDataMap.get("id")=="" || attributeDataMap.get("id")==null) {
    		Clients.showNotification("Project name empty. Fill the textbox.");	
    	}
    	else {
    		idList.remove(root.getDescription());
    		root.setDescription(attributeDataMap.get("id"));
    		idList.add(attributeDataMap.get("id"));
    		// RESET WINDOW SELECTIONS OR CONTENT
    		renameBack(); 
    	}
    }
    
    @Command
    @NotifyChange("*")
    public void renameBack() {
    	renamePopupVisibility=false;
    	selectedFragment=null;
    }
    

    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////UTILITIES
        
    //CHECK IF ALL THE MANDATORY FIELDS ARE NOT EMPTY
	private String checkFields(ArrayList<String> idList, FragmentType selectedType, Map<String, String> attributeMap,
			HashMap<String, Boolean> controlComponentMap) {
		String errMsgFun = "";

		int controlKeyPosition = 0;
		int controlCheckPosition = 0;
		
		// CYCLE ON CONTROL MAP ID'S
		for (String currentCheckName : controlComponentMap.keySet()) {
			// SETTING CURRENT POSITION
			controlCheckPosition = 0;
			// CYCLE ON MAP BOOLEANS
			for (Boolean currentCheckBool : controlComponentMap.values()) {
				// ONLY ON THE DIAGONAL OF THE MATRIX
				if (controlKeyPosition == controlCheckPosition) {
					System.out.println(currentCheckName + " con controllo obbligatorio " + currentCheckBool);

					if (currentCheckBool && (attributeMap.get(currentCheckName) == "" || attributeMap.get(currentCheckName) == null)) {
						errMsgFun += currentCheckName;
						errMsgFun += " \n";
					}
				}
				controlCheckPosition++;
			}
			controlKeyPosition++;
		}

		if ((errMsgFun.equals("")) == false) {
			errMsgFun += " empty. Please insert all the mandatory data. \n";
		}

		if (idList.contains(attributeMap.get("id"))) {
			errMsgFun += "Node Id already exists. Please change it";
		}
		System.out.println(errMsgFun);
		return errMsgFun;
	}
	
	private void generateFragment(Map<String, String> userMap) throws Exception {
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
		vc.put("attributeDataMap", userMap);
		StringWriter writer = new StringWriter();
		template.merge(vc, writer);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		////// SAVING OUTPUT
		String path = System.getProperty("user.home");
		out = new FileWriter(path + "/git/CMSProject/zkVelocityLayout/src/main/webapp/templateFolder/mod.html");
		out.write(writer.toString());
		out.close();
		System.out.println(writer);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////// REMOVE CHILDREN ID
    public void removeChildrenId(DraggableTreeElement currentElement) {
     	if(currentElement.getChilds().size()>0) {
     		int listSize=currentElement.getChilds().size();
     		List<DraggableTreeElement> currentList = currentElement.getChilds();
     		
     		for(int i=0;i<listSize;i++) {
     			removeChildrenId(currentList.get(i));
     		}
     		System.out.println(currentElement.getChilds());
     	}	
     	else {
     		System.out.println("Non ho figli");
     		String currentId=currentElement.getDescription();
     		System.out.println(currentId);
     		idList.remove(currentId);
     		System.out.println("Rimosso");
     	}  		
    }
}
