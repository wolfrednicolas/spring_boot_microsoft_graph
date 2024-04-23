package live.hi.services.ms365connector.dto.msgraph;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

public class AttachmentUploader {

    public int uploadAttachment(String uploadUrl, MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File file = File.createTempFile("temp", null);

        // Transfer the data from the MultipartFile to the file
        multipartFile.transferTo(file);

        // Call the original method with the temporary file
        int responseCode = uploadAttachment(uploadUrl, file);

        // Delete temporary file after upload
        file.delete();

        return responseCode;
    }
    
    public int uploadAttachment(String uploadUrl, File file) throws IOException {
        // Open HTTP connection
        HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();

        //Set PUT method
        connection.setRequestMethod("PUT");

        // Set headers
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("Content-Length", String.valueOf(file.length()));
        connection.setRequestProperty("Content-Range", "bytes 0-" + (file.length() - 1) + "/" + file.length());

        //Enable writing and set the request body
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        int responseCode = connection.getResponseCode();

        // Close Connection
        connection.disconnect();
        
        return responseCode;
    }
}
