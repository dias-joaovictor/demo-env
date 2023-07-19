package com.example.demoenv;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Properties;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class DemoEnvApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoEnvApplication.class, args);
    }

    private final ConfigurableApplicationContext context;

//    @Value("${sftp.private.key}")
//    private String privateKey;

    @Value("${my.variable}")
    private String privateKey;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.port}")
    private int port;

    @Value("file:/heyworld")
    private Resource myFile;

    @Value("file:/root/.ssh/known_hosts")
    private Resource knownHosts;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");
        //System.out.println(privateKey);
        log.info(new String(myFile.getInputStream().readAllBytes()));
        System.out.println(host);
        System.out.println(user);
        System.out.println(port);
        // System.out.println(privateKey.length());
        // System.out.println(privateKey.split("\n").length);

        // Resource resource = new ByteArrayResource(privateKey.getBytes());
        // System.out.println(resource.contentLength());
        // System.out.println(resource.getFilename());
        // tryToConnect();
        context.close();
    }

    private void tryToConnect() throws IOException {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setUser(user);
        if (privateKey != null) {
            // Resource resource = new ByteArrayResource(privateKey.getBytes());
            Resource resource = myFile;
            log.info(String.valueOf(resource.contentLength()));
            factory.setPrivateKey(resource);
            factory.setSessionConfig(new Properties());
        } else {
            throw new IllegalArgumentException("HeyWorld SFTP password or private key is not found!");
        }
        factory.setHost(host);
        factory.setPort(port);
        factory.setAllowUnknownKeys(true);
        factory.setKnownHostsResource(knownHosts);
        SftpRemoteFileTemplate template = new SftpRemoteFileTemplate(factory);

		JSch.setLogger(new Logger() {
			public boolean isEnabled(int level) {
				return true;
			}

			public void log(int level, String message) {
				log.info(message);
			}
		});

        try {
            template.execute(session -> {
                session.list(".");  // list files in the home directory
                return null;
            });
        } catch (Exception ex) {
            log.error("Failed to connect to SFTP server: {}", ex.getMessage(), ex);
        }
    }
}
