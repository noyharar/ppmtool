package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        try{
            projectTask.setBacklog(backlog);
//    backlog.getProjectTasks().add(projectTask);
            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            Integer priority = projectTask.getPriority();
            if (priority == null || priority == 0) {
                projectTask.setPriority(3);//low
            }
            String status = projectTask.getStatus();
            if (status == null || status == "") {
                projectTask.setStatus("TODO");
            }
            return projectTaskRepository.save(projectTask);

        }catch (Exception e){
            throw new ProjectNotFoundException("Project not found");
        }
    }


    public Iterable<ProjectTask> findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with id:" + id + " not found");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTbyProjectSequence(String backlog_id, String pt_id){
     Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
     if(backlog == null){
         throw new ProjectNotFoundException("Project with id:" + backlog_id + " not found");
     }
     ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

     if(projectTask == null){
         throw new ProjectNotFoundException("Project task with id:" + pt_id + " not found");
     }
     if(!projectTask.getProjectIdentifier().equals(backlog_id)){
         throw new ProjectNotFoundException("Project task with id:" + pt_id + " does not exist in project " +backlog_id);
     }
        return projectTask;
    }

    public ProjectTask updateProjectSequence(ProjectTask updateTask, String backlog_id){
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(updateTask.getProjectSequence());
        projectTask = updateTask;
        return projectTaskRepository.save(projectTask);
    }
}