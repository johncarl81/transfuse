package org.androidrobotics.util;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;

public class ResourceCodeWriter extends CodeWriter {
	
	private final Filer filer;
	
	public ResourceCodeWriter(Filer filer) {
		this.filer = filer;
	}

	@Override
	public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
		FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, pkg.name(), fileName);
		return resource.openOutputStream();
	}


	@Override
	public void close() throws IOException {}
}