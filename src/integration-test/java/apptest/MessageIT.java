package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.notifier.Application;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(files = "classpath:/MessageIT/", classes = Application.class)
@Testcontainers
@Sql(scripts = {
        "/db/script/truncate.sql",
        "/db/script/testdata.sql"
})
class MessageIT extends AbstractAppTest {

    private static final String PATH = "/api/notifier/messages";
    private static final String REQUEST_FILE = "request.json";
    private static final String EXPECTED_FILE = "expected.json";

    @Test
    void test1_createMessageSuccess() {
        setupCall()
                .withServicePath(PATH)
                .withHttpMethod(POST)
                .withRequest(REQUEST_FILE)
                .withExpectedResponseStatus(NO_CONTENT)
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test2_createMessageBadRequest() {
        setupCall()
                .withServicePath(PATH)
                .withHttpMethod(POST)
                .withRequest(REQUEST_FILE)
                .withExpectedResponseStatus(BAD_REQUEST)
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_getMessagesSuccess() {
        setupCall()
                .withServicePath(b -> b.path(PATH)
                        .queryParam("sender", "test@sundsvall.se")
                        .build())
                .withHttpMethod(GET)
                .withExpectedResponseStatus(OK)
                .withExpectedResponse(EXPECTED_FILE)
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test4_getMessageBadRequest() {
        setupCall()
                .withServicePath(PATH)
                .withHttpMethod(GET)
                .withExpectedResponseStatus(BAD_REQUEST)
                .sendRequestAndVerifyResponse();
    }
}
