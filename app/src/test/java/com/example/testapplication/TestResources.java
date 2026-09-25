package com.example.testapplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class TestResources {

    private TestResources() {
    }

    public static String read(String name) throws IOException {
        try (InputStream in = TestResources.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) {
                throw new IOException("Test resource not found: " + name);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toString(StandardCharsets.UTF_8.name());
        }
    }
}
