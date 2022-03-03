package com.codeup.springblog;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import javax.servlet.http.HttpSession;


import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringblogApplication.class)
@AutoConfigureMockMvc
public class PostsIntegrationTests {

    private User testuser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    PostRepository postDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {
        testuser = userDao.findByUsername("testUser");

        // Creates the test if a user doesn't exist
        if (testuser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setEmail("testUser@test.com");
            testuser = userDao.save(newUser);
        }

        // cross site request forgery
        httpSession = this.mvc.perform(post("/login").with(csrf())
                        .param("username", "testUser")
                        .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/posts"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    @Test
    public void contextLoads() {
        // Sanity test to make sure MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        // make sure returned session is not null
        assertNotNull(httpSession);
    }

    @Test
    public void testCreatePost() throws Exception {
        // post request to /posts/create and expect a redirect to the post
        this.mvc.perform(
                        post("/posts/create").with(csrf())
                                .session((MockHttpSession) httpSession)
                                // add all the parameters to your request
                                .param("title", "test")
                                .param("body", "body by test"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testShowPost() throws Exception {
        Post existingPost = postDao.findAll().get(0);

        // get request to /posts/{id} and expect redirect to post show page
        this.mvc.perform(get("/posts/" + existingPost.getId()))
                .andExpect(status().isOk())
                // test the dynamic content
                .andExpect(content().string(containsString(existingPost.getBody())));
    }

    @Test
    public void testPostsIndex() throws Exception {
        Post existingPost = postDao.findAll().get(0);

        //get request to check for static text of index page and check if it has a title
        this.mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                // tests static content
//                .andExpect(content().string(containsString("Latest Posts")))
                // tests dynamic content
                .andExpect(content().string(containsString(existingPost.getTitle())));
    }

    @Test
    public void testEditPost() throws Exception {
        // get first post
        Post existingPost = postDao.findAll().get(0);

        // post request to /posts/{id} should redirect to post show page
        this.mvc.perform(
                        post("/posts/" + existingPost.getId() + "/edit").with(csrf())
                                .session((MockHttpSession) httpSession)
                                .param("title", "edited title")
                                .param("body", "edited body"))
                .andExpect(status().is3xxRedirection());

        // get request to /posts/{id} and redirect to post show page
        this.mvc.perform(get("/posts/" + existingPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("edited title")))
                .andExpect(content().string(containsString("edited body")));
    }

    @Test
    public void testDeletePost() throws Exception {
        // create post to be deleted
        if (postDao.findPostByTitle("post to be deleted") == null) {
            this.mvc.perform(
                            post("/posts/create").with(csrf())
                                    .session((MockHttpSession) httpSession)
                                    .param("title", "post to be deleted")
                                    .param("body", "body to be deleted"))
                    .andExpect(status().is3xxRedirection());
        }

        // get recent post that matches title
        Post existingPost = postDao.findPostByTitle("post to be deleted");

        // post to /posts/{id}/delete and redirect to index page
        this.mvc.perform(
                        // used get instead of post because it is a get request in the postController
                        get("/posts/" + existingPost.getId() + "/delete").with(csrf())
                                .session((MockHttpSession) httpSession)
                                .param("id", String.valueOf(existingPost.getId())))
                .andExpect(status().is3xxRedirection());

    }
}
