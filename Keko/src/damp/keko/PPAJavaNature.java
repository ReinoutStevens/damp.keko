package damp.keko;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class PPAJavaNature implements IProjectNature {
	
	public static final String NATURE_ID = "keko.projectNature";

	IProject project;
	public static void toggleNature(IProject project){
		try {
			damp.util.Natures.toggleNature(project, NATURE_ID);
		} catch (CoreException e) {
			e.printStackTrace();
		}
				
	}
	@Override
	public void configure() throws CoreException {
	}

	@Override
	public void deconfigure() throws CoreException {

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project; 
	}

}
