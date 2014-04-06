import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskAssign {

	/**
	 * 启动下载进程
	 * 
	 * @param task
	 * @throws IOException
	 */
	public void work(Task task) throws IOException {

		File file = new File(task.getSaveFile());
		if (file.exists()) {
			return;
		}

		// 这个是记录是否下载成功。我这里也没有增加失败回复、重试之类的工作。
		final AtomicBoolean success = new AtomicBoolean(true);

		// 任务描述文件
		File taskFile = new File(task.getSaveFile() + ".r_task");

		// 真正下载的文件
		File saveFile = new File(task.getSaveFile() + ".r_save");

		boolean taskFileExist = taskFile.exists();

		RandomAccessFile taskRandomFile = null;
		RandomAccessFile downRandomFile = null;

		try {
			taskRandomFile = new RandomAccessFile(taskFile, "rw");
			downRandomFile = new RandomAccessFile(saveFile, "rw");

			// 获取文件内容长度
			long rtnLen = getContentLength(task.getDownURL());

			// 如果文件不存在，就初始化任务文件和下载文件
			if (!taskFileExist) {
				task.setContentLength(rtnLen);
				initTaskFile(taskRandomFile, task);
				downRandomFile.setLength(rtnLen);
			}
			// 任务文件存在就读取任务文件
			else {
				task.read(taskRandomFile);
				if (task.getContentLength() != rtnLen) {
					throw new RuntimeException();
				}
			}

			int secCount = task.getSectionCount();

			// 分配线程去下载，这里用到线程池
			ExecutorService es = Executors.newFixedThreadPool(task.getWorkerCount());

			// 根据线程数创建并启动所有线程
			for (int i = 0; i < secCount; i++) {
				final int j = i;
				final Task t = task;
				final RandomAccessFile f1 = taskRandomFile;
				final RandomAccessFile f2 = downRandomFile;

				es.execute(new Runnable() {
					public void run() {
						try {
							down(f1, f2, t, j);
						} catch (IOException e) {
							success.set(false);
							e.printStackTrace(System.out);
						}
					}
				});
			}

			es.shutdown();

			try {
				es.awaitTermination(24 * 3600, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			taskRandomFile.close();
			taskRandomFile = null;
			downRandomFile.close();
			downRandomFile = null;

			// 如果下载成功，去掉任务描述文件、帮下载文件改名
			if (success.get()) {
				taskFile.delete();
				saveFile.renameTo(file);
			}
		} finally {
			if (taskRandomFile != null) {
				taskRandomFile.close();
				taskRandomFile = null;
			}

			if (downRandomFile != null) {
				downRandomFile.close();
				downRandomFile = null;
			}
		}
	}

	/**
	 * 下载当前线程对应的字节范围数据
	 * 
	 * @param taskRandomFile
	 * @param downRandomFile
	 * @param task
	 * @param sectionNo
	 * @throws IOException
	 */
	public void down(RandomAccessFile taskRandomFile,
			RandomAccessFile downRandomFile, Task task, int sectionNo)
			throws IOException {

		// 这里我用HttpURLConnection下载，你也可以用HttpClient或者自己实现一个Http协议（不过貌似没有必要）

		URL u = new URL(task.getDownURL());
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();

		long start = task.getSectionsOffset()[sectionNo];
		long end = -1;

		// 这里要注意一下，这里是计算当前块的长度
		// 如果不是最后一块
		if (sectionNo < task.getSectionCount() - 1) {
			long per = task.getContentLength() / task.getSectionCount();
			end = per * (sectionNo + 1);
		}
		// 最后一块
		else {
			end = task.getContentLength();
		}

		if (start >= end) {
			System.out.println("Section has finished before. " + sectionNo);
			return;
		}

		String range = "bytes=" + start + "-" + (end - 1); // 计算出当前线程下载的字节范围
		conn.setRequestProperty("Range", range);
		conn.setRequestProperty("User-Agent", "Ray-Downer");

		try {
			conn.connect();
			
			// 检查响应码
			if (conn.getResponseCode() != 206) {
				throw new RuntimeException();
			}

			// 检查内容长度
			if (conn.getContentLength() != (end - start)) {
				throw new RuntimeException();
			}

			InputStream is = conn.getInputStream();
			byte[] temp = new byte[task.getBufferSize()];
			BufferedInputStream bis = new BufferedInputStream(is, temp.length);
			int readed = 0;

			while ((readed = bis.read(temp)) > 0) {
				long offset = task.getSectionsOffset()[sectionNo];
				synchronized (task) {
					// 下载之后顺便更新描述文件，你可能会发现这里效率比较低，在一个线程同步里进行两次文件操作。你可以自己实现一个缓冲写。
					downRandomFile.seek(offset);
					downRandomFile.write(temp, 0, readed);
					offset += readed;
					task.getSectionsOffset()[sectionNo] = offset;
					task.writeOffset(taskRandomFile); // 每下载写入一次数据，更新一次线程对应的开始下载索引号
				}
			}
		} finally {
			conn.disconnect();
		}
		System.out.println("Section finished. " + sectionNo);
	}

	/**
	 * 初始化任务文件
	 * 
	 * @param taskRandomFile
	 * @param task
	 * @throws IOException
	 */
	public void initTaskFile(RandomAccessFile taskRandomFile, Task task)
			throws IOException {
		int secCount = task.getSectionCount();
		long per = task.getContentLength() / secCount; // 每块的内容长度
		long[] sectionsOffset = new long[secCount]; // 每块的开始索引位置
		
		// 设置每块的开始索引位置
		for (int i = 0; i < secCount; i++) {
			sectionsOffset[i] = per * i;
		}
		
		task.setSectionsOffset(sectionsOffset);
		task.create(taskRandomFile);
	}

	/**
	 * 获取文件内容长度
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public long getContentLength(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		try {
			return conn.getContentLength();
		} finally {
			conn.disconnect();
		}
	}

}