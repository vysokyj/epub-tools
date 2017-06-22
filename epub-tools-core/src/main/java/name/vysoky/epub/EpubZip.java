package name.vysoky.epub;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Compressing and decompressing tool for raw ePUB directory.
 * @author Jiri Vysoky
 */
public class EpubZip {

    public static final String MIME_FILE = "mimetype";

    /**
     * Extract ePUB file to given directory.
     * @param epubFile ePUB file
     * @param epubDir ePUB directory
     */
    public static void extract(File epubFile, File epubDir) throws IOException {
        ZipFile zfile = new ZipFile(epubFile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File file = new File(epubDir, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                InputStream in = zfile.getInputStream(entry);
                try {
                    FileUtils.copyInputStreamToFile(in, file);
                } finally {
                    in.close();
                }
            }
        }
    }

    /**
     * Compress directory content to given ePUB file.
     * @param epubDir ePUB directory
     * @param epubFile ePUB file
     */
    public static void compress(File epubDir, File epubFile) throws IOException {
        URI base = epubDir.toURI();
        File dir = epubDir; // currently processed directory
        Deque<File> queue = new LinkedList<File>();
        queue.push(dir);
        ZipOutputStream os = null;
        try {
            os = new ZipOutputStream(new FileOutputStream(epubFile));

            File mimeFile = findMimeFile(epubDir);
            if (mimeFile != null) {
                String mimeFileName =   base.relativize(mimeFile.toURI()).getPath();
                final byte[] content = FileUtils.readFileToByteArray(mimeFile);
                ZipEntry mimeZipEntry = new ZipEntry(mimeFileName);
                mimeZipEntry.setMethod(ZipEntry.STORED);
                mimeZipEntry.setSize(content.length);
                final CRC32 checksumCalculator = new CRC32();
                checksumCalculator.update(content);
                mimeZipEntry.setCrc(checksumCalculator.getValue());
                os.putNextEntry(mimeZipEntry);
                FileUtils.copyFile(mimeFile, os);
                os.closeEntry();
            }

            while (!queue.isEmpty()) {
                dir = queue.pop();
                File[] files = dir.listFiles();
                if (files == null) continue;
                for (File file : files) {
                    String name = base.relativize(file.toURI()).getPath();
                    if (file.isDirectory()) {
                        queue.push(file);
                        name = name.endsWith("/") ? name : name + "/";
                        os.putNextEntry(new ZipEntry(name));
                    }  else if (file.isFile() && !file.getName().equals(MIME_FILE)) {
                        os.putNextEntry(new ZipEntry(name));
                        FileUtils.copyFile(file, os);
                        os.closeEntry();
                    }
                }
            }
        } finally {
            if (os != null) os.close();
        }
    }

    private static File findMimeFile(File epubDir) {
        File[] files = epubDir.listFiles();
        if (files == null) return null;
        for (File file : files)
            if (file.getName().equals(MIME_FILE)) return file;
        return null;
    }
}