package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileUtil {

	public static final File ROOT_DIR = new File(new File("").getAbsolutePath());

	public static String read(File file) throws IOException {
		return new String(Files.readAllBytes(file.toPath()));
	}

	public static void list(File file, Consumer<File> consumer) {
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			consumer.accept(f);
		}
	}

	public static Stream<File> list(File file) {
		File[] files = file.listFiles();
		Collection<File> collection = Collections.emptyList();
		if (files != null) {
			collection = Arrays.asList(files);
		}
		return collection.stream();
	}

	public static boolean delete(List<File> files) {
		boolean success = true;
		for (File file : files) {
			success &= delete(file);
		}
		return success;
	}

	@SafeVarargs
	public static boolean delete(File file, Consumer<Boolean>... afterDeleteConsumers) {
		boolean result = false;
		if (file.exists()) {
			try {
				result = deleteRecursive(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (afterDeleteConsumers != null) {
			for (Consumer<Boolean> afterDelete : afterDeleteConsumers) {
				afterDelete.accept(result);
			}
		}
		return result;
	}

	private static boolean deleteRecursive(File file) throws IOException {
		boolean result = true;
		if (file.isDirectory() && file.listFiles() != null) {
			for (File child : file.listFiles()) {
				result &= deleteRecursive(child);
			}
		}
		return result && file.delete();
	}

	public static boolean delete(File[] files) {
		return delete(Arrays.asList(files));
	}

	public static void move(File srcDir, File destDir) {
		String srcDirPath = srcDir.getAbsolutePath();
		String destDirPath = destDir.getAbsolutePath();
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		for (File file : srcDir.listFiles()) {
			file.renameTo(new File(file.getAbsolutePath().replace(srcDirPath, destDirPath)));
		}

	}

	public static File buildFile(String... paths) {
		return new File(build(paths));
	}

	public static void write(File file, String content) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.append(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String build(String... paths) {
		StringBuilder builder = new StringBuilder(paths[0]);
		for (int i = 1; i < paths.length; i++) {
			builder.append(File.separator).append(paths[i]);
		}
		return builder.toString();
	}

	public static File buildFile(File dir, String... paths) {
		return new File(dir, build(paths));
	}

	public static String relativePath(File file) {
		return file.getAbsolutePath().replace(ROOT_DIR.getAbsolutePath(), ".");
	}

	public static File relativeFile(String name) {
		return new File(new File("").getAbsoluteFile(), name);
	}

	public static String[] directoyHierarchy(File file) {
		String separator = "\\".equals(File.separator) ? "\\\\" : File.separator;
		if (file.isFile()) {
			file = file.getParentFile();
		}
		return file.getPath().split(separator);
	}

}
