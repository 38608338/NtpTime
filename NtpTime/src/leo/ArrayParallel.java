package leo;

/**
 * 下图是一个很简单的指令并发的例子，
 * 可以看到第二个循环里的两个语句因为没有前后数据依赖关系，
 * 可以被CPU并发，速度上有明显的优势。
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
