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
 * 操作数据库的工具类
 * @author wxt
 * */
public class DBHelper {

	public static final String url="jdbc:mysql://127.0.0.1:3306/test"
	+"?useUnicode=true&characterEncoding=utf8";
	public static final String user="root";
	public static final String password="";
	//jdbc链接数据库访问数据需要该包的支持
	public static final String name="com.mysql.jdbc.Driver";
	private Connection connection;
	public PreparedStatement pst;
	public DBHelper(String sql){
		//ctrl+1
		try{
			//使用工具类加载驱动
			Class.forName(name);
			System.out.println("驱动加载成功");
			//获取连接
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("成功获取连接");
			//准备可执行的sql语句
			pst= connection.prepareStatement(sql);
		}catch(ClassNotFoundException e){
           e.printStackTrace();
           System.out.println("驱动加载失败");
		
		}catch(SQLException e){
	           e.printStackTrace();
	           System.out.println("获取连接失败");
	   	}
	}
	/**
	 * 关闭数据库连接
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
 * 存储数据到数据库
 * @Param bean
 * @return
 * */	
	public static boolean addBmi(BmiBean bean){
		String sql="insert into bmiinfo(date,height,weight,bmi) values(?,?,?,?)";
		DBHelper db=new DBHelper(sql);
		try {
			//替换占位符
			db.pst.setString(1, bean.getDate());
			db.pst.setString(2, bean.getHeight());
			db.pst.setString(3, bean.getWeight());
			db.pst.setString(4, bean.getBmi());
			//执行sql语句
			int update=db.pst.executeUpdate();
			if(update>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			//关闭连接
			db.close();
		}
		return false;
	}
	
	/**
	 * 获取数据
	 * */	
		public static List<BmiBean> getBmi(){
			String sql="select * from bmiinfo";
			DBHelper db=new DBHelper(sql);
			List<BmiBean> list=null;
			//alt+上下键，选中当前行进行上下移动
			try {
				//执行sql语句
				ResultSet set = db.pst.executeQuery();
				list=new ArrayList<BmiBean>(); 
				while(set.next()){
					//把数据库中获取的数据存入bean中，add list中
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
		 * 删除数据
		 * */	
			public static boolean deleteBmi(String sign){
				//把string类型转成int型
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
