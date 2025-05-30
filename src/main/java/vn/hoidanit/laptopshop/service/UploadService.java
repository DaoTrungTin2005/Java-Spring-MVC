package vn.hoidanit.laptopshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        // don't upload file
        if (file.isEmpty())
            return "";

        // relative path : absolute path
        // đường link trỏ tới thư mục web app (cộng thêm /resources/images đẻ trỏ đúng
        // đường link thực tế)
        // đó là đường link trỏ đến nơi lưu file
        // đã khai báo ServletContext ở trên
        String rootPath = this.servletContext.getRealPath("/resources/images");
        String finalName = "";
        try {
            // lấy hình ảnh ở dạng binary
            byte[] bytes;

            bytes = file.getBytes();
            // cộng chuỗi trỏ tới thư mục avatar
            File dir = new File(rootPath + File.separator + targetFolder);

            // nếu thư mục chưa tồn tại -> tạo mới nó
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server (quy định tên file để lưu) (tạo file thêm thời gain
            // tránh trùng lặp)

            // tên file (lưu tên này do database)
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // tên lưu
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

            // truyền vào file muốn lưu
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            // lưu vào database lun
            // this.userService.handleSaveUser(Daotrungtin);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return finalName;
    }
}
