package de.voomdoon.util.file;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

/**
 * Utility for generating a textual tree representation of a directory structure. Uses Unicode box-drawing characters to
 * visually indicate the hierarchy.
 *
 * <p>
 * Example output:
 * 
 * <pre>
 * project/
 * ├── src/
 * │   └── Main.java
 * └── README.md
 * </pre>
 * 
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class FileTreeFormatter {

	/**
	 * Generates a tree-like string representation of the given directory or file.
	 * 
	 * @param directory
	 *            the root file or directory to format
	 * @return formatted tree {@link String}
	 * @since 0.1.0
	 */
	public String format(File directory) {
		StringBuilder sb = new StringBuilder();

		appendFileOrDirectory(sb, directory, 0, Optional.empty());

		return sb.toString();
	}

	/**
	 * @param sb
	 *            {@link StringBuilder}
	 * @param fileOrDirectory
	 *            {@link File}
	 * @param level
	 *            the current nesting level (starting at 0)
	 * @param last
	 *            whether this entry is the last child at its level
	 * @since 0.1.0
	 */
	private void appendFileOrDirectory(StringBuilder sb, File fileOrDirectory, int level, Optional<Boolean> last) {
		if (level > 0 && last.isPresent()) {// TESTME
			sb.append("\n");
			sb.append(" ".repeat((level - 1) * 4));

			if (!last.get()) {
				sb.append("├── ");
			} else {
				sb.append("└── ");
			}
		}

		sb.append(fileOrDirectory.getName());

		if (fileOrDirectory.isDirectory()) {
			sb.append("/");
		}

		appendFilesOfDirectory(sb, fileOrDirectory, level);
	}

	/**
	 * @param sb
	 *            {@link StringBuilder}
	 * @param directory
	 *            directory as {@link File}
	 * @param level
	 *            the current nesting level (starting at 0)
	 * @since 0.1.0
	 */
	private void appendFilesOfDirectory(StringBuilder sb, File directory, int level) {
		if (!directory.isDirectory()) {
			return;
		}

		File[] files = directory.listFiles();
		Arrays.sort(files);// TESTME

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			boolean last = (i == files.length - 1);
			appendFileOrDirectory(sb, file, level + 1, Optional.of(last));
		}
	}
}
