package com.log.service;

import com.log.dao.ProjectDAO;
import com.log.model.Project;

import java.sql.Connection;
import java.util.List;

public class ProjectService {
    private final ProjectDAO projectDAO;

    public ProjectService(){
        this.projectDAO = new ProjectDAO();
    }

    public int createProject(Connection conn, String projectName){

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }

        Project project = new Project(projectName.trim());
        return projectDAO.insertProject(conn, project);
    }

    public List<Project> getAllProjects(){
        return projectDAO.getAllProjects();
    }



    public void deleteProject(int projectId){
        projectDAO.deleteProject(projectId);
    }

}