package api.automation;

import POJO.UserPostsInfo;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.jayway.restassured.RestAssured.given;


public class JsonPlaceHolderFakeAPITests {

    int userId = (int) Math.floor(Math.random() * 10 + 1);

    @Test
    public void get_User_EmailAddress() {
        String uri = "https://jsonplaceholder.typicode.com/users/"+userId;
        System.out.println(uri);
        Response response = RestAssured.get(uri);
        System.out.println(response.jsonPath().getString("email"));
    }

    @Test
    public void get_User_Posts() {
        String uri = "https://jsonplaceholder.typicode.com/posts?userId="+userId;
        System.out.println(uri);
        int postId;
        SoftAssert softAssertion = new SoftAssert();
        Response response = RestAssured.get(uri);
        UserPostsInfo[] list = response.jsonPath().getObject("", UserPostsInfo[].class);
        for(UserPostsInfo userPosts: list) {
            postId = userPosts.getId();
            boolean validId = (postId >= 1 && postId <= 100) ? true : false;
            softAssertion.assertTrue(validId, postId + " is not between 1 and 100 for user " + userPosts.getUserId() + " with title " + userPosts.getTitle());
        }
        softAssertion.assertAll();
    }

    @Test
    public void create_User_Post() {
        System.out.println("userId:" +userId);
        String uri = "https://jsonplaceholder.typicode.com/posts";
        UserPostsInfo userPostsInfo = new UserPostsInfo();
        userPostsInfo.setBody("body test");
        userPostsInfo.setUserId(userId);
        userPostsInfo.setTitle("title test");
        Response response = given().contentType("application/json; charset=UTF-8").body(userPostsInfo)
                .when().post(uri);
        System.out.println(response.asString());
        response.then().statusCode(201);
    }
}
