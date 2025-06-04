package com.enotes.Util;

public class AppConstant {

    /**
     * ==== Mobile no Regex Explanation ====
     * | Input | Valid?
     * | ----------------- |   ------          |
     * | `03001234567`     |   true            |
     * | `0300-1234567`    |   true            |
     * | `+92 3001234567`  |   true            |
     * | `+92-300-1234567` |   true            |
     * | `+923001234567`   |   true            |
     * | `3001234567`      |   true            |
     * | `0315 6543210`    |   true            |
     * ====  Email Regex Explanation ====
     * | Part                | Meaning                                   |
     * | ------------------- | ----------------------------------------- |
     * | `^`                 | Start of the string                       |
     * | `[a-zA-Z0-9._%+-]+` | Local part (username) before `@`          |
     * | `@`                 | At symbol                                 |
     * | `[a-zA-Z0-9.-]+`    | Domain name (e.g., `gmail`, `example.co`) |
     * | `\.`                | Dot before the TLD                        |
     * | `[a-zA-Z]{2,}`      | TLD with **at least 2 characters**        |
     * | `$`                 | End of the string                         |
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String MOBILE_NO_REGEX = "^(?:\\+92[\\s-]?|0)?3\\d{2}[\\s-]?\\d{7}$";


    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";


    public static final String DefaultPageNumber = "0";
    public static final String DefaultPageSize = "5";

}
