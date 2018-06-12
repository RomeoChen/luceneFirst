package test.java;

import main.java.TikaBasic;
import org.junit.Test;

import java.io.File;

/**
 * @author 陈梓桐
 * @date 20:22 2018/6/11
 */
public class TikaBasicTest {

    @Test
    public void test(){
        TikaBasic tikaBasic = new TikaBasic();
        String txt = tikaBasic.fileToTxt(new File("C://lucene//data//test1.docx"));
//        System.out.println(txt);
    }
}
