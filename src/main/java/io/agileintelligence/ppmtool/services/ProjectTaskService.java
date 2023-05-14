package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
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
    }


    public Iterable<ProjectTask> findBacklogById(String id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}