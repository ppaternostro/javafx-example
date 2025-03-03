package com.pasquasoft.application.exception;

public class NotFoundException extends RuntimeException
{
  private static final long serialVersionUID = 1177689255217731874L;

  public NotFoundException(String message)
  {
    super(message);
  }

}
