package studio.aroundhub.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.pado.service.FileStorageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserGalleryService {
    private static final String STORAGE_DIR = "gallery/";
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public void makeUserGallery(String userId, String imageUrl) throws IOException {
        User user = userRepository.findByLoginId(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found."));

        Path filePath;
        if(user.getGallery() == null) {
            String fileName = userId.hashCode() + ".txt";
            filePath = Paths.get(STORAGE_DIR, fileName);
            Files.createDirectories(filePath.getParent());
        } else filePath = Paths.get(user.getGallery());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()))) {
            writer.write(imageUrl);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        user.setGallery(filePath.toString());
        userRepository.save(user);
    }

    public List<String> getUserGallery(String userId) throws IOException {
        User user = userRepository.findByLoginId(userId).orElse(null);
        if(user == null) return List.of();
        else if(user.getGallery() == null) return List.of();

        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(user.getGallery()))) {
            String line;
            while ((line = reader.readLine()) != null) list.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
