package eu.einfracentral.registry.service;

import com.fasterxml.jackson.databind.*;
import eu.einfracentral.domain.User;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pgl on 07/08/17.
 */
@RestController
@RequestMapping("user")
public class UserController extends GenericRestController<User> {
    final private UserService userService;

    @Autowired
    UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @CrossOrigin
    @RequestMapping(path = "activate/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> activate(@PathVariable String token) {
        return new ResponseEntity<>(this.userService.activate(token), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "reset", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> reset(@RequestBody User user) {
        return new ResponseEntity<>(this.userService.reset(user), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "forgot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> forgot(@PathVariable String email) {
        return new ResponseEntity<>(this.userService.forgot(email), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User userR = this.userService.register(user);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(mapper.writeValueAsString(userR));
        return new ResponseEntity<>(userR, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> login(@RequestBody User credentials, HttpServletResponse res) {
        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            return new ResponseEntity<>(credentials, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!this.userService.authenticate(credentials)) {
            return new ResponseEntity<>(credentials, HttpStatus.FORBIDDEN);
        }
        User ret = this.userService.getUserByEmail(credentials.getEmail());
        if (ret == null) {
            return new ResponseEntity<>(credentials, HttpStatus.NOT_FOUND);
        }
        try {
            String token = this.userService.getToken(credentials);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            res.addCookie(cookie);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(credentials, HttpStatus.BAD_REQUEST);
        }
    }
//    @RequestMapping(value = "list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<Browsing> getAll(@RequestParam Map<String, Object> allRequestParams, HttpServletRequest request) {
//        if (request.getRemoteAddr().equals("194.177.192.118")) {
//            ResponseEntity<Browsing> ret = super.getAll(allRequestParams, WebUtils.getCookie(request, "jwt").getValue()); //TODO: Only allow verified admin user access to this
//            return ret;
//        } else {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//    }
}
