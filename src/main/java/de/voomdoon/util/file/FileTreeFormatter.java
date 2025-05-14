package de.voomdoon.util.file;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class FileTreeFormatter {

	/**
	 * DOCME add JavaDoc for method format
	 * 
	 * @param directory
	 * @return
	 * @since 0.1.0
	 */
	public String format(File directory) {
		StringBuilder sb = new StringBuilder();

		appendDirectory(sb, directory, 0, Optional.empty());

		return sb.toString();
	}

	/**
	 * DOCME add JavaDoc for method append
	 * 
	 * @param sb
	 * @param directory
	 * @param level
	 * @param last
	 * @since 0.1.0
	 */
	private void appendDirectory(StringBuilder sb, File directory, int level, Optional<Boolean> last) {
		if (level > 0 && last.isPresent()) {
			sb.append("\n");
			sb.append(" ".repeat((level - 1) * 4));

			if (!last.get()) {
				sb.append("├── ");
			} else {
				sb.append("└── ");
			}
		}

		sb.append(directory.getName());
		appendFilesOfDirectory(sb, directory, level);
	}

	/**
	 * DOCME add JavaDoc for method appendFilesOfDirectory
	 * 
	 * @param sb
	 * @param directory
	 * @param level
	 * @since 0.1.0
	 */
	private void appendFilesOfDirectory(StringBuilder sb, File directory, int level) {
		if (!directory.isDirectory()) {
			return;
		}

		File[] files = directory.listFiles();
		Arrays.sort(files);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			boolean last = (i == files.length - 1);
			appendDirectory(sb, file, level + 1, Optional.of(last));
		}
	}
}
