package com.pluxurydolo.vk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public Mono<File> createTempFile(String fileName, String fileExtension, byte[] bytes) {
        return Mono.fromCallable(() -> Files.createTempFile(fileName, fileExtension))
            .map(Path::toFile)
            .flatMap(file -> addBytes(file, bytes))
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("rgva Успешно создан временный файл {}{}", fileName, fileExtension))
            .doOnError(throwable -> LOGGER.error("egbz Произошла ошибка при создании временного файла", throwable));
    }

    private static Mono<File> addBytes(File file, byte[] bytes) {
        try {
            writeByteArrayToFile(file, bytes);
        } catch (IOException exception) {
            return Mono.error(exception);
        }

        file.deleteOnExit();
        return Mono.just(file);
    }
}
