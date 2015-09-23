package ru.gdo.android.library.materialdesign.exception;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class ToolBarActivityException extends RuntimeException {

    public ToolBarActivityException(String detailMessage) {
        super(detailMessage);
    }

    public ToolBarActivityException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
