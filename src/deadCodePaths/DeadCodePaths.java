// DeadCodePaths.java
// by Chuck Plater
// ab3189@wayne.edu
// cplater@me.com
// and Marek Malinowski
// marek@wayne.edu
//
// Term Paper Project for CSC 6110 Winter 2020
//
// Use soot to determine if there are any methods that exist but are not called
// using the callgraph generated by soot.
//
// TODO: When getting the methods, we should use a tuple to store the class name and the line number
//       to be used when reporting potential dead code paths.

package deadCodePaths;

import soot.*;
import soot.jimple.infoflow.android.*;
import soot.jimple.toolkits.callgraph.*;
import java.util.stream.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class DeadCodePaths {

	public static void main(String[] args) {

		String androidPlatformPath = "/Users/cplater/Developer/android-platforms";
//		String androidPlatformPath = "/home/marek/Downloads/csc6110/android-platforms-master";
		String appPath = "/Users/cplater/Documents/School/202001 Winter 2020/CSC 6110 Software Engineering/Homework 1/apks/two.apk";
//		String appPath = "/Users/cplater/Downloads/PUBG MOBILE_v0.17.0_apkpure.com.apk";
		if (args.length == 2) {
			androidPlatformPath = args[0];
			appPath = args[1];
		}

		final String temp = androidPlatformPath;

		try (Stream<Path> walk = Files
				.walk(Paths.get("/Users/cplater/Developer/DeadCodeSample/app/build/outputs/apk/debug"))) {
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".apk"))
					.collect(Collectors.toList());
			result.forEach(item -> System.out.println(item));
			System.out.println("\n-----------------");
			result.forEach(item -> processAPK(temp, item));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void processAPK(String androidPlatformPath, String appPath) {
		SetupApplication app = new SetupApplication(androidPlatformPath, appPath);
		app.constructCallgraph();
		// Create a list to store the method names
		List<String> availableMethods = new ArrayList<String>();

		for (SootClass entryPoint : app.getEntrypointClasses()) {
			for (SootMethod method : entryPoint.getMethods()) {
				// Check to see if the method has already been added to the list
				if ((!availableMethods.contains(method.getDeclaration())
						& (!method.getDeclaration().contains("android.content.Intent"))))
					// Add the method to the list if it is new
					availableMethods.add(method.getDeclaration());
			}
		}
		System.out.println();
		System.out.println("\n-----------------");
		System.out.println(appPath);
		System.out.println("\n-----------------");
		System.out.printf("%d methods found.\n", availableMethods.size());
		CallGraph appCallGraph = Scene.v().getCallGraph();
		for (Edge edge : appCallGraph) {
			if (app.getEntrypointClasses().contains(edge.getTgt().method().getDeclaringClass())) {
				// remove target methods (they're getting called)
				if (availableMethods.contains(edge.getTgt().method().getDeclaration())) {
					availableMethods.remove(edge.getTgt().method().getDeclaration());
				}
				// remove source methods (they're doing the calling)
				if (availableMethods.contains(edge.getSrc().method().getDeclaration())) {
					availableMethods.remove(edge.getSrc().method().getDeclaration());
				}
			}
		}
		System.out.printf("%d methods left.\n", availableMethods.size());
		System.out.println("\n-----------------");
		for (String method : availableMethods) {
			System.out.print(method);
			System.out.print(" not found in call graph\n");
		}
	}
}
