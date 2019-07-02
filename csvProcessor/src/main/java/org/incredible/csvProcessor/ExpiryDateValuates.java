package org.incredible.csvProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpiryDateValuates {

    public String issuedDate;

    public ExpiryDateValuates(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getExpiryDate(String expiryDate) {
        try {
            /**
             to split expiry date of form (2m 2y)
             */
            String[] splitExpiry = expiryDate.split(" ");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(issuedDate);
            cal.setTime(date);

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
