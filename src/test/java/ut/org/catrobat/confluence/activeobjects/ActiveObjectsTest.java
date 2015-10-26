package ut.org.catrobat.confluence.activeobjects;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.EntityManager;
import static org.junit.Assert.*;

import org.catrobat.confluence.activeobjects.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.activeobjects.test.TestActiveObjects;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.catrobat.confluence.activeobjects.Category;
import org.catrobat.confluence.activeobjects.Timesheet;
import org.catrobat.confluence.activeobjects.TimesheetEntry;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(MySampleDatabaseUpdater.class)

/**
 * This Test Suite verifies that active object entities are correctly mapped
 * to each other
 */
public class ActiveObjectsTest {

	private EntityManager entityManager;
	private ActiveObjects ao;

	@Before
	public void setUp() throws Exception
	{
		assertNotNull(entityManager);
		ao = new TestActiveObjects(entityManager);
	}

	@Test
	public void testTeamToCategoryMapping() throws Exception
	{
		Team[] teams = ao.find(Team.class, "TEAM_NAME = ?", "CATROBAT");

		assertEquals(teams.length, 1);

		Team catrobatTeam = teams[0];

		assertEquals(catrobatTeam.getEntries().length, 1);
		assertEquals(catrobatTeam.getCategories().length, 2);
	}

	@Test
	public void testSheetToTeamMapping() throws Exception
	{
		Timesheet[] sheets = ao.find(Timesheet.class, "USER_KEY = ?", "chris");

		assertEquals(sheets.length, 1);

		Timesheet chrisSheet = sheets[0];
		TimesheetEntry[] entries = chrisSheet.getEntries();

		assertEquals(entries.length, 1);

		TimesheetEntry entry1 = entries[0];

		assertEquals(entry1.getDescription(), "Besprechung: Team Fetcher");

		Team scratchTeam = entry1.getTeam();

		assertEquals(scratchTeam.getTeamName(), "SCRATCH");

		Category meetingCategory = entry1.getCategory();

		assertEquals(meetingCategory.getName(), "Meeting");

		Team[] projectsOfMeeting = meetingCategory.getTeams();

		assertEquals(projectsOfMeeting.length, 2);
	}

	@Test
	public void testDateQueries() throws Exception {
		//get all time sheet entries where duration < 20 minutes
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		tf.setTimeZone(TimeZone.getTimeZone("UTC"));

		TimesheetEntry[] entries = ao.find(TimesheetEntry.class,
						"DATEDIFF('minute', BEGIN_DATE, END_DATE) - PAUSE_MINUTES < 20" );

		//assert
		assertEquals(entries.length, 1);
		assertEquals(entries[0].getDescription(), "Master Fixen");
	}
}
