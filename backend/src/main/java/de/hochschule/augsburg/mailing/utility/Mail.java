package de.hochschule.augsburg.mailing.utility;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mail {
    String Subject;
    String mailTo;
    String content;
    String mailFrom;
}
