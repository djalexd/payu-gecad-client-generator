package com.payu.gecad.alu.swagger.plugin;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;

/**
 * Adds necessary support files, nothing more.
 */
public class PayUGecadJavaClientCodegen extends JavaClientCodegen {
	public PayUGecadJavaClientCodegen() {
		supportedLibraries.put("httpclient-jackson", "Apache components HttpClient + Jackson mapper");
	}

	@Override
	public String getLibrary() {
		return "httpclient-jackson";
	}

	@Override
	public void processOpts() {
		super.processOpts();

		additionalProperties.remove("gson");
		additionalProperties.put("jackson", "true");

		supportingFiles.clear();
		final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
		final String modelFolder = (sourceFolder + '/' + modelPackage).replace(".", "/");
		final String apiFolder = (sourceFolder + '/' + apiPackage).replace(".", "/");

		writeOptional(outputFolder, new SupportingFile("pom.xml.mustache", "", "pom.xml"));
		writeOptional(outputFolder, new SupportingFile("README.mustache", "", "README.md"));
		supportingFiles.add(new SupportingFile("ApiResponse.mustache", invokerFolder, "ApiResponse.java"));
		supportingFiles.add(new SupportingFile("ApiException.mustache", invokerFolder, "ApiException.java"));
		supportingFiles.add(new SupportingFile("Hashing.mustache", invokerFolder, "Hashing.java"));
		supportingFiles.add(new SupportingFile("UTCDateTimeAdapter.mustache", modelFolder, "UTCDateTimeAdapter.java"));
		
		supportingFiles.add(new SupportingFile("AluApiBuilder.mustache", apiFolder, "AluApiBuilder.java"));
		supportingFiles.add(new SupportingFile("AluApi.mustache", apiFolder, "AluApi.java"));
		
		final String testFolder = (this.testFolder + '/' + apiPackage).replace(".", "/");
		supportingFiles.add(new SupportingFile("AluApiTest.mustache", testFolder, "AluApiTest.java"));
		importMapping.put("Stream", "java.util.stream.Stream");
		apiTemplateFiles.clear();
		apiTestTemplateFiles().clear();
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
