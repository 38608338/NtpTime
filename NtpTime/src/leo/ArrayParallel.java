package leo;

/**
 * ��ͼ��һ���ܼ򵥵�ָ��������ӣ�
 * ���Կ����ڶ���ѭ��������������Ϊû��ǰ������������ϵ��
 * ���Ա�CPU�������ٶ��������Ե����ơ�
 * @author leo
 *
 */
public class ArrayParallel {
	public static void main(String[] args) {
		int n = 100000000;
		int v[] = { 0, 0 };

		long start = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			v[0]++;
			v[0]++;
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		start = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			v[0]++;
			v[1]++;
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
