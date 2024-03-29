package edu.school21.cinema.servlet;

import edu.school21.cinema.exception.UserIsPresentSignUpException;
import edu.school21.cinema.model.CinemaUser;
import edu.school21.cinema.properties.JspPathProperties;
import edu.school21.cinema.service.AuthHistoryService;
import edu.school21.cinema.service.CinemaUserService;
import edu.school21.cinema.service.PasswordEncoderService;
import edu.school21.cinema.service.UserImagesService;
import edu.school21.cinema.token.TokenConstant;
import edu.school21.cinema.type.ContentType;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

  private ApplicationContext applicationContext;
  private CinemaUserService cinemaUserService;
  private AuthHistoryService authHistoryService;
  private PasswordEncoderService passwordEncoderService;
  private UserImagesService userImagesService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType(ContentType.HTML.getType());

    String signUpPath = ((JspPathProperties) applicationContext.getBean(
        "jspPathProperties")).getSignUp();
    RequestDispatcher requestDispatcher = req.getRequestDispatcher(signUpPath);
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    Optional<CinemaUser> cinemaUser = cinemaUserService.findByEmail(req.getParameter("email"));
    if (cinemaUser.isPresent()) {
      throw new UserIsPresentSignUpException();
    }

    CinemaUser user = cinemaUserService.save(
        new CinemaUser(req.getParameter("first-name")
        , req.getParameter("last-name")
        , req.getParameter("phone-number")
        , req.getParameter("email")
        , passwordEncoderService.encode(req.getParameter("password"))
        , userImagesService.getDefaultUserImageFilename()
    ));

    authHistoryService.saveSignUpEvent(user, createdAt, req.getRemoteAddr());

    HttpSession httpSession = req.getSession();
    httpSession.setAttribute(TokenConstant.TOKEN, user.getEmail());
    resp.sendRedirect("/profile");
  }

  @Override
  public void init(ServletConfig config) {
    applicationContext = (ApplicationContext) config.getServletContext()
        .getAttribute("applicationContext");
    getBeansFromSpringApplicationContext();
  }

  private void getBeansFromSpringApplicationContext() {
    if (cinemaUserService == null) {
      cinemaUserService = applicationContext.getBean("cinemaUserService", CinemaUserService.class);
    }
    if (passwordEncoderService == null) {
      passwordEncoderService = applicationContext.getBean("passwordEncoderService", PasswordEncoderService.class);
    }
    if (authHistoryService == null) {
      authHistoryService = applicationContext.getBean("authHistoryService", AuthHistoryService.class);
    }
    if (userImagesService == null) {
      userImagesService = applicationContext.getBean("userImagesService", UserImagesService.class);
    }
  }
}
