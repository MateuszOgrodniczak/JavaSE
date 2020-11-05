package com.pretius.file_manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;
import static com.pretius.file_manager.utils.NameUtils.*;

public class FileManagerApplication {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);

    private int MOVED_FILES;
    private int MOVED_FILES_DEV;
    private int MOVED_FILES_TEST;

    public static void main(String[] args) {
        logger.info("File manager application started");

        FileManagerApplication fileManager = new FileManagerApplication();
        try {
            fileManager.createDirectory(TEST_DIR_NAME);
            fileManager.createDirectory(DEV_DIR_NAME);
            fileManager.createDirectory(HOME_DIR_NAME);
        } catch (InterruptedException | IOException | NumberFormatException e) {
            logger.error("Error occurred while managing files: shutting down application");
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Unexpected error");
            e.printStackTrace();
        }
    }

    private void createDirectory(String directoryName) throws InterruptedException, IOException, NumberFormatException {
        File dir = new File(directoryName);
        if (dir.mkdir()) {
            logger.info("Directory '" + directoryName + "' created");
        }

        if (directoryName.equals(HOME_DIR_NAME)) {
            addListenerToHomeDirectory(dir);
        }
    }

    private void addListenerToHomeDirectory(File dir) throws InterruptedException, IOException, NumberFormatException {
        String homeDirPathString = dir.getPath() + "/count.txt";
        File countFile = new File(homeDirPathString);

        if (countFile.createNewFile()) {
            logger.info("Count.txt file created");
        } else {
            logger.info("Count.txt file already exists");
        }
        updateStatus(countFile);

        Path homeDirPath = dir.toPath();
        WatchService watchService = homeDirPath.getFileSystem().newWatchService();
        homeDirPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey watch = watchService.take();

        while (true) {
            List<WatchEvent<?>> events = watch.pollEvents();
            for (WatchEvent event : events) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    File addedFile = new File(HOME_DIR_NAME + "/" + event.context().toString());
                    String fileExtension = getFileExtension(addedFile.getName());
                    if (fileExtension.equals(JAR_FILE_EXTENSION)) {
                        handleJarFile(countFile, event, addedFile);
                    } else if (fileExtension.equals(XML_FILE_EXTENSION)) {
                        handleXmlFile(countFile, event, addedFile);
                    }
                }
            }
        }
    }

    private void handleXmlFile(File countFile, WatchEvent event, File addedFile) throws IOException {
        MOVED_FILES++;
        MOVED_FILES_DEV++;
        moveFile(addedFile, DEV_DIR_NAME + "/" + event.context().toString());
        updateStatus(countFile);
    }

    private void handleJarFile(File countFile, WatchEvent event, File addedFile) throws IOException, NumberFormatException {
        BasicFileAttributes atr = Files.readAttributes(addedFile.toPath(), BasicFileAttributes.class);

        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hours = df.format(atr.creationTime().toMillis());
        int creationHour = Integer.parseInt(hours);

        if (creationHour % 2 == 0) {
            moveFile(addedFile, DEV_DIR_NAME + "/" + event.context().toString());
            MOVED_FILES++;
            MOVED_FILES_DEV++;
            updateStatus(countFile);
        } else {
            moveFile(addedFile, TEST_DIR_NAME + "/" + event.context().toString());
            MOVED_FILES++;
            MOVED_FILES_TEST++;
            updateStatus(countFile);
        }
    }


    private void updateStatus(File countFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(countFile);
        String filesStatus = "All: " + MOVED_FILES + "\nHOME->DEV: " + MOVED_FILES_DEV + "\nHOME->TEST: " + MOVED_FILES_TEST;
        outputStream.write(filesStatus.getBytes());

        logger.info("Count.txt status updated");
    }

    private void moveFile(File file, String destinationFolder) {
        while (!file.renameTo(new File(destinationFolder))) {
            destinationFolder = renameFileToBeCopied(destinationFolder);
            logger.info("File with this name already exists in destination. Creating copy file.");
        }
        file.delete();
        logger.info("File moved successfully");
    }

    private String renameFileToBeCopied(String destinationFolder) {
        String fileExtension = destinationFolder.substring(destinationFolder.lastIndexOf("."));
        String filePathWithoutExtension = destinationFolder.substring(0, destinationFolder.lastIndexOf("."));
        System.out.println(fileExtension + ", " + filePathWithoutExtension);
        filePathWithoutExtension += 1;
        destinationFolder = filePathWithoutExtension + fileExtension;
        return destinationFolder;
    }
}
