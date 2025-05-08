package org.example.fpis_project.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Notification {
    String to;
    String subject;
    String text;
}
