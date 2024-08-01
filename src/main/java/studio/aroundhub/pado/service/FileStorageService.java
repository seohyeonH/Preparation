package studio.aroundhub.pado.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
public class FileStorageService {
    private static final String STORAGE_DIR = "responses/";

    public String saveResponseToFile(String prompt, String response) throws IOException {
        String fileName = prompt.hashCode() + ".txt";
        Path filePath = Paths.get(STORAGE_DIR, fileName);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, response);
        return filePath.toString();
    }

    public String loadResponseFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}
