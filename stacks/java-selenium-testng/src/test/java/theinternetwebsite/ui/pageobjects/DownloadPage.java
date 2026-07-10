package theinternetwebsite.ui.pageobjects;

import static theinternetwebsite.ui.UITest.downloadsFolder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class DownloadPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='File Downloader']")
    private WebElement pageTitle;

    @FindBy(how = How.XPATH, using = "//a[normalize-space()='some-file.txt']")
    private WebElement downloadLink;

    private final Path relativePathToReferenceFile = Paths.get("src/test/resources", "some-file.txt");

    public DownloadPage(@NotNull UITest caller) {
        super(caller, "/download");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public boolean fileDownload() {
        String downloadHref = defaultWait().until(ExpectedConditions.elementToBeClickable(downloadLink)).getAttribute("href");
        if (downloadHref == null || downloadHref.isBlank()) {
            throw new IllegalStateException("Download link does not expose a file URL.");
        }
        String downloadFileName = Paths.get(URI.create(downloadHref).getPath()).getFileName().toString();
        Path pathToReferenceFile = Paths.get(relativePathToReferenceFile.toString());
        Path expectedDownloadedFilePath = Paths.get(downloadsFolder, downloadFileName).toAbsolutePath();

        if (!pathToReferenceFile.toAbsolutePath().toFile().exists()) {
            throw new IllegalStateException("Reference file does not exist: " + pathToReferenceFile.toAbsolutePath());
        }

        deleteExistingDownload(expectedDownloadedFilePath, downloadFileName);
        defaultWait().until(ExpectedConditions.elementToBeClickable(downloadLink)).click();
        waitForBrowserDownload(downloadFileName, expectedDownloadedFilePath);

        try {
            return compareByMemoryMappedFiles(expectedDownloadedFilePath, pathToReferenceFile.toAbsolutePath());
        } catch (IOException e) {
            throw new UncheckedIOException("Could not compare downloaded file", e);
        }
    }

    public static @NotNull Boolean compareByMemoryMappedFiles(Path path1, Path path2) throws IOException {
        try (RandomAccessFile randomAccessFile1 = new RandomAccessFile(path1.toFile(), "r");
             RandomAccessFile randomAccessFile2 = new RandomAccessFile(path2.toFile(), "r")) {

            FileChannel ch1 = randomAccessFile1.getChannel();
            FileChannel ch2 = randomAccessFile2.getChannel();
            if (ch1.size() != ch2.size()) {
                return false;
            }
            long size = ch1.size();
            MappedByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
            MappedByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);

            return m1.equals(m2);
        }
    }

    private void waitForBrowserDownload(@NotNull String downloadFileName, @NotNull Path expectedDownloadedFilePath) {
        HasDownloads downloads = driver();
        if (downloads.isDownloadsEnabled()) {
            defaultWait().until(d -> downloads.getDownloadedFiles().stream()
                    .anyMatch(downloadedFile -> downloadedFile.getName().equals(downloadFileName)));
            try {
                downloads.downloadFile(downloadFileName, expectedDownloadedFilePath.getParent());
                downloads.deleteDownloadableFiles();
            } catch (IOException e) {
                throw new UncheckedIOException("Could not retrieve managed browser download", e);
            }
            return;
        }

        defaultWait().until(d -> Files.exists(expectedDownloadedFilePath) && !Files.exists(partialDownloadPath(downloadFileName)));
    }

    private static void deleteExistingDownload(@NotNull Path expectedDownloadedFilePath, @NotNull String downloadFileName) {
        try {
            Files.createDirectories(expectedDownloadedFilePath.getParent());
            Files.deleteIfExists(expectedDownloadedFilePath);
            Files.deleteIfExists(partialDownloadPath(downloadFileName));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete existing download files", e);
        }
    }

    private static @NotNull Path partialDownloadPath(@NotNull String downloadFileName) {
        String tempFileName = downloadFileName.split("\\.")[0] + ".crdownload";
        return Paths.get(downloadsFolder, tempFileName).toAbsolutePath();
    }
}
