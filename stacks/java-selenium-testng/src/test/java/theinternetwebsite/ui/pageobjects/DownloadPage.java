package theinternetwebsite.ui.pageobjects;

import static theinternetwebsite.ui.UITest.downloadsFolder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;
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
        String downloadFileName = Paths.get(downloadHref).getFileName().toString();
        String tempFileName = downloadFileName.split("\\.")[0] + ".crdownload";
        Path pathToReferenceFile = Paths.get(relativePathToReferenceFile.toString());
        Path tempDownloadedFilePath = Paths.get(downloadsFolder, tempFileName);
        Path expectedDownloadedFilePath = Paths.get(downloadsFolder, downloadFileName);
        File expectedFile = expectedDownloadedFilePath.toAbsolutePath().toFile();
        File expectedTmpFile = tempDownloadedFilePath.toAbsolutePath().toFile();
        long bytes = -1;
        long newBytes = 0;

        if (!pathToReferenceFile.toAbsolutePath().toFile().exists()) {
            throw new IllegalStateException("Reference file does not exist: " + pathToReferenceFile.toAbsolutePath());
        }

        try {
            Files.deleteIfExists(expectedFile.toPath());
            Files.deleteIfExists(expectedTmpFile.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete existing download files", e);
        }

        caller().downloadFileHeadless(downloadHref, expectedFile.toString());
        sleepForDownloadBuffer(5000);

        while ((bytes != newBytes) && expectedTmpFile.exists() && !expectedFile.exists()) {
            try {
                newBytes = Files.size(tempDownloadedFilePath);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read partial download size", e);
            }
            bytes = newBytes;
            sleepForDownloadBuffer(1000);
        }

        if (!expectedFile.exists()) {
            throw new IllegalStateException("Download failed. File does not exist: " + expectedFile);
        }

        try {
            return compareByMemoryMappedFiles(expectedDownloadedFilePath.toAbsolutePath(), pathToReferenceFile.toAbsolutePath());
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

    private static void sleepForDownloadBuffer(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the download buffer", e);
        }
    }
}
