package deadCodePaths;

import soot.*;
import soot.jimple.infoflow.android.*;
import soot.jimple.toolkits.callgraph.*;
import java.util.*;

public class DeadCodePaths {

	
	public static void main(String[] args) {
		
//		System.out.println("Hello World!");
		
		String androidPlatformPath = "/Users/cplater/Developer/android-platforms";
		String appPath = "/Users/cplater/Documents/School/202001 Winter 2020/CSC 6110 Software Engineering/Homework 1/apks/one.apk";
		
		SetupApplication app = new SetupApplication
                (androidPlatformPath,
                        appPath);
		
		app.constructCallgraph();
		Set<SootClass> entryPoints = app.getEntrypointClasses();
//		System.out.print(entryPoints);
		for (SootClass method : entryPoints) {
			System.out.print(method);
			System.out.print("\n");
		}
		CallGraph appCallGraph = Scene.v().getCallGraph();
		
		for (Edge cg : appCallGraph) {
			System.out.print(cg);
			System.out.print("\n");
		}
//		System.out.print(appCallGraph);
		

	}
}
