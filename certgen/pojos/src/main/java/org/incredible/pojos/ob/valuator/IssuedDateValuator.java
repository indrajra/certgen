package org.incredible.pojos.ob.valuator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class IssuedDateValuator implements IEvaluator {

    private static List<SimpleDateFormat>
            dateFormats = new ArrayList<SimpleDateFormat>() {
        {
            add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            add(new SimpleDateFormat("yyyy-MM-dd"));
        }
    };

    @Override
    public String evaluates(Object inputVal) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar cal = Calendar.getInstance();
        Date date = convertToDate((String) inputVal);
        if (date == null) {

            /** if date is null get current date and time **/
            date = new Date();
            DateFormat dateFormat = simpleDateFormat;
            return dateFormat.format(date);

        } else {
            cal.setTime(date);
            return simpleDateFormat.format(cal.getTime());
        }
    }


    public static Date convertToDate(String input) {
        Date date = null;
        if (null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(input);
            } catch (ParseException e) {
            }
            if (date != null) {
                break;
            }
        }

        return date;
    }

}
