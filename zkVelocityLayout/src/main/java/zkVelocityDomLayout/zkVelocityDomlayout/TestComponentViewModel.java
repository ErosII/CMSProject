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
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;

import biz.opengate.zkComponents.draggableTree.DraggableTreeElement;
import biz.opengate.zkComponents.draggableTree.DraggableTreeModel;

public class TestComponentViewModel {
	
	private FileWriter out;
	private String fragmentId;
	private FragmentType fragmentTypeDef;
	private Map<String,String> attributeDataMap;
	
	private String title;
	private String subtitle;
	private String text;
	
	public DraggableTreeModel getChildModel() {
		return childModel;
	}

	public void setChildModel(DraggableTreeModel childModel) {
		this.childModel = childModel;
	}

	public DraggableTreeElement getChildRoot() {
		return childRoot;
	}

	public void setChildRoot(DraggableTreeElement childRoot) {
		this.childRoot = childRoot;
	}

	private DraggableTreeModel childModel;
	private DraggableTreeElement childRoot;
	private DraggableTreeElement childSelectedElement;
	
	public DraggableTreeElement getSelectedElement() {
		return childSelectedElement;
	}

	public void setSelectedElement(DraggableTreeElement selectedElement) {
		this.childSelectedElement = selectedElement;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(String fragmentId) {
		this.fragmentId = fragmentId;
	}

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
	
	@Command
	@NotifyChange
	public void generatePage() throws Exception{
		
		
		Map<String,String> attributeDataMap = new HashMap<String,String>();
		attributeDataMap.put("1001", title);
		attributeDataMap.put("1002", subtitle);
		attributeDataMap.put("1003", text);
        VelocityEngine engine = new VelocityEngine();    
        
        ServletContext sc = WebApps.getCurrent().getServletContext();
        engine.setApplicationAttribute("javax.servlet.ServletContext", sc);
        		
        engine.setProperty("resource.loader", "webapp");
        engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");        
        engine.setProperty("webapp.resource.loader.path","/templateFolder/");
       
        engine.init();
                
        Template template = engine.getTemplate("template.vm");
        
        VelocityContext vc = new VelocityContext();
        vc.put("userDataMap", attributeDataMap);
          
        StringWriter writer = new StringWriter();
        
        template.merge(vc, writer);

         try {
        	 String path = System.getProperty("user.home");
           	 out = new FileWriter(path + "/git/CMSProject/zkVelocityLayout/src/main/webapp/templateFolder/mod.html");
        	 out.write(writer.toString());
        	 out.close();
        	 System.out.println(writer);
        	 
         }catch (Exception e) {
        	 
         }
     
	}
	
	@Command
	@NotifyChange ("*")
	public void addNode(@BindingParam("model") DraggableTreeModel parentModel, @BindingParam("parent") DraggableTreeElement parentRoot) {
		

		System.out.println("Aggiungo il nodo");
		System.out.println(parentRoot);
		new DraggableTreeElement(parentRoot,title);
		childModel=new DraggableTreeModel(parentRoot);
		
	}
	
	@Init
	@NotifyChange ("*")
	public void init() {
				
		DraggableTreeElement root=new DraggableTreeElement(null,"Root");
		new DraggableTreeElement(root,"Child");
		childModel=new DraggableTreeModel(root);
		
	}
	
}
