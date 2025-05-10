package org.example.fpis_project.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<String>>() {}.getType();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        return gson.toJson(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty() || "null".equals(dbData)) {
            return new ArrayList<>();
        }
        try {
            return gson.fromJson(dbData, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}