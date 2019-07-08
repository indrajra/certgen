package org.incredible.certProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
        try {
            /**
             to split expiry dates of form (2m 2y)
             */
            String[] splitExpiry = expiryDate.split(" ");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            Date parsedIssuedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(issuedDate);
            cal.setTime(parsedIssuedDate);


            for (int index = 0; index < splitExpiry.length; index++) {
                if (splitExpiry[index].endsWith("d") || splitExpiry[index].endsWith("D")) {
                    cal.add(Calendar.DATE, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else if (splitExpiry[index].endsWith("m") || splitExpiry[index].endsWith("M")) {
                    cal.add(Calendar.MONTH, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else if (splitExpiry[index].endsWith("y") || splitExpiry[index].endsWith("Y")) {
                    cal.add(Calendar.YEAR, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else {
                    return expiryDate;
                }

            }
            return simpleDateFormat.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }


}
