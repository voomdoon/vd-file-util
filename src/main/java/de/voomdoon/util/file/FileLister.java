package de.voomdoon.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility for recursively listing files in a directory with optional depth and filter settings.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class FileLister {

	/**
	 * @since 0.1.0
	 */
	private FileFilter fileFilter;

	/**
	 * @since 0.1.0
	 */
	private Integer maxDepth;

	/**
	 * Returns a list of files under the given file or directory. Only files are included; directories are excluded.
	 * 
	 * @param fileOrDirectory
	 *            the root directory or file to start listing from
	 * @return {@link List} of matching {@link File}s
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since 0.1.0
	 */
	public List<File> listFiles(File fileOrDirectory) throws IOException {
		return getStream(fileOrDirectory)//
				.map(Path::toFile)//
				.filter(File::isFile)//
				.filter(file -> fileFilter == null || fileFilter.accept(file))//
				.toList();
	}

	/**
	 * Sets the maximum recursion depth for file listing.
	 * 
	 * @param maxDepth
	 *            the maximum depth
	 * @return this instance
	 * @since 0.1.0
	 */
	public FileLister withDepth(int maxDepth) {
		this.maxDepth = maxDepth;

		return this;
	}

	/**
	 * Sets a {@link FileFilter} to restrict which files are included.
	 * 
	 * @param fileFilter
	 *            {@link FileFilter}
	 * @return this instance
	 * @since 0.1.0
	 */
	public FileLister withFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;

		return this;
	}

	/**
	 * @param fileOrDirectory
	 *            {@link File}
	 * @return {@link Stream} of {@link Path}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since 0.1.0
	 */
	private Stream<Path> getStream(File fileOrDirectory) throws IOException {
		if (maxDepth != null) {
			return Files.walk(fileOrDirectory.toPath(), maxDepth);
		}

		return Files.walk(fileOrDirectory.toPath());
	}
}
