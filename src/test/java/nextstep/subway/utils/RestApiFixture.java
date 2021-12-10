package nextstep.subway.utils;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestApiFixture {

	public static ExtractableResponse<Response> post(Object body, String path, Object... pathParams) {
		return response(request(body).post(path, pathParams));
	}

	public static ExtractableResponse<Response> get(String path, Object... pathParams) {
		return response(request().get(path, pathParams));
	}

	public static ExtractableResponse<Response> put(Object body, String path, Object... pathParams) {
		return response(request(body).put(path, pathParams));
	}

	public static ExtractableResponse<Response> delete(String path, Object... pathParams) {
		return response(request().delete(path, pathParams));
	}

	public static RequestSpecification request() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE);
	}

	public static RequestSpecification request(Object body) {
		return request()
			.body(body)
			.contentType(MediaType.APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> response(Response response) {
		return response.then().log().all().extract();
	}
}
