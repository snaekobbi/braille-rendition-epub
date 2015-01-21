import java.io.File;
import javax.inject.Inject;

import com.google.common.collect.ImmutableMap;

import org.daisy.maven.xproc.api.XProcEngine;

import static org.daisy.pipeline.pax.exam.Options.brailleModule;
import static org.daisy.pipeline.pax.exam.Options.bundlesAndDependencies;
import static org.daisy.pipeline.pax.exam.Options.domTraversalPackage;
import static org.daisy.pipeline.pax.exam.Options.felixDeclarativeServices;
import static org.daisy.pipeline.pax.exam.Options.forThisPlatform;
import static org.daisy.pipeline.pax.exam.Options.logbackBundles;
import static org.daisy.pipeline.pax.exam.Options.pipelineModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.PathUtils;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class convert {
	
	@Test
	public void run() throws Exception {
		File baseDir = new File(PathUtils.getBaseDir());
		if ("".equals("${source}"))
			throw new RuntimeException("source not specified");
		File source = new File(baseDir, "${source}");
		if ("".equals("${output-dir}"))
			throw new RuntimeException("output-dir not specified");
		File outputDir = new File(baseDir, "${output-dir}");
		engine.run("http://www.daisy.org/pipeline/modules/braille/braille-in-epub3/braille-in-epub3.xpl",
		           null,
		           null,
		           ImmutableMap.of("source", source.toURI().toASCIIString(),
		                           "output-dir", outputDir.toURI().toASCIIString()),
		           null);
	}
	
	@Inject
	private XProcEngine engine;
	
	@Configuration
	public Option[] config() {
		return options(
			systemProperty("logback.configurationFile").value("file:" + PathUtils.getBaseDir() + "/logback.xml"),
			systemProperty("org.daisy.pipeline.xproc.configuration").value(PathUtils.getBaseDir() + "/calabash.xml"),
			systemProperty("com.xmlcalabash.config.user").value(""),
			domTraversalPackage(),
			logbackBundles(),
			felixDeclarativeServices(),
			mavenBundle().groupId("net.java.dev.jna").artifactId("jna").versionAsInProject(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.antlr-runtime").versionAsInProject(),
			mavenBundle().groupId("org.daisy.braille").artifactId("braille-utils.api").versionAsInProject(),
			mavenBundle().groupId("org.daisy.libs").artifactId("jing").versionAsInProject(),
			mavenBundle().groupId("org.daisy.libs").artifactId("jstyleparser").versionAsInProject(),
			mavenBundle().groupId("org.unbescape").artifactId("unbescape").versionAsInProject(),
			mavenBundle().groupId("org.daisy.braille").artifactId("braille-css").versionAsInProject(),
			mavenBundle().groupId("org.liblouis").artifactId("liblouis-java").versionAsInProject(),
			mavenBundle().groupId("org.daisy.bindings").artifactId("jhyphen").versionAsInProject(),
			brailleModule("braille-in-epub3"),
			brailleModule("common-utils"),
			brailleModule("liblouis-core"),
			brailleModule("liblouis-saxon"),
			brailleModule("liblouis-utils"),
			brailleModule("liblouis-tables"),
			brailleModule("liblouis-calabash"),
			brailleModule("libhyphen-core"),
			brailleModule("css-core"),
			brailleModule("css-calabash"),
			brailleModule("css-utils"),
			brailleModule("pef-core"),
			forThisPlatform(brailleModule("liblouis-native")),
			pipelineModule("fileset-utils"),
			pipelineModule("file-utils"),
			pipelineModule("common-utils"),
			pipelineModule("html-utils"),
			pipelineModule("zip-utils"),
			pipelineModule("mediatype-utils"),
			bundlesAndDependencies("org.daisy.maven.xproc-engine-daisy-pipeline"),
			junitBundles()
		);
	}
}
