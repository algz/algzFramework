package com.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import algz.platform.util.excel.ExcelService;
import algz.platform.util.excel.ExcelView;

import com.service.HelloService;

@Controller
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@Autowired
	private ExcelService excelService;
	
    @RequestMapping(value="/hello")
    public ModelAndView  printHello() {
        return new ModelAndView("pages/hello", "message", "Hello Spring to "+helloService.printHello()+" !");
    }
    
    
    /** 
  	* @Title: exportExcel 
  	* @Description: 导出用户数据生成的excel文件
  	* @param  model
  	* @param  request
  	* @param  response
  	* @param  设定文件 
  	* @return ModelAndView    返回类型 
  	* @throws 
  	*/
  	@RequestMapping(value="/exportExcel.shtml",method=RequestMethod.POST)  
    public ModelAndView exportExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) {  
         View excelView = new ExcelView();   
         //Map model = new HashMap();
         //model.put("users", dao.findUserAll());
         
        // Map<String, Object> obj = null;
         
         //获取数据库表生成的workbook
         //Map<String, Object> condition = new HashMap<String, Object>();
         //HSSFWorkbook workbook = activityManageService.generateWorkbook(condition);
         try {
      	   //excelView.buildExcelDocument(obj, workbook, request, response);
        	 excelView= excelService.createExcelView(model,request, response);
         } catch (Exception e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
         }
         return new ModelAndView(excelView, model);   
     }  
}
