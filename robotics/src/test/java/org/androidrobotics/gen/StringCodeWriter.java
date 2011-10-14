package org.androidrobotics.gen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class StringCodeWriter extends CodeWriter {

    private Map<PackageFileName, ByteArrayOutputStream> streams = new HashMap<PackageFileName, ByteArrayOutputStream>();

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        PackageFileName packageFileName = new PackageFileName(pkg.name(), fileName);
        if (!streams.containsKey(packageFileName)) {
            streams.put(packageFileName, new ByteArrayOutputStream());
        }

        return streams.get(packageFileName);
    }

    @Override
    public void close() throws IOException {
        for (OutputStream outputStream : streams.values()) {
            outputStream.flush();
            outputStream.close();
        }
    }

    public String getValue(PackageFileName packageFileName) {
        return new String(streams.get(packageFileName).toByteArray());
    }
}
