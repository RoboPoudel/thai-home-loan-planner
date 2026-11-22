package th.homeloan.planner;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Phone reminders for the planner: payment due dates, rate changes, the end of the
 * promotion and tax time. The page sends the list; alarms fire at 09:00 the day before.
 */
public class Reminders extends BroadcastReceiver {
    private static final String PREFS = "reminders";
    private static final String CHANNEL = "loan_reminders";
    private static final String ACTION_FIRE = "th.homeloan.planner.REMINDER";

    /** Replaces all scheduled reminders with the given JSON list [{at, title, text}]. */
    static int schedule(Context ctx, String json) {
        cancel(ctx);
        int n = 0;
        try {
            JSONArray list = new JSONArray(json);
            long now = System.currentTimeMillis();
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            for (int i = 0; i < list.length() && i < 60; i++) {
                JSONObject o = list.getJSONObject(i);
                long at = o.getLong("at");
                if (at <= now) continue;
                PendingIntent pi = pending(ctx, i, o.optString("title"), o.optString("text"));
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi);
                n++;
            }
            prefs(ctx).edit().putString("list", json).putInt("count", list.length()).putBoolean("on", true).apply();
        } catch (Exception ignored) { }
        return n;
    }

    static void cancel(Context ctx) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        int count = prefs(ctx).getInt("count", 60);
        for (int i = 0; i < Math.max(count, 60); i++) am.cancel(pending(ctx, i, "", ""));
        prefs(ctx).edit().putBoolean("on", false).remove("list").apply();
    }

    static boolean isOn(Context ctx) { return prefs(ctx).getBoolean("on", false); }

    private static SharedPreferences prefs(Context ctx) { return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE); }

    private static PendingIntent pending(Context ctx, int id, String title, String text) {
        Intent i = new Intent(ctx, Reminders.class).setAction(ACTION_FIRE)
                .putExtra("id", id).putExtra("title", title).putExtra("text", text);
        return PendingIntent.getBroadcast(ctx, id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String json = prefs(ctx).getString("list", null);
            if (isOn(ctx) && json != null) schedule(ctx, json);
            return;
        }
        if (!ACTION_FIRE.equals(intent.getAction())) return;
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26 && nm.getNotificationChannel(CHANNEL) == null) {
            nm.createNotificationChannel(new NotificationChannel(CHANNEL, "Loan reminders", NotificationManager.IMPORTANCE_DEFAULT));
        }
        Intent open = new Intent(ctx, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent tap = PendingIntent.getActivity(ctx, 0, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx, CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("text"))
                .setContentIntent(tap)
                .setAutoCancel(true);
        try {
            NotificationManagerCompat.from(ctx).notify(1000 + intent.getIntExtra("id", 0), b.build());
        } catch (SecurityException ignored) { /* notifications not allowed */ }
    }
}
