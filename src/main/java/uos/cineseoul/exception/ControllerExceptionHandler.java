package uos.cineseoul.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import uos.cineseoul.utils.enums.StatusEnum;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalExceptionHandler(Exception ex, WebRequest request) {

        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        ErrorResponse message = new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR.getStatusCode(), new Date(), url,
                ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        ErrorResponse message = new ErrorResponse(StatusEnum.NOT_FOUND.getStatusCode(), new Date(), url, ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<ErrorResponse> handleDataFormatException(DataFormatException ex, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        ErrorResponse message = new ErrorResponse(StatusEnum.BAD_REQUEST.getStatusCode(), new Date(), url, ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        ErrorResponse message = new ErrorResponse(StatusEnum.FORBIDDEN.getStatusCode(), new Date(), url, ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataInconsistencyException.class)
    public ResponseEntity<ErrorResponse> handleDataInconsistencyException(DataInconsistencyException ex, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        ErrorResponse message = new ErrorResponse(StatusEnum.PRECONDITION_FAILED.getStatusCode(), new Date(), url, ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException ex, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

        String firstBindingMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse message = new ErrorResponse(StatusEnum.BAD_REQUEST.getStatusCode(), new Date(), url, firstBindingMessage,
                request.getDescription(false));

        return new ResponseEntity<ErrorResponse>(message, HttpStatus.BAD_REQUEST);
    }
}