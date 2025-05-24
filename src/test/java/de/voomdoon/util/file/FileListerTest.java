package de.voomdoon.util.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempInputDirectory;
import de.voomdoon.testing.file.TempInputFile;
import de.voomdoon.testing.file.WithTempInputDirectories;
import de.voomdoon.testing.tests.TestBase;

/**
 * Tests for {@link FileLister}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class FileListerTest {

	/**
	 * Tests for {@link FileLister#listFiles(File)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@ExtendWith(TempFileExtension.class)
	@WithTempInputDirectories(create = true)
	@Nested
	class ListFiles_File_Test extends TestBase {

		/**
		 * @since 0.1.0
		 */
		private FileLister fileLister = new FileLister();

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directoriesAreExcluded(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			new File(directory, "subDir").mkdirs();

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).isEmpty();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directory_doesNotExistsThrowsNoSuchFileException(@TempInputDirectory File directory)
				throws Exception {
			logTestStart();

			assumeThat(directory.delete()).isTrue();

			assertThatThrownBy(() -> fileLister.listFiles(directory)).isInstanceOf(NoSuchFileException.class);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directory_empty(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).isEmpty();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directoryWithFile(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			File file = new File(directory, "test.txt");
			file.createNewFile();

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).contains(file);
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_fileAtDirectory_maxDepthOne_hasFile(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			Path file = Path.of(directory.toString(), "file");
			Files.writeString(file, "content");

			fileLister.withDepth(1);

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).containsExactly(file.toFile());
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_fileAtDirectory_maxDepthZero_isEmpty(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			Path file = Path.of(directory.toString(), "file");
			Files.writeString(file, "content");

			fileLister.withDepth(0);

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).isEmpty();
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_fileAtSubDirectory_maxDepthOne_isEmpty(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			File subDir = new File(directory, "sub");
			subDir.mkdirs();
			Path nestedFile = subDir.toPath().resolve("file.txt");
			Files.writeString(nestedFile, "nested");

			fileLister.withDepth(1);

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).isEmpty();
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_fileAtSubDirectory_maxDepthTwo_hasFile(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			File subDir = new File(directory, "sub");
			subDir.mkdirs();
			Path nestedFile = subDir.toPath().resolve("file.txt");
			Files.writeString(nestedFile, "nested");

			fileLister.withDepth(2);

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).containsExactly(nestedFile.toFile());
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_fileFilter_accept(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			Path file = Path.of(directory.toString(), "accept");
			Files.writeString(file, "content");

			fileLister.withFileFilter(pathname -> pathname.toString().contains("accept"));

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).containsExactly(file.toFile());
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_fileFilter_reject(@TempInputDirectory File directory) throws Exception {
			logTestStart();

			Path file = Path.of(directory.toString(), "rejct");
			Files.writeString(file, "content");

			fileLister.withFileFilter(pathname -> pathname.toString().contains("accept"));

			List<File> actuals = fileLister.listFiles(directory);

			assertThat(actuals).isEmpty();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_rootIsFile(@TempInputFile File file) throws Exception {
			logTestStart();

			file.createNewFile();

			List<File> actuals = fileLister.listFiles(file);

			assertThat(actuals).containsExactly(file);
		}
	}

	/**
	 * Tests for {@link FileLister#withDepth(int)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class WithDepthTest extends TestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_withDepth_returnsInstance() {
			logTestStart();

			FileLister fileLister = new FileLister();

			FileLister actual = fileLister.withDepth(1);

			assertThat(actual).isSameAs(fileLister);
		}
	}

	/**
	 * Tests for {@link FileLister#withFileFilter(FileFilter)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class WithFileFilterTest extends TestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_fileFilter_returnsInstance() {
			logTestStart();

			FileLister fileLister = new FileLister();

			FileLister actual = fileLister.withFileFilter(pathname -> pathname.toString().contains("accept"));

			assertThat(actual).isSameAs(fileLister);
		}
	}
}
