package com.liveperson.sample.app.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.contactatonce.messaging.Messaging;
import com.liveperson.sample.app.R;
import com.liveperson.sample.app.Utils.SampleAppStorage;


/**
 * ***** Sample app class - Not related to MessagingController SDK ****
 * <p>
 * The main activity of the sample app
 */
public class SettingsActivity extends AppCompatActivity {
	EditText applicationID;
	EditText providerID;
	EditText baseURL;
	CheckBox useFullScreen;
	CheckBox provideNoConversationsImage;
	TextView sdkVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		applicationID = (EditText) findViewById(R.id.settings_application_id);
		applicationID.setText(String.valueOf(SampleAppStorage.getInstance(this).getCaoApplicationId()));

		providerID = (EditText) findViewById(R.id.settings_provider_id);
		providerID.setText(String.valueOf(SampleAppStorage.getInstance(this).getCAOProviderId()));

		baseURL = (EditText) findViewById(R.id.settings_baseurl);
		baseURL.setText(SampleAppStorage.getInstance(this).getCaoBaseDomain());

		useFullScreen = (CheckBox) findViewById(R.id.settings_use_full_screen);
		useFullScreen.setChecked(SampleAppStorage.getInstance(this).getUseFullScreen());

		provideNoConversationsImage = (CheckBox) findViewById(R.id.settings_provide_noconversations_image);
		provideNoConversationsImage.setChecked(SampleAppStorage.getInstance(this).getProvideNoConversationImage());

		sdkVersion = (TextView) findViewById(R.id.settings_sdkversion);
		sdkVersion.setText(Messaging.getSDKVersion());
	}

	@Override
	protected void onPause() {
		super.onPause();

		SampleAppStorage.getInstance(this).setCaoApplicationId(Integer.parseInt(applicationID.getText().toString().trim()));
		SampleAppStorage.getInstance(this).setCaoProviderId(Integer.parseInt(providerID.getText().toString().trim()));
		SampleAppStorage.getInstance(this).setCAOBaseDomain(baseURL.getText().toString().trim());
		SampleAppStorage.getInstance(this).setUsefullScreen(useFullScreen.isChecked());
		SampleAppStorage.getInstance(this).setProvideNoConversationImage(provideNoConversationsImage.isChecked());
	}
}