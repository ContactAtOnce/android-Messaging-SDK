package com.liveperson.sample.app.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.liveperson.sample.app.R;

/**
 * ***** Sample app class - Not related to MessagingController SDK ****
 *
 * Sample app "Shared Preferences" object - helper class to save
 * fields like: first name, last name, account and auth code
 */
public class SampleAppStorage {
    public static final String SDK_SAMPLE_APP_ID = "com.liveperson.sdksample";
    public static final String SDK_SAMPLE_FCM_APP_ID = "com.liveperson.sdksampleFcm";

    private final String CAO_BASE_DOMAIN = "cao_base_domain";
    private final String CAO_APPLICATION_ID = "cao_application_id";
    private final String CAO_PROVIDER_ID = "cao_provider_id";
    private final String FULL_SCREEN = "full_screen";
    private final String PROVIDE_NOCONVERSATIONIMAGE_ID = "noConversationImage_id";

    private SharedPreferences mDefaultSharedPreferences;
    private static volatile SampleAppStorage Instance = null;
    private String defaultURL;

    private SampleAppStorage(Context context) {
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        defaultURL = context.getString(R.string.qaURL);
    }

    public static SampleAppStorage getInstance(Context context) {
        if (Instance == null) {
            synchronized (SampleAppStorage.class) {
                if (Instance == null) {
                    Instance = new SampleAppStorage(context);
                }
            }
        }
        return Instance;
    }

	public String getCaoBaseDomain() { return mDefaultSharedPreferences.getString(CAO_BASE_DOMAIN, defaultURL); };
	public void setCAOBaseDomain(String domain) {
        mDefaultSharedPreferences.edit().putString(CAO_BASE_DOMAIN, domain).apply();
    }

    public int getCAOProviderId() { return mDefaultSharedPreferences.getInt(CAO_PROVIDER_ID, -1);}
    public void setCaoProviderId(int caoProviderId) {
        mDefaultSharedPreferences.edit().putInt(CAO_PROVIDER_ID, caoProviderId).apply();
    }

    public int getCaoApplicationId() { return  mDefaultSharedPreferences.getInt(CAO_APPLICATION_ID, -1);}
    public void setCaoApplicationId(int caoApplicationId) {
        mDefaultSharedPreferences.edit().putInt(CAO_APPLICATION_ID, caoApplicationId).apply();
    }

    public Boolean getUseFullScreen(){return mDefaultSharedPreferences.getBoolean(FULL_SCREEN, false);}
    public void setUsefullScreen(Boolean usefullScreen) {
        mDefaultSharedPreferences.edit().putBoolean(FULL_SCREEN, usefullScreen).apply();
    }

	public Boolean getProvideNoConversationImage(){return mDefaultSharedPreferences.getBoolean(PROVIDE_NOCONVERSATIONIMAGE_ID, false);}
	public void setProvideNoConversationImage(Boolean provideImage) {
		mDefaultSharedPreferences.edit().putBoolean(PROVIDE_NOCONVERSATIONIMAGE_ID, provideImage).apply();
	}
 }
