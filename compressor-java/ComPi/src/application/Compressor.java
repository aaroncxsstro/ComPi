package application;

import java.io.*;
import java.util.List;
import java.util.zip.*;

public class Compressor {
    public static void compress(String sourceFilePath, String destFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        try (FileOutputStream fos = new FileOutputStream(destFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            if (sourceFile.isDirectory()) {
                compressDirectory(sourceFile, sourceFile.getName(), zos);
            } else {
                compressFile(sourceFile, zos);
            }
        }
    }

    public static void compressMany(List<File> sourceFiles, String destFilePath) throws IOException {
        String tempDirPath = "Nuevo Archivo";
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        } else {
            int count = 1;
            while (tempDir.exists()) {
                tempDirPath = "Nuevo Archivo (" + count + ")";
                tempDir = new File(tempDirPath);
                count++;
            }
            tempDir.mkdir();
        }

        for (File sourceFile : sourceFiles) {
            File destFile = new File(tempDir, sourceFile.getName());
            copyFile(sourceFile, destFile);
        }

        compress(tempDir.getAbsolutePath(), destFilePath);

        deleteDirectory(tempDir);
    }

    private static void compressDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                compressDirectory(file, parentFolder + "/" + file.getName(), zos);
            } else {
                zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                }
                zos.closeEntry();
            }
        }
    }

    private static void compressFile(File file, ZipOutputStream zos) throws IOException {
        String fileName = file.getName();
        System.out.println(fileName);
        int extensionIndex = fileName.lastIndexOf(".");
        System.out.println(extensionIndex);
        if (extensionIndex != -1) {
        }

        zos.putNextEntry(new ZipEntry(fileName));
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
        }
        zos.closeEntry();
    }



    private static void copyFile(File sourceFile, File destFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }
}
