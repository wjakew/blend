/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.maintanance;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Object for storing file data
 */
public class FileObject {
    public String fileName;
    public InputStream fileStream;
    public LocalDateTime fileTime;

    /**
     * Constructor
     * @param fileName
     * @param fileStream
     * @param fileTime
     */
    public FileObject(String fileName, InputStream fileStream, LocalDateTime fileTime) {
        this.fileName = fileName;
        this.fileStream = fileStream;
        this.fileTime = fileTime;
    }

    /**
     * Function for getting file stream
     * @return InputStream
     */
    public InputStream getFileStream(){
        return fileStream;
    }

    /**
     * Function for getting fileName
     * @return String
     */
    public String getFileName(){
        return fileName;
    }
}
