package zkVelocityDomLayout.zkVelocityDomlayout;

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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import biz.opengate.zkComponents.draggableTree.DraggableTreeComponent;
import biz.opengate.zkComponents.draggableTree.DraggableTreeElement;
import biz.opengate.zkComponents.draggableTree.DraggableTreeModel;
import zkVelocityLayout.FragmentPackage.FragmentType;

//@ComponentAnnotation("@ZKBIND(ACCESS=both, SAVE_EVENT=onAddedNode)")
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////GETTERS AND SETTERS MASK
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
	@NotifyChange
	public void addComponent(@BindingParam("model") DraggableTreeModel mainPageModel,
							 @BindingParam("selectedElement") DraggableTreeCmsElement mainPageselectedElement,
							 @BindingParam("root") DraggableTreeCmsElement mainPageRoot,
							 @BindingParam("fragmentType") FragmentType componentType) throws Exception{
		
		Map<String,String> attributeDataMap = new HashMap<String,String>();
		attributeDataMap.put("id", fragmentId);
		attributeDataMap.put("contentString", contentString);
		attributeDataMap.put("colorAttribute", colorAttribute);
        VelocityEngine engine = new VelocityEngine();    
        
        ServletContext sc = WebApps.getCurrent().getServletContext();
        engine.setApplicationAttribute("javax.servlet.ServletContext", sc);
        		
        engine.setProperty("resource.loader", "webapp");
        engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");        
        engine.setProperty("webapp.resource.loader.path","/templateFolder/");
       
        engine.init();
                
        Template template = engine.getTemplate("template.vm");
        
        VelocityContext vc = new VelocityContext();
        vc.put("attributeDataMap", attributeDataMap);
          
        StringWriter writer = new StringWriter();
        
        template.merge(vc, writer);

         try {
        	 String path = System.getProperty("user.home");
           	 out = new FileWriter(path + "/git/CMSProject/zkVelocityLayout/src/main/webapp/templateFolder/mod.html");
        	 out.write(writer.toString());
        	 out.close();
        	 System.out.println(writer);
        	 
        	///////////////////////////////////////////////////////////////////////////////////////////////////////////
        	//////NODE ADDING
        	 System.out.println("Aggiunta nodo componente");
        	 
        		componentRoot=mainPageRoot;
        		componentModel=mainPageModel;
        		componentSelectedElement=mainPageselectedElement;
        		
        		if (componentSelectedElement==null) {
        			Clients.showNotification("No selected node!");
        		}
        		else {
        			
        			new DraggableTreeCmsElement(componentSelectedElement,fragmentId, componentType, attributeDataMap);
        			componentRoot.recomputeSpacersRecursive();
        			//Events.postEvent("onAddedNode", (Component) TestComponentViewModel.this, null);
        		}
         }catch (Exception e) {
        	 
         }
     
	}
	
	@Command
	@NotifyChange ("*")
	public void saveColor(@BindingParam ("colorAttribute") String color) {
		this.colorAttribute=color;
	}
}
