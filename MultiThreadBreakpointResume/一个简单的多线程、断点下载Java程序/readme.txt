1、设置下载、保存文件路径，设置分割块数、下载的线程数，缓冲区大小
2、开始下载
  2.1、创建任务描述文件、临时下载文件
  2.2、初始化任务文件
    2.2.1、对实际文件大小进行分块
    2.2.2、将: downURL、saveFile、sectionCount、contentLength写到任务文件
           在头部信息后写入每块的索引开始位置
  2.3、根据线程数创建并启动所有线程
    2.3.1、这里是计算当前块的长度、计算出当前线程下载的字节范围
    2.3.2、开始下载，每下载写入一次数据，更新一次线程对应的开始下载索引号