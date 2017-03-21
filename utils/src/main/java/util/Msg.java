package util;

import static util.Verify.argNotNull;

public class Msg {

	public static String extract(String message, Throwable throwable) {
		StringBuilder b = new StringBuilder(argNotNull(message));
		Throwable ex = argNotNull(throwable);
		while (ex != null) {
			String m = ex.getMessage();
			if (b.length() > 0 && m != null && !(m = m.trim()).isEmpty()) {
				b.append(": ");
				b.append(m);
			}
			ex = (ex != ex.getCause()) ? ex.getCause() : null;
		}
		return b.toString();
	}
}
