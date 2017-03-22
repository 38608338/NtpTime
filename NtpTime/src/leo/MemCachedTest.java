package leo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

public class MemCachedTest {
	public static void main(String[] args) throws IOException {
		MemcachedClient client=new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211),
				new InetSocketAddress("127.0.0.1", 11212),
				new InetSocketAddress("127.0.0.1", 11213),
				new InetSocketAddress("127.0.0.1", 11214),
				new InetSocketAddress("127.0.0.1", 11215)); 

        client.set("abcderghijklmnopqrstuvwxyzABCDERGHIJKLMNOPQRSTUVWXYZ", 3600, "英文测试通过");
        client.set("1234567890", 3600, "数字测试通过");
        client.set("中文测试中文测试", 3600, "中文测试通过");
        client.set("~!@#$%^&*2([{-+=}])_;'<>/,.", 3600, "特殊符号通过");
        
		for (int i = 0; i < 100; i++) {
	        client.get("abcderghijklmnopqrstuvwxyzABCDERGHIJKLMNOPQRSTUVWXYZ");
	        client.get("1234567890");
	        client.get("中文测试中文测试");
	        client.get("~!@#$%^&*2([{-+=}])_;'<>/,.");
		}

        Map<SocketAddress, Map<String, String>> stats = client.getStats();
        for (SocketAddress sa : stats.keySet()) {
			System.out.println(sa);
			
			Map<String, String> map = stats.get(sa);
			for (String key : map.keySet()) {
				if (key.equals("cmd_get") || key.equals("cmd_set") ||key.equals("get_hits")  ) {
					System.out.println(key+"="+map.get(key));
				}
			}
		}
        client.flush();
        System.exit(0);
	}
}
