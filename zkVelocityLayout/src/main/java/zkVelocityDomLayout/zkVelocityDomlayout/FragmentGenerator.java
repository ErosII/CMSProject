package zkVelocityDomLayout.zkVelocityDomlayout;

import java.io.StringWriter;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.zkoss.zk.ui.WebApps;

import zkVelocityLayout.FragmentPackage.FragmentType;

public class FragmentGenerator {
	
	private static VelocityEngine engine;
	
	public FragmentGenerator(String templatesFolderName) throws Exception {
		////// SETTING UP VELOCITY
			engine = new VelocityEngine();
			ServletContext sc = WebApps.getCurrent().getServletContext();
			engine.setApplicationAttribute("javax.servlet.ServletContext", sc);
			engine.setProperty("resource.loader", "webapp");
			engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
			engine.setProperty("webapp.resource.loader.path", "/" + templatesFolderName +"/");
			engine.init();
	}
	
	////// GENERATE HTML FRAGMENT
	public String generateFragment(String type, Map<String, String> userMap) throws Exception {
		String templateFileName = type.toString().toLowerCase() + ".vm";
		////// GETTING THE TEMPLATE AND USE IT
		Template template = engine.getTemplate(templateFileName);
		VelocityContext vc = new VelocityContext();
		vc.put("attributeDataMap", userMap); //USED IN THE VELOCITY TEMPLATE AS $attributeDataMap
		StringWriter writer = new StringWriter();
		template.merge(vc, writer);
		return writer.toString();
	}	
	
}
