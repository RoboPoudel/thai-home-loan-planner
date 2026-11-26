package th.homeloan.planner;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import org.json.JSONObject;

/** Home-screen widget: next payment, days left and how much of the loan is paid. The page sends the numbers. */
public class LoanWidget extends AppWidgetProvider {
    private static final String PREFS = "widget";

    static void save(Context ctx, String json) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString("data", json).apply();
        refresh(ctx);
    }

    static void refresh(Context ctx) {
        AppWidgetManager m = AppWidgetManager.getInstance(ctx);
        int[] ids = m.getAppWidgetIds(new ComponentName(ctx, LoanWidget.class));
        if (ids.length > 0) update(ctx, m, ids);
    }

    static boolean pin(Context ctx) {
        if (Build.VERSION.SDK_INT < 26) return false;
        AppWidgetManager m = ctx.getSystemService(AppWidgetManager.class);
        if (m == null || !m.isRequestPinAppWidgetSupported()) return false;
        return m.requestPinAppWidget(new ComponentName(ctx, LoanWidget.class), null, null);
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager m, int[] ids) { update(ctx, m, ids); }

    private static void update(Context ctx, AppWidgetManager m, int[] ids) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), R.layout.widget_loan);
        try {
            String s = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString("data", null);
            if (s != null) {
                JSONObject o = new JSONObject(s);
                v.setTextViewText(R.id.wg_name, o.optString("name", "Home Loan Planner"));
                v.setTextViewText(R.id.wg_lbl, o.optString("lbl"));
                v.setTextViewText(R.id.wg_amt, o.optString("amt"));
                String days = o.optString("days");
                String iso = o.optString("nextISO", "");
                if (iso.length() == 10) {
                    // keep "in N days" right between app launches; monthly payments roll on to the next month
                    java.util.Calendar due = java.util.Calendar.getInstance();
                    due.set(Integer.parseInt(iso.substring(0, 4)), Integer.parseInt(iso.substring(5, 7)) - 1, Integer.parseInt(iso.substring(8, 10)), 0, 0, 0);
                    java.util.Calendar today = java.util.Calendar.getInstance();
                    today.set(java.util.Calendar.HOUR_OF_DAY, 0); today.set(java.util.Calendar.MINUTE, 0); today.set(java.util.Calendar.SECOND, 0);
                    boolean rolled = false;
                    while (due.before(today) && !rolled) { due.add(java.util.Calendar.MONTH, 1); if (!o.optBoolean("monthly", true)) rolled = true; }
                    long n = Math.round((due.getTimeInMillis() - today.getTimeInMillis()) / 86400000.0);
                    days = n <= 0 ? o.optString("todayTxt", days) : o.optString("inTpl", "{n}").replace("{n}", String.valueOf(n));
                    if (n > 0 && !iso.equals(String.format(java.util.Locale.US, "%tF", due))) {
                        v.setTextViewText(R.id.wg_next, java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(due.getTime()) + "  ·  " + days);
                    } else v.setTextViewText(R.id.wg_next, o.optString("next") + "  ·  " + days);
                } else v.setTextViewText(R.id.wg_next, o.optString("next") + "  ·  " + days);
                v.setProgressBar(R.id.wg_bar, 1000, (int) Math.round(o.optDouble("paid", 0) * 10), false);
                v.setTextViewText(R.id.wg_paid, o.optString("paidTxt") + "  ·  " + o.optString("free"));
            }
        } catch (Exception ignored) { }
        Intent open = new Intent(ctx, MainActivity.class).setAction(Intent.ACTION_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        v.setOnClickPendingIntent(R.id.wg_root, PendingIntent.getActivity(ctx, 1, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
        for (int id : ids) m.updateAppWidget(id, v);
    }
}
