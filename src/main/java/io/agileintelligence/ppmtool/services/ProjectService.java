package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdate(Project project){
        String project_id = project.getProjectIdentifier().toUpperCase();
        try{
            project.setProjectIdentifier(project_id);
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project_id);
            }
            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project_id));
            }
        return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project id:" + project_id + " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project doesn't exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        Iterable<Project>  projects = projectRepository.findAll();
        return projects;
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project doesn't exist");
        }
        projectRepository.delete(project);
    }




}
