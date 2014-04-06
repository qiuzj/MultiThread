import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Task {
	private int bufferSize = 64 * 1024;

	/** 文件分割块数 */
	private int sectionCount;

	/** 文件下载路径 */
	private String downURL;

	/** 文件内容长度 */
	private long contentLength;

	/** 每块的索引开始位置 */
	private long[] sectionsOffset;

	/** 线程数 */
	private int workerCount;
	
	/** 头部信息字节数 */
	public static final int HEAD_SIZE = 4096;

	/** 保存文件路径(包括文件名称) */
	private String saveFile;

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public long[] getSectionsOffset() {
		return sectionsOffset;
	}

	public void setSectionsOffset(long[] sectionsOffset) {
		this.sectionsOffset = sectionsOffset;
	}

	public int getSectionCount() {
		return sectionCount;
	}

	public String getDownURL() {
		return downURL;
	}

	public void setDownURL(String downURL) {
		this.downURL = downURL;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	public void setSectionCount(int sectionCount) {
		this.sectionCount = sectionCount;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	/**
	 * 读下载描述文件内容
	 * 
	 * @param file
	 * @throws IOException
	 */
	public synchronized void read(RandomAccessFile file) throws IOException {

		byte[] temp = new byte[HEAD_SIZE];

		file.seek(0);
		int readed = file.read(temp);
		if (readed != temp.length) {
			throw new RuntimeException();
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(temp);
		DataInputStream dis = new DataInputStream(bais);

		downURL = dis.readUTF();
		saveFile = dis.readUTF();
		sectionCount = dis.readInt();
		contentLength = dis.readLong();
		sectionsOffset = new long[sectionCount];

		for (int i = 0; i < sectionCount; i++) {
			sectionsOffset[i] = file.readLong();
		}
	}

	/**
	 * 创建下载描述文件内容
	 * 
	 * @param file
	 * @throws IOException
	 */
	public synchronized void create(RandomAccessFile file) throws IOException {
		if (sectionCount != sectionsOffset.length) {
			throw new RuntimeException();
		}

		long len = HEAD_SIZE + 8 * sectionCount;

		file.setLength(len);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		dos.writeUTF(downURL);
		dos.writeUTF(saveFile);
		dos.writeInt(sectionCount);
		dos.writeLong(contentLength);

		byte[] src = baos.toByteArray();
		byte[] temp = new byte[HEAD_SIZE];

		System.arraycopy(src, 0, temp, 0, src.length);

		file.seek(0);
		file.write(temp); // 将: downURL、saveFile、sectionCount、contentLength写到任务文件
		writeOffset(file);
	}

	/**
	 * 更新下载的过程
	 * 
	 * @param file
	 * @throws IOException
	 */
	public synchronized void writeOffset(RandomAccessFile file)
			throws IOException {
		if (sectionCount != sectionsOffset.length) {
			throw new RuntimeException();
		}

		// 在头部信息后写入每块的索引开始位置
		file.seek(HEAD_SIZE);
		for (int i = 0; i < sectionsOffset.length; i++) {
			file.writeLong(sectionsOffset[i]);
		}
	}

}