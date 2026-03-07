package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public int insertProject(Project project) {

        String sql = "INSERT INTO Project (Project_name) VALUES (?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, project.getProjectName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1); // auto-generated Project_ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // indicates failure
    }


    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int id = rs.getInt("Project_ID");
                String name = rs.getString("Project_name");

                projects.add(new Project(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }


    public void deleteProject(int projectId) {

        String sql = "DELETE FROM Project WHERE Project_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}