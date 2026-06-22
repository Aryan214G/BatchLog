package com.log.dao;

import com.log.database.DBUtil;
import com.log.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public int insertProject(Connection conn, Project project) {

        String sql = "INSERT INTO Project (Project_name) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        String sql = "SELECT * FROM Project WHERE is_deleted = 0";

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

    public int getProject(Connection conn, String projectName){

        String sql = "SELECT * FROM Project WHERE Project_name = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, projectName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getInt("Project_id");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }


    public boolean updateProjectName(int projectId, String newName) {

        String sql = "UPDATE Project SET Project_name = ? WHERE Project_ID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setInt(2, projectId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true if update succeeded

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // indicates failure
    }

    public List<String> getAllProjectNames(Connection conn) {

        List<String> projects = new ArrayList<>();
        String sql = "SELECT Project_name FROM Project";

        try (
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {


                String name = rs.getString("Project_name");

                projects.add(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    // In ProjectDAO
    public boolean setDeleted(Connection conn, int projectId, boolean deleted) {
        String sql = "UPDATE Project SET is_deleted = ? WHERE Project_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deleted ? 1 : 0);
            stmt.setInt(2, projectId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}