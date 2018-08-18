package com.ca.sport.product;

public interface UploadService {

    //上传图片
    String uploadPic(byte[] pic, String name, long size);

}
