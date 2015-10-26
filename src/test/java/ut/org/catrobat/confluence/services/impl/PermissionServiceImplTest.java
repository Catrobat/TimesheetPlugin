package ut.org.catrobat.confluence.services.impl;

import com.atlassian.confluence.core.service.NotAuthorizedException;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import junit.framework.Assert;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.catrobat.confluence.activeobjects.Team;
import org.catrobat.confluence.activeobjects.Timesheet;
import org.catrobat.confluence.activeobjects.TimesheetEntry;
import org.catrobat.confluence.rest.json.JsonTimesheetEntry;
import org.catrobat.confluence.services.TeamService;
import org.catrobat.confluence.services.TimesheetEntryService;
import org.catrobat.confluence.services.TimesheetService;
import org.catrobat.confluence.services.impl.PermissionServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(PermissionServiceImplTest.MyDatabaseUpdater.class)

public class PermissionServiceImplTest {

  private static Team catroid, html5, drone;
  @Rule public org.mockito.junit.MockitoRule mockitoRule = MockitoJUnit.rule();
  private PermissionServiceImpl permissionService, permissionServiceException;
  private TeamService teamService;
  private UserManager userManager;
  private EntityManager entityManager;
  private UserProfile coord, owner, eve, test, admin;
  private Timesheet sheet;
  private TimesheetEntry timeSheetEntry;
  private HttpServletRequest request;
  private Team team;
  private TimesheetService sheetService;
  private TimesheetEntryService entryService;
  private SimpleDateFormat sdf;

  @Before
  public void setUp() throws Exception
  {
    teamService = Mockito.mock(TeamService.class);
    userManager = Mockito.mock(UserManager.class);

    assertNotNull(entityManager);

    permissionService = new PermissionServiceImpl(userManager, teamService);

    //arrange
    coord = Mockito.mock(UserProfile.class);
    owner = Mockito.mock(UserProfile.class);
    eve   = Mockito.mock(UserProfile.class);
    test   = Mockito.mock(UserProfile.class);
    admin = Mockito.mock(UserProfile.class);
    sheet = Mockito.mock(Timesheet.class);
    sheetService = Mockito.mock(TimesheetService.class);
    entryService = Mockito.mock(TimesheetEntryService.class);
    timeSheetEntry = Mockito.mock(TimesheetEntry.class);
    team = Mockito.mock(Team.class);
    request = Mockito.mock(HttpServletRequest.class);
    permissionServiceException = Mockito.mock(PermissionServiceImpl.class);

    UserKey coord_key = new UserKey("coord_key");
    UserKey owner_key = new UserKey("owner_key");
    UserKey eve_key   = new UserKey("eve_key");
    UserKey test_key   = new UserKey("test_key");
    UserKey admin_key = new UserKey("admin_key");

    Mockito.when(sheet.getUserKey()).thenReturn("owner_key");

    Mockito.when(coord.getUserKey()).thenReturn(coord_key);
    Mockito.when(owner.getUserKey()).thenReturn(owner_key);
    Mockito.when(eve.getUserKey()).thenReturn(eve_key);
    Mockito.when(test.getUserKey()).thenReturn(test_key);
    Mockito.when(admin.getUserKey()).thenReturn(admin_key);

    Mockito.when(coord.getUsername()).thenReturn("coord");
    Mockito.when(owner.getUsername()).thenReturn("owner");
    Mockito.when(eve.getUsername()).thenReturn("eve");
    Mockito.when(test.getUsername()).thenReturn("test");
    Mockito.when(admin.getUsername()).thenReturn("admin");

    Mockito.when(userManager.isAdmin(owner_key)).thenReturn(false);
    Mockito.when(userManager.isAdmin(coord_key)).thenReturn(false);
    Mockito.when(userManager.isAdmin(eve_key)).thenReturn(false);
    Mockito.when(userManager.isAdmin(test_key)).thenReturn(false);
    Mockito.when(userManager.isAdmin(admin_key)).thenReturn(true);

    Mockito.when(userManager.getUserProfile(owner_key.getStringValue())).thenReturn(owner);
    Mockito.when(userManager.getUserProfile(admin_key.getStringValue())).thenReturn(admin);
    Mockito.when(userManager.getUserProfile(eve_key.getStringValue())).thenReturn(eve);
    Mockito.when(userManager.getUserProfile(test_key.getStringValue())).thenReturn(test);
    Mockito.when(userManager.getUserProfile(coord_key.getStringValue())).thenReturn(coord);

    Set<Team> owner_teams  = new HashSet<Team>();
    Set<Team> eve_teams    = new HashSet<Team>();
    Set<Team> coord_cteams = new HashSet<Team>();
    Set<Team> no_teams     = new HashSet<Team>();

    owner_teams.add(html5);
    owner_teams.add(drone);
    eve_teams.add(catroid);
    coord_cteams.add(html5);

    Mockito.when(teamService.getTeamsOfUser("owner")).thenReturn(owner_teams);
    Mockito.when(teamService.getTeamsOfUser("eve"))  .thenReturn(eve_teams);
    Mockito.when(teamService.getCoordinatorTeamsOfUser("coord")).thenReturn(coord_cteams);

    Mockito.when(teamService.getTeamsOfUser("coord")).thenReturn(no_teams);
    Mockito.when(teamService.getTeamsOfUser("admin")).thenReturn(no_teams);
    Mockito.when(teamService.getCoordinatorTeamsOfUser("owner")).thenReturn(no_teams);
    Mockito.when(teamService.getCoordinatorTeamsOfUser("eve")).thenReturn(no_teams);
    Mockito.when(teamService.getCoordinatorTeamsOfUser("admin")).thenReturn(no_teams);
  }

