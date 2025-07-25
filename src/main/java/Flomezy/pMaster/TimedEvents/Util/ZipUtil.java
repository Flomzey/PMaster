package Flomezy.pMaster.TimedEvents.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private FilenameFilter filter = (dir, name) -> !name.contains("session.lock");
    //TODO: maybe create a filter that can filter on input of user


    public void zip(File sourceDir, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFileRecursive(sourceDir, sourceDir, zos);
        }
    }

    //TODO: rewrite zip

    private void zipFileRecursive(File rootDir, File currentFile, ZipOutputStream zos) throws IOException {
        if (currentFile.isDirectory()) {
            File[] children = currentFile.listFiles();
            if (children == null || children.length == 0) {
                String zipEntryName = rootDir.toPath().relativize(currentFile.toPath()).toString().replace("\\", "/") + "/";
                zos.putNextEntry(new ZipEntry(zipEntryName));
                zos.closeEntry();
            } else {
                for (File child : children) {
                    zipFileRecursive(rootDir, child, zos);
                }
            }
        } else {
            String zipEntryName = rootDir.toPath().relativize(currentFile.toPath()).toString().replace("\\", "/");
            zos.putNextEntry(new ZipEntry(zipEntryName));

            try (FileInputStream fis = new FileInputStream(currentFile)) {
                byte[] buffer = new byte[2048];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
            }

            zos.closeEntry();
        }
    }


    public void copy(File source, File target) throws IOException {
        if (!source.exists()) return;
        if (!source.isDirectory()) {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        if (!target.exists() && !target.mkdirs())
            throw new IOException("Couldn't find or create destination directory");
        File[] children = source.listFiles(filter);
        if (children == null) return;
        for (File child : children) {
            copy(child, new File(target, child.getName()));
        }
    }

    public void del(File target) throws IOException {
        if (!target.exists()) return;
        if (!target.isDirectory()){
            if(!target.delete())
                throw new IOException("Couldn't delete: " + target.getAbsolutePath());
            return;
        }

        File[] children = target.listFiles();
        if(children == null)
            throw new IOException("Couldn't delete: " + target.getAbsolutePath() + " doesn't exist");

        for(File child : children) del(child);

        if(!target.delete())
            throw new IOException("Couldn't delete: " + target.getAbsolutePath());
    }
}
