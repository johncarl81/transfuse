package org.androidrobotics.gen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import org.androidrobotics.model.PackageClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class StringCodeWriter extends CodeWriter {

    private Map<PackageClass, ByteArrayOutputStream> streams = new HashMap<PackageClass, ByteArrayOutputStream>();

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        PackageClass packageFileName = new PackageClass(pkg.name(), fileName);
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

    public String getValue(PackageClass packageFileName) {
        return new String(streams.get(packageFileName).toByteArray());
    }
}
