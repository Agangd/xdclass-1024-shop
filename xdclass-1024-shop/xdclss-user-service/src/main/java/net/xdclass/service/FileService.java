package net.xdclass.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public String uploadUserImg(MultipartFile file) throws IOException;

}
