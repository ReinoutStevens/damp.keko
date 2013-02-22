package damp.keko.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ca.mcgill.cs.swevo.ppa.PPAOptions;
import ca.mcgill.cs.swevo.ppa.ui.PPAUtil;
import damp.ekeko.JavaProjectModel;

public class PPAJavaProjectModel extends JavaProjectModel {

	
	private Collection<IFile> partialFiles = new ArrayList<IFile>();
	
	public PPAJavaProjectModel(IProject p) {
		super(p);
	}

	
	@Override
	public void populate(IProgressMonitor monitor) throws CoreException {
		//super.populate(monitor); //it borks because the project has no Java Model normally.
		findAndAddJavaFiles();
		PPAOptions options = new PPAOptions();//true, true, true, true, 512);
		
		List<File> files = new ArrayList<File>();
		
		
		
		for (IFile partialFile : partialFiles) {
			String osPath = partialFile.getRawLocation().toOSString();
			File f = new File(osPath);
			files.add(f);
		}
		
		List<CompilationUnit> cus = PPAUtil.getCUs(files, options, getProject().getName());
		for (CompilationUnit compilationUnit : cus) {
			icu2ast.put((ICompilationUnit)compilationUnit.getJavaElement(),compilationUnit);
		}
		
		gatherInformationFromCompilationUnits();
	}
	
	
	
	public void setPartialFiles(Collection<IFile> partialFiles) {
		this.partialFiles = partialFiles;
	}
	
	public void addFile(String filePath){
		IPath path = new Path(filePath);
		addFile(path);
	}
	
	
	public void addFile(IPath path){
		partialFiles.add(ResourcesPlugin.getWorkspace().getRoot().getFile(path));	
	}
	
	
	public ASTNode populateSnippet(String code, boolean isTypeBody){
		return PPAUtil.getSnippet(code, new PPAOptions(), isTypeBody);
	}
	
	/*
	 * Retrieves and adds all the java files that can be found in the project.
	 * It ignores files stored inside hidden folder (eg. .git/Foo.java)
	 */
	private void findAndAddJavaFiles() throws CoreException{
		if(partialFiles.isEmpty()){
			for(IResource resource : getProject().members()){
				processResource(resource);
			}	
		}
	}
	

	private void processResource(IResource resource) throws CoreException{
		if (resource instanceof IFolder) {
			IFolder folder = (IFolder) resource;
			processFolder(folder);
		} else if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			processFile(file);
		}
	}
	
	private void processFolder(IFolder folder) throws CoreException{
		IPath path = folder.getFullPath();
		if(!isHidden(path)){
			for(IResource resource : folder.members()){
				processResource(resource);
			}
		}
	}
	
	
	
	private void processFile(IFile file){
		IPath path = file.getFullPath();
		if(path.getFileExtension().equals("java")){
			addFile(path);
		}
	}
	
	
	private boolean isHidden(IPath path){
		String segment = path.lastSegment();
		if(segment == null){
			return false;
		}
		return segment.startsWith(".");
	}
}
