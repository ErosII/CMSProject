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
	
	private String title;
	private String subtitle;
	private String text;
	
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

	private FileWriter out;
	
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
		
		//root = null;
		System.out.println("Model " + model);
		System.out.println("Root " + root );
		
		root = new DraggableTreeElement(null,"Area");
		DraggableTreeElement B=new DraggableTreeElement(root,"Altezza");
		
		for (int i=0; i<10; i++) {
			new DraggableTreeElement(B,"Altezza "+(i+1));	
		}
		
		model = new DraggableTreeModel(root);
		
		
		System.out.println("Model " + model);
		System.out.println("Root " + root );
		
			
		System.out.println("Chiamata funzione");
	}
	
	@Command
	@NotifyChange
	public void generatePage() throws Exception{
		Map<String,String> userDataMap = new HashMap<String,String>();
		userDataMap.put("1001", title);
		userDataMap.put("1002", subtitle);
		userDataMap.put("1003", text);
        VelocityEngine engine = new VelocityEngine();    
        
        ServletContext sc = WebApps.getCurrent().getServletContext();
        engine.setApplicationAttribute("javax.servlet.ServletContext", sc);
        		
        engine.setProperty("resource.loader", "webapp");
        engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");        
        engine.setProperty("webapp.resource.loader.path","/templateFolder/");
       
        engine.init();
                
        Template template = engine.getTemplate("template.vm");
        
        VelocityContext vc = new VelocityContext();
        vc.put("userDataMap", userDataMap);
          
        StringWriter writer = new StringWriter();
        
        template.merge(vc, writer);
        
         try {
        	 out = new FileWriter("/home/piccioni/eclipse-workspace/zkVelocityLayout/src/main/webapp/templateFolder/mod.html");
        	 System.out.println("Salvo il template generato");
        	 out.write(writer.toString());
        	 out.close();
        	 System.out.println(writer);
         }catch (Exception e) {
        	 
         }
     
	}
	
	@Command
	public void comandRefresh(){
	    Executions.sendRedirect("");
	}

}
