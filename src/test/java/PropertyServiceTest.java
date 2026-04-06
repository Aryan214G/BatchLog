import com.log.database.DBUtil;
import com.log.model.*;
import com.log.service.*;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PropertyServiceTest {

    @Test
    void insertProperty() throws Exception {

        Connection conn = DBUtil.getConnection();
        conn.setAutoCommit(false); // rollback later

        PropertyService propertyService = new PropertyService();
        TemperatureService temperatureService = new TemperatureService();
        DirectionService directionService = new DirectionService();
        CategoryService categoryService = new CategoryService();
        UnitsService unitsService = new UnitsService();

        // --- Create required data ---

        Temperature temp = new Temperature(12, "1/°C");
        int tempId = temperatureService.createTemperature(conn, temp);

        Direction dir = new Direction("Thickness Direction");
        int dirId = directionService.getDirectionByName(conn, dir.getDirVal()).getDirId();

        Category cat = categoryService.getCategory(conn, "Mechanical");
        int categoryId = cat.getCategoryId();

        Unit unit = unitsService.getUnit(conn, "JUnitUnit");
        int unitId = unit.getUnitId();

        // --- Create property using REAL IDs ---
        Property property = new Property(
                "JUnitProperty",
                categoryId,
                tempId,
                dirId,
                unitId,
                "B001"
        );

        int propertyId = propertyService.insertProperty(conn, property);

        // --- Verify ---
        assertTrue(propertyId > 0);

        Property result = propertyService.getProperty(conn, propertyId);
        assertEquals("JUnitProperty", result.getPropertyName());

        // --- Cleanup ---
        conn.rollback();
        conn.close();
    }
}