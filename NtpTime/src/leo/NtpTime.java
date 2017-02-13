package leo;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NtpTime {

	public static void SetTime(Date date) {
		String osName = System.getProperty("os.name");

		SimpleDateFormat sdf = new SimpleDateFormat();
		String cmd = "";
		try {
			if (osName.matches("^(?i)Windows.*$")) {// Window ϵͳ
				// ��ʽ HH:mm:ss
				sdf.applyPattern("HH:mm:ss");
				cmd = "  cmd /c time " + sdf.format(date);
				Runtime.getRuntime().exec(cmd);
				// ��ʽ��yyyy-MM-dd
				sdf.applyPattern("yyyy-MM-dd");
				cmd = " cmd /c date " + sdf.format(date);
				Runtime.getRuntime().exec(cmd);
			} else {// Linux ϵͳ
				// ��ʽ��yyyyMMdd
				sdf.applyPattern("yyyyMMdd");
				cmd = "  date -s " + sdf.format(date);
				Runtime.getRuntime().exec(cmd);
				// ��ʽ HH:mm:ss
				sdf.applyPattern("HH:mm:ss");
				cmd = "  date -s " + sdf.format(date);
				Runtime.getRuntime().exec(cmd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����ʱ��
	 * @return ��������ʱ��
	 */
	public static Date getInternetTime() {
		return getInternetTime("http://time.windows.com");
	}
	
	public static Date getInternetTime(String serverUrl) {
		URL url;
		try {
			url = new URL(serverUrl);
			URLConnection uc = url.openConnection();// �������Ӷ���
			uc.connect(); // ��������
			long ld = uc.getDate(); // ȡ����վ����ʱ��
			Date date = new Date(ld); // ת��Ϊ��׼ʱ�����
			return date;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡNTPʱ���
	 * @param serverip NTP��������ַ
	 * @return ���ر�׼ʱ���뱾��ʱ��Ĳ�ֵ����λ��
	 */
	public static double getNtpTime(String serverip){  
        int retry = 2;  
        int port = 123;  
        int timeout = 3000;  
  
        // get the address and NTP address request  
        //  
        InetAddress ipv4Addr = null;  
        try {  
            ipv4Addr = InetAddress.getByName(serverip);//����NTPʱ��������ο���ע  
               } catch (UnknownHostException e1) {  
            e1.printStackTrace();  
        }  
  
        int serviceStatus = -1;  
        DatagramSocket socket = null;  
        long responseTime = -1;  
        double localClockOffset = 0;  
        try {  
            socket = new DatagramSocket();  
            socket.setSoTimeout(timeout); // will force the  
            // InterruptedIOException  
  
            for (int attempts = 0; attempts <= retry && serviceStatus != 1; attempts++) {  
                try {  
                    // Send NTP request  
                    //  
                    byte[] data = new NtpMessage().toByteArray();  
                    DatagramPacket outgoing = new DatagramPacket(data, data.length, ipv4Addr, port);  
                    long sentTime = System.currentTimeMillis();  
                    socket.send(outgoing);  
  
                    // Get NTP Response  
                    //  
                    // byte[] buffer = new byte[512];  
                    DatagramPacket incoming = new DatagramPacket(data, data.length);  
                    socket.receive(incoming);  
                    responseTime = System.currentTimeMillis() - sentTime;  
                    double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;  
                    //����Ҫ��2208988800������Ϊ��õ���ʱ���Ǹ�������ʱ�䣬����Ҫ��ɶ�������ʱ�䣬��������뱱��ʱ����8Сʱ��ʱ��  
  
                    // Validate NTP Response  
                    // IOException thrown if packet does not decode as expected.  
                    NtpMessage msg = new NtpMessage(incoming.getData());  
                    localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;  
  
                    System.out.println("poll: valid NTP request received the local clock offset is " + localClockOffset + ", responseTime= " + responseTime + "ms");  
                    System.out.println("poll: NTP message : " + msg.toString());  
                    serviceStatus = 1;  
                } catch (InterruptedIOException ex) {  
                    // Ignore, no response received.  
                }  
            }  
        } catch (NoRouteToHostException e) {  
            System.out.println("No route to host exception for address: " + ipv4Addr);  
        } catch (ConnectException e) {  
            // Connection refused. Continue to retry.  
            e.fillInStackTrace();  
            System.out.println("Connection exception for address: " + ipv4Addr);  
        } catch (IOException ex) {  
            ex.fillInStackTrace();  
            System.out.println("IOException while polling address: " + ipv4Addr);  
        } finally {  
            if (socket != null)  
                socket.close();  
        }  
  
        // Store response time if available  
        //  
        if (serviceStatus == 1) {  
            System.out.println("responsetime=="+responseTime);  
        }
		return localClockOffset;  
    }
	
	public static void main(String[] args) {
		//System.out.println(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT"));
		//System.out.println(new Date(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT")));//Ĭ��toString()
		//System.out.println(new Date(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT")).toString());
		//System.out.println(new Date(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT")).toGMTString());
		//System.out.println(new Date(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT")).toLocaleString());

		System.out.println(new Date().toLocaleString());
		//SetTime(new Date(Date.parse("Mon, 13 Feb 2017 01:29:31 GMT")));
		Date idate=getInternetTime();
		//SetTime(idate);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(idate.toLocaleString());
		System.out.println(new Date().toLocaleString());
		
		getNtpTime("cn.ntp.org.cn");
	}
}
