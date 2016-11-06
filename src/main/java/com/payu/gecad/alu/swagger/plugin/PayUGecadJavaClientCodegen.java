package com.payu.gecad.alu.swagger.plugin;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;

/**
 * Adds necessary support files, nothing more.
 */
public class PayUGecadJavaClientCodegen extends JavaClientCodegen {
	@Override
	public void processOpts() {
		super.processOpts();

		additionalProperties.put("gson", "false");
		additionalProperties.put("jackson", "true");

		supportingFiles.clear();
		final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
		final String modelFolder = (sourceFolder + '/' + modelPackage).replace(".", "/");

		writeOptional(outputFolder, new SupportingFile("pom.mustache", "", "pom.xml"));
		writeOptional(outputFolder, new SupportingFile("README.mustache", "", "README.md"));
		supportingFiles.add(new SupportingFile("ApiResponse.mustache", invokerFolder, "ApiResponse.java"));
		supportingFiles.add(new SupportingFile("ApiException.mustache", invokerFolder, "ApiException.java"));
		supportingFiles.add(new SupportingFile("Hashing.mustache", invokerFolder, "Hashing.java"));
		supportingFiles.add(new SupportingFile("UTCDateTimeAdapter.mustache", modelFolder, "UTCDateTimeAdapter.java"));

		importMapping.put("Stream", "java.util.stream.Stream");
	}

	@Override
	public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
		super.postProcessModelProperty(model, property);
		if (property.isEnum) {
			// Enums in PayU model have an additional method that uses java.util.stream.Stream
			model.imports.add("Stream");
		}
	}
}
