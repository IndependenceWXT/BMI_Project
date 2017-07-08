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
	private HttpServletRequest request;//������
	private HttpServletResponse response;//��Ӧ��
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
			//��ʼ������
		this.request=request;
		this.response=response;
		//�����������ñ����ʽ
		this.request.setCharacterEncoding("utf-8");
		this.response.setCharacterEncoding("utf-8");
		//����Ӧ���ݽ������±���
		this.response.setContentType("text/html;charset=utf-8");
		//������
		this.response.setHeader("Access-Control-Allow-Origin", "*");
		writer = this.response.getWriter();
		//�������룬��ȡ�������
		String method = this.request.getParameter("method");
//		switch(method){//��֧��jdk1.6
//		case "add_bmi"://�������
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
		//�����
		writer.flush();
	}

	private void deleteBmi() {
		//��ȡAjax��������sign����
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
		//ʹ��gson��list����ת����Ϊstring���͵��ַ���
		String json=gson.toJson(list);
		System.out.println(json);
		//��String���͵��ַ���ת����jsonArray
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
		//��ȡ�������ݴ浽���ݿ���
		boolean success=DBHelper.addBmi(bean);
		if(success){
			//�������ݿ�ɹ���������ת��json����ǰ̨ҳ��
			JsonArray array = getBmiData();
			writer.print(array);
		}
		//����ʧ��ʱ��ʲôҲ�����أ�����Ϊ��
	}
	public void init() throws ServletException {
	}

}
