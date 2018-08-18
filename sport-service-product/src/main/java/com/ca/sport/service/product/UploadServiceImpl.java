package com.ca.sport.service.product;

import com.ca.sport.fdfs.FastDFSUtils;
import com.ca.sport.product.UploadService;
import org.springframework.stereotype.Service;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

	
	//上传图片
	public String uploadPic(byte[] pic , String name, long size){
		return FastDFSUtils.uploadPic(pic, name, size);
	}
}
