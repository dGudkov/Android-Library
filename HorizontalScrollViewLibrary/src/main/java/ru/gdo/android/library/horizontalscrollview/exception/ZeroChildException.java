package ru.gdo.android.library.horizontalscrollview.exception;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class ZeroChildException extends Exception {

	private static final long serialVersionUID = 8489555896148670378L;

	public ZeroChildException() {}

	public ZeroChildException(String errorMessage) {
		super(errorMessage);
	}

}
