package org.incredible.pojos.ob.valuator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class ExpiryDateValuator implements IEvaluator {
    public String issuedDate;

    public ExpiryDateValuator(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    @Override
    public String evaluates(Object inputVal) {
        return getExpiryDate((String) inputVal);
    }


    public String getExpiryDate(String expiryDate) {
        String pattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z";
        if (expiryDate.matches(pattern)) {
            return expiryDate;
        } else {
            try {
                /**
                 to split expiry dates of form (2m 2y)
                 */
                String[] splitExpiry = expiryDate.split(" ");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date parsedIssuedDate = simpleDateFormat.parse(issuedDate);
                cal.setTime(parsedIssuedDate);

                for (int index = 0; index < splitExpiry.length; index++) {
                    if (splitExpiry[index].endsWith("d") || splitExpiry[index].endsWith("D")) {
                        cal.add(Calendar.DATE, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                    } else if (splitExpiry[index].endsWith("m") || splitExpiry[index].endsWith("M")) {
                        cal.add(Calendar.MONTH, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                    } else if (splitExpiry[index].endsWith("y") || splitExpiry[index].endsWith("Y")) {
                        cal.add(Calendar.YEAR, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                    }
                }
                return simpleDateFormat.format(cal.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }


    }
}
