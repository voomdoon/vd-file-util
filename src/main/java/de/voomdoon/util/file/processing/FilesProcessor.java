package de.voomdoon.util.file.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * <b>DRAFT</b> <br>
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class FilesProcessor {

	/**
	 * DOCME add JavaDoc for method process
	 * 
	 * @param input
	 * @throws IOException
	 * @since 0.1.0
	 */
	public void processFiles(String input, Consumer<File> fileProcessor) throws IOException {
		File inputFile = new File(input);

		if (inputFile.isFile()) {
			fileProcessor.accept(new File(input));
		} else {
			processDirectory(inputFile, fileProcessor);
		}
	}

	/**
	 * DOCME add JavaDoc for method processDirectory
	 * 
	 * @param inputFile
	 * @param fileProcessor
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void processDirectory(File inputFile, Consumer<File> fileProcessor) throws IOException {
		Stream<Path> files = Files.find(inputFile.toPath(), 999, (f, a) -> f.toFile().isFile());

		files.forEach(path -> fileProcessor.accept(path.toFile()));

		files.close();
	}
}
