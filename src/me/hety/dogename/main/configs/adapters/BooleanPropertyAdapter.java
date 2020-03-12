package me.hety.dogename.main.configs.adapters;

import com.google.gson.*;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Type;

public class BooleanPropertyAdapter implements JsonSerializer<SimpleBooleanProperty>, JsonDeserializer<SimpleBooleanProperty> {

    @Override
    public SimpleBooleanProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement==null) {
            throw new JsonParseException("Json is wrong.");
        }else {
            return new SimpleBooleanProperty(jsonElement.getAsBoolean());
        }
    }

    @Override
    public JsonElement serialize(SimpleBooleanProperty simpleBooleanProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        if(simpleBooleanProperty==null){
            throw new JsonParseException("Json is wrong.");
        }else {
            return new JsonPrimitive(simpleBooleanProperty.get());
        }
    }
}
