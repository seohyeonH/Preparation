package studio.aroundhub.pado.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Acl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileUploadService {
    @Value("${gcp.project-id}")
    private String projectId;
    private final Storage storage;

    @Autowired
    public FileUploadService(@Value("${gcp.credentials.location}") String credentialsPath) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }

    public void uploadFile(File file) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder("bada__storage", file.getName())
                .setContentType("application/pdf")
                .setAcl(List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();

        byte[] bytes = Files.readAllBytes(file.toPath());
        storage.create(blobInfo, bytes);
    }

    public String generateFileUrl(String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", "bada__storage", fileName);
    }
}
