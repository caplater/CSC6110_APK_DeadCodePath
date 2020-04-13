package deadCodePaths;

import soot.*;
import soot.jimple.infoflow.android.*;
import soot.jimple.toolkits.callgraph.*;
import java.util.*;

public class DeadCodePaths {
	
	public static void main(String[] args) {
		
		String androidPlatformPath = "/Users/cplater/Developer/android-platforms";
		String appPath = "/Users/cplater/Documents/School/202001 Winter 2020/CSC 6110 Software Engineering/Homework 1/apks/two.apk";

		if (args.length == 2) {
			androidPlatformPath = args[0];
			appPath = args[1];
		}
		
		SetupApplication app = new SetupApplication(androidPlatformPath,appPath);
		
		app.constructCallgraph();
		
		List<String> availableMethods = new ArrayList<String>();
		
		for (SootClass entryPoint : app.getEntrypointClasses()) {
			for (SootMethod methods : entryPoint.getMethods()) {
				if (! availableMethods.contains(methods.getName())) 
					availableMethods.add(methods.getName());
				}
		}
		CallGraph appCallGraph = Scene.v().getCallGraph();
		for (Edge edge : appCallGraph) {
			if (app.getEntrypointClasses().contains(edge.getTgt().method().getDeclaringClass())) {
				if (availableMethods.contains(edge.getTgt().method().getName())) {
					availableMethods.remove(edge.getTgt().method().getName());
				}
				if (availableMethods.contains(edge.getSrc().method().getName())) {
					availableMethods.remove(edge.getSrc().method().getName());
				}
			}
		}
			System.out.print(method);
			System.out.print(" not found in call graph\n");
		}
	}
}
