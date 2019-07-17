package org.incredible.csvProcessor;


import org.apache.commons.lang3.StringUtils;
import org.incredible.certProcessor.CertificateFactory;
import org.sunbird.cloud.storage.BaseStorageService;


import org.sunbird.cloud.storage.factory.StorageConfig;
import org.sunbird.cloud.storage.factory.StorageServiceFactory;
import scala.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class CloudStorageUtil {

    private static BaseStorageService storageService = null;

    static CertificateFactory certificateFactory = new CertificateFactory();

    static Properties properties = certificateFactory.readPropertiesFile();

    private static String cloudStoreType = properties.getProperty("cloud_storage_type");

    static {

        if (StringUtils.equalsIgnoreCase(cloudStoreType, "azure")) {
            String storageKey = System.getenv("azure_account_name");
            String storageSecret = System.getenv("azure_storage_key");
            storageService = StorageServiceFactory.getStorageService(new StorageConfig(cloudStoreType, storageKey, storageSecret));
        } else if (StringUtils.equalsIgnoreCase(cloudStoreType, "aws")) {
            String storageKey = System.getenv("aws_storage_key");
            String storageSecret = System.getenv("aws_storage_secret");
            storageService = StorageServiceFactory.getStorageService(new StorageConfig(cloudStoreType, storageKey, storageSecret));
        } else {
//            throw new ServerException("ERR_INVALID_CLOUD_STORAGE Error while initialising cloud storage");
        }
    }

    public static String uploadFile(String container, String path, File file, boolean isDirectory) {
        int retryCount = Integer.parseInt(properties.getProperty("cloud_upload_retry_count"));
        String objectKey = path + file.getName();
        String url = storageService.upload(container,
                file.getAbsolutePath(),
                objectKey,
                Option.apply(isDirectory),
                Option.apply(1),
                Option.apply(retryCount), Option.empty());
        return url;
    }

    public static void downloadFile(String downloadUrl, File fileToSave) throws IOException {
        URL url = new URL(downloadUrl);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileChannel.close();
        fileOutputStream.close();
        readableByteChannel.close();
    }

}
