package net.xdclass.service.serviceImpl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.OssConfig;
import net.xdclass.service.FileService;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private OssConfig ossConfig;

    @Override
    public String uploadUserImg(MultipartFile file) {

        String buketname = ossConfig.getBucketname();
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        //创建OSS对象
        OSS build = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        //获取原始文件名  xxx.jpg
        String originalFileName = file.getOriginalFilename();

        //JDK8的日期格式
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        //拼装路径,oss上的存储路劲 user/2022/12/1/sdfsaf.jpg
        String folder = dtf.format(now);
        String fileName = CommonUtil.generateUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String newFileName = "user/" + folder + "/"  + fileName + extension;

        try {
            PutObjectResult result = build.putObject(buketname, newFileName, file.getInputStream());
            log.info("文件上传成功");
            //拼装返回路径
            if (result != null) {
                String imgUrl = "https:// -" + buketname + "." + endpoint + "/" + newFileName;
                return imgUrl;
            }
        } catch (IOException e) {
            log.info("文件上传失败");
        } finally {
            //oss关闭服务,不然会造成oss内存泄露
            build.shutdown();
        }
        return null;
    }
}
