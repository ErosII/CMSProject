package zkVelocityDomLayout.zkVelocityDomlayout;


import org.zkoss.bind.annotation.NotifyChange;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
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
	
	public MainPageViewModel(){
		attributeDataMap = new HashMap<String,String>();
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("id", "root");
		root= new DraggableTreeCmsElement(null, "root", null, rootMap);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS
	public List<FragmentType> getFragmentList() {
		return FragmentMap.getFragmentList();
	}
	
	public void setFragmentMap(Map<FragmentType, HashMap<String, Boolean>> fragmentMap) {
		this.fragmentMap = fragmentMap;
	}

	public Map<FragmentType, HashMap<String, Boolean>> getFragmentMap() {
		return FragmentMap.getFragmentMap();
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////COMMANDS		
	@Command
	@NotifyChange("*")
	public void deleteNode(){
		DraggableTreeComponent.removeFromParent(selectedElement);
		root.recomputeSpacersRecursive();
		idList.remove(selectedElement.getAttributeDataMap().get("id"));
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
		
		System.out.println( fragmentMap.get(selectedFragment));
		System.out.println( fragmentMap.get(selectedFragment).toString());
		//errString=checkFields(idList, selectedFragment, attributeDataMap, fragmentMap.get(selectedFragment));
//		if ((errString.equals(""))==true) {
//			
//			Clients.showNotification("Tutto OK");	
//			
////			attributeDataMap=generateFragment();
////			///////////////////////////////////////////////////////////////////////////////////////////////////////////
////			////// INITIALIZING COMPONENT ELEMENTS WITH MAIN PAGE ELEMENTS
////			componentIdList=mainPageIdList;
////			componentSelectedElement = mainPageselectedElement;
////			///////////////////////////////////////////////////////////////////////////////////////////////////////////
////			////// NODE ADDING
////			new DraggableTreeCmsElement(componentSelectedElement, fragmentId, selectedFragment, attributeDataMap);
////			componentIdList.add(fragmentId);
////			Map<String, Object> args = new HashMap<String, Object>();
////			args.put("selectedElement", componentSelectedElement);
////			args.put("idList", componentIdList);
////			BindUtils.postGlobalCommand(null, null, "reloadMainPageTree", args);
////			// RESET WINDOW SELECTIONS OR CONTENT
////			resetPopUpSelectionAndBack();
//		}else {
//			addPopupVisibility=true;
//			Clients.showNotification(errString);	
//		}
	}
    @Command
    @NotifyChange("attributeDataMap")
    public void resetHashMap() {
    	for(String currentKey:attributeDataMap.values()) {
    		attributeDataMap.put(currentKey, "");
    	}
    	
    	
    }
    
    @Command
    @NotifyChange("*")
    public void resetPopUpSelectionAndBack() {
    	resetHashMap();
    	addPopupVisibility=false;
    }
    
    
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////UTILITIES
	private String checkFields(ArrayList<String> idList, FragmentType selectedType, Map<String, String> attributeMap, HashMap<String, Boolean> controlComponentMap) {
		String errMsgFun = "";

		for (Boolean currentCheck : controlComponentMap.values()) {
			for (String currentCheckName : controlComponentMap.keySet()) {
				
				for (String currentAttributeValue : attributeMap.values()) {
					for (String currentAttributeName : attributeMap.keySet()) {
						if (currentCheck && currentCheckName.equals(currentAttributeName)) {
	
							if (currentAttributeValue == "" || currentAttributeValue == null) {
								errMsgFun += currentAttributeName;
								errMsgFun += " \n";
							}
						}
					}		
				}
			}
		}
		
		if((errMsgFun.equals(""))==false) {
			errMsgFun += " empty. Please insert all the data. \n";
		}
		
		if(idList.contains(attributeMap.get("id"))){
			errMsgFun += "Node Id already exists. Please change it";
		}
		
		return errMsgFun;
	}

}
