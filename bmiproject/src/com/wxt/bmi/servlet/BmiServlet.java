package com.wxt.bmi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.wxt.bmi.bean.BmiBean;
import com.wxt.bmi.dao.DBHelper;

public class BmiServlet extends HttpServlet {
	private static final long serialVersionUID=1L;
	private HttpServletRequest request;//请求码
	private HttpServletResponse response;//响应码
	private PrintWriter writer;
	public BmiServlet() {
		super();
	}
	public void destroy() {
		super.destroy();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			//初始化变量
		this.request=request;
		this.response=response;
		//给请求码设置编码格式
		this.request.setCharacterEncoding("utf-8");
		this.response.setCharacterEncoding("utf-8");
		//给响应内容进行重新编码
		this.response.setContentType("text/html;charset=utf-8");
		//跨域处理
		this.response.setHeader("Access-Control-Allow-Origin", "*");
		writer = this.response.getWriter();
		//从请求码，获取请求参数
		String method = this.request.getParameter("method");
//		switch(method){//不支持jdk1.6
//		case "add_bmi"://添加数据
//			addBmi();
//			break;
//		default:
//			break;
//		}
		if(method.equals("add_bmi")){
			addBmi();
		}else if(method.equals("start_bmi")){
			JsonArray array = getBmiData();
			writer.print(array);
		}else if(method.equals("delete_bmi")){
			deleteBmi();
		}
//		writer.println("success");
		//清空流
		writer.flush();
	}

	private void deleteBmi() {
		//获取Ajax传过来的sign参数
		String sign=this.request.getParameter("sign");
		boolean success=DBHelper.deleteBmi(sign);
		if(success){
			writer.println(1);
		}else{
			writer.println(0);
		}
		
	}
	private JsonArray getBmiData() {
		List<BmiBean> list=DBHelper.getBmi();
		Gson gson=new Gson();
		//使用gson把list数组转换成为string类型的字符串
		String json=gson.toJson(list);
		System.out.println(json);
		//把String类型的字符串转换成jsonArray
		JsonArray array=new JsonParser().parse(json).getAsJsonArray();
		return array;
		
	}
	private void addBmi() {
		String height=this.request.getParameter("height");
		String weight=this.request.getParameter("weight");
		String bmi=this.request.getParameter("bmi");
		String date=this.request.getParameter("datetime");
		BmiBean bean=new BmiBean();
		bean.setBmi(bmi);
		bean.setDate(date);
		bean.setHeight(height);
		bean.setWeight(weight);
		//把取到的数据存到数据库中
		boolean success=DBHelper.addBmi(bean);
		if(success){
			//插入数据库成功，把数据转成json传到前台页面
			JsonArray array = getBmiData();
			writer.print(array);
		}
		//插入失败时，什么也不返回，返回为空
	}
	public void init() throws ServletException {
	}

}
