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
		final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
		supportingFiles.add(new SupportingFile("Hashing.mustache", invokerFolder, "Hashing.java"));
		supportingFiles.add(new SupportingFile("XML.mustache", invokerFolder, "XML.java"));
		supportingFiles.add(new SupportingFile("TypeConverter.mustache", invokerFolder, "TypeConverter.java"));
		supportingFiles.add(new SupportingFile("TypeConverterRegistry.mustache", invokerFolder, "TypeConverterRegistry.java"));
		supportingFiles.add(new SupportingFile("Convertors.mustache", invokerFolder, "Convertors.java"));

		final String apiFolder = (sourceFolder + '/' + apiPackage).replace(".", "/");
		supportingFiles.add(new SupportingFile("alu.mustache", apiFolder, "Alu.java"));

		final String modelFolder = (sourceFolder + '/' + modelPackage).replace(".", "/");
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
