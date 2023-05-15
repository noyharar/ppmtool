package io.agileintelligence.ppmtool.web;


import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.services.MapValidationErrorService;
import io.agileintelligence.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPtToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null){
            return errorMap;
        }
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask);
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id){
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id){
        ProjectTask projectTask = projectTaskService.findPTbyProjectSequence(backlog_id, pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }
    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask, BindingResult result, @PathVariable String backlog_id, @PathVariable String pt_id){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null){
            return errorMap;
        }
        ProjectTask projectTask = projectTaskService.updateProjectSequence(updatedProjectTask, backlog_id);

        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask( @PathVariable String backlog_id, @PathVariable String pt_id){
        projectTaskService.deletePTProjectSequence(backlog_id,pt_id);
        return new ResponseEntity<String>("project task "+ pt_id + " was deleted", HttpStatus.OK);
    }
}
