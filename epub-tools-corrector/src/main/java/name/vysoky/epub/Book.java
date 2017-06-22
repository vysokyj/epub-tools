/*
 * ePUB Corrector - https://github.com/vysokyj/epub-corrector/
 *
 * Copyright (C) 2012 Jiri Vysoky
 *
 * ePUB Corrector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * ePUB Corrector is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cobertura; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package name.vysoky.epub;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.tool.Checker;
import com.adobe.epubcheck.util.EPUBVersion;
import name.vysoky.xhtml.Corrector;
import name.vysoky.xhtml.CorrectorFactory;
import name.vysoky.xhtml.TidyFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.tidy.Tidy;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static name.vysoky.epub.Command.printHeader;

/**
 * ePUB book representation.
 * Provide methods for book manipulation.
 */
public class Book implements TextContainer, FileBased {

    public static final String MIME_FILE = "mimetype";
    private static PrintStream out = System.out;
    private File file;
    private File workingDirectory;
    private ZipParameters fileZipParameters;
    private ZipParameters mimeZipParameters;
    private List<Part> parts;
    private Tidy tidy;
    private Corrector corrector;

    public static final String WORKING_DIRECTORY_EXTENSION = "extracted";

    public Book(String path) {
        this(new File(path));
    }

    public Book(File file) {
        printHeader("Opening book ...");
        this.file = file;
        this.workingDirectory = new File(file.getAbsolutePath() + "." + WORKING_DIRECTORY_EXTENSION);
        // initialise zip parameters
        fileZipParameters = new ZipParameters();
        fileZipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        fileZipParameters.setCompressionLevel(9);
        fileZipParameters.setIncludeRootFolder(true);
        mimeZipParameters = new ZipParameters();
        mimeZipParameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
        mimeZipParameters.setCompressionLevel(0);
        out.println("Opened book: \"" + file.getName() + "\"");
    }

    /**
     * Setter write out print stream.
     * @param out print stream
     */
    public static void setOut(PrintStream out) {
        Book.out = out;
    }

