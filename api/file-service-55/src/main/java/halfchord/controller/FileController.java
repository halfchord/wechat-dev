package halfchord.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/uploadFace")
    public GraceJSONResult upLoadFace(@RequestParam("file")MultipartFile file, String userId, HttpServletRequest request) throws IOException {

        String filename = file.getOriginalFilename(); //获取文件名
        String suffixName = filename.substring(filename.lastIndexOf(".")); // 获取文件后缀名

        String newFileName = userId + suffixName;// 新文件名

        //设置文件新的保存路径
        String rootPath = "/temp" + File.separator;
        String filePath = rootPath + File.separator + "face" + File.separator + newFileName;

        File newFile = new File(filePath);

        //判断文件所在目录是否存在
        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }

        //将内存中的数据写入磁盘
        file.transferTo(newFile);
        return GraceJSONResult.ok();
    }
}