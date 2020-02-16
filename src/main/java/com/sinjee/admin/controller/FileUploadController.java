package com.sinjee.admin.controller;

import com.sinjee.common.KeyUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.common.UUIDUtil;
import com.sinjee.vo.ResultVO;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 02 -13
 * 中台上传图片处理类
 * @author kweitan
 */

@RestController
@RequestMapping("/admin/upload")
@Slf4j
public class FileUploadController {

    //文件上传路径
    /**
     * /images/products/
     * **/
    @Value("${admin.imgPath}")
    private String imgPath ;

    //图片服务器地址
    /**
     * /resources/common
     * **/
    @Value("${admin.server.uploadfiles.dir}")
    private String uploadFiles ;

    @CrossOrigin(origins = "*")
    @PostMapping("/uploadIcon")
    public ResultVO uploadIcon(HttpServletRequest request){
        log.info("上传图片开始");
        String imgName = UUIDUtil.genUUIDStr()+KeyUtil.genUniqueKey();
        String imgType = null ;
        List<String> resList = new ArrayList<>() ;
        //
        try {
            request.setCharacterEncoding("utf-8");

            //获得磁盘文件
            DiskFileItemFactory factory = new DiskFileItemFactory() ;

            //获取图片需要上传的路径
            String path = uploadFiles+ imgPath;

            Assert.notNull(path);

            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs() ; //创建文件夹
            }

            //设置暂时存放空间
            factory.setRepository(new File(path));

            //设置缓存大小 当上传图片的容量超过该缓存时 直接放到临时空间
            factory.setSizeThreshold(1024*1024);

            //图片上传处理
            ServletFileUpload upload = new ServletFileUpload(factory) ;
            OutputStream out = null ;
            InputStream in = null ;

            //可以上传多个图片
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request) ;
            for (int i=0;i<list.size();i++){
                FileItem fileItem = list.get(i) ;
                //获取表单的属性
                if (fileItem.isFormField()){}
                else {
                    //处理图片
                    String contentType = fileItem.getContentType() ;
                    String[] types = new String[]{"image/png","image/gif","image/jpeg","image/bmp","image/jpg"};
                    for (int j=0; j<types.length;j++){
                        String type = types[j] ;
                        if (contentType.equals(type)){
                            imgType = contentType.substring(contentType.indexOf("/")+1) ;
                            imgName = imgName + "."+imgType ;
                            break;
                        }
                    }

                    if (imgType == null){
                        //请上传正确的图片
                        return ResultVOUtil.error(333,"请上传正确的图片") ;
                    }

                    //获取图片大小
                    Long size = fileItem.getSize() ;
                    if (size/1024>10*1024){
                        //图片大小超过10M
                        return ResultVOUtil.error(333,"图片大小超过10M") ;
                    }

                    String serverPath = path ;
                    File serverDir = new File(serverPath) ;
                    if (!serverDir.exists()){
                        serverDir.mkdirs();
                    }

                    out = new FileOutputStream(new File(serverPath,imgName)) ;
                    in = fileItem.getInputStream() ;
                    int length = 0 ;
                    byte[] buf = new byte[1024] ;
                    log.info("获取上传图片的大小为：{}",size);

                    while ((length = in.read(buf))!= -1){
                        out.write(buf,0,length);
                    }

                    //返回图片回显地址
                    String realWebImgPath = imgPath + imgName ;

                    //拼装返回图片的URL地址
                    resList.add(realWebImgPath);
                }
            }

        }catch (Exception e){
            log.info(e.getMessage());
            ResultVOUtil.error(333,e.getMessage()) ;
        }

        return ResultVOUtil.success(resList) ;
    }


}
