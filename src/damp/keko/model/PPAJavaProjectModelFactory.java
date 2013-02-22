package damp.keko.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


import org.eclipse.core.resources.IProject;

import damp.ekeko.IProjectModel;
import damp.ekeko.IProjectModelFactory;
import damp.keko.PPAJavaNature;

public class PPAJavaProjectModelFactory implements IProjectModelFactory {

	public PPAJavaProjectModelFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IProjectModel createModel(IProject project) {
		return new PPAJavaProjectModel(project);
	}

	@Override
	public Collection<String> applicableNatures() {
		return Arrays.asList(new String[]{PPAJavaNature.NATURE_ID});
	}

	@Override
	public Collection<IProjectModelFactory> conflictingFactories(IProject p,
			Collection<IProjectModelFactory> applicableFactories) {
		return Collections.emptyList();
	}

}
