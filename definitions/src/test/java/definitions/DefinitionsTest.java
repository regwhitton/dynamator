package definitions;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DefinitionsTest {

	private final static String JAVA_FILE_SUFFIX = ".java";
	private final static int JAVA_FILE_SUFFIX_LEN = JAVA_FILE_SUFFIX.length();

	@Test
	public void definitionsShouldContainInterfacesAndEnumsOnly() throws Exception {
		for (Class<?> cls : new ClassList("src/main/java").list()) {
			if(!cls.isInterface() && !cls.isEnum()){
				fail(cls.getName() + " must be an interface or enum");
			}
		}
	}

	private static class ClassList {
		
		private final List<Class<?>> list = new ArrayList<Class<?>>();

		ClassList(String dir) throws ClassNotFoundException {
			searchDir(new File(dir), "");
		}

		List<Class<?>> list() {
			return list;
		}

		private void searchDir(File dir, String packagePrefix) throws ClassNotFoundException {
			for (File file : dir.listFiles()) {
				String fileName = file.getName();
				if (file.isDirectory()) {
					searchDir(file, packagePrefix + fileName + ".");
				} else if (fileName.endsWith(JAVA_FILE_SUFFIX)) {
					String className = packagePrefix + fileName.substring(0, fileName.length() - JAVA_FILE_SUFFIX_LEN);
					addClassAndInnerClasses(className);
				}
			}
		}

		private void addClassAndInnerClasses(String className) throws ClassNotFoundException {
			Class<?> cls = Class.forName(className);
			list.add(cls);
			for (Class<?> cls2 : cls.getClasses()) {
				list.add(cls2);
			}
		}
	}
}
