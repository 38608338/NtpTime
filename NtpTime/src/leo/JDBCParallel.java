package leo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JDBCParallel {
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
				//synchronized (this) {//���ڴ˴���Ȼ"Too many connections"
					boolean result = dnc.isWcbszt("110105750119422");
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
