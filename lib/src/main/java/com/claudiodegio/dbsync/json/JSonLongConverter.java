package com.claudiodegio.dbsync.json;

import com.claudiodegio.dbsync.core.ValueMetadata;
import com.claudiodegio.dbsync.core.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;


public class JSonLongConverter implements JSonConverter {
    @Override
    public Value jsonToColumnValue(final JsonParser parser, final ValueMetadata metadata) throws IOException {

        JsonToken token;
        Value value;

        // Go to the next token
        token = parser.nextToken();

        if (token != JsonToken.VALUE_NUMBER_INT && token != JsonToken.VALUE_NULL) {
            throw new IOException("Unable to parse field " + metadata.getName() + " expected int or null at line " + parser.getCurrentLocation().getLineNr());
        }

        // Can be a integer or null
        if (token == JsonToken.VALUE_NUMBER_INT) {
            value = new Value(parser.getValueAsLong(), metadata);
        } else {
            // null
            if (metadata.isNotNull()) {
                throw new IOException("Unable to parse field " + metadata.getName() + " expected int bu found null at line " + parser.getCurrentLocation().getLineNr());
            }

            value = new Value(metadata);
        }

        return value;
    }

    @Override
    public void columnValueToJson(JsonGenerator gen, Value value) throws IOException {

        String fieldName;

        fieldName = value.getMetadata().getName();

        if (value.isNull()) {
            gen.writeNullField(fieldName);
        } else {
            gen.writeNumberField(fieldName, value.getValueLong());
        }
    }
}
