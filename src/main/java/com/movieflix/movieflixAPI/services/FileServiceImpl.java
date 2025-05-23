package com.movieflix.movieflixAPI.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
// get name of the file
        String fileName = file.getOriginalFilename();

        //get the file path
        String filePath = path + File.separator + fileName;

        //file object creation
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }
        //upload file
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String name) throws FileNotFoundException {
        String filePath = path + File.separator + name;
        return new FileInputStream(filePath);
    }


}
