package Flomezy.pMaster.TimedEvents.Util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;


import java.io.*;

public class ZipUtil {

    private final File pathToBackup, saveBackupPath;

    public ZipUtil(File pathToBackup, File saveBackupPath){
        this.pathToBackup = pathToBackup;
        this.saveBackupPath = saveBackupPath;
    }

    //TODO: implement zipping of file wiht apache commons
}
