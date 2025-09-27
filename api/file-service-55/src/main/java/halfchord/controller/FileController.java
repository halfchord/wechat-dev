package halfchord.controller;

import ch.qos.logback.classic.Logger;
import feign.UserInfoMicroServiceFeign;
import halfchord.config.MinIOConfig;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.itzixi.utils.JsonUtils;
import org.itzixi.utils.MinIOUtils;
import org.itzixi.utils.QrCodeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.vo.UsersVO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
   /* @PostMapping("/uploadFace1")
    public GraceJSONResult upLoadFace1(@RequestParam("file")MultipartFile file, String userId, HttpServletRequest request) throws IOException {

        String filename = file.getOriginalFilename(); //获取文件名
        String suffixName = filename.substring(filename.lastIndexOf(".")); // 获取文件后缀名

        String newFileName = userId + suffixName;// 新文件名

        //设置文件新的保存路径
        String rootPath = "D:/work/photo" + File.separator;
        String filePath = rootPath + File.separator + "face" + File.separator + newFileName;

        File newFile = new File(filePath);

        //判断文件所在目录是否存在
        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }

        //将内存中的数据写入磁盘
        file.transferTo(newFile);
        return GraceJSONResult.ok();
    }*/

    @Resource
    private MinIOConfig minIOConfig;

    @Resource
    private UserInfoMicroServiceFeign userInfoMicroServiceFeign;

    @PostMapping("/uploadFace")
    public GraceJSONResult upLoadFace(@RequestParam("file")MultipartFile file, String userId, HttpServletRequest request) throws Exception {

        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String filename = file.getOriginalFilename(); //获取文件名

        if(StringUtils.isBlank(filename)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        filename = "face/"+userId + "/" + filename;
        System.out.println(File.separator);

        MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream());

        String faceUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + filename;

        GraceJSONResult jsonResult = userInfoMicroServiceFeign.updateFace(userId,faceUrl);
        return upload(userId,faceUrl,jsonResult);
    }

    @PostMapping("/generatorQrCode")
    public String generatorQrCode(@RequestParam("wechatNumber") String wechatNumber,@RequestParam("userId") String userId) throws Exception {
        //构建map对象
        Map<String, String> map = new HashMap<>();
        map.put("wechatNumber", wechatNumber);
        map.put("userId", userId);

        //将map对象转换成json字符串，放入二维码中
        String data = JsonUtils.objectToJson(map);

        //生成二维码
        String codePath = QrCodeUtils.generateQRCode(data);
        if(StringUtils.isBlank(codePath)){
            return "二维码生成失败";
        }

        //将二维码上传到Minio中
        String filename = "Qrcode/" + userId + ".png";

        return MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, codePath, true);
    }

    @PostMapping("/uploadFriendCircleBg")
    public GraceJSONResult uploadFriendCircleBg(@RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) throws Exception {

        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String filename = file.getOriginalFilename(); //获取文件名

        if(StringUtils.isBlank(filename)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        filename = "FriendCircleBg/"+userId + "/" + dealWithoutFilename(filename);

        String FriendCircleBgUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);


        GraceJSONResult jsonResult = userInfoMicroServiceFeign.updateFriendCircleBg(userId,FriendCircleBgUrl);
        return upload(userId,FriendCircleBgUrl,jsonResult);
    }

    @PostMapping("/uploadChatBg")
    public GraceJSONResult uploadChatBg(@RequestParam("file") MultipartFile file,
                                                @RequestParam("userId") String userId) throws Exception {

        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String filename = file.getOriginalFilename(); //获取文件名

        if(StringUtils.isBlank(filename)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        filename = "ChatBg/"+userId + "/" + dealWithoutFilename(filename);

        String ChatBgUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
        GraceJSONResult jsonResult = userInfoMicroServiceFeign.updateChatBg(userId,ChatBgUrl);
        return upload(userId,ChatBgUrl,jsonResult);
    }

    private GraceJSONResult upload(String userId,String Url,GraceJSONResult jsonResult){
        //更新用户头像，聊天背景，朋友圈背景等

        Object data = jsonResult.getData();
        String json = JsonUtils.objectToJson(data);
        UsersVO userVO = JsonUtils.jsonToPojo(json, UsersVO.class);
        return GraceJSONResult.ok(userVO);

    }

    private String dealWithFilename(String filename){
        String suffixName=filename.substring(filename.lastIndexOf("."));
        String fName=filename.substring(0,filename.lastIndexOf("."));
        String uuid= UUID.randomUUID().toString();
        return fName+"-"+uuid+suffixName;

    }
    private String dealWithoutFilename(String filename){
        String suffixName=filename.substring(filename.lastIndexOf("."));
        String uuid= UUID.randomUUID().toString();
        return uuid+suffixName;
    }

}