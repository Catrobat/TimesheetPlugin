package ut.org.catrobat.confluence.servlet;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.catrobat.confluence.activeobjects.Team;
import org.catrobat.confluence.activeobjects.Timesheet;
import org.catrobat.confluence.services.PermissionService;
import org.catrobat.confluence.services.TeamService;
import org.catrobat.confluence.services.TimesheetService;
import org.catrobat.confluence.servlet.TimesheetServlet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimesheetServletTest {

  private TimesheetServlet timesheetServlet;

  private LoginUriProvider loginUriProvider;
  private TemplateRenderer templateRenderer;
  private TimesheetService sheetService;
  private PermissionService permissionService;

  private Timesheet timeSheet;
  private UserProfile userProfile;
  private TeamService teamService;
  private UserManager userManager;

  private HttpServletResponse response;
  private HttpServletRequest request;

  private Team team;
  private UserProfile admin;

  @Before
  public void setUp() throws Exception
  {
    teamService = Mockito.mock(TeamService.class);
    userManager = Mockito.mock(UserManager.class);
    loginUriProvider = Mockito.mock(LoginUriProvider.class);
    templateRenderer = Mockito.mock(TemplateRenderer.class);
    sheetService = Mockito.mock(TimesheetService.class);
    permissionService = Mockito.mock(PermissionService.class);

    userProfile = Mockito.mock(UserProfile.class);
    timeSheet = Mockito.mock(Timesheet.class);
    sheetService = Mockito.mock(TimesheetService.class);
    team = Mockito.mock(Team.class);

    request = Mockito.mock(HttpServletRequest.class);
    response = Mockito.mock(HttpServletResponse.class);

    timesheetServlet = new TimesheetServlet(loginUriProvider, templateRenderer, sheetService, permissionService);

    admin = Mockito.mock(UserProfile.class);
    UserKey admin_key = new UserKey("admin_key");

    Mockito.when(admin.getUserKey()).thenReturn(admin_key);
    Mockito.when(admin.getUsername()).thenReturn("admin");
    Mockito.when(userManager.isAdmin(admin_key)).thenReturn(true);
    Mockito.when(userManager.getUserProfile(admin_key.getStringValue())).thenReturn(admin);

  }

  @Test
  public void testDoGet() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(admin);
    Mockito.when(permissionService.checkIfUserExists(request)).thenReturn(admin);
    Mockito.when(sheetService.getTimesheetByUser("admin_key")).thenReturn(timeSheet);
    Mockito.when(permissionService.checkIfUserIsGroupMember(request, "confluence-administrators")).thenReturn(false);
    Mockito.when(permissionService.checkIfUserIsGroupMember(request, "Timesheet")).thenReturn(true);

    timesheetServlet.doGet(request, response);
  }

  @Test(expected = NullPointerException.class)
  public void testDoGetNullPointerException() throws Exception
  {
    Mockito.when(userManager.getRemoteUser(request)).thenReturn(admin);
    Mockito.when(permissionService.checkIfUserExists(request)).thenReturn(admin);
    Mockito.when(sheetService.getTimesheetByUser("admin_key")).thenReturn(null);

    timesheetServlet.doGet(request, response);
  }
}
