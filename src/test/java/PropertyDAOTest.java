import com.log.dao.PropertyDAO;
import com.log.database.DBUtil;
import com.log.model.Property;
import org.junit.jupiter.api.*;

import java.sql.Connection;

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
}