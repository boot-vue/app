package com.bootvue.utils.doc;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图像util
 */
public class ImageUtil {

    public final static float DEFAULT_SCALE = 1f;  //缩放比
    public final static float DEFAULT_QUALITY = 0.5f; //质量


    /**
     * 压缩图片
     *
     * @param in      文件流
     * @param path    压缩后的文件路径
     * @param scale   缩放比  0--1 取值 1f为原图大小
     * @param quality 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static void compressImage(InputStream in, String path, Float scale, Float quality) throws IOException {
        Thumbnails.of(in)
                .scale(scale == null ? DEFAULT_SCALE : scale)
                .outputQuality(quality == null ? DEFAULT_QUALITY : quality)
                .toFile(path);
    }

    /**
     * 压缩图片
     *
     * @param in      文件流
     * @param scale   缩放比  0--1 取值 1f为原图大小
     * @param quality 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static Thumbnails.Builder<? extends InputStream> compressImage(InputStream in, Float scale, Float quality) throws IOException {
        return Thumbnails.of(in)
                .scale(scale == null ? DEFAULT_SCALE : scale)
                .outputQuality(quality == null ? DEFAULT_QUALITY : quality);
    }

    /**
     * 压缩图片
     *
     * @param file    文件
     * @param path    压缩后的文件路径
     * @param scale   缩放比  0--1 取值 1f为原图大小
     * @param quality 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static Thumbnails.Builder<File> compressImage(File file, String path, Float scale, Float quality) throws IOException {
        Thumbnails.Builder<File> builder = Thumbnails.of(file)
                .scale(scale == null ? DEFAULT_SCALE : scale)
                .outputQuality(quality == null ? DEFAULT_QUALITY : quality);
        return builder;
    }


    /**
     * 压缩图片
     *
     * @param url     文件路径
     * @param path    压缩后的文件路径
     * @param scale   缩放比  0--1 取值 1f为原图大小
     * @param quality 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static void compressImage(String url, String path, Float scale, Float quality) throws IOException {
        Thumbnails.of(url)
                .scale(scale == null ? DEFAULT_SCALE : scale)
                .outputQuality(quality == null ? DEFAULT_QUALITY : quality)
                .toFile(path);
    }

    /**
     * 压缩图片
     *
     * @param url     文件地址
     * @param path    压缩后的文件路径
     * @param scale   缩放比  0--1 取值 1f为原图大小
     * @param quality 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static void compressImage(URL url, String path, Float scale, Float quality) throws IOException {
        Thumbnails.of(url)
                .scale(scale == null ? DEFAULT_SCALE : scale)
                .outputQuality(quality == null ? DEFAULT_QUALITY : quality)
                .toFile(path);
    }


    /**
     * 指定大小压缩图片
     *
     * @param in     文件流
     * @param path   压缩后的文件路径
     * @param width  长宽  0--1 取值 1f为原图大小
     * @param height 图片质量 0--1取值趋近1质量越好 反之质量越差
     */
    public static void compressImageBySize(InputStream in, String path, int width, int height) throws IOException {
        Thumbnails.of(in)
                .size(width, height)
                .toFile(path);
    }


    /**
     * 裁剪图片
     *
     * @param in     文件流
     * @param path   压缩后的文件路径
     * @param width  宽
     * @param height 高
     */
    public static void sourceRegionImage(InputStream in, String path, int x, int y, int width, int height) throws IOException {
        Thumbnails.of(in)
                .sourceRegion(x, y, width, height)
                .size(width, height).keepAspectRatio(false)
                .toFile(path);
    }

    /**
     * 裁剪图片
     *
     * @param in     文件流
     * @param width  宽
     * @param height 高
     */
    public static Thumbnails.Builder<? extends InputStream> sourceRegionImage(InputStream in, int x, int y, int width, int height) throws IOException {
        return Thumbnails.of(in)
                .sourceRegion(x, y, width, height)
                .size(width, height).keepAspectRatio(false);
    }

}
