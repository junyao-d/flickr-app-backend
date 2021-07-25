package com.gcp.spring.mysql.controller;

import com.gcp.spring.mysql.model.User;
import com.gcp.spring.mysql.repository.UserRepository;
import com.gcp.spring.mysql.service.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/flickrapi")
public class UserController {

  private static final String FLICKR_API =
    "https://www.flickr.com/services/feeds/photos_public.gne?tagmode=any&format=json&nojsoncallback=1";
  private static final String USER_AGENT = "Mozilla/5.0";

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @GetMapping("/")
  public ResponseEntity<String> testApi() {
    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  // @GetMapping("/user")
  // public ResponseEntity<List<User>> getAllUsers() {
  //   return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  // }

  @PostMapping("/user/register")
  public ResponseEntity<HashMap<String, Object>> registerUser(@RequestBody HashMap<String, Object> userInfo
  ) {
    String username = userInfo.get("username").toString();
    String password = userInfo.get("password").toString();
    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(password);

    //Check whether user exists
    if (userService.userExist(username)) {
      return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    else {
      userService.saveOrUpdateUser(newUser);
      ResponseEntity<HashMap<String, Object>> authenticateResult;
      try {
        authenticateResult = this.authenticateUser(userInfo);
      } catch (IOException e) {
        return new ResponseEntity<>(null,HttpStatus.NON_AUTHORITATIVE_INFORMATION);
      }
      return authenticateResult;
    }
  }

  @GetMapping("/user/me")
  public ResponseEntity<String> accessTokenAuth(@RequestHeader HashMap<String, Object> headerInfo) {
    System.out.println(headerInfo);
    String username = headerInfo.get("authorization").toString().split("&")[0];
    String token = headerInfo.get("authorization").toString().split("&")[1];
    User user;
    try {
      user = userService.getUserByUsername(username);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(null,HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    if (token.equals(user.getToken())) {
      return new ResponseEntity<>("Token Match!", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Token Not Match!",HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
    // return new ResponseEntity<>("User Created", HttpStatus.OK);
  }

  //Handle user login and create access token for login session
  @PostMapping("/user/login")
  public ResponseEntity<HashMap<String, Object>> authenticateUser(@RequestBody HashMap<String, Object> userInfo)
    throws IOException {
    String username = userInfo.get("username").toString();
    String password = userInfo.get("password").toString();

    User user;
    try {
      user = userService.getUserByUsername(username);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(null,HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    if (password.equals(user.getPassword())) {
      String token = userService.generateToken(username, password);
      HashMap<String, Object> map = new HashMap<>();
      map.put("token", token);
      user.setToken(token);
      userService.saveOrUpdateUser(user);
      return new ResponseEntity<>(map, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(null,HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
  }

  // Flickr Api Proxy, returning CrossOrigin to request header
  @GetMapping("/search")
  public ResponseEntity<String> apiProxy(@RequestParam("tags") String tags)throws IOException {
    System.out.println(tags);
    URL obj = new URL(FLICKR_API + "&tags=" + tags);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", USER_AGENT);
    int responseCode = con.getResponseCode();
    System.out.println("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream())
      );
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      // return result
      return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }
}
