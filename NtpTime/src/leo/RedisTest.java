package leo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisTest {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// ���л�
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// �����л�
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		// jedis.auth("admin");

		System.out.println("==================����String==================");
		System.out.println(jedis.get("mm"));
		jedis.del("mm");
		System.out.println(jedis.get("mm"));
		jedis.set("mm", "abc");
		System.out.println(jedis.get("mm"));

		jedis.set("name", "xinxin");// ��key-->name�з�����value-->xinxin
		System.out.println(jedis.get("name"));// ִ�н����xinxin

		jedis.append("name", " is my lover"); // ƴ��
		System.out.println(jedis.get("name"));

		jedis.del("name"); // ɾ��ĳ����
		System.out.println(jedis.get("name"));
		// ���ö����ֵ��
		jedis.mset("name", "liuling", "age", "23", "qq", "476777XXX");
		jedis.incr("age"); // ���м�1����
		System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-"
				+ jedis.get("qq"));

		System.out.println("==================����Map==================");
		jedis.del("user");
		System.out.println(jedis.exists("user"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "xinxin");
		map.put("age", "22");
		map.put("qq", "123456");
		jedis.hmset("user", map);
		// ��һ�������Ǵ���redis��map�����key����������Ƿ���map�еĶ����key�������key���Ը�������ǿɱ����
		List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
		System.out.println(rsmap);

		System.out.println(jedis.exists("user"));
		System.out.println(jedis.hlen("user"));
		jedis.hdel("user", "age");
		System.out.println(jedis.hlen("user"));
		System.out.println(jedis.hkeys("user"));
		System.out.println(jedis.hvals("user"));
		Iterator<String> iter = jedis.hkeys("user").iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + ":" + jedis.hmget("user", key));
		}

		System.out.println("==================����List==================");
		// ��ʼǰ�����Ƴ����е�����
		jedis.del("java framework");
		System.out.println(jedis.lrange("java framework", 0, -1));
		// ����key java framework�д����������
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "hibernate");
		// ��ȡ����������jedis.lrange�ǰ���Χȡ����
		// ��һ����key���ڶ�������ʼλ�ã��������ǽ���λ�ã�jedis.llen��ȡ���� -1��ʾȡ������
		System.out.println(jedis.lrange("java framework", 0, -1));

		jedis.del("java framework");
		jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "struts");
		jedis.rpush("java framework", "hibernate");
		System.out.println(jedis.lrange("java framework", 0, -1));

		System.out.println("==================����Set==================");
		// ���
		jedis.del("user");
		jedis.sadd("user", "liuling");
		jedis.sadd("user", "xinxin");
		jedis.sadd("user", "ling");
		jedis.sadd("user", "zhangxinxin");
		jedis.sadd("user", "who");
		// �Ƴ�noname
		System.out.println(jedis.smembers("user"));// ��ȡ���м����value
		jedis.srem("user", "who");
		System.out.println(jedis.smembers("user"));// ��ȡ���м����value
		System.out.println(jedis.sismember("user", "who"));// �ж� who
															// �Ƿ���user���ϵ�Ԫ��
		System.out.println(jedis.srandmember("user"));
		System.out.println(jedis.scard("user"));// ���ؼ��ϵ�Ԫ�ظ���

		System.out.println("==================�����������==================");
		jedis.set("mmap".getBytes(), serialize(map));
		System.out.println(jedis.get("mmap"));
		System.out.println(unserialize(jedis.get("mmap".getBytes())));
		jedis.close();
	}
}
