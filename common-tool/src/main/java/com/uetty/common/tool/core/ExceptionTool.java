package com.uetty.common.tool.core;

public class ExceptionTool {

	public static void getStackTraceOnPackage(String packageName, Throwable e, StringBuilder sb) {
		printStackTraceOnPackage_0(packageName, e, sb);
	}
	
	public static void printStackTraceOnPackage(String packageName, Throwable e) {
		StringBuilder sb = new StringBuilder();
		printStackTraceOnPackage_0(packageName, e, sb);
		System.out.println(sb);
	}
	
	/**
	 * 只打印starnet有关的异常
	 */
	private static void printStackTraceOnPackage_0(String packageName, Throwable e, StringBuilder sb) {
		if (e == null || sb == null) return;
		
		sb.append(e.getClass().getName() + " : " + e.getMessage() + "\n");
		StackTraceElement[] stackTraces = e.getStackTrace();
		for (StackTraceElement trace : stackTraces) {
			if (trace.getClassName().startsWith(packageName)) {
				sb.append("\tat " + trace.toString() + "\n");
			}
		}
		Throwable[] suppressed = e.getSuppressed();
		for (Throwable throwable : suppressed) {
			printStackTraceOnPackage_0(packageName, throwable, sb);
		}
		Throwable cause = e.getCause();
		printStackTraceOnPackage_0(packageName, cause, sb);
	}
}
