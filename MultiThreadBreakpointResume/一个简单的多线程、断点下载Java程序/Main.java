import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		test3();
		System.out.println("\n\n===============\nFinished.");
	}

	public static void test1() throws IOException {
		Task task = new Task();
		task.setDownURL("http://dl2.csdn.net/down4/20070710/10140804846.rar");
		task.setSaveFile("d:/Test2.rar");
		task.setSectionCount(200);
		task.setWorkerCount(100);
		task.setBufferSize(256 * 1024);
		TaskAssign ta = new TaskAssign();
		ta.work(task);
	}

	public static void test3() throws IOException {
		Task task = new Task();
		task.setDownURL("http://labs.xiaonei.com/apache-mirror/ant/ivy/2.0.0/apache-ivy-2.0.0-bin-with-deps.tar.gz");
		task.setSaveFile("d:/apache-maven-2.1.0-bin.zip");
		task.setSectionCount(500);
		task.setWorkerCount(10);
		task.setBufferSize(128 * 1024);
		TaskAssign ta = new TaskAssign();
		ta.work(task);

	}

	public static void test2() throws IOException {
		Task task = new Task();
		task.setDownURL("http://down.sandai.net/Thunder5.7.9.472.exe");
		task.setSaveFile("c:/Thunder.exe");
		task.setSectionCount(30);
		task.setWorkerCount(30);
		task.setBufferSize(128 * 1024);
		TaskAssign ta = new TaskAssign();
		ta.work(task);
	}
}