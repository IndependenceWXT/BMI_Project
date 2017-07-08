package com.wxt.bmi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wxt.bmi.bean.BmiBean;

/**
 * �������ݿ�Ĺ�����
 * @author wxt
 * */
public class DBHelper {

	public static final String url="jdbc:mysql://127.0.0.1:3306/test"
	+"?useUnicode=true&characterEncoding=utf8";
	public static final String user="root";
	public static final String password="";
	//jdbc�������ݿ����������Ҫ�ð���֧��
	public static final String name="com.mysql.jdbc.Driver";
	private Connection connection;
	public PreparedStatement pst;
	public DBHelper(String sql){
		//ctrl+1
		try{
			//ʹ�ù������������
			Class.forName(name);
			System.out.println("�������سɹ�");
			//��ȡ����
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("�ɹ���ȡ����");
			//׼����ִ�е�sql���
			pst= connection.prepareStatement(sql);
		}catch(ClassNotFoundException e){
           e.printStackTrace();
           System.out.println("��������ʧ��");
		
		}catch(SQLException e){
	           e.printStackTrace();
	           System.out.println("��ȡ����ʧ��");
	   	}
	}
	/**
	 * �ر����ݿ�����
	 * */	
	public void close(){
		if(connection!=null){
			try {
				connection.close();
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connection=null;
				pst=null;
			}
		}
	}
/**
 * �洢���ݵ����ݿ�
 * @Param bean
 * @return
 * */	
	public static boolean addBmi(BmiBean bean){
		String sql="insert into bmiinfo(date,height,weight,bmi) values(?,?,?,?)";
		DBHelper db=new DBHelper(sql);
		try {
			//�滻ռλ��
			db.pst.setString(1, bean.getDate());
			db.pst.setString(2, bean.getHeight());
			db.pst.setString(3, bean.getWeight());
			db.pst.setString(4, bean.getBmi());
			//ִ��sql���
			int update=db.pst.executeUpdate();
			if(update>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			//�ر�����
			db.close();
		}
		return false;
	}
	
	/**
	 * ��ȡ����
	 * */	
		public static List<BmiBean> getBmi(){
			String sql="select * from bmiinfo";
			DBHelper db=new DBHelper(sql);
			List<BmiBean> list=null;
			//alt+���¼���ѡ�е�ǰ�н��������ƶ�
			try {
				//ִ��sql���
				ResultSet set = db.pst.executeQuery();
				list=new ArrayList<BmiBean>(); 
				while(set.next()){
					//�����ݿ��л�ȡ�����ݴ���bean�У�add list��
					BmiBean bean=new BmiBean();
					bean.setBmi(set.getString("bmi"));
					bean.setDate(set.getString("date"));
					bean.setHeight(set.getString("height"));
					bean.setWeight(set.getString("weight"));
					bean.setId(set.getInt("id"));
					list.add(bean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
			return list;
		}
		/**
		 * ɾ������
		 * */	
			public static boolean deleteBmi(String sign){
				//��string����ת��int��
				int id = Integer.valueOf(sign);
				String sql="delete from bmiinfo where id=?";
				DBHelper db=new DBHelper(sql);
				try {
					db.pst.setInt(1, id);
					int update = db.pst.executeUpdate();
					if(update>0){
						return true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return false;
			}
}
