package rocks.danielw.config.interceptor;

import io.jsonwebtoken.Header;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ClientHttpRequestHeaderInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    HttpHeaders headers = request.getHeaders();
    if (!headers.containsKey(Header.CONTENT_TYPE)) {
      headers.setContentType(APPLICATION_JSON);
    }

    if (!headers.containsKey("Accept")) {
      headers.setAccept(Collections.singletonList(APPLICATION_JSON));
    }

    headers.setAcceptCharset(Collections.singletonList(Charset.defaultCharset()));
    return execution.execute(request, body);
  }
}