  @Test
  public void testOwnerCanViewTimesheet() throws Exception
  {
    assertTrue(permissionService.userCanViewTimesheet(owner, sheet));
  }

  @Test
  public void testCoordinatorCanViewTimesheet() throws Exception
  {
    assertTrue(permissionService.userCanViewTimesheet(coord, sheet));
  }

  @Test
  public void testAdminCanViewTimesheet() throws Exception
  {
    assertTrue(permissionService.userCanViewTimesheet(admin, sheet));
  }

  @Test
  public void testEveCantViewTimesheet() throws Exception
  {
    assertFalse(permissionService.userCanViewTimesheet(eve, sheet));
  }

  @Test
  public void testOwnerOfSheetIsNull() throws Exception
  {
    Mockito.when(sheet.getUserKey()).thenReturn("Test");
    assertFalse(permissionService.userCanViewTimesheet(eve, sheet));
  }

  @Test
  public void testIfUserExistsOk() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(admin);
    Mockito.when(permissionService.checkIfUserExists(request)).thenReturn(admin);
    UserProfile responseProfile = permissionService.checkIfUserExists(request);
    assertTrue(responseProfile == admin);
  }

  @Test
  public void testIfUserExistsWrongUserProfile() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(admin);
    Mockito.when(permissionService.checkIfUserExists(request)).thenReturn(eve);
    UserProfile responseProfile = permissionService.checkIfUserExists(request);
    Assert.assertFalse(responseProfile == admin);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testIfUserExistsExceptionHandling() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(eve);
    Mockito.when(permissionService.checkIfUserExists(request)).thenThrow(new NotAuthorizedException("User does not exist."));
    Assert.assertEquals(permissionService.checkIfUserExists(request), NotAuthorizedException.class);
  }

  @Test
  public void testIfUserCanEditTimesheetEntry() throws Exception
  {
    JsonTimesheetEntry entry = new JsonTimesheetEntry(1,
            timeSheetEntry.getBeginDate(), timeSheetEntry.getEndDate(), timeSheetEntry.getPauseMinutes(),
            timeSheetEntry.getDescription(), 1, 1);

    permissionServiceException.userCanEditTimesheetEntry(owner, sheet, entry);
    Mockito.verify(permissionServiceException).userCanEditTimesheetEntry(owner, sheet, entry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void userCanEditTimesheetEntryOldEntryException() throws Exception
  {
    Mockito.when(sheet.getUserKey()).thenReturn("admin_key");

    //TimesheetEntry
    sdf = new SimpleDateFormat("dd-MM-yy hh:mm");
    Mockito.when(timeSheetEntry.getBeginDate()).thenReturn(sdf.parse("01-01-1015 00:01"));
    Mockito.when(timeSheetEntry.getEndDate()).thenReturn(sdf.parse("31-12-1015 23:59"));
    Mockito.when(timeSheetEntry.getDescription()).thenReturn("My First Entry");
    Mockito.when(timeSheetEntry.getPauseMinutes()).thenReturn(30);
    Mockito.when(timeSheetEntry.getID()).thenReturn(1);
    Mockito.when(teamService.getTeamByID(1)).thenReturn(team);
    Mockito.when(sheetService.getTimesheetByID(1)).thenReturn(sheet);
    Mockito.when(entryService.getEntryByID(1)).thenReturn(timeSheetEntry);

    JsonTimesheetEntry entry = new JsonTimesheetEntry(1,
            timeSheetEntry.getBeginDate(), timeSheetEntry.getEndDate(), timeSheetEntry.getPauseMinutes(),
            timeSheetEntry.getDescription(), 1, 1);

    permissionService.userCanEditTimesheetEntry(admin, sheet, entry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void userCanEditTimesheetEntryNotAdminException() throws Exception
  {
    //TimesheetEntry
    sdf = new SimpleDateFormat("dd-MM-yy hh:mm");
    Mockito.when(timeSheetEntry.getBeginDate()).thenReturn(sdf.parse("01-01-1015 00:01"));
    Mockito.when(timeSheetEntry.getEndDate()).thenReturn(sdf.parse("31-12-1015 23:59"));
    Mockito.when(timeSheetEntry.getDescription()).thenReturn("My First Entry");
    Mockito.when(timeSheetEntry.getPauseMinutes()).thenReturn(30);
    Mockito.when(timeSheetEntry.getID()).thenReturn(1);
    Mockito.when(teamService.getTeamByID(1)).thenReturn(team);
    Mockito.when(sheetService.getTimesheetByID(1)).thenReturn(sheet);
    Mockito.when(entryService.getEntryByID(1)).thenReturn(timeSheetEntry);

    JsonTimesheetEntry entry = new JsonTimesheetEntry(1,
            timeSheetEntry.getBeginDate(), timeSheetEntry.getEndDate(), timeSheetEntry.getPauseMinutes(),
            timeSheetEntry.getDescription(), 1, 1);

    permissionService.userCanEditTimesheetEntry(owner, sheet, entry);
  }

  @Test
  public void testIfUserCanDeleteTimesheetEntry() throws Exception
  {
    TimesheetEntry newEntry = Mockito.mock(TimesheetEntry.class);
    Mockito.when(newEntry.getID()).thenReturn(1);

    permissionServiceException.userCanDeleteTimesheetEntry(owner, newEntry);
    Mockito.verify(permissionServiceException).userCanDeleteTimesheetEntry(owner, newEntry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testUserCanNotDeleteTimesheetException() throws Exception
  {
    Mockito.when(sheet.getUserKey()).thenReturn("admin_key");

    //TimesheetEntry
    sdf = new SimpleDateFormat("dd-MM-yy hh:mm");
    Mockito.when(timeSheetEntry.getBeginDate()).thenReturn(sdf.parse("01-01-1015 00:01"));
    Mockito.when(timeSheetEntry.getEndDate()).thenReturn(sdf.parse("31-12-1015 23:59"));
    Mockito.when(timeSheetEntry.getDescription()).thenReturn("My First Entry");
    Mockito.when(timeSheetEntry.getPauseMinutes()).thenReturn(30);
    Mockito.when(timeSheetEntry.getID()).thenReturn(1);
    Mockito.when(timeSheetEntry.getTimeSheet()).thenReturn(sheet);
    Mockito.when(teamService.getTeamByID(1)).thenReturn(team);
    Mockito.when(sheetService.getTimesheetByID(1)).thenReturn(sheet);
    Mockito.when(sheetService.getTimesheetByUser("admin_key")).thenReturn(sheet);
    Mockito.when(entryService.getEntryByID(1)).thenReturn(timeSheetEntry);

    permissionService.userCanDeleteTimesheetEntry(admin, timeSheetEntry);
    //Mockito.verify(permissionServiceException).userCanDeleteTimesheetEntry(admin, timeSheetEntry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void userDoesNotExistException() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(eve);
    Mockito.when(permissionService.checkIfUserExists(request)).thenReturn(null);
    permissionService.checkIfUserExists(request);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testNullUserCanNotEditTimesheetException() throws Exception
  {
    JsonTimesheetEntry entry = new JsonTimesheetEntry(1,
            timeSheetEntry.getBeginDate(), timeSheetEntry.getEndDate(), timeSheetEntry.getPauseMinutes(),
            timeSheetEntry.getDescription(), 1, 1);

    permissionService.userCanEditTimesheetEntry(eve, sheet, entry);
    //Mockito.verify(permissionServiceException).userCanEditTimesheetEntry(eve, sheet, entry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testNullUserCanNotDeleteTimesheetException() throws Exception
  {
    //TimesheetEntry
    sdf = new SimpleDateFormat("dd-MM-yy hh:mm");
    Mockito.when(timeSheetEntry.getBeginDate()).thenReturn(sdf.parse("01-01-1015 00:01"));
    Mockito.when(timeSheetEntry.getEndDate()).thenReturn(sdf.parse("31-12-1015 23:59"));
    Mockito.when(timeSheetEntry.getDescription()).thenReturn("My First Entry");
    Mockito.when(timeSheetEntry.getPauseMinutes()).thenReturn(30);
    Mockito.when(timeSheetEntry.getID()).thenReturn(1);
    Mockito.when(teamService.getTeamByID(1)).thenReturn(team);
    Mockito.when(sheetService.getTimesheetByID(1)).thenReturn(sheet);
    Mockito.when(entryService.getEntryByID(1)).thenReturn(timeSheetEntry);

    permissionService.userCanDeleteTimesheetEntry(eve, timeSheetEntry);
    //Mockito.verify(permissionServiceException).userCanDeleteTimesheetEntry(eve, timeSheetEntry);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testUserCanNotDeleteOldDateTimesheetException() throws Exception
  {
    //TimesheetEntry
    sdf = new SimpleDateFormat("dd-MM-yy hh:mm");
    Mockito.when(timeSheetEntry.getBeginDate()).thenReturn(sdf.parse("01-01-2015 00:01"));
    Mockito.when(timeSheetEntry.getEndDate()).thenReturn(sdf.parse("01-01-2015 23:59"));
    Mockito.when(timeSheetEntry.getDescription()).thenReturn("My First Old Entry");
    Mockito.when(timeSheetEntry.getPauseMinutes()).thenReturn(30);
    Mockito.when(timeSheetEntry.getID()).thenReturn(1);
    Mockito.when(teamService.getTeamByID(1)).thenReturn(team);
    Mockito.when(sheetService.getTimesheetByID(1)).thenReturn(sheet);
    Mockito.when(entryService.getEntryByID(1)).thenReturn(timeSheetEntry);

    permissionService.userCanDeleteTimesheetEntry(owner, timeSheetEntry);
    //Mockito.verify(permissionServiceException).userCanDeleteTimesheetEntry(owner, timeSheetEntry);
  }

  public static class MyDatabaseUpdater implements DatabaseUpdater {

    @Override
    public void update(EntityManager em) throws Exception {
      em.migrate(Team.class);
      catroid = em.create(Team.class);
      catroid.setTeamName("catroid");
      catroid.save();

      html5 = em.create(Team.class);
      html5.setTeamName("html5");
      html5.save();

      drone = em.create(Team.class);
      drone.setTeamName("drone");
      drone.save();
    }
  }
}
