package main.java;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
/**
 *
 * @author 陈梓桐
 */
public class Indexer {
    /**
     * 写索引
     */
    private IndexWriter writer;

    /**
     *
     * @param indexDir index directory
     * @throws IOException if there is a IO error
     */
    private Indexer(String indexDir) throws IOException {
        //得到索引所在目录的路径
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        // 标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        //保存用于创建IndexWriter的所有配置。
        IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
        //实例化IndexWriter
        writer = new IndexWriter(directory, iwConfig);
    }

    /**
     * 关闭写索引
     *
     * @throws IOException if there is a IO error
     */
    private void close() throws IOException {
        writer.close();
    }

    private int index(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        if(files != null){
            for (File file : files) {
            //索引指定文件
            indexFile(file);
            }
        }

        //返回索引了多少个文件
        return writer.numDocs();

    }

    /**
     * 索引指定文件
     *
     * @param file write document file into index file
     */
    private void indexFile(@NotNull File file) throws Exception {
        //输出索引文件的路径
        System.out.println("索引文件：" + file.getCanonicalPath());
        //获取文档，文档里再设置每个字段
        Document doc = getDocument(file);
        //开始写入,就是把文档写进了索引文件里去了；
        writer.addDocument(doc);
    }

    /**
     * 获取文档，文档里再设置每个字段
     *
     * @param file get Document of file
     * @return document
     */
    private Document getDocument(File file) throws Exception {
        Document doc = new Document();
        //把设置好的索引加到Document里，以便在确定被索引文档
        doc.add(new TextField("contents", new FileReader(file)));
        //Field.Store.YES：把文件名存索引文件里，为NO就说明不需要加到索引文件里去
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        //把完整路径存在索引文件里
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    public static void main(String[] args) {
        //索引指定的文档路径
        String indexDir = "C:\\lucene\\dataindex";
        ////被索引数据的路径
        String dataDir = "C:\\lucene\\data";
        Indexer indexer = null;
        int numIndexed = 0;
        //索引开始时间
        long start = System.currentTimeMillis();
        try {
            indexer = new Indexer(indexDir);
            numIndexed = indexer.index(dataDir);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if(indexer!=null) {
                    indexer.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //索引结束时间
        long end = System.currentTimeMillis();
        System.out.println("索引：" + numIndexed + " 个文件 花费了" + (end - start) + " 毫秒");
    }

}