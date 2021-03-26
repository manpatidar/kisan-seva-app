package com.manohar.kisansevapp.util;

public class MalformedJwtException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MalformedJwtException(String msg) {
		super(msg);
	}
}
