package com.mooc.house.api.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mooc.house.api.utils.FileUtil;

@Service
public class FileService {

  private Logger logger = LoggerFactory.getLogger(FileService.class);
  @Value("${file.path}")
  private String filePath;
  
  public List<String> getImgPaths(List<MultipartFile> files) {
    List<String> paths = Lists.newArrayList();
    files.forEach(file -> {
      File localFile = null;
      try {
        if (!file.isEmpty()) {
          //将上传文件保存到本地，filePath：静态文件路径
          localFile = FileUtil.saveToLocal(file, filePath);
          logger.warn("绝对路径localFile.getAbsolutePath()："+localFile.getAbsolutePath());
          logger.warn("filePath："+filePath);
          /*
                    localFile.getAbsolutePath()的路径是xxx\xxx\xx, filePath的路径是xxx/xxx/xx，
                    不进行转化的话substringAfterLast的结果为空
                    */
          String fileReplace=String.valueOf(localFile.getAbsolutePath());
          String newFileReplace=fileReplace.replace("\\","/");
          ////生成相对路径，把前缀移除
          String path =  StringUtils.substringAfterLast(newFileReplace, filePath);
          logger.warn("生成的相对路径："+path);
          paths.add(path);
        }
      } catch (IOException e) {
        logger.warn("图像文件保存失败");
        throw new RuntimeException(e.getMessage());
      }
    });
    return paths;
    
  }

}
