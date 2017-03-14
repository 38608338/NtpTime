package leo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JDBCParallel {
	ThreadLocal<Integer> counter =new ThreadLocal<Integer>(){
		public Integer initialValue() {  
            return 0;  
        }
	};

	public synchronized boolean isWcbszt(String nsrsbh) {//不再"Too many connections"
	//public boolean isWcbszt(String nsrsbh) {
		counter.set(counter.get() +1);
		System.out.println(Thread.currentThread().getName()+"@"+"第"+counter.get()+"次调用");
		
		Connection conn =null;
		ResultSet rs=null;
		Statement stmt =null;
		try{
			Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// 创建数据连接
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
			//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
			stmt = conn.createStatement();
			//System.out.println("第一步:根据发行时间检查该纳税人是否是本月新发行户");
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			rs = stmt.executeQuery(sql);
			//定义是否是新发行户
			boolean yjqljs=false;
			if(rs.next()){
				//获取当前系统时间（月份）
				Date dt = new Date();
				//转换日期格式为yyyy-MM
				DateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//转换日期（字符串类型）
				String nowTime = sdf.format(dt);
				//取数据库返回结果
				Object obj = rs.getObject(1);
				//发行时间与系统时间比对
				if (obj !=null && obj.toString().equals(nowTime)) {
					System.out.println(Thread.currentThread().getName()+"@"+"时间比对结果一致，为新发行户，不需要抄报税");
					return false;
				}
			}
			rs.close();

			//System.out.println("第二步：当纳税人不是新发行户，判断该纳税人是否完成抄报税");
			//如果不是新发行户
			if(!yjqljs){
				sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					System.out.println(Thread.currentThread().getName()+"@"+"纳税人已完税");
					//Object obj1 = rs.getObject(1);
					return false;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
            try {
            	if (rs !=null) {
            		rs.close();
            		System.out.println(Thread.currentThread().getName()+"@"+"rs closed");
				}
            } catch (Exception e) {
            }
            try {
                if (stmt !=null) {
                	stmt.close();
            		System.out.println(Thread.currentThread().getName()+"@"+"stmt closed");
				}
            } catch (Exception e) {
            }
            try {
            	if (conn !=null) {
    				conn.close();
            		System.out.println(Thread.currentThread().getName()+"@"+"conn closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
            conn = null;
        }
		return true;
	}
	
	public synchronized boolean isWQKzt(String nsrsbh) {
		counter.set(counter.get() +1);
		System.out.println(Thread.currentThread().getName()+"@"+"第"+counter.get()+"次调用");
		try {
			DriverManagerDataSource dataSource = new DriverManagerDataSource(); 
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/test");
			dataSource.setUsername("root");
			dataSource.setPassword("root");
			
			//java.lang.NoClassDefFoundError: org/springframework/jdbc/core/JdbcTemplate //添加spring-beans-3.2.18.RELEASE.jar解决
			JdbcTemplate template=new JdbcTemplate(dataSource);
			//Communications link failure due to underlying exception: ** BEGIN NESTED EXCEPTION ** 
			//MESSAGE: Permission denied: connect
			//使用同步方法解决
			
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			String fxsj= template.queryForObject(sql, String.class);
			String nowTime = new SimpleDateFormat("yyyy-MM").format(new Date());
			if (fxsj !=null && fxsj.equals(nowTime)) {
				System.out.println(Thread.currentThread().getName()+"@"+"时间比对结果一致，为新发行户，不需要抄报税");
				return false;
			}
			
			sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
			String nsrsbhx= template.queryForObject(sql, String.class);
			if (nsrsbhx !=null && nsrsbhx.equals(nsrsbh)) {
				System.out.println(Thread.currentThread().getName()+"@"+"纳税人已完税");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) {
		JDBCParallel t=new JDBCParallel();
		for (int i = 0; i < 1000; i++) {//Data source rejected establishment of connection,  message from server: "Too many connections"
			//for (int i = 0; i < 100; i++) {
			new Thread(t.new MyThread(t)).start();
		}
	}

	private class MyThread implements Runnable {
		JDBCParallel dnc;

		public MyThread(JDBCParallel dnc) {
			this.dnc = dnc;
		}

		@Override
		public void run() {
			while (true) {
				//synchronized (this) {//加在此处依然"Too many connections"
					//boolean result = dnc.isWcbszt("110105750119422");
					boolean result = dnc.isWQKzt("110105750119422");
					System.out.println(Thread.currentThread().getName()+"@"+ result);
				//}
				/*try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
}
