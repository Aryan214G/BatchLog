import com.log.dao.PropertyDAO;
import com.log.database.DBUtil;
import com.log.model.Property;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class PropertyDAOTest {

    private Connection conn;
    private PropertyDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        dao = new PropertyDAO();
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    void testGetPropertyId_whenExists() {

        // Using real data from Default_Properties table
        Property property = new Property();
        property.setPropertyName("Density"); // exists in your table
        property.setCategoryID(6);
        property.setUnitID(15);
        property.setTempID(1); // dummy but required
        property.setDirID(1);  // dummy but required
        property.setBatchCode(100);

        // Insert first
        int insertedId = dao.insertProperty(conn, property);

        // Now fetch
        int fetchedId = dao.getPropertyId(conn, property);

        assertEquals(insertedId, fetchedId);
    }

    @Test
    void testGetPropertyId_whenNotExists() {

        Property property = new Property();
        property.setPropertyName("NonExistingProperty"); // not in default table
        property.setCategoryID(999);
        property.setUnitID(1);
        property.setTempID(1);
        property.setDirID(1);
        property.setBatchCode(999);

        int result = dao.getPropertyId(conn, property);

        assertEquals(-1, result);
    }

    private int createTestBatch(Connection conn) throws Exception {

        Statement stmt = conn.createStatement();

        stmt.execute("INSERT INTO Project (Project_name) VALUES ('TestProject')");
        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
        rs.next();
        int projectId = rs.getInt(1);

        PreparedStatement ps = conn.prepareStatement("""
        INSERT INTO Product (Product_ID, Product_name, Project_ID)
        VALUES (?, ?, ?)
    """);
        ps.setString(1, "P1");
        ps.setString(2, "TestProduct");
        ps.setInt(3, projectId);
        ps.executeUpdate();

        rs = stmt.executeQuery("SELECT last_insert_rowid()");
        rs.next();
        int productCode = rs.getInt(1);

        ps = conn.prepareStatement("""
        INSERT INTO Batch (Batch_ID, Product_CODE)
        VALUES (?, ?)
    """);
        ps.setString(1, "B1");
        ps.setInt(2, productCode);
        ps.executeUpdate();

        rs = stmt.executeQuery("SELECT last_insert_rowid()");
        rs.next();
        return rs.getInt(1); // Batch_CODE
    }
}