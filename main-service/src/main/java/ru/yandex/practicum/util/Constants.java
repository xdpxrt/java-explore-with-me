package ru.yandex.practicum.util;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final String CATEGORIES_ADMIN_URI = "/admin/categories";
    public static final String CATEGORIES_PUBLIC_URI = "/categories";
    public static final String CATEGORY_NOT_FOUND = "Category ID%d not found";
    public static final String COMPILATIONS_ADMIN_URI = "/admin/compilations";
    public static final String COMPILATIONS_PUBLIC_URI = "/compilations";
    public static final String COMPILATION_ID_URI = "/{compId}";
    public static final String COMPILATION_NOT_FOUND = "Compilation ID%d not found";
    public static final String EVENTS_ADMIN_URI = "/admin/events";
    public static final String EVENT_ID_URI = "/{eventId}";
    public static final String EVENTS_PRIVATE_URI = "/users/{userId}/events";
    public static final String EVENTS_PUBLIC_URI = "/events";
    public static final String EVENT_NOT_FOUND = "Event ID%d not found";
    public static final String REQUESTS_PRIVATE_URI = "/users/{userId}/requests";
    public static final String USERS_ADMIN_URI = "/admin/users";
    public static final String USER_NOT_FOUND = "User ID%d not found";
}