    public File getFile() {
        return file;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public Tidy getTidy() {
        if (tidy == null) tidy = TidyFactory.getDefaultTidy();
        return tidy;
    }

    public void setTidy(Tidy tidy) {
        this.tidy = tidy;
    }

    public Corrector getCorrector() {
        if (corrector == null) corrector = CorrectorFactory.getDefaultCorrector();
        return corrector;
    }

    public void setCorrector(Corrector corrector) {
        this.corrector = corrector;
    }

    public void correct() throws IOException {
        printHeader("Correcting book using plain text extractor...");
        for (Part part : getTextParts()) part.correct();
    }

    public void tidy() throws IOException {
        printHeader("Cleaning book using TIDY....");
        for (Part part : getTextParts()) part.tidy();
    }

    public void check() throws IOException {
       check(new LoggingReport());
    }

    public void check(Report report) throws IOException  {
        printHeader("Checking book using Adobe EpubCheck tool...");
        File bookFile = null;
        try {
            if (isExtracted()) {
                bookFile = File.createTempFile("book", ".epub");
                bookFile.delete(); // zip4j not accept existing file
                compress(bookFile);
            } else {
                bookFile = getFile();
            }
            Checker.validateFile(bookFile.getAbsolutePath(), "application/epub+zip", EPUBVersion.VERSION_2, report);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error when checking ePUB file!", e);
        } finally {
            if (isExtracted()) FileUtils.deleteQuietly(bookFile);
        }

    }

    public void findCSSClasses(StyleReport styleReport) throws IOException {
        List<Part> parts = getTextParts();
        for (int i = 0; i < parts.size(); i++) {
            Part part = parts.get(i);
            part.findCSSClasses(styleReport);
            int progress = ((i + 1) / parts.size() * 100);
            styleReport.progress(progress);
        }
    }

    @Override
    public void removeCSSClass(String className) throws IOException {
        printHeader("Removing style class '" + className + "'...");
        for (Part part : getTextParts()) part.removeCSSClass(className);
    }

    public void writeOutStyleClasses() throws IOException {
        printHeader("Listing styles...");
        StyleSet styleMap = new StyleSet();
        findCSSClasses(styleMap);
        styleMap.printReport();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public List<Part> getTextParts() throws IOException {
        if (parts == null) parts = findAllTextParts();
        return parts;
    }

    /**
     * Check if ePUB is extracted.
     * In real check if working directory exists only.
     * @return true if ePUB is extracted
     * @throws IOException thrown when working directory file exist but is not directory file
     */
    public boolean isExtracted() throws IOException  {
        if (workingDirectory == null || !workingDirectory.exists()) return false;
        if (workingDirectory.isDirectory()) return true;
        else throw new IOException("File '" + workingDirectory.getAbsolutePath() + "' is not directory!");
    }

    /**
     * Remove working directory.
     * Do nothing if working directory not exist.
     */
    public void clean() throws IOException {
        if (!isExtracted()) return;
        out.println("Removing working directory: \"" + workingDirectory.getAbsolutePath() + "\"");
        FileUtils.deleteDirectory(workingDirectory);
    }

    /**
     * Clean existing working directory and extract ePUB.
     */
    public void extract() throws IOException {
        try {
            printHeader("Extracting working directory...");
            clean();
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(workingDirectory.getAbsolutePath());
            out.println("Extracted to directory: \"" + getWorkingDirectory().getAbsolutePath()  + "\"");
        } catch (Exception e) {
            throw new IOException("Error when extracting EPUB file!", e);
        }
    }

    /**
     * Compress ePUB and clean working directory.
     * If ePUB is not extracted do re-compression only.
     */
    public void compress() throws IOException {
        try {
            if (!isExtracted()) extract();
            if (!file.delete()) throw new IOException("Unable to delete old compressed file!");
            compress(file);
            clean();
        } catch (Exception e) {
            throw new IOException("Error when compressing EPUB file!", e);
        }
    }

    private void compress(File target) throws ZipException {
        ZipFile zipFile = new ZipFile(target);
        File mimeFile = new File(workingDirectory, MIME_FILE);
        // add uncompressed mime type file first
        zipFile.createZipFile(mimeFile, mimeZipParameters);
        // add other files
        for (File f : workingDirectory.listFiles()) {
            if (f.getName().equals(MIME_FILE)) continue;
            if (f.isDirectory()) zipFile.addFolder(f, fileZipParameters);
            if (f.isFile())  zipFile.addFile(f, fileZipParameters);
        }
    }

    /**
     * Find all text parts, even not listed in metadata.
     * This action invoke book extraction, if book is note yet extracted.
     * @return list of all text parts.
     * @throws IOException common input output errors
     */
    public List<Part> findAllTextParts() throws IOException {
        if (!isExtracted()) extract();
        // Find files with xhtml or html extension.
        List<File> foundFiles = new ArrayList<File>();
        findFilesWithExtension(workingDirectory, foundFiles, "html");
        findFilesWithExtension(workingDirectory, foundFiles, "xhtml");
        // Create text parts from found files
        List<Part> parts = new ArrayList<Part>(foundFiles.size());
        for (File file : foundFiles) parts.add(new Part(this, file));
        return parts;
    }

    /**
     * Compute relative path to ePUB document root form file stored in this book.
     * If file is outside the book, than absolute path is returned.
     * Used for simple re output.
     * @param file file stored in ePUB document working directory
     * @return relative path to document
     */
    protected String getRelativePathToBook(File file) {
        String full = file.getAbsolutePath();
        String root = workingDirectory.getAbsolutePath();
        if (full.contains(root)) return full.substring(root.length() + 1);
        else return full;
    }

    /**
     * Recursive function - find files with given extension and add these files to given list.
     * @param directory directory for scan
     * @param foundFiles found file list
     * @param extension filtered extension without dot
     */
    private void findFilesWithExtension(File directory, List<File> foundFiles, String extension) {
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) findFilesWithExtension(file, foundFiles, extension);
                    if (file.isFile() && FilenameUtils.isExtension(file.getName(), extension)) foundFiles.add(file);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
