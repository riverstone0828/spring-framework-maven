package co.kr.abacus.base.common.tlo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import co.kr.abacus.base.common.tlo.properties.TloLoggerProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TloLogger {
    private static final Integer DEFAULT_INTERVAL = 5;
    private static final Integer DEFAULT_CHECK_INTERVAL = 1000;
    private final AtomicReference<RandomAccessFile> currentFile = new AtomicReference<>();
    private final TloLoggerProperties properties;

    private final Timer timer = new Timer("tslRotator");
    private FileChannel currentFileChannel;
    private String currentFileName;

    @PostConstruct
    public void init() {
        if (Boolean.TRUE.equals(properties.getEnable()) && Boolean.TRUE.equals(properties.getEnableFileLog())) {
            this.timer.schedule(new TimerTask() {
                public void run() {
                    if (TloLogger.this.currentFileName != null) {
                        File file = new File(TloLogger.this.currentFileName);
                        if (!file.exists()) {
                            TloLogger.this.currentFileName = null;
                        }
                    }

                    String candidateFileName = TloLogger.this.generateFileName();
                    if (!candidateFileName.equals(TloLogger.this.currentFileName)) {
                        try {
                            TloLogger.this.generateFile(candidateFileName);
                            TloLogger.this.setCurrentFile(candidateFileName);
                        } catch (IOException var3) {
                            TloLogger.log.error("An error occurred while rotating TSL file.", var3);
                        }
                    }
                }
            }, 1000L, Optional.of(properties.getCheckIntervalMillis()).orElse(DEFAULT_CHECK_INTERVAL));
        }
    }

    public void destroy() throws IOException {
        if (this.currentFileChannel != null) {
            this.currentFileChannel.close();
        }

        this.timer.cancel();
        this.timer.purge();
    }

    public void write(Map<String, Object> tsl) {
        if (!properties.getEnable() || !properties.getEnableFileLog()) {
            return;
        }

        try {
            String fileName = getFileName();
            generateFile(fileName);
            StringJoiner joiner = new StringJoiner("|");
            tsl.forEach((key, value) -> {
                joiner.add(key + "=" + value);
            });
            String t = joiner + System.lineSeparator();
            log.info("TSL: {}", joiner);
            this.currentFileChannel.write(ByteBuffer.wrap(t.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            log.error("TSL file write error", e);
        }
    }

    public void write(String tsl) {
        try {
            log.info("TSL: {}", tsl);
            this.currentFileChannel.write(ByteBuffer.wrap(tsl.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException var3) {
            log.error("Failed to write TSL log.", var3);
        }
    }

    private String getFileName() {
        return String.format("%s/%s_%s_%s.tsl",
                properties.getBaseDir(),
                properties.getServiceName(),
                properties.getInstanceCode(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    private void generateFile(String fileName) throws IOException {
        if (!properties.getEnableFileLog()) {
            return;
        }

        Path path = Paths.get(fileName);
        File file = path.toFile();
        if (!file.exists()) {
            Files.createDirectories(file.getParentFile().toPath());
            Files.createFile(path);
            log.info("TSL file generated: {}", fileName);
        }
    }

    private String generateFileName() {
        String instanceNumber = System.getenv("INSTANCE_NO") == null ? properties.getInstanceCode()
                : System.getenv("INSTANCE_NO");

        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute()
                - now.getMinute() % Optional.of(properties.getIntervalMinutes()).orElse(DEFAULT_INTERVAL);
        String fileDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileTime = String.format("%02d", now.getHour()) + String.format("%02d", minute);
        String baseDir = properties.getBaseDir();
        if (!baseDir.endsWith(File.separator)) {
            baseDir = baseDir + File.separator;
        }

        baseDir = baseDir + fileDate + File.separator;
        String fileName = properties.getInstanceName() + "." + instanceNumber + "." + fileDate + fileTime
                + ".log";
        return baseDir + fileName;
    }

    private void setCurrentFile(String fileName) throws IOException {
        RandomAccessFile oldFile = this.currentFile.getAndSet(new RandomAccessFile(fileName, "rw"));
        if (oldFile != null) {
            oldFile.close();
        }

        FileChannel channel = this.currentFile.get().getChannel();
        channel.force(Optional.of(properties.getUseImmediatelyWrite()).orElse(false));
        this.currentFileChannel = channel;
        this.currentFileName = fileName;
        log.debug("Ready to write TSL file: {}", fileName);
    }
}
