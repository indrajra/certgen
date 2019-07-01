package org.incredible.csvProcessor;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.incredible.certProcessor.CertModel;


public class CertModelFactory {
    private HashMap<String, String> csvProperties;
    private static Logger logger = LoggerFactory.getLogger(CertModelFactory.class);


    public CertModelFactory(HashMap<String, String> csvprop) {
        this.csvProperties = csvprop;
    }

    public CertModel create(CSVRecord csvRecord) {
        String expiry;
        try {
            CertModel certModel = new CertModel();
            for (HashMap.Entry<String, String> member : csvProperties.entrySet()) {
                String methodName = "set" + member.getKey();
                Class params = getParamType(methodName);
                Method method = certModel.getClass().getMethod(methodName, params);
                method.setAccessible(true);
                if (member.getValue().equals("Expiry")) {
                    expiry = calculateExpiryDateOfCertificate(csvRecord.get(member.getValue()), csvRecord.get("IssuedDate"));
                    method.invoke(certModel, expiry);
                    System.out.println(expiry + "expiry" + csvRecord.get("IssuedDate"));
                } else
                    method.invoke(certModel, csvRecord.get(member.getValue()));
            }
            return certModel;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.error("Exception in creating certmodel" + e);
            return null;
        }
    }

    private Class getParamType(String methodName) {
        Method[] declaredMethods = CertModel.class.getDeclaredMethods();
        Class[] parameterTypes = new Class[0];
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(methodName)) {
                parameterTypes = declaredMethod.getParameterTypes();
                break;
            }
        }

        if (parameterTypes.length == 0) {
            return null;

        } else
            return parameterTypes[0];
    }


    private String calculateExpiryDateOfCertificate(String expiryDate, String issueDate) {
        try {
            /**
            to split expiry date of form 2m 2y
             */
            String[] splitExpiry = expiryDate.split(" ");

            System.out.println(expiryDate);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(issueDate);
            cal.setTime(date);
            
            for (int index = 0; index < splitExpiry.length; index++) {
                if (splitExpiry[index].endsWith("d")) {
                    cal.add(Calendar.DATE, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else if (splitExpiry[index].endsWith("m")) {
                    cal.add(Calendar.MONTH, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else if (splitExpiry[index].endsWith("y")) {
                    cal.add(Calendar.YEAR, Integer.parseInt(splitExpiry[index].substring(0, splitExpiry[index].length() - 1)));
                } else {
//                long sum = simpleDateFormat.parse(expiryDate).getTime() + date.getTime();
//                Date sumDate = new Date(sum);
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



