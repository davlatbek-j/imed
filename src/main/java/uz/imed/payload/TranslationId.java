package uz.imed.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class TranslationId implements Serializable {

    private String entityType;
    private Long entityId;
    private String fieldName;
    private String languageCode;

    // Default constructor, equals, and hashCode methods
}
