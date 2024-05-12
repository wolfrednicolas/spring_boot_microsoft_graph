package live.hi.services.ms365connector.dto.msgraph;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class AttachmentUploader {

    public int uploadAttachment(String uploadUrl, File file) throws IOException {
        long fileSize = file.length();
        long bytesUploaded = 0;
        int chunkSize = 4 * 1024 * 1024; // 4 MB chunk size
        int responseCode = 0;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            while (bytesUploaded < fileSize) {
                // Calculate range for this chunk
                long startByte = bytesUploaded;
                long endByte = Math.min(bytesUploaded + chunkSize - 1, fileSize - 1);
                long contentLength = endByte - startByte + 1;
                // Open connection and set headers
                HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/octet-stream");
                connection.setRequestProperty("Content-Length", String.valueOf(contentLength));
                connection.setRequestProperty("Content-Range", "bytes " + startByte + "-" + endByte + "/" + fileSize);
                connection.setDoOutput(true);

                // Read the file fragment and write it to the output connection
                try (OutputStream outputStream = connection.getOutputStream()) {
                    int maxBytesToRead = (int) Math.min(chunkSize, fileSize - bytesUploaded);
                    byte[] buffer = new byte[maxBytesToRead];
                    int bytesRead = inputStream.read(buffer);
                    outputStream.write(buffer, 0, bytesRead);
                    bytesUploaded += bytesRead;
                }
                
                // Response Code
                responseCode = connection.getResponseCode();
                // Close Connection
                connection.disconnect();
            }
        } catch (Exception e) {
            responseCode = 500;
        }

        return responseCode;
    }
}

