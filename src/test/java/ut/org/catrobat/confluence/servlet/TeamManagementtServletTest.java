package ut.org.catrobat.confluence.servlet;

import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.user.GroupManager;
import org.catrobat.confluence.activeobjects.Team;
import org.catrobat.confluence.activeobjects.Timesheet;
import org.catrobat.confluence.services.TeamService;
import org.catrobat.confluence.servlet.TeamManagementServlet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TeamManagementtServletTest {

  private TeamManagementServlet teamManagementServlet;

  private UserAccessor userAccessor;
  private GroupManager groupManager;
  private UserManager userManager;
  private LoginUriProvider loginUriProvider;
  private TemplateRenderer templateRenderer;
  private TeamService teamService;

  private Timesheet timeSheet;
  private UserProfile userProfile;

  private HttpServletResponse response;
  private HttpServletRequest request;

  private Team team;
  private UserProfile admin;

  @Before
  public void setUp() throws Exception {
    userAccessor = Mockito.mock(UserAccessor.class);
    groupManager = Mockito.mock(GroupManager.class);
    userManager = Mockito.mock(UserManager.class);
    loginUriProvider = Mockito.mock(LoginUriProvider.class);
    templateRenderer = Mockito.mock(TemplateRenderer.class);
    teamService = Mockito.mock(TeamService.class);

    userProfile = Mockito.mock(UserProfile.class);
    timeSheet = Mockito.mock(Timesheet.class);
    team = Mockito.mock(Team.class);

    request = Mockito.mock(HttpServletRequest.class);
    response = Mockito.mock(HttpServletResponse.class);

    teamManagementServlet = new TeamManagementServlet(userAccessor, groupManager, userManager, loginUriProvider, templateRenderer, teamService);

    admin = Mockito.mock(UserProfile.class);
  }

  @Test
  public void testDoGet() throws Exception {
    UserKey admin_key = new UserKey("admin_key");

    Mockito.when(userManager.getRemoteUserKey(request)).thenReturn(admin_key);
    Mockito.when(admin.getUsername()).thenReturn("admin");
    Mockito.when(userManager.isAdmin(admin_key)).thenReturn(true);
    Mockito.when(userManager.getUserProfile(admin_key)).thenReturn(admin);

    teamManagementServlet.doGet(request, response);
  }

  @Test(expected = NullPointerException.class)
  public void testDoGetRedirectToLoginGetURINullpointerException() throws Exception {
    UserKey admin_key = new UserKey("admin_key");

    Mockito.when(userManager.getRemoteUserKey(request)).thenReturn(null);
    Mockito.when(admin.getUsername()).thenReturn("admin");
    Mockito.when(userManager.isAdmin(admin_key)).thenReturn(true);
    Mockito.when(userManager.getUserProfile(admin_key)).thenReturn(null);

    teamManagementServlet.doGet(request, response);
  }
}
