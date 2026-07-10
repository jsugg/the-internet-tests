package theinternetwebsite.ui.support;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import org.jetbrains.annotations.NotNull;

public final class HttpFileDownloader {
    private HttpFileDownloader() {}

    public static void download(@NotNull String fileUrl, @NotNull String localFilePath) {
        try {
            URLConnection connection = new URL(fileUrl).openConnection();
            try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream outputStream = new FileOutputStream(localFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to download " + fileUrl + " to " + localFilePath, e);
        }
    }
}
