package com.bootvue.utils.doc;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * 导出csv 文件
 */
public class CsvUtil {

    /**
     * @param datas   ,号分割的内容,  每一行数据必须以 "\n" 换行符结尾.   大量数据导出时 切割数据 循环导出
     * @param file    csv文件
     * @param charSet 文件编码, 默认GBK
     * @throws IOException
     */
    public static void exportCsv(String datas, RandomAccessFile file, Charset charSet) throws IOException {
        if (StringUtils.isEmpty(datas) || ObjectUtils.isEmpty(file)) {
            throw new RuntimeException("参数错误");
        }
        if (ObjectUtils.isEmpty(charSet)) {
            charSet = Charset.forName("GBK");
        }

        //移动指针,  写入数据
        long length = file.length();
        file.seek(length);
        file.write(datas.getBytes(charSet));
    }

    /**
     * 默认GBK 编码
     *
     * @param datas
     * @param file
     * @throws IOException
     */
    public static void exportCsv(String datas, RandomAccessFile file) throws IOException {
        CsvUtil.exportCsv(datas, file, Charset.forName("GBK"));
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(new File("D:\\test.csv"), "rw");
        List<String> headers = Arrays.asList("标题1", "标题2", "标题3", "标题4", "标题5", "标题6");
        int i = 0;
        while (i < 100) {
            if (Long.valueOf(file.length()).equals(0L)) {
                CsvUtil.exportCsv(String.join(",", headers) + "\n", file, null);
            } else {
                StringBuffer datas = new StringBuffer();
                for (int j = 0; j < 50; j++) {
                    List<String> tmp = Arrays.asList(RandomStringUtils.randomAlphanumeric(10),
                            RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10),
                            RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));
                    datas.append(String.join(",", tmp) + "\n");
                }
                CsvUtil.exportCsv(datas.toString(), file, Charset.forName("GBK"));
            }
            i++;
        }

        file.close();
    }
}
