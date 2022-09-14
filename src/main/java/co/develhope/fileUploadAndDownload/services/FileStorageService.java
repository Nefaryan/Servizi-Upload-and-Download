package co.develhope.fileUploadAndDownload.services;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    @SneakyThrows
    public String upload(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newNameForFile = UUID.randomUUID().toString() + "." + extension;
        File fileRepository = new File(fileRepositoryFolder);
        if(!fileRepository.exists()) throw new IOException("Folder doesn't exist");
        if(!fileRepository.isDirectory()) throw  new IOException("Folder isn't a directory");
        File destination = new File(fileRepositoryFolder + "\\" + newNameForFile);
        if(destination.exists()) throw new IOException("File conflict");
        file.transferTo(destination);
        return newNameForFile;
    }

    @SneakyThrows
    public byte[] download(String fileName) {
        File fireFromRepo = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fireFromRepo.exists()) throw new IOException("File doesn't exists");
        return IOUtils.toByteArray(new FileInputStream(fireFromRepo));
    }
}
