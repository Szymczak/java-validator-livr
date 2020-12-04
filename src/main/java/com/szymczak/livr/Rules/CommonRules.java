package com.szymczak.livr.Rules;

import com.szymczak.livr.FunctionKeeper;
import com.szymczak.livr.LIVRUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.List;
import java.util.function.Function;

/**
 * Created by vladislavbaluk on 9/28/2017.
 */
public class CommonRules {

    public static Function<List<Object>, Function> required = objects -> (Function<FunctionKeeper, Object>) (wrapper) -> {
        if (LIVRUtils.isNoValue(wrapper.getValue())) {
            return "REQUIRED";
        }
        return "";
    };

    public static Function<List<Object>, Function<FunctionKeeper, Object>> not_empty = objects -> (Function<FunctionKeeper, Object>) (wrapper) -> {
        if (wrapper.getValue() != null && wrapper.getValue().equals("")) {
            return "CANNOT_BE_EMPTY";
        }
        return "";
    };

    public static Function<List<Object>, Function> not_empty_list = objects -> (Function<FunctionKeeper, Object>) (wrapper) -> {
        if (LIVRUtils.isNoValue(wrapper.getValue())) return "CANNOT_BE_EMPTY";
        if (!(wrapper.getValue() instanceof JSONArray)) return "FORMAT_ERROR";
        if (((JSONArray) wrapper.getValue()).size() == 0) return "CANNOT_BE_EMPTY";

        return "";
    };

    public static Function<List<Object>, Function> any_object = objects -> (Function<FunctionKeeper, Object>) (wrapper) -> {
        if (LIVRUtils.isNoValue((wrapper.getValue()))) return "";

        if (!LIVRUtils.isObject(wrapper.getValue())) {
            return "FORMAT_ERROR";
        }
        return "";
    };

    public static Function<List<Object>, Function<FunctionKeeper, Object>> list_length = objects -> {
        Object object = objects.get(0);
        if (object instanceof JSONArray) {
            JSONArray array = (JSONArray) object;
            Long minLength = Long.valueOf(array.get(0).toString());
            Long maxLength = Long.valueOf(array.get(1).toString());
            return checkArrayLength(minLength, maxLength);
        } else {
            final Long length = Long.valueOf(object.toString());
            return checkArrayLength(length, length);
        }
    };

    private static Function<FunctionKeeper, Object> checkArrayLength(Long minLength, Long maxLength) {
        return (FunctionKeeper wrapper) -> {
            Object value = wrapper.getValue();
            if (value == null || (value.toString()).equals("")) return "";
            if (value instanceof JSONArray) {
                JSONArray valuesArray = (JSONArray) value;
                if (valuesArray.size() < minLength) return "TOO_FEW_ITEMS";
                if (valuesArray.size() > maxLength) return "TOO_MANY_ITEMS";
            } else {
                return "FORMAT_ERROR";
            }
            return "";
        };
    }
    
}
