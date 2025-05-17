package de.voomdoon.util.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempInputDirectory;
import de.voomdoon.testing.file.WithTempInputDirectories;
import de.voomdoon.testing.tests.TestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@ExtendWith(TempFileExtension.class)
@WithTempInputDirectories(create = true)
class FileTreeFormatterTest extends TestBase {

	/**
	 * @since 0.1.0
	 */
	private FileTreeFormatter formatter = new FileTreeFormatter();

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_rootDirectoryContainsSlashAtTheEnd(@TempInputDirectory File directory) {
		logTestStart();

		String actual = format(directory);

		assumeThat(actual).contains(directory.getName());
		assertThat(actual).contains(directory.getName() + "/");
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_rootEmpty(@TempInputDirectory File directory) {
		logTestStart();

		String actual = format(directory);

		assertThat(actual).contains(directory.getName());
	}

	/**
	 * @throws IOException
	 * @since 0.1.0
	 */
	@Test
	void test_rootWithFile(@TempInputDirectory File directory) throws IOException {
		logTestStart();

		new File(directory + "/test.txt").createNewFile();

		String actual = format(directory);

		assertThat(actual).isEqualTo(directory.getName() + "/" //
				+ "\n└── test.txt");
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_subDirectory1(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		new File(directory + "/test-sub-dir").mkdir();

		String actual = format(directory);

		assertThat(actual).isEqualTo(directory.getName() + "/"//
				+ "\n└── test-sub-dir/");
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_subDirectory2(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		new File(directory + "/test-sub-dir1").mkdir();
		new File(directory + "/test-sub-dir2").mkdir();

		String actual = format(directory);

		assertThat(actual).isEqualTo(directory.getName() + "/"//
				+ "\n├── test-sub-dir1/" //
				+ "\n└── test-sub-dir2/");
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_subDirectory2_order(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		new File(directory + "/test-sub-dir2").mkdir();
		new File(directory + "/test-sub-dir1").mkdir();

		String actual = format(directory);

		assertThat(actual).isEqualTo(directory.getName() + "/" //
				+ "\n├── test-sub-dir1/" //
				+ "\n└── test-sub-dir2/");
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void test_subSubDirectory(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		new File(directory + "/test-sub-dir1/test-sub-dir2").mkdirs();

		String actual = format(directory);

		assertThat(actual).isEqualTo(directory.getName() + "/" //
				+ "\n└── test-sub-dir1/" //
				+ "\n    └── test-sub-dir2/");
	}

	/**
	 * @param directory
	 * @return
	 * @since 0.1.0
	 */
	private String format(File directory) {
		String actual = formatter.format(directory);

		logger.debug("result:\n" + actual);

		return actual;
	}
}
