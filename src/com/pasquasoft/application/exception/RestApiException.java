package com.pasquasoft.application.exception;

public class RestApiException extends RuntimeException
{
  private static final long serialVersionUID = 7556357284914975081L;

  public RestApiException(String message)
  {
    super(message);
  }

}
