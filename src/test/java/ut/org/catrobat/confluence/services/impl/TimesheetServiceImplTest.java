package ut.org.catrobat.confluence.services.impl;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.catrobat.confluence.activeobjects.Timesheet;
import org.catrobat.confluence.services.TimesheetService;
import org.catrobat.confluence.services.impl.TimesheetServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(TimesheetServiceImplTest.MyDatabaseUpdater.class)

public class TimesheetServiceImplTest {

    private EntityManager entityManager;
    private TimesheetService service;
    private ActiveObjects ao;

    final String userKey = "USER_001";
    final int targetHoursPractice = 150;
    final int targetHoursTheory = 0;
    final int targeHours = 300;
    final int targetHoursCompleted = 150;
    final int ects = 10;
    final String lectures = "Mobile Applications (705.881)";

    @Before
    public void setUp() throws Exception {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        service = new TimesheetServiceImpl(ao);
    }

    public static class MyDatabaseUpdater implements DatabaseUpdater {

        @Override
        public void update(EntityManager em) throws Exception {
            em.migrate(Timesheet.class);

            Timesheet sheet = em.create(Timesheet.class);
            sheet.setUserKey("USER_000");
            sheet.save();
        }
    }

    @Test
    public void testAdd() throws Exception {
        //Act
        service.add(userKey, targetHoursPractice, targetHoursTheory, targeHours, targetHoursCompleted, lectures, ects);
        Timesheet[] timesheet = ao.find(Timesheet.class, "USER_KEY = ?", userKey);

        //Assert
        assertEquals(1, timesheet.length);
        assertEquals(userKey, timesheet[0].getUserKey());
        assertEquals(targetHoursPractice, timesheet[0].getTargetHoursPractice());
        assertEquals(targetHoursTheory, timesheet[0].getTargetHoursTheory());
        assertEquals(targeHours, timesheet[0].getTargetHours());
        assertEquals(targetHoursCompleted, timesheet[0].getTargetHoursCompleted());
        assertEquals(ects, timesheet[0].getEcts());
    }

    @Test
    public void testAll() throws Exception {
        //Arange
        Timesheet sheet = ao.create(Timesheet.class);
        sheet.setUserKey(userKey);
        sheet.setTargetHoursPractice(targetHoursPractice);
        sheet.setTargetHoursTheory(targetHoursTheory);
        sheet.setTargetHoursTheory(targeHours);
        sheet.setTargetHoursTheory(targetHoursCompleted);
        sheet.setLectures(lectures);
        sheet.setEcts(ects);
        sheet.setIsActive(true);
        sheet.setIsEnabled(true);
        sheet.save();
        ao.flushAll();

        //Act
        List<Timesheet> timesheets = service.all();

        //Assert
        assertEquals(timesheets.size(), 2);
    }

    @Test
    public void testGetTimesheetByUser() throws Exception {
        //Arange
        Timesheet sheet = ao.create(Timesheet.class);
        sheet.setUserKey(userKey);
        sheet.setTargetHoursPractice(targetHoursPractice);
        sheet.setTargetHoursTheory(targetHoursTheory);
        sheet.setTargetHoursTheory(targeHours);
        sheet.setTargetHoursTheory(targetHoursCompleted);
        //sheet.setLectures(lectures);
        sheet.setEcts(ects);
        sheet.setIsActive(true);
        sheet.setIsEnabled(true);
        sheet.save();
        ao.flushAll();

        //Act
        Timesheet sheet0 = service.getTimesheetByUser("USER_000");

        //Assert
        assertNotNull(sheet0);
        assertEquals(sheet0.getUserKey(), "USER_000");

        //Act
        Timesheet sheet1 = service.getTimesheetByUser("USER_001");

        //Assert
        assertNotNull(sheet1);
        assertEquals(sheet1.getUserKey(), "USER_001");
    }

    @Test
    public void testGetTimesheetByUserNotFound() throws Exception {
        //Act
        Timesheet missingSheet = service.getTimesheetByUser("USER_DOES_NOT_EXIST");

        //Assert
        assertNull(missingSheet);
    }

    @Test
    public void testGetTimesheetByID() throws Exception {
        //Act
        Timesheet[] sheets = ao.find(Timesheet.class, "USER_KEY = ?", "USER_000");
        Timesheet refSheet = sheets[0];
        Timesheet checkSheet = service.getTimesheetByID(refSheet.getID());

        //Assert
        assertEquals(refSheet, checkSheet);
    }

    @Test
    public void testGetTimesheetByMissingID() throws Exception {
        //Act
        Timesheet sheet = service.getTimesheetByID(12345);

        //Assert
        assertNull(sheet);
    }

    @Test
    public void testUpdateTimesheetPractialhours() throws Exception {
        //Act
        Timesheet[] sheets = ao.find(Timesheet.class, "USER_KEY = ?", "USER_000");
        Timesheet refSheet = sheets[0];
        Timesheet checkSheet = service.getTimesheetByID(refSheet.getID());

        //Assert
        assertEquals(refSheet, checkSheet);

        //Act
        final int newPracticalHours = 300;
        refSheet.setTargetHoursPractice(newPracticalHours);
        //Assert
        assertTrue(refSheet.getTargetHoursPractice() == 300);
    }
}
