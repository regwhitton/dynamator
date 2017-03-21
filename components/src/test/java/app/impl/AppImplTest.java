package app.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import app.Project;
import app.camera.CameraAccess;
import app.storage.ProjectStore;
import app.storage.ProjectStoreManager;

@RunWith(MockitoJUnitRunner.class)
public class AppImplTest {

	@Mock
	private CameraAccess cameraAccess;
	
	@Mock
	private ProjectStoreManager projectStoreManager;

	@Mock
	private ProjectFactory projectFactory;

	@InjectMocks
	private AppImpl app;

	@Mock
	private ProjectStore projectStore;

	@Mock
	private ProjectImpl projectImpl;

	@Test
	public void shouldNotOpenLastProjectIfNone() throws Exception {
		when(projectStoreManager.lastProjectPath()).thenReturn(Optional.empty());

		Optional<Project> project = app.openLastProject();
		
		assertThat(project).isEmpty();
	}

	@Test
	public void shouldOpenLastProjectIfOne() throws Exception {
		Path path = Paths.get("lastProject");
		when(projectStoreManager.lastProjectPath()).thenReturn(Optional.of(path));
		when(projectStoreManager.openProjectStore(path)).thenReturn(projectStore);
		when(projectFactory.createProject(projectStore)).thenReturn(projectImpl);

		Optional<Project> project = app.openLastProject();
		
		assertThat(project.get()).isSameAs(projectImpl);
		verify(projectImpl, times(1)).open();
	}

	@Test
	public void shouldOpenProject() throws Exception {
		Path path = Paths.get("project");
		when(projectStoreManager.openProjectStore(path)).thenReturn(projectStore);
		when(projectFactory.createProject(projectStore)).thenReturn(projectImpl);

		Project project = app.openProject(path);
		
		assertThat(project).isSameAs(projectImpl);
		verify(projectImpl, times(1)).open();
	}

	@Test
	public void shouldCreateProject() throws Exception {
		Path path = Paths.get("newProject");
		when(projectStoreManager.createProjectStore(path)).thenReturn(projectStore);
		when(projectFactory.createProject(projectStore)).thenReturn(projectImpl);

		Project project = app.createProject(path);
		
		assertThat(project).isSameAs(projectImpl);
		verify(projectImpl, times(1)).open();
	}
}
