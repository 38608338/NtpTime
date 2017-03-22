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

public class JDBCParallelClass {
	static JDBCParallelClass instance;
	
	private JDBCParallelClass(){
	}
	
	public static synchronized JDBCParallelClass getInstance(){
		if (instance ==null) {
			instance=new JDBCParallelClass();
		}
		return instance;
	}
	
	ThreadLocal<Integer> counter =new ThreadLocal<Integer>(){
		public Integer initialValue() {  
            return 0;  
        }
	};

	public synchronized boolean isWcbszt(String nsrsbh) {//����"Too many connections"
	//public boolean isWcbszt(String nsrsbh) {
		counter.set(counter.get() +1);
		System.out.println(Thread.currentThread().getName()+"@"+"��"+counter.get()+"�ε���");
		
		Connection conn =null;
		ResultSet rs=null;
		Statement stmt =null;
		try{
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// ������������
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
			//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
			stmt = conn.createStatement();
			//System.out.println("��һ��:���ݷ���ʱ�������˰���Ƿ��Ǳ����·��л�");
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			rs = stmt.executeQuery(sql);
			//�����Ƿ����·��л�
			boolean yjqljs=false;
			if(rs.next()){
				//��ȡ��ǰϵͳʱ�䣨�·ݣ�
				Date dt = new Date();
				//ת�����ڸ�ʽΪyyyy-MM
				DateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//ת�����ڣ��ַ������ͣ�
				String nowTime = sdf.format(dt);
				//ȡ���ݿⷵ�ؽ��
				Object obj = rs.getObject(1);
				//����ʱ����ϵͳʱ��ȶ�
				if (obj !=null && obj.toString().equals(nowTime)) {
					System.out.println(Thread.currentThread().getName()+"@"+"ʱ��ȶԽ��һ�£�Ϊ�·��л�������Ҫ����˰");
					return false;
				}
			}
			rs.close();

			//System.out.println("�ڶ���������˰�˲����·��л����жϸ���˰���Ƿ���ɳ���˰");
			//��������·��л�
			if(!yjqljs){
				sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					System.out.println(Thread.currentThread().getName()+"@"+"��˰������˰");
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
		System.out.println(Thread.currentThread().getName()+"@"+"��"+counter.get()+"�ε���");
		try {
			DriverManagerDataSource dataSource = new DriverManagerDataSource(); 
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/test");
			dataSource.setUsername("root");
			dataSource.setPassword("root");
			
			//java.lang.NoClassDefFoundError: org/springframework/jdbc/core/JdbcTemplate //����spring-beans-3.2.18.RELEASE.jar���
			JdbcTemplate template=new JdbcTemplate(dataSource);
			//Communications link failure due to underlying exception: ** BEGIN NESTED EXCEPTION ** 
			//MESSAGE: Permission denied: connect
			//ʹ��ͬ���������
			
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			String fxsj= template.queryForObject(sql, String.class);
			String nowTime = new SimpleDateFormat("yyyy-MM").format(new Date());
			if (fxsj !=null && fxsj.equals(nowTime)) {
				System.out.println(Thread.currentThread().getName()+"@"+"ʱ��ȶԽ��һ�£�Ϊ�·��л�������Ҫ����˰");
				return false;
			}
			
			sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
			String nsrsbhx= template.queryForObject(sql, String.class);
			if (nsrsbhx !=null && nsrsbhx.equals(nsrsbh)) {
				System.out.println(Thread.currentThread().getName()+"@"+"��˰������˰");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized boolean goood(String nsrsbh) {
			Connection conn =null;
			ResultSet rs=null;
			Statement stmt =null;
			try{
				Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// ������������
				//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
				//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
				stmt = conn.createStatement();
				//System.out.println("��һ��:���ݷ���ʱ�������˰���Ƿ��Ǳ����·��л�");
				String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				//�����Ƿ����·��л�
				boolean yjqljs=false;
				if(rs.next()){
					//��ȡ��ǰϵͳʱ�䣨�·ݣ�
					Date dt = new Date();
					//ת�����ڸ�ʽΪyyyy-MM
					DateFormat sdf = new SimpleDateFormat("yyyy-MM");
					//ת�����ڣ��ַ������ͣ�
					String nowTime = sdf.format(dt);
					//ȡ���ݿⷵ�ؽ��
					Object obj = rs.getObject(1);
					//����ʱ����ϵͳʱ��ȶ�
					System.out.println(Thread.currentThread().getName()+"#goood1@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +obj.toString()+"--"+nowTime);
					if (obj !=null && obj.toString().equals(nowTime)) {
						return false;
					}
				}
				rs.close();

				//System.out.println("�ڶ���������˰�˲����·��л����жϸ���˰���Ƿ���ɳ���˰");
				//��������·��л�
				if(!yjqljs){
					sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
					rs = stmt.executeQuery(sql);
					if (rs.next()) {
						System.out.println(Thread.currentThread().getName()+"#goood1@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +"��˰������˰");
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
					}
	            } catch (Exception e) {
	            }
	            try {
	                if (stmt !=null) {
	                	stmt.close();
					}
	            } catch (Exception e) {
	            }
	            try {
	            	if (conn !=null) {
	    				conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
	            conn = null;
	        }
			return true;
		}
	
	public synchronized boolean goood2(String nsrsbh) {
		Connection conn =null;
		ResultSet rs=null;
		Statement stmt =null;
		try{
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// ������������
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
			//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
			stmt = conn.createStatement();
			//System.out.println("��һ��:���ݷ���ʱ�������˰���Ƿ��Ǳ����·��л�");
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			rs = stmt.executeQuery(sql);
			//�����Ƿ����·��л�
			boolean yjqljs=false;
			if(rs.next()){
				//��ȡ��ǰϵͳʱ�䣨�·ݣ�
				Date dt = new Date();
				//ת�����ڸ�ʽΪyyyy-MM
				DateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//ת�����ڣ��ַ������ͣ�
				String nowTime = sdf.format(dt);
				//ȡ���ݿⷵ�ؽ��
				Object obj = rs.getObject(1);
				//����ʱ����ϵͳʱ��ȶ�
				System.out.println(Thread.currentThread().getName()+"#goood2@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +obj.toString()+"--"+nowTime);
				if (obj !=null && obj.toString().equals(nowTime)) {
					return false;
				}
			}
			rs.close();

			//System.out.println("�ڶ���������˰�˲����·��л����жϸ���˰���Ƿ���ɳ���˰");
			//��������·��л�
			if(!yjqljs){
				sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					System.out.println(Thread.currentThread().getName()+"#goood2@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +"��˰������˰");
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
				}
            } catch (Exception e) {
            }
            try {
                if (stmt !=null) {
                	stmt.close();
				}
            } catch (Exception e) {
            }
            try {
            	if (conn !=null) {
    				conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
            conn = null;
        }
		return true;
	}
	public synchronized boolean goood3(String nsrsbh) {
		Connection conn =null;
		ResultSet rs=null;
		Statement stmt =null;
		try{
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// ������������
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
			//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
			stmt = conn.createStatement();
			//System.out.println("��һ��:���ݷ���ʱ�������˰���Ƿ��Ǳ����·��л�");
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			rs = stmt.executeQuery(sql);
			//�����Ƿ����·��л�
			boolean yjqljs=false;
			if(rs.next()){
				//��ȡ��ǰϵͳʱ�䣨�·ݣ�
				Date dt = new Date();
				//ת�����ڸ�ʽΪyyyy-MM
				DateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//ת�����ڣ��ַ������ͣ�
				String nowTime = sdf.format(dt);
				//ȡ���ݿⷵ�ؽ��
				Object obj = rs.getObject(1);
				//����ʱ����ϵͳʱ��ȶ�
				System.out.println(Thread.currentThread().getName()+"#goood3@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +obj.toString()+"--"+nowTime);
				if (obj !=null && obj.toString().equals(nowTime)) {
					return false;
				}
			}
			rs.close();

			//System.out.println("�ڶ���������˰�˲����·��л����жϸ���˰���Ƿ���ɳ���˰");
			//��������·��л�
			if(!yjqljs){
				sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					System.out.println(Thread.currentThread().getName()+"#goood3@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +"��˰������˰");
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
				}
            } catch (Exception e) {
            }
            try {
                if (stmt !=null) {
                	stmt.close();
				}
            } catch (Exception e) {
            }
            try {
            	if (conn !=null) {
    				conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
            conn = null;
        }
		return true;
	}
	public synchronized boolean goood4(String nsrsbh) {
		Connection conn =null;
		ResultSet rs=null;
		Statement stmt =null;
		try{
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");// ������������
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@68.16.202.202:1521:fwsk","fwsk","asd123@ASD");
			//conn = DriverManager.getConnection(hx_dburl,hx_username,hx_password);
			stmt = conn.createStatement();
			//System.out.println("��һ��:���ݷ���ʱ�������˰���Ƿ��Ǳ����·��л�");
			String sql = "select fxsj from fx_qy_nsrxx where nsrsbh='"+nsrsbh+"'";
			rs = stmt.executeQuery(sql);
			//�����Ƿ����·��л�
			boolean yjqljs=false;
			if(rs.next()){
				//��ȡ��ǰϵͳʱ�䣨�·ݣ�
				Date dt = new Date();
				//ת�����ڸ�ʽΪyyyy-MM
				DateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//ת�����ڣ��ַ������ͣ�
				String nowTime = sdf.format(dt);
				//ȡ���ݿⷵ�ؽ��
				Object obj = rs.getObject(1);
				//����ʱ����ϵͳʱ��ȶ�
				System.out.println(Thread.currentThread().getName()+"#goood4@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +obj.toString()+"--"+nowTime);
				if (obj !=null && obj.toString().equals(nowTime)) {
					return false;
				}
			}
			rs.close();

			//System.out.println("�ڶ���������˰�˲����·��л����жϸ���˰���Ƿ���ɳ���˰");
			//��������·��л�
			if(!yjqljs){
				sql = "select nsrsbh from cb_qy_bsqk_tjb where nsrsbh = '"+nsrsbh+"'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					System.out.println(Thread.currentThread().getName()+"#goood4@"+new SimpleDateFormat("HH:mm:ss SSS  ").format(new Date()) +"��˰������˰");
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
				}
            } catch (Exception e) {
            }
            try {
                if (stmt !=null) {
                	stmt.close();
				}
            } catch (Exception e) {
            }
            try {
            	if (conn !=null) {
    				conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
            conn = null;
        }
		return true;
	}
	
	public static void main(String[] args) {
		JDBCParallelClass t=new JDBCParallelClass();
		for (int i = 0; i < 10; i++) {//Data source rejected establishment of connection,  message from server: "Too many connections"
			//for (int i = 0; i < 100; i++) {
			//new Thread(t.new MyThread(t)).start();s
			
			//Communications link failure due to underlying exception:
			//MESSAGE: Permission denied: connect
			//Last packet sent to the server was 1 ms ago.
			//new Thread(t.new MyThread(new JDBCParallel())).start();
			
			new Thread(t.new MyThread(JDBCParallelClass.getInstance())).start();
		}
	}

	private class MyThread implements Runnable {
		JDBCParallelClass dnc;

		public MyThread(JDBCParallelClass dnc) {
			this.dnc = dnc;
		}

		@Override
		public void run() {
			while (true) {
				//synchronized (this) {//���ڴ˴���Ȼ"Too many connections"
				//boolean result = dnc.isWcbszt("110105750119422");
					//boolean result = dnc.isWQKzt("110105750119422");
				boolean result = dnc.goood("110105750119422");
					result = dnc.goood2("110105750119422");
					result = dnc.goood3("110105750119422");
					result = dnc.goood4("110105750119422");
					System.out.println(Thread.currentThread().getName()+"@"+ result);
				//}
					//User root already has more than 'max_user_connections' active connections
				/*try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
}