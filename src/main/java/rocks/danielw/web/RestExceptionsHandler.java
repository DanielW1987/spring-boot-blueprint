package rocks.danielw.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import rocks.danielw.service.NotFoundException;
import rocks.danielw.service.UserAlreadyExistsException;
import rocks.danielw.web.api.ErrorResponse;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Slf4j
@ControllerAdvice
public class RestExceptionsHandler {

  @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class})
  public Object handleIllegalOrMissingArgumentErrors(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), BAD_REQUEST);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<ErrorResponse> handleMissingRequestBody(Exception exception) {
    log.warn("Request body could not be read", exception);
    return new ResponseEntity<>(createErrorResponse(exception), BAD_REQUEST);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse> handleHttpMethodNotSupported(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
  public ResponseEntity<ErrorResponse>  handleMediaTypeNotSupported(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ErrorResponse> handleValidationErrors(Exception exception, WebRequest webRequest) {
    log.warn(exception.getMessage(), exception);
    BindingResult bindingResult = null;
    if (exception instanceof BindException) {
      bindingResult = ((BindException) exception).getBindingResult();
    }

    return bindingResult != null
        ? new ResponseEntity<>(createErrorResponse(bindingResult), UNPROCESSABLE_ENTITY)
        : handleAllOtherExceptions(exception, webRequest);
  }

  @ExceptionHandler({ValidationException.class})
  public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler({UsernameNotFoundException.class, AccessDeniedException.class})
  public  ResponseEntity<ErrorResponse> handleExceptionsToForbiddenStatus(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), FORBIDDEN);
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), NOT_FOUND);
  }

  @ExceptionHandler({UserAlreadyExistsException.class})
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(Exception exception) {
    log.warn(exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), CONFLICT);
  }


  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception exception, WebRequest webRequest) {
    log.error(String.format("Internal Server Error on url '%s'", webRequest.getContextPath()), exception);
    return new ResponseEntity<>(createErrorResponse(exception), INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse createErrorResponse(Throwable exception) {
    return new ErrorResponse(exception.getMessage());
  }

  private ErrorResponse createErrorResponse(BindingResult bindingResult) {
    List<String> fieldErrors = bindingResult.getAllErrors()
        .stream()
        .map(this::transformFieldError)
        .sorted()
        .distinct()
        .collect(Collectors.toList());

    return new ErrorResponse(fieldErrors);
  }

  private String transformFieldError(ObjectError error) {
    if (error instanceof FieldError) {
      return "Error regarding field '" +
          ((FieldError) error).getField() +
          "': " +
          error.getDefaultMessage() +
          ".";
    }
    else {
      return error.getDefaultMessage();
    }
  }
}
