package com.blogspot.carirunners.run.db;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import timber.log.Timber;

public class BloggerTypeConverters {
    private static final String DELIMITER = ",";

    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToIntList(data);
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        return StringUtil.joinIntoString(ints);
    }

    @TypeConverter
    public static List<String> stringToStringList(String input) {
        if (input == null) {
            return null;
        } else {
            List<String> result = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(input, DELIMITER);

            while (tokenizer.hasMoreElements()) {
                String item = tokenizer.nextToken();

                try {
                    result.add(item);
                } catch (NumberFormatException e) {
                    Timber.e("Malformed list", e);
                }
            }

            return result;
        }
    }

    @TypeConverter
    public static String stringListToString(List<String> input) {
        if (input == null) {
            return null;
        } else {
            int size = input.size();
            if (size == 0) {
                return null;
            } else {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < size; ++i) {
                    sb.append(input.get(i));
                    if (i < size - 1) {
                        sb.append(DELIMITER);
                    }
                }

                return sb.toString();
            }
        }
    }
}
