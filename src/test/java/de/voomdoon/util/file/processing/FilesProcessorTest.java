package de.voomdoon.util.file.processing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempInputDirectory;
import de.voomdoon.testing.file.WithTempInputDirectories;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * Tests for {@link FilesProcessor}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@ExtendWith(TempFileExtension.class)
@WithTempInputDirectories(create = true)
class FilesProcessorTest extends LoggingCheckingTestBase {

	/**
	 * @since 0.1.0
	 */
	@Test
	void testProcessFiles_directory(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		Path file1 = Path.of(directory.toString(), "test1.file");
		Files.writeString(file1, "test");

		Path file2 = Path.of(directory.toString(), "test2.file");
		Files.writeString(file2, "test");

		@SuppressWarnings("unchecked")
		Consumer<File> fileProcessor = mock(Consumer.class);

		new FilesProcessor().processFiles(directory.toString(), fileProcessor);

		ArgumentCaptor<File> argument = ArgumentCaptor.forClass(File.class);

		Mockito.verify(fileProcessor, Mockito.atLeastOnce()).accept(argument.capture());

		List<File> values = argument.getAllValues();

		assertThat(values).containsExactlyInAnyOrder(file1.toFile(), file2.toFile());
	}

	/**
	 * @throws IOException
	 * 
	 * @since 0.1.0
	 */
	@Test
	void testProcessFiles_file(@TempInputDirectory File directory) throws IOException {
		Path file = Path.of(directory.toString(), "test.file");
		Files.writeString(file, "test");

		@SuppressWarnings("unchecked")
		Consumer<File> fileProcessor = mock(Consumer.class);

		new FilesProcessor().processFiles(file.toString(), fileProcessor);

		Mockito.verify(fileProcessor).accept(file.toFile());
	}
}