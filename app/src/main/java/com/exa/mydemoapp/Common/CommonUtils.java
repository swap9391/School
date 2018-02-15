package com.exa.mydemoapp.Common;

/**
 * Created by midt-006 on 11/10/17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exa.mydemoapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Key;
import java.security.Security;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CommonUtils {
    public static final String NEW_LINE = "\n";
    public static final String TEXT_SEPERATOR = ",";
    private static final long DAY_IN_MILLI = 24 * 60 * 60 * 1000;
    private static String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static String DEFAULT_TIME_FORMAT = "HHmm";
    public static Locale locale = Locale.US;
    private static DateFormatSymbols formatter;
    private static JSONObject jsonObject;
    private static ProgressDialog dialog;
    private static Context context;
    private static Uri fileUri;
    private static String individual_id, fk_group_id;

    private static String extension;
    //public static String URL = Config.DOC_UPLOAD;
    private static String filePath = null;
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String getString(TextView editText) {
        if (editText.getText() != null
                && !"".equals(editText.getText().toString())) {
            return editText.getText().toString();
        }
        return null;
    }

    public static String getString(EditText editText) {
        if (editText.getText() != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    public static String[] splitByLength(String str, int len) {

        List<String> result = new ArrayList<String>();
        try {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                if (sb.length() >= len) {
                    while (true) {
                        if (i >= str.length()) {
                            break;
                        }
                        if (str.charAt(i) == ' ' || str.charAt(i) == ',') {
                            result.add(sb.toString().trim());
                            sb.setLength(0);
                            break;
                        }
                        sb.append(str.charAt(i));
                        i++;
                    }
                } else {
                    sb.append(str.charAt(i));
                }
            }
            if (sb.length() > 0) {
                result.add(sb.toString().trim());

            }
        } catch (Throwable t) {
            t.printStackTrace();
            result.clear();
            result.add(str);
        }
        return result.toArray(new String[result.size()]);

    }

    public static boolean isEmpty(TextView txtView) {
        String str = getString(txtView);
        return str == null || "".equals(str.trim());

    }

    public static String emptyIfNull(String str) {
        return str == null || str.equalsIgnoreCase("null") ? "" : str;
    }

    public static String nullIfEmpty(String str) {
        return str == null || str.trim().equals("") ? null : str;
    }

    public static void setSelected(Spinner spn, String text) {
        for (int i = 0; i < spn.getCount(); i++) {
            String item = (String) spn.getItemAtPosition(i);
            if (item.equals(text)) {
                spn.setSelection(i);
                return;
            }
        }

    }

    public static Integer toInt(String str) {
        return toInt(str, null);
    }

    public static Integer toInt(String str, Integer defaultVal) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static long toLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            return -1;
        }
    }

	/*public static boolean startsWith(String text, String str) {
        if (text == null || str == null) {
			return false;
		}
		return text.toUpperCase(App.getAppLocale()).startsWith(
				str.toUpperCase(App.locale));
	}
*/
    // public static String append(String... strs) {
    // return append(" ", strs);
    // }

    public static String append(String seperator, String... objects) {
        if (objects == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : objects) {
            if (s == null) {
                continue;
            }
            if (!first) {
                sb.append(seperator);
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    public static String append(String seperator, int... integers) {
        if (integers == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i : integers) {
            if (!first) {
                sb.append(seperator);
            }
            sb.append(i);
            first = false;
        }
        return sb.toString();
    }

    public static String appendKeyValue(String seperator, String... strs) {
        if (strs == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < strs.length; i++) {
            String label = strs[i];
            String value = strs[++i];
            String s = nullIfEmpty(value);
            if (s == null) {
                continue;
            }
            if (!first) {
                sb.append(seperator);
            }
            sb.append(label).append(":").append(value);
            first = false;
        }
        return sb.toString();
    }

    public static String appendForSql(Object... objs) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object ob : objs) {
            String s = "";
            if (ob instanceof String) {
                String str = ob.toString().replace("'", "");
                s = "'" + str + "'";
            } else if (ob instanceof Number) {
                s = ob.toString();
            } else if (ob != null) {
                s = "'" + ob.toString() + "'";
            } else {
                s = "null";
            }
            if (!first) {
                sb.append(",");
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String toString(Double f) {
        if (f != null) {
            return f.toString();
        }
        return "";
    }

    public static String toString(Integer i) {
        if (i != null) {
            return i.toString();
        }
        return "";
    }

    public static String getString(Spinner selection) {
        return (String) selection.getSelectedItem();
    }

    public static Integer getString(RadioGroup selection) {
        return selection.getCheckedRadioButtonId();
    }

    public static Float asFloat(TextView ob) {
        String str = getString(ob);
        return str == null ? null : Float.parseFloat(str);
    }

    public static Double asDouble(String str) {
        try {
            return asDouble(str, null);
        } catch (Exception ex) {
            return null;
        }

    }

    public static Double asDouble(String str, Double defaultValue) {
        try {
            return str == null ? defaultValue : Double.parseDouble(str);
        } catch (Exception ex) {
            return defaultValue;
        }

    }

    public static Integer asInt(String str, Integer defaultValue) {
        try {
            return str == null ? defaultValue : Integer.parseInt(str);
        } catch (Exception ex) {
            return defaultValue;
        }

    }

    public static Integer asInt(TextView ob, Integer detaultVal) {
        String str = getString(ob);
        return toInt(str, detaultVal);
    }

    public static Integer asInt(TextView ob) {

        return asInt(ob, null);
    }


    public static Double asDouble(TextView ob) {
        String str = getString(ob);
        try {
            return str == null ? null : Double.parseDouble(str);
        } catch (Exception ex) {
            return null;
        }
    }


    public static Date toDisplayDate(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new SimpleDateFormat("dd/MM/yyyy", getAppLocale())
                    .parse(str);

        } catch (ParseException e) {
            return toDate(str);
        }

    }


    public static Date toDate(String str) {
        return toDate(str, DEFAULT_DATE_FORMAT);
    }

    public static Date toDate(String str, String format) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new SimpleDateFormat(format, getAppLocale()).parse(str);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Locale getAppLocale() {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static void setAppLocale(Locale newLocale) {
        locale = newLocale;
        formatter = new DateFormatSymbols(locale);
    }

    public static Date toTime(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new SimpleDateFormat(DEFAULT_TIME_FORMAT,
                    getAppLocale()).parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDateForDisplay(Date d, String format) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(format, getAppLocale()).format(d);

    }

    /**
     * @param d
     * @return dd-MMM-yyyy
     */
    public static String formatDateForDisplay(Date d) {
        return formatDateForDisplay(d, "dd-MMM-yyyy");
    }

    /**
     * @param d
     * @return
     */
    public static String formatDateYYYYMM(Date d) {
        return formatDateForDisplay(d, "yyyyMM");
    }

    /**
     * MM-YYYY
     *
     * @param d
     * @return
     */
    public static String formatDateMMYYYY(Date d) {
        return formatDateForDisplay(d, "MM-yyyy");
    }

    public static String formatDateMMMYYYY(Date d) {
        return formatDateForDisplay(d, "MMM yyyy");
    }

    public static String formatDateForBirthdayAnniversary(Date d) {
        Calendar cal2 = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
        return formatDateForDisplay(cal.getTime(), "dd-MMM-yyyy");
    }

    public static String formatDateForJson(Date d) {
        return formatDateForDisplay(d, "dd-MM-yyyy");
    }

    public static String getGapText(Date d) {
        if (d == null) {
            return "";
        }
        Calendar c = Calendar.getInstance();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);


        if (compare(c, c1) == 0) {

            return "Call Right Now";
        }
        c.add(Calendar.DAY_OF_YEAR, 1);
        if (compare(c, c1) == 0) {
            return "Tomorrow";
        }
        c.add(Calendar.DAY_OF_YEAR, -2);
        if (compare(c, c1) == 0) {
            return "Yesterday";
        }
        return "";
    }

    public static String toUserFriendlyDate(Context context, Date d, Date time, boolean allowPast) {
        if (d == null) {
            return "";
        }
        Calendar c = Calendar.getInstance();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);
        String timeText = "";
        if (time != null) {
            timeText = " " + formatTimeForDisplay(context,
                    time);
        }

        if (compare(c, c1) == 0) {

            return context.getResources().getString(R.string.today) + timeText;
        }
        c.add(Calendar.DAY_OF_YEAR, 1);
        if (compare(c, c1) == 0) {
            return context.getResources().getString(R.string.tomorrow) + timeText;
        }
        c.add(Calendar.DAY_OF_YEAR, -2);
        if (allowPast && compare(c, c1) == 0) {
            return context.getResources().getString(R.string.yesterday) + timeText;
        }
        SimpleDateFormat format;
        if (c.get(Calendar.YEAR) == c1.get(Calendar.YEAR)) {
            format = new SimpleDateFormat("EEE, d MMM", locale);
        } else if (c.get(Calendar.YEAR) <= 2000) {
            format = new SimpleDateFormat("EEE, d MMM yyyy", locale);
        } else {
            format = new SimpleDateFormat("EEE, d MMM yy", locale);
        }
        return format.format(d);
    }

    public static String formatDateForDisplay(Context context, Date date) {
        if (date == null) {
            return "";
        }
        return android.text.format.DateFormat.getDateFormat(context).format(
                date);
    }

    public static String formatTimeForDisplay(int h, int m) {
        String amPm = "AM";
        if (h >= 12) {
            amPm = "PM";
            h = h - 12;
        }
        return prefixZero(h) + ":" + prefixZero(m) + " " + amPm;
    }

    public static String formatTimeForDisplay(Context context, Date time) {
        if (time == null) {
            return "";
        }
        return android.text.format.DateFormat.getTimeFormat(context).format(
                time);
    }

    /**
     * @param d
     * @return yyyyMMdd
     */
    public static String format(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT,
                getAppLocale()).format(d);
    }

    public static String formatMonthDay(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat("MMdd", getAppLocale()).format(d);
    }

    public static String formatTime(Date d) {
        return formatTime(d, DEFAULT_TIME_FORMAT);
    }

    public static String formatTime(Date d, String format) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(format, getAppLocale()).format(d);
    }

    public static boolean isToday(Date d) {
        if (d == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy",
                getAppLocale());
        return formater.format(d).equals(formater.format(c.getTime()));
    }

    public static String lpad(char padChar, String str, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = str.length(); i < len; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    public static String dateBeforeTodayAsText(Date d) {
        Calendar other = Calendar.getInstance();
        other.setTime(d);
        Calendar today = Calendar.getInstance();

        String gapText = "Year";
        int gap = today.get(Calendar.YEAR) - other.get(Calendar.YEAR);

        if (gap == 0) {
            gapText = "Month";
            gap = today.get(Calendar.MONTH) - other.get(Calendar.MONTH);
            if (gap == 0) {
                gap = today.get(Calendar.WEEK_OF_MONTH)
                        - other.get(Calendar.WEEK_OF_MONTH);
                gapText = "Week";
                if (gap == 0 || gap == 1) {
                    gapText = "Day";
                    gap = today.get(Calendar.DAY_OF_MONTH)
                            - other.get(Calendar.DAY_OF_MONTH);

                    if (gap == 0) {
                        gapText = "Today";
                    }
                }
            }
        }

        if ("Today".equals(gapText)) {
            return gapText;
        }
        if ("Day".equals(gapText) && gap == 1) {
            return "Yesterday";
        }
        return gap + " " + (gap > 1 ? gapText + "s" : gapText) + " Before";
    }

    public void exportLog() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "log.txt");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            return;
        }
        // final StringBuilder log = new StringBuilder();
        try {
            ArrayList<String> commandLine = new ArrayList<String>();
            commandLine.add("logcat");
            commandLine.add("-d");
            // ArrayList<String> arguments = ((params != null) && (params.length
            // > 0)) ? params[0] : null;
            // if (null != arguments){
            // commandLine.addAll(arguments);
            // }
            Process process = Runtime.getRuntime().exec(
                    commandLine.toArray(new String[0]));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                out.write((line + "\n").getBytes("ISO-8859-1"));
            }
            out.close();
        } catch (IOException e) { // } return log;
        }
    }

    public static void hideSoftKeyboard(Dialog context) {
        View focused = context.getCurrentFocus();
        if (focused != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getContext().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(
                        focused.getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(Context context) {
        View focused = null;
        if (context instanceof Activity) {
            focused = ((Activity) context).getCurrentFocus();
            if (focused != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            focused.getWindowToken(), 0);
                }
            }
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (context instanceof Activity) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static Date[] calculateMonthStartEnd(Calendar c) {
        Date[] range = new Date[2];
        c.set(Calendar.DAY_OF_MONTH, 1);
        int month = c.get(Calendar.MONTH);
        range[0] = c.getTime();
        for (int i = 29; i <= 32; i++) {
            c.set(Calendar.DAY_OF_MONTH, i);
            if (c.get(Calendar.MONTH) != month) {
                c.add(Calendar.DAY_OF_MONTH, -1);
                range[1] = c.getTime();
                break;
            }
        }
        return range;
    }

    public static Date[] calculateRangeToday(Calendar c) {
        Date[] range = new Date[2];
        range[0] = c.getTime();
        range[1] = c.getTime();
        return range;
    }

    public static Date[] calculateYearStartEnd(Calendar c) {
        Date[] range = new Date[2];
        if (c.get(Calendar.MONTH) < 4) {
            c.add(Calendar.YEAR, -1);
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, Calendar.APRIL);
        range[0] = c.getTime();

        c.add(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 31);

        range[1] = c.getTime();

        return range;
    }

    public static int compareWithToday(Date date) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        return compare(c1, c2);

    }

    public static boolean isBetween(Date d, Date d1, Date d2) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return compare(c, c1) >= 0 && compare(c, c2) <= 0;

    }

    public static boolean isBetween(Calendar c, Calendar c1, Calendar c2) {
        return compare(c, c1) >= 0 && compare(c, c2) <= 0;

    }

    public static int compare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return compare(c1, c2);

    }

    public static int compare(Calendar c1, Calendar c2) {

        int result = compare(c1, c2, Calendar.YEAR);
        if (result == 0) {
            result = compare(c1, c2, Calendar.DAY_OF_YEAR);
        }
        return result;
    }

    public static int compare(Calendar c1, Calendar c2, int field) {
        Integer i1 = c1.get(field);
        Integer i2 = c2.get(field);
        return i1.compareTo(i2);
    }

    public static String format(double d) {
        return new DecimalFormat("#").format(d);
    }

    public static String formatAmount(double d) {
        return amountFormater().format(d);
    }

    public static String formatNumber(long n) {
        return numberFormater().format(n);
    }

    public static NumberFormat numberFormater() {
        return NumberFormat.getIntegerInstance();
    }

    public static NumberFormat amountFormater() {
        NumberFormat f = DecimalFormat.getInstance();
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        return f;
    }

    /*
        public static void updatePrefDefault(Context context, int keyId,
                boolean value) {
            SharedPreferences pref = context.getSharedPreferences(PREF_KEY,
                    Context.MODE_PRIVATE);
            String key = context.getString(keyId);
            if (!pref.contains(key)) {
                Editor editor = pref.edit().putBoolean(key, value);
                editor.commit();
            }
        }
        public static void updatePref(Context context, int keyId, int value) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            String key = context.getString(keyId);
            Editor editor = pref.edit().putInt(key, value);
            editor.commit();
        }
        public static void updatePref(Context context, int keyId, boolean value) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            String key = context.getString(keyId);
            Editor editor = pref.edit().putBoolean(key, value);
            editor.commit();
        }
        public static void updatePref(Context context, int keyId, String value) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            String key = context.getString(keyId);
            Editor editor = pref.edit().putString(key, value);
            editor.commit();
        }
        public static void updatePrefDefault(Context context, int keyId, int value) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            String key = context.getString(keyId);
            if (!pref.contains(key)) {
                Editor editor = pref.edit().putInt(key, value);
                editor.commit();
            }
        }
        public static String getPreference(Context context, int key) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            return pref.getString(context.getString(key), null);
        }
        public static boolean getBooleanPreference(Context context, int key) {
            SharedPreferences pref = context.getSharedPreferences(App.PREF_KEY,
                    Context.MODE_PRIVATE);
            return pref.getBoolean(context.getString(key), false);
        }
        public static double getDoublePreference(Context context, int key,
                double defValue) {
            return asDouble(getPreference(context, key), defValue);
        }
        public static int getIntPreference(Context context, int key, int defValue) {
            return asInt(getPreference(context, key), defValue);
        }
        public static boolean isAllEmpty(String... strs) {
            for (String s : strs) {
                if (!TextUtils.isEmpty(s)) {
                    return false;
                }
            }
            return true;
        }
    */
    public static String toGenederDisplay(String gen) {
        if (gen == null) {
            return "";
        }
        return gen.equals("M") ? "MALE" : gen.equals("F") ? "FEMALE" : "";
    }

    public static String getDeviceId(Context context) {
        return Secure
                .getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static void appendWithCommaIfRequired(StringBuilder sb, Object ob) {
        if (ob == null) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(ob);
    }


//	public static <T> void addAll(ArrayAdapter<T> adapter, Collection<T> objects) {
//		adapter.clear();
//		if (objects != null) {
//			// If the platform supports it, use addAll, otherwise add in loop
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				adapter.addAll(objects);
//			} else {
//				for (T item : objects) {
//					adapter.add(item);
//				}
//			}
//			adapter.notifyDataSetChanged();
//		}
//	}

    public static String prefixZero(int value) {
        return value < 10 ? "0" + value : "" + value;
    }

    public static String formatDate(int yr, int month, int day) {
        return prefixZero(month) + prefixZero(day) + yr;
    }

    public static String formatDayOfMonth(int n) {

        if (n >= 11 && n <= 13) {
            return n + "th";
        }
        switch (n) {
            case 1:
                return n + "st";
            case 2:
                return n + "nd";
            case 3:
                return n + "rd";
            default:
                return n + "th";
        }

    }

//    public static Date convertToDate(String str) {
//        if (StringUtils.isEmpty(str)) {
//            return null;
//        }
//        List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
//        knownPatterns.add(new SimpleDateFormat("dd-MM-yyyy"));
//        knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
//        knownPatterns.add(new SimpleDateFormat("dd/MM/yy"));
//        knownPatterns.add(new SimpleDateFormat("dd-MM-yy"));
//        knownPatterns.add(new SimpleDateFormat("yyyyMMdd"));
//        knownPatterns.add(new SimpleDateFormat("MMM d"));
//        knownPatterns.add(new SimpleDateFormat("d MMM"));
//        knownPatterns.add(new SimpleDateFormat("d-MMM"));
//        knownPatterns.add(new SimpleDateFormat("MMM-d"));
//
//        for (SimpleDateFormat pattern : knownPatterns) {
//            try {
//                return pattern.parse(str);
//            } catch (ParseException pe) {
//                // Loop on
//            }
//        }
//        return null;
//    }


    public static String getTimeDiff(String start, String end) {
        boolean startPM = false;
        boolean endPM = false;
        if (start.contains("PM")) {
            startPM = true;

        }
        if (end.contains("PM")) {
            endPM = true;
        }
        start = start.replace("PM", "");
        start = start.replace("AM", "");
        end = end.toUpperCase().replace("AM", "");
        end = end.toUpperCase().replace("PM", "");
        start = start.replace(":", "").trim();
        end = end.replace(":", "").trim();

        int startHr = CommonUtils.asInt(start.substring(0, 2), 0);
        int startMinute = Integer.parseInt(start.substring(2));
        int endHr = Integer.parseInt(end.substring(0, 2));
        int endMinute = Integer.parseInt(end.substring(2));
        int startTime = (startHr + (startPM ? 12 : 0)) * 60 + startMinute;
        int endTime = (endHr + (endPM ? 12 : 0)) * 60 + endMinute;
        int diff = endTime - startTime;
        int hr = diff / 60;
        int minute = diff % 60;
        return "" + Math.abs(hr) + " Hr " + (minute > 0 ? minute + " Minute" : "");
    }

    public static String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }


    public static byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }


    /*public static void UploadImage(final Context context, Bitmap bitmap, String fileName) {
        jsonObject = new JSONObject();
        dialog = new ProgressDialog(context);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject.put("name", fileName);
            // Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.IMAGE_UPLOAD, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("Message from server", jsonObject.toString());
                        dialog.dismiss();
                        //messageText.setText("Image Uploaded Successfully");
                        //Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Message from server", volleyError.toString());
                dialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }*/


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String toMinutes(int input) {
        int hours = input / 3600;
        int minutes = (input % 3600) / 60;
        int second = (input % 3600) % 60;

        String ans = String.format("%02d:%02d:%02d", hours, minutes, second);


        return ans;
    }

    public static void UploadDoc(Context context, String ext, String encoded) {
        context = context;
        extension = ext;
        filePath = encoded;
    }

   /* public static class uploadToServer extends AsyncTask<Void, Void, String> {
        private ProgressDialog pd = new ProgressDialog(context);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
            String formattedDate = sdf.format(date);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("individual_id", individual_id));
            nameValuePairs.add(new BasicNameValuePair("fk_group_id", fk_group_id));
            nameValuePairs.add(new BasicNameValuePair("image", filePath));
            nameValuePairs.add(new BasicNameValuePair("extension", extension));
            nameValuePairs.add(new BasicNameValuePair("flag", "2"));
         *//*   if("1".equals(FlagCheck)){
                nameValuePairs.add(new BasicNameValuePair("flag", "0"));
            }if("2".equals(FlagCheck)){
                nameValuePairs.add(new BasicNameValuePair("flag", "1"));
            }*//*
            Log.e("filePath", filePath);
            Log.e("Image extension", extension);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            if ("jpg".equals(extension) || "jpeg".equals(extension)) {
                Toast.makeText(context, "Photos uploaded Succesfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Documents uploaded Succesfully", Toast.LENGTH_SHORT).show();
            }
            //imgPreview.setImageBitmap(null);
            //  Intent startGroupShareActivity=new Intent(getApplicationContext(),GroupSharePhotoActivity.class);
            // startActivity(startGroupShareActivity);
            // finish();
            filePath = null;
        }
    }
*/

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;// isReachable(App.HTTP_SERVER_URL, App.HTTP_PORT, 1000);
        }
        return false;
    }

    public static ArrayList jsonParser(JSONArray jsonArray, Class myClass,
                                       ArrayList data) {

        Gson g = new Gson();
        JSONArray ja = jsonArray;
        try {

            for (int i = 0; i < ja.length(); i++) {
                //showLog(ja.getString(i), myClass);
                data.add(g.fromJson(ja.getString(i), myClass));
                // listener.loading(i,ja.length());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    }

    public static ArrayList jsonParser(InputStream in, Class myClass, ArrayList data) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        Gson g = new Gson();
        try {
            while ((line = br.readLine()) != null) {
                JSONArray ja = new JSONArray(line);
                for (int i = 0; i < ja.length(); i++) {
                    //	showLog(ja.getString(i), myClass);
                    data.add(g.fromJson(ja.getString(i), myClass));
                    // listener.loading(i,ja.length());
                }
            }
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;


    }

    public static void showSnack(String msg, CoordinatorLayout coordinatorLayout) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            /*.setAction("RETRY", new View.OnClickListener() {
                @Override
				public void onClick(View view) {
				}
			});
// Changing message text color
		snackbar.setActionTextColor(Color.RED);
*/
// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }


    public static String getHotelCode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS", context.MODE_PRIVATE);
        String hotelCode = prefs.getString("hotelcode", "");
        return hotelCode;
    }


    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    /*public static void getAlertDialog(Context context, String title, String message, String positiveButton, String negativeButton, final IDialog iDialog) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                iDialog.PositiveMethod(dialog, id);
            }
        }).setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                iDialog.NegativeMethod(dialog, id);
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
        if (false) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }
    }*/

    public static File getAttachmentDir(Context context, String dirName) {

        File attch = getPasDir();
        File folder = new File(attch, "" + dirName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

   /* public static File getAttachmentsDir(Context context) {
        File f = getPasDir();
        File attch = new File(f, "ATTACHMENTS");
        if (!attch.exists()) {
            attch.mkdir();
            attch = new File(f, "ATTACHMENTS");
        }
        return attch;
    }*/

    public static File getPasDir() {

        File f = Environment.getExternalStorageDirectory();
        File pasDir = new File(f, "hotel_system_attachment");
        if (!pasDir.exists()) {
            pasDir.mkdir();
        }
        return pasDir;
    }

    public static void InsertSharedPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void InsertSharedPref(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static int getSharedPref(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS", context.MODE_PRIVATE);
        int value = prefs.getInt(key, 0);
        return value;
    }

    public static String getSharedPref(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS", context.MODE_PRIVATE);
        String value = prefs.getString(key, "");
        return value;
    }

    public static void removeSharePref(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }


    public static void insertSharedPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void removeAllPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static String getStudentId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS", context.MODE_PRIVATE);
        String value = prefs.getString(Constants.STUDENT_ID, "");
        return value;
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(String filePath) {
        Bitmap myBitmap = null;
        File imgFile = new File(filePath);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //Drawable d = new BitmapDrawable(getResources(), myBitmap);
        }

        return myBitmap;
    }

    public static Uri convertUrlToUri(String myUrlStr) {
        URL url;
        Uri uri = null;
        try {
            url = new URL(myUrlStr);
            uri = Uri.parse(url.toURI().toString());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

   /* public static String encyptPassword(String input1) {
        String encyptedText = null;
        try {
            //  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] input = input1.getBytes();
            byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                    0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            System.out.println(new String(input));
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            System.out.println(new String(cipherText));
            System.out.println(ctLength);
            encyptedText = new String(cipherText);
        } catch (Exception e) {

        }
        return encyptedText;
    }

    public static String decryptPassword(String input1) {
        String decryptedText = null;
        try {
            byte[] input = input1.getBytes();
            byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                    0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            System.out.println(new String(input));

            // decryption pass
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = new byte[cipher.getOutputSize(input.length)];
            int ptLength = cipher.update(input, 0, input.length, plainText, 0);
            ptLength += cipher.doFinal(plainText, ptLength);
            decryptedText = new String(plainText);
            System.out.println(new String(plainText));
            System.out.println(ptLength);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return decryptedText;
    }*/

    public static String encrypt(String value) throws Exception {
        String encryptedValue64 = null;
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
            encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.NO_PADDING);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return encryptedValue64;
    }

    public static String decrypt(String value) throws Exception {
        String decryptedValue = null;
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue64 = Base64.decode(value, Base64.NO_PADDING);
            byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
            decryptedValue = new String(decryptedByteValue, "utf-8");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        return key;
    }

    public static String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Error", e.getMessage());
        }

        return result;
    }


}