package com.operate.common;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

public class BeanUtils {
	
	private static final String RT_1 = "\r\n";
	private static final String RT_2 = RT_1+RT_1;
	private String path = "";
	private String url = "";

	/**
	 * 自动生成仓库接口
	 * @param c
	 * @param packageName
	 * @throws Exception
	 */
	public void createBeanDao(Class c, String packageName) throws Exception {
		path = "src/main/java/com/operate/repository/"+packageName;
		url = "com.operate.repository."+packageName;
		String cName = c.getName();
		String className = getLastChar(cName);
		String projectPath = System.getProperty("user.dir");
		String javaPath = projectPath+"/"+ path;
		File root = new File(javaPath);
		if(!root.isDirectory()){
			root.mkdirs();
		}
		String fileName = javaPath+"/" + className+ "Repository.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package "+url+";"+RT_2+"import com.operate.repository.HibernateRepository;"
				+RT_1+"import "+cName+";"+RT_2+"public interface " +  className + "Repository extends HibernateRepository<"+ className +">{"+RT_2+"}");
		fw.flush();
		fw.close();
		System.out.println(className+"Repository创建成功");
	}
	
	/**
	 * 自动生成仓库实现类
	 * @param c
	 * @param packageName
	 * @throws Exception
	 */
	public void createBeanDaoImpl(Class c, String packageName) throws Exception {
		path = "src/main/java/com/operate/repository/"+packageName+"/impl";
		url = "com.operate.repository."+packageName+".impl";
		String cName = c.getName();
		String className = getLastChar(cName);
		String projectPath = System.getProperty("user.dir");
		String javaPath = projectPath+"/"+ path;
		String interaceName = className+"Repository";
		String interfacePath = "com.operate.repository."+packageName+"."+interaceName;
		File root = new File(javaPath);
		if(!root.isDirectory()){
			root.mkdirs();
		}
		String fileName = javaPath+"/" + className+ "RepositoryImpl.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package "+url+";"+RT_2+"import com.operate.repository.HibernateRepositoryImpl;"
				+RT_1+"import org.springframework.stereotype.Repository;"
				+RT_1+"import "+interfacePath+";"
				+RT_1+"import "+cName+";"+RT_2+"@Repository"+RT_1+"public class " +  className + "RepositoryImpl extends HibernateRepositoryImpl<"+ className 
				+"> implements "+interaceName+" {"+RT_2+"}");
		fw.flush();
		fw.close();
		System.out.println(className+"RepositoryImpl创建成功");
	}
	
	/**
	 * 自动生成Controller
	 * @param c
	 * @param packageName
	 * @throws Exception
	 */
	public void createBeanController(Class c, String packageName) throws Exception {
		path = "src/main/java/com/operate/controller/" + packageName;
		url = "com.operate.controller." + packageName;
		String cName = c.getName();
		String className = getLastChar(cName);
		String projectPath = System.getProperty("user.dir");
		String javaPath = projectPath + "/" + path;
		File root = new File(javaPath);
		if (!root.isDirectory()) {
			root.mkdirs();
		}
		String smallName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String fileName = javaPath + "/" + className + "Controller.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("package " + url + ";" + RT_2 + "import org.springframework.web.bind.annotation.RequestMapping;"+ RT_1 
				+ "import org.springframework.web.bind.annotation.ResponseBody;" + RT_1 
				+"import org.springframework.stereotype.Controller;"+ RT_1
				+"import com.operate.tools.CommonUtil;"+ RT_1
				+"import org.springframework.validation.BindingResult;"+ RT_1
				+"import javax.servlet.http.HttpServletRequest;"+ RT_1
				+"import javax.validation.Valid;"+ RT_1
				+"import java.util.ArrayList;"+ RT_1
				+"import com.operate.tools.Groups;"+ RT_1
				+"import com.operate.tools.Page;"+ RT_1
				+ "import com.operate.tools.TableVo;" + RT_1 + "import " + cName + ";" + RT_2
				+ "@RequestMapping(\"/"+smallName+"\")" + RT_1
				+ "@Controller" + RT_1
				+ "public class " + className + "Controller {" + RT_2
				+"    @RequestMapping(\"/\")"+ RT_1
				+"    public String index() {"+ RT_1
				+"        return \""+packageName+"/"+smallName+"\";"+ RT_1
				+"    }"+ RT_2
				+"    @RequestMapping(\"load"+className+"\")" + RT_1
				+"    @ResponseBody" + RT_1
				+"    public TableVo load"+className+"(@Valid TableVo tableVo, BindingResult result, HttpServletRequest request) {"+ RT_1
				+"        if (result.hasErrors()) {"+ RT_1
				+"            tableVo.setAaData(new ArrayList<>());"+ RT_1
				+"            tableVo.setiTotalDisplayRecords(0);"+ RT_1
				+"            tableVo.setiTotalRecords(0);"+ RT_1
				+"            return tableVo;"+ RT_1
				+"        }"+ RT_1
				+"        int pageSize = tableVo.getiDisplayLength();"+ RT_1
				+"        int index = tableVo.getiDisplayStart();"+ RT_1
				+"        int currentPage = index / pageSize + 1;"+ RT_1
				+"        String params = tableVo.getsSearch();"+ RT_1
				+"        int col = tableVo.getiSortCol_0();"+ RT_1
				+"        String dir = tableVo.getsSortDir_0();"+ RT_1
				+"        String colname = request.getParameter(\"mDataProp_\" + col);"+ RT_1
				+"        Groups groups = CommonUtil.filterGroup(params);"+ RT_1
				+"        groups.setOrderby(colname);"+ RT_1
				+"        groups.setOrder(dir);"+ RT_1
				+"        Page<"+className+"> page = new Page<"+className+">(pageSize,currentPage);"+ RT_1
				+"        int total = page.getTotalCount();"+ RT_1
				+"        tableVo.setAaData(page.getItems());"+ RT_1
				+"        tableVo.setiTotalDisplayRecords(total);"+ RT_1
				+"        tableVo.setiTotalRecords(total);"+ RT_1
				+"        return tableVo;"+ RT_1
				+"    }"+ RT_2
				+ "}");
		fw.flush();
		fw.close();
		System.out.println(className + "Controller创建成功");
	}
	
	/**
	 * 自动生成js文件
	 * @param c
	 * @param packageName
	 * @throws Exception
	 */
	public void createBeanJavascript(Class c,String packageName) throws Exception{
		path = "src/main/resources/public/js/" + packageName;
		String cName = c.getName();
		String className = getLastChar(cName);
		String projectPath = System.getProperty("user.dir");
		String javaPath = projectPath + "/" + path;
		File root = new File(javaPath);
		if (!root.isDirectory()) {
			root.mkdirs();
		}
		
		String beanColumns = "{}";
		Field[] fieldlist = c.getDeclaredFields();
		if(fieldlist.length > 0){
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("    {'title' : 'ID','class' : 'center','width' : '80px','data' : 'id'},"+ RT_1);
			for (int i = 0; i < fieldlist.length; i++) {
				Field fld = fieldlist[i];
				String fieldName = fld.getName();
				if(!"serialVersionUID".equals(fieldName)){
					if(i<fieldlist.length-1){
						sBuffer.append("    {'title':'"+fieldName+"','class':'center','sortable':false,'data' : '"+fieldName+"'},"+ RT_1);
					}else{
						sBuffer.append("    {'title':'"+fieldName+"','class':'center','sortable':false,'data' : '"+fieldName+"'}"+ RT_1);
					}
				}
			}
			beanColumns = sBuffer.toString();
		}
		
		String smallName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String fileName = javaPath + "/" + smallName + ".js";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("var "+smallName+"Table;"+ RT_1);
		fw.write("$(function(){"+ RT_1);
		fw.write("var objData = ["+ RT_1);
		fw.write(beanColumns);
		fw.write("];"+ RT_1);
		fw.write("    "+smallName+"Table = initTables(\""+smallName+"Table\", \"load"+className+"\", objData, false,false,null, function() {});"+ RT_1);
		fw.write("});"+ RT_1);
		fw.write("function search(){"+ RT_1);
		fw.write("    searchButton("+smallName+"Table);"+ RT_1);
		fw.write("}"+ RT_1);
		fw.flush();
		fw.close();
		System.out.println(smallName + ".js创建成功");
	}
	
	/**
	 * 自动创建html文件
	 * @param c
	 * @param packageName
	 * @throws Exception
	 */
	public void createBeanHtml(Class c,String packageName) throws Exception{
		path = "src/main/resources/templates/" + packageName;
		String cName = c.getName();
		String className = getLastChar(cName);
		String projectPath = System.getProperty("user.dir");
		String javaPath = projectPath + "/" + path;
		File root = new File(javaPath);
		if (!root.isDirectory()) {
			root.mkdirs();
		}
		String smallName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String fileName = javaPath + "/" + smallName + ".html";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("<!DOCTYPE html>"+ RT_1);
		fw.write("<html xmlns:th=\"http://www.thymeleaf.org\""+ RT_1);
		fw.write("    xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity4\">"+ RT_1);
		fw.write("<head>"+ RT_1);
		fw.write("<meta charset=\"UTF-8\" />"+ RT_1);
		fw.write("<title>"+smallName+"</title>"+ RT_1);
		fw.write("<head th:include=\"tag::tag\" />"+ RT_1);
		fw.write("<script type=\"text/javascript\" th:src=\"@{/js/"+packageName+"/"+smallName+".js}\"></script>"+ RT_1);
		fw.write("</head>"+ RT_1);
		fw.write("<body>"+ RT_1);
		fw.write("<div class=\"page-header\">&nbsp;</div>"+ RT_1);
		fw.write("<div class=\"left-10 de-size-wrapper\" id=\"searchDiv\">"+ RT_1);
		fw.write("</div>"+ RT_1);
		fw.write("<div class=\"col-md-12\" style=\"overflow-y: auto; overflow-x: auto\">"+ RT_1);
		fw.write("    <table id=\""+smallName+"Table\" class=\"table table-striped table-bordered table-hover\"></table>"+ RT_1);
		fw.write("</div>"+ RT_1);
		fw.write("</body>"+ RT_1);
		fw.write("</html>"+ RT_1);
		fw.flush();
		fw.close();
		System.out.println(smallName + ".html创建成功");
	}
	

	public String getLastChar(String str) {
		if ((str != null) && (str.length() > 0)) {
			int dot = str.lastIndexOf('.');
			if ((dot > -1) && (dot < (str.length() - 1))) {
				return str.substring(dot + 1);
			}
		}
		return str;
	}

}
