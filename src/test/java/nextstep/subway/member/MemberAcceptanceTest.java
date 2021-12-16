package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestApiFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        final String accessToken = 회원_로그인_요청(EMAIL, PASSWORD);

        final ExtractableResponse<Response> getResponse1 = 내회원정보_조회_요청(accessToken);
        회원_정보_조회됨(getResponse1, EMAIL, AGE);

        final ExtractableResponse<Response> editResponse = 내회원정보_수정_요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(editResponse);

        final String newAccessToken = 회원_로그인_요청(NEW_EMAIL, NEW_PASSWORD);

        final ExtractableResponse<Response> getResponse2 = 내회원정보_조회_요청(newAccessToken);
        회원_정보_조회됨(getResponse2, NEW_EMAIL, NEW_AGE);

        final ExtractableResponse<Response> deleteResponse = 내회원정보_삭제_요청(newAccessToken);
        회원_삭제됨(deleteResponse);
    }

    private String 회원_로그인_요청(String email, String password) {
        final ExtractableResponse<Response> response = AuthAcceptanceTest.로그인_요청(email, password);
        return response.as(TokenResponse.class).getAccessToken();
    }

    public static ExtractableResponse<Response> 내회원정보_조회_요청(String accessToken) {
        final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken);
        return RestApiFixture.response(request.get("/members/me"));
    }

    public static ExtractableResponse<Response> 내회원정보_수정_요청(String accessToken, String email, String password, int age) {
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken, memberRequest);
        return RestApiFixture.response(request.put("/members/me"));
    }

    public static ExtractableResponse<Response> 내회원정보_삭제_요청(String accessToken) {
        final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken);
        return RestApiFixture.response(request.delete("/members/me"));
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestApiFixture.post(memberRequest, "/members");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestApiFixture.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        final String uri = response.header("Location");
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestApiFixture.put(memberRequest, uri);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestApiFixture.delete(uri);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
