package main.java;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * @author 陈梓桐
 * @date 19:10 2018/6/11
 */
public class TikaBasic {

    public String fileToTxt(File file){

//        创建一个自动识别文件类型的解析器parser
        Parser parser = new AutoDetectParser();

//        构造输入流
        InputStream inputStream = null;
        try{
//            新建元数据
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());

//            文件输入流
            inputStream = new FileInputStream(file);

//            内容处理器
            BodyContentHandler contentHandler = new BodyContentHandler();

//            解析器上下文
            ParseContext parseContext = new ParseContext();
            parseContext.set(Parser.class, parser);

//            执行解析
            parser.parse(inputStream, contentHandler, metadata, parseContext);

//            打印元数据
            for(String name : metadata.names()) {
                System.out.println(name + " : " + metadata.get(name));
            }
            System.out.println("内容 : " + contentHandler.toString());
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
