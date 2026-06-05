package com.log.service;

import com.log.dao.ProjectDAO;
import com.log.model.Project;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProjectService {
    private final ProjectDAO projectDAO;

    public ProjectService(){
        this.projectDAO = new ProjectDAO();
    }

    public int createProject(Connection conn, String projectName) throws SQLException {
        System.out.println(conn.getMetaData().getURL());

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        int projectId = getProjectId(conn, projectName);
        if( projectId != -1){
            System.out.println("Project already exists. Returning project id");
            return projectId;
        }

        Project project = new Project(projectName.trim());
        return projectDAO.insertProject(conn, project);
    }

    public List<Project> getAllProjects(){
        return projectDAO.getAllProjects();
    }

    public List<String> getAllProjectNames(Connection connection){ return projectDAO.getAllProjectNames(connection);}

    public int getProjectId(Connection conn, String projectName){
        int projectId = projectDAO.getProject(conn, projectName);
        if(projectId == -1){
            System.out.println("Error retrieving project id");
            return -1;
        }
        return projectId;
    }



    public void deleteProject(int projectId){
        projectDAO.deleteProject(projectId);
    }


    public void editProject(int projectId,String updatedName){ projectDAO.updateProjectName(projectId,updatedName);}

}