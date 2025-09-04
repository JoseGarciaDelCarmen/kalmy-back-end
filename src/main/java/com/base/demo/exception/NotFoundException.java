package com.base.demo.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String msg) { super(msg); }
